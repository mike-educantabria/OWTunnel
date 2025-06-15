import React, { useCallback } from 'react';
import {
	AlertDialog,
	AlertDialogAction,
	AlertDialogCancel,
	AlertDialogContent,
	AlertDialogDescription,
	AlertDialogFooter,
	AlertDialogHeader,
	AlertDialogTitle,
} from '@/components/ui/alert-dialog';
import { toast } from '@/hooks/useToast';

interface DeleteModalProps {
	isOpen: boolean;
	onClose: () => void;
	title: string;
	description: string;
	itemName: string;
	onConfirm: () => Promise<void>;
}

const DeleteModal: React.FC<DeleteModalProps> = ({
	isOpen,
	onClose,
	title,
	description,
	itemName,
	onConfirm
}) => {
	const handleConfirm = useCallback(async () => {
		try {
			await onConfirm();
			toast({
				title: "Deleted",
				description: "Item has been deleted.",
				variant: "destructive",
			});
			onClose();
		} catch (error) {
			toast({
				title: "Error",
				description: "Cannot delete. Existing relation found.",
				variant: "destructive",
			});
		}
	}, [onConfirm, onClose]);

	return (
		<AlertDialog open={isOpen} onOpenChange={onClose}>
			<AlertDialogContent>
				<AlertDialogHeader>
					<AlertDialogTitle>{title}</AlertDialogTitle>
					<AlertDialogDescription>
						{description}
						<br />
						This action can't be undone.
					</AlertDialogDescription>
				</AlertDialogHeader>
				<AlertDialogFooter>
					<AlertDialogCancel onClick={onClose}>
						Cancel
					</AlertDialogCancel>
					<AlertDialogAction
						onClick={handleConfirm}
						className="bg-red-600 hover:bg-red-700 text-white dark:text-white"
					>
						Delete
					</AlertDialogAction>
				</AlertDialogFooter>
			</AlertDialogContent>
		</AlertDialog>
	);
};

export default DeleteModal;