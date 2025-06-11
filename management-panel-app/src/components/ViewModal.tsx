import React, { useCallback } from 'react';
import {
	Dialog,
	DialogContent,
	DialogDescription,
	DialogHeader,
	DialogTitle,
} from '@/components/ui/dialog';

interface FieldConfig {
	key: string;
	label: string;
	type?: 'text' | 'badge' | 'boolean';
	badgeColors?: Record<string, string>;
}

interface ViewModalProps {
	isOpen: boolean;
	onClose: () => void;
	title: string;
	data: Record<string, any>;
	fields: FieldConfig[];
}

const ViewModal: React.FC<ViewModalProps> = ({
	isOpen,
	onClose,
	title,
	data,
	fields,
}) => {
	const renderFieldValue = useCallback((field: FieldConfig, value: any) => {
		if (field.type === 'badge') {
			const colors =
				field.badgeColors?.[value] ||
				'bg-gray-100 text-gray-800 dark:bg-gray-900/20 dark:text-gray-300';
			return (
				<span
					className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${colors}`}
				>
					{value ?? '-'}
				</span>
			);
		}

		if (field.type === 'boolean') {
			return value ? 'Yes' : 'No';
		}

		return value ?? '-';
	}, []);

	return (
		<Dialog open={isOpen} onOpenChange={onClose}>
			<DialogContent className="max-w-2xl max-h-[80vh] overflow-hidden">
				<DialogHeader>
					<DialogTitle>{title}</DialogTitle>
					<DialogDescription>
						Details of the selected record
					</DialogDescription>
				</DialogHeader>
				<div className="mt-6 space-y-4 max-h-[60vh] overflow-y-auto">
					{fields.map((field) => (
						<div key={field.key} className="space-y-2">
							<label className="text-sm font-medium text-gray-700 dark:text-gray-300">
								{field.label}
							</label>
							<div className="text-sm text-gray-900 dark:text-white">
								{renderFieldValue(field, data[field.key])}
							</div>
						</div>
					))}
				</div>
			</DialogContent>
		</Dialog>
	);
};

export default ViewModal;