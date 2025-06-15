import React, {
	createContext,
	useContext,
	useState,
	useCallback,
	type ReactNode,
} from "react";

export interface RefreshContextType {
	refreshTrigger: number;
	triggerRefresh: () => void;
}

export const RefreshContext = createContext<RefreshContextType | undefined>(undefined);

interface RefreshProviderProps {
	children: ReactNode;
}

export const RefreshProvider: React.FC<RefreshProviderProps> = ({ children }) => {
	const [refreshTrigger, setRefreshTrigger] = useState(0);

	const triggerRefresh = useCallback(() => {
		setRefreshTrigger(prev => prev + 1);
	}, []);

	return (
		<RefreshContext.Provider value={{ refreshTrigger, triggerRefresh }}>
			{children}
		</RefreshContext.Provider>
	);
};

export const useRefresh = (): RefreshContextType => {
	const context = useContext(RefreshContext);
	if (!context) {
		throw new Error("useRefresh must be used within a RefreshProvider");
	}
	return context;
};