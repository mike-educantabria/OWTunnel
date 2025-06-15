import * as React from "react";
import type { ToastActionElement, ToastProps } from "@/components/ui/toast";

const TOAST_LIMIT = 1;
const TOAST_REMOVE_DELAY = 10_000;

type ToastId = string;

type ToasterToast = ToastProps & {
	id: ToastId;
	title?: React.ReactNode;
	description?: React.ReactNode;
	action?: ToastActionElement;
};

type Toast = Omit<ToasterToast, "id">;

const actionTypes = {
	ADD_TOAST: "ADD_TOAST",
	UPDATE_TOAST: "UPDATE_TOAST",
	DISMISS_TOAST: "DISMISS_TOAST",
	REMOVE_TOAST: "REMOVE_TOAST",
} as const;

type Action =
	| { type: typeof actionTypes.ADD_TOAST; toast: ToasterToast }
	| { type: typeof actionTypes.UPDATE_TOAST; toast: Partial<ToasterToast> }
	| { type: typeof actionTypes.DISMISS_TOAST; toastId?: ToastId }
	| { type: typeof actionTypes.REMOVE_TOAST; toastId?: ToastId };

interface State {
	toasts: ToasterToast[];
}

const toastTimeouts = new Map<ToastId, ReturnType<typeof setTimeout>>();
const listeners: Array<(state: State) => void> = [];
let memoryState: State = { toasts: [] };

function genId(): ToastId {
	return Date.now().toString(36) + Math.random().toString(36).slice(2);
}

function addToRemoveQueue(toastId: ToastId) {
	if (toastTimeouts.has(toastId)) return;

	const timeout = setTimeout(() => {
		toastTimeouts.delete(toastId);
		dispatch({ type: actionTypes.REMOVE_TOAST, toastId });
	}, TOAST_REMOVE_DELAY);

	toastTimeouts.set(toastId, timeout);
}

function reducer(state: State, action: Action): State {
	switch (action.type) {
		case actionTypes.ADD_TOAST:
			return {
				...state,
				toasts: [action.toast, ...state.toasts].slice(0, TOAST_LIMIT),
			};

		case actionTypes.UPDATE_TOAST:
			return {
				...state,
				toasts: state.toasts.map((t) =>
					t.id === action.toast.id ? { ...t, ...action.toast } : t
				),
			};

		case actionTypes.DISMISS_TOAST: {
			const { toastId } = action;

			toastId
				? addToRemoveQueue(toastId)
				: state.toasts.forEach((t) => addToRemoveQueue(t.id));

			return {
				...state,
				toasts: state.toasts.map((t) =>
					!toastId || t.id === toastId ? { ...t, open: false } : t
				),
			};
		}

		case actionTypes.REMOVE_TOAST:
			return {
				...state,
				toasts: action.toastId
					? state.toasts.filter((t) => t.id !== action.toastId)
					: [],
			};

		default:
			return state;
	}
}

function dispatch(action: Action) {
	memoryState = reducer(memoryState, action);
	listeners.forEach((listener) => listener(memoryState));
}

function toast(props: Toast) {
	const id = genId();

	const dismiss = () => dispatch({ type: actionTypes.DISMISS_TOAST, toastId: id });
	const update = (newProps: Partial<ToasterToast>) =>
		dispatch({ type: actionTypes.UPDATE_TOAST, toast: { ...newProps, id } });

	dispatch({
		type: actionTypes.ADD_TOAST,
		toast: {
			...props,
			id,
			open: true,
			onOpenChange: (open) => {
				if (!open) dismiss();
			},
		},
	});

	return { id, dismiss, update };
}

function useToast() {
	const [state, setState] = React.useState<State>(memoryState);

	React.useEffect(() => {
		listeners.push(setState);
		return () => {
			const index = listeners.indexOf(setState);
			if (index !== -1) listeners.splice(index, 1);
		};
	}, []);

	return {
		...state,
		toast,
		dismiss: (toastId?: ToastId) =>
			dispatch({ type: actionTypes.DISMISS_TOAST, toastId }),
	};
}

export { useToast, toast };