import React, { useState, useEffect } from 'react';
import {
	Dialog,
	DialogContent,
	DialogHeader,
	DialogTitle,
	DialogFooter,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Checkbox } from '@/components/ui/checkbox';
import {
	Select,
	SelectContent,
	SelectItem,
	SelectTrigger,
	SelectValue,
} from '@/components/ui/select';
import { toast } from '@/hooks/useToast';
import { Textarea } from '@/components/ui/textarea';

interface FieldModalProps {
	isOpen: boolean;
	onClose: () => void;
	title: string;
	data: Record<string, any>;
	fields: Array<{
		key: string;
		label: string;
		type?: 'text' | 'number' | 'email' | 'select' | 'checkbox' | 'badge' | 'boolean' | 'textarea';
		options?: Array<{ value: string; label: string }>;
		readonly?: boolean;
		badgeColors?: Record<string, string>;
	}>;
	mode: 'view' | 'edit';
	onSave?: (updatedData: Record<string, any>) => void;
}

const FieldModal: React.FC<FieldModalProps> = ({
	isOpen,
	onClose,
	title,
	data,
	fields,
	mode,
	onSave
}) => {
	const [formData, setFormData] = useState<Record<string, any>>({});

	useEffect(() => {
		if (isOpen) {
			setFormData({ ...data });
		}
	}, [isOpen, data]);

	const handleInputChange = (key: string, value: any) => {
		setFormData(prev => ({
			...prev,
			[key]: value
		}));
	};

	const isFormValid = () => {
		if (mode === 'view') return true;
		return fields.every(field => {
			const value = formData[field.key];
			return value !== undefined && value !== null && value !== '';
		});
	};

	const handleSave = () => {
		if (!isFormValid()) {
			toast({
				title: "Error",
				description: "Please fill in all required fields.",
				variant: "destructive"
			});
			return;
		}
		if (onSave) {
			onSave(formData);
			toast({
				title: "Success",
				description: "Changes have been saved successfully.",
			});
		}
		onClose();
	};

	const renderFieldValue = (field: any, value: any) => {
		const isReadOnly = mode === 'view' || field.readonly;

		if (field.type === 'checkbox') {
			return (
				<div className="flex items-center space-x-2">
					<Checkbox
						checked={value}
						disabled={isReadOnly}
						onCheckedChange={(checked) => !isReadOnly && handleInputChange(field.key, checked)}
					/>
					<span className="text-sm">{value ? 'Yes' : 'No'}</span>
				</div>
			);
		}

		if (field.type === 'boolean' && mode === 'view') {
			return <span className="text-sm">{value ? 'Yes' : 'No'}</span>;
		}

		if (field.type === 'select') {
			return (
				<Select
					value={value || ''}
					disabled={isReadOnly}
					onValueChange={(newValue) => !isReadOnly && handleInputChange(field.key, newValue)}
				>
					<SelectTrigger className={`w-full ${isReadOnly ? "bg-muted" : ""}`}>
						<SelectValue placeholder="Select an option" />
					</SelectTrigger>
					<SelectContent>
						{field.options?.map((option) => (
							<SelectItem key={option.value} value={option.value}>
								{option.label}
							</SelectItem>
						))}
					</SelectContent>
				</Select>
			);
		}

		if (field.type === 'textarea') {
			return (
				<Textarea
					value={value || ''}
					disabled={isReadOnly}
					onChange={(e) => !isReadOnly && handleInputChange(field.key, e.target.value)}
					className={isReadOnly ? "bg-muted" : ""}
					rows={3}
				/>
			);
		}

		return (
			<Input
				type={field.type === 'boolean' ? 'text' : (field.type || 'text')}
				value={field.type === 'boolean' ? (value ? 'Yes' : 'No') : (value || '')}
				disabled={isReadOnly}
				onChange={(e) => !isReadOnly && handleInputChange(field.key, e.target.value)}
				className={isReadOnly ? "bg-muted" : ""}
			/>
		);
	};

	return (
		<Dialog open={isOpen} onOpenChange={onClose}>
			<DialogContent className="w-[500px] max-w-[90vw] h-[600px] max-h-[90vh] flex flex-col">
				<DialogHeader className="flex-shrink-0">
					<DialogTitle className="text-xl font-semibold">{title}</DialogTitle>
				</DialogHeader>
				<div className="flex-1 overflow-y-auto pr-4 py-4">
					<div className="space-y-4 pr-2">
						{fields.map((field) => (
							<div key={field.key} className="space-y-2">
								<Label htmlFor={field.key} className="text-sm font-medium">
									{field.label} {mode === 'edit' && <span className="text-red-500">*</span>}
								</Label>
								{renderFieldValue(field, formData[field.key])}
							</div>
						))}
					</div>
				</div>
				{mode === 'edit' && (
					<DialogFooter className="flex-shrink-0 border-t pt-4">
						<Button variant="outline" onClick={onClose}>
							Cancel
						</Button>
						<Button
							onClick={handleSave}
							disabled={!isFormValid()}
							className={!isFormValid() ? "opacity-50 cursor-not-allowed" : ""}
						>
							Save Changes
						</Button>
					</DialogFooter>
				)}
			</DialogContent>
		</Dialog>
	);
};

export default FieldModal;