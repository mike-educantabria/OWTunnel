import React, { useState, useEffect } from 'react';
import {
	Dialog,
	DialogContent,
	DialogDescription,
	DialogHeader,
	DialogTitle,
	DialogFooter,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Checkbox } from '@/components/ui/checkbox';
import { toast } from '@/hooks/useToast';

interface EditModalProps {
	isOpen: boolean;
	onClose: () => void;
	title: string;
	data: Record<string, any>;
	fields: Array<{
		key: string;
		label: string;
		type?: 'text' | 'number' | 'email' | 'select' | 'checkbox';
		options?: Array<{ value: string; label: string }>;
		readonly?: boolean;
	}>;
	onSave: (updatedData: Record<string, any>) => void;
}

const EditModal: React.FC<EditModalProps> = ({
	isOpen,
	onClose,
	title,
	data,
	fields,
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
		return fields.every(field => {
			if (field.readonly) return true;
			const value = formData[field.key];
			return value !== undefined && value !== null && value !== '';
		});
	};

	const handleSave = () => {
		if (!isFormValid()) {
			toast({
				title: "Error",
				description: "Por favor, completa todos los campos requeridos.",
				variant: "destructive"
			});
			return;
		}
		onSave(formData);
		toast({
			title: "Éxito",
			description: "Los cambios se han guardado correctamente.",
		});
		onClose();
	};

	const renderField = (field: any) => {
		const value = formData[field.key] ?? '';

		if (field.readonly) {
			return (
				<Input
					value={value}
					disabled
					className="bg-gray-100 dark:bg-gray-800"
				/>
			);
		}

		switch (field.type) {
			case 'checkbox':
				return (
					<div className="flex items-center space-x-2">
						<Checkbox
							checked={!!value}
							onCheckedChange={(checked) => handleInputChange(field.key, checked)}
						/>
						<span className="text-sm">{value ? 'Sí' : 'No'}</span>
					</div>
				);
			case 'select':
				return (
					<select
						value={value}
						onChange={(e) => handleInputChange(field.key, e.target.value)}
						className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-700 dark:text-white"
					>
						<option value="" disabled>Selecciona una opción</option>
						{field.options?.map((option) => (
							<option key={option.value} value={option.value}>
								{option.label}
							</option>
						))}
					</select>
				);
			default:
				return (
					<Input
						type={field.type || 'text'}
						value={value}
						onChange={(e) => handleInputChange(field.key, e.target.value)}
					/>
				);
		}
	};

	return (
		<Dialog open={isOpen} onOpenChange={onClose}>
			<DialogContent className="max-w-2xl max-h-[80vh] overflow-hidden">
				<DialogHeader>
					<DialogTitle>{title}</DialogTitle>
					<DialogDescription>
						Edita los campos del registro
					</DialogDescription>
				</DialogHeader>
				<div className="mt-6 space-y-4 max-h-[50vh] overflow-y-auto">
					{fields.map((field) => (
						<div key={field.key} className="space-y-2">
							<Label htmlFor={field.key} className="text-sm font-medium">
								{field.label} {!field.readonly && <span className="text-red-500">*</span>}
							</Label>
							{renderField(field)}
						</div>
					))}
				</div>
				<DialogFooter className="mt-6">
					<Button variant="outline" onClick={onClose}>
						Cancelar
					</Button>
					<Button onClick={handleSave} disabled={!isFormValid()}>
						Guardar Cambios
					</Button>
				</DialogFooter>
			</DialogContent>
		</Dialog>
	);
};

export default EditModal;