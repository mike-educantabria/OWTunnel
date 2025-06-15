import React, { useState } from 'react';
import { Eye, Edit, Trash2 } from 'lucide-react';
import { Button } from '@/components/ui/button';
import FieldModal from '@/components/FieldModal';
import DeleteModal from '@/components/DeleteModal';

interface TableActionsProps {
	data: Record<string, any>;
	entityType: 'user' | 'plan' | 'connection' | 'subscription' | 'server' | 'payment';
	onUpdate?: (updatedData: Record<string, any>) => void;
	onDelete?: (id: string) => Promise<void>;
}

interface EntityConfig {
	viewTitle: string;
	editTitle: string;
	deleteTitle: string;
	deleteDescription: string;
	viewFields: Array<{
		key: string;
		label: string;
		type?: 'text' | 'badge' | 'boolean' | 'select' | 'textarea';
		badgeColors?: Record<string, string>;
		options?: Array<{ value: string; label: string }>;
	}>;
	editFields: Array<{
		key: string;
		label: string;
		type?: 'text' | 'number' | 'email' | 'select' | 'checkbox' | 'textarea';
		options?: Array<{ value: string; label: string }>;
		readonly?: boolean;
	}>;
}

const entityConfigs: Record<string, EntityConfig> = {
	user: {
		viewTitle: 'User Details',
		editTitle: 'Edit User',
		deleteTitle: 'Delete User',
		deleteDescription: 'Are you sure you want to delete this user?',
		viewFields: [
			{ key: 'id', label: 'ID' },
			{ key: 'email', label: 'Email' },
			{ key: 'first_name', label: 'First Name' },
			{ key: 'last_name', label: 'Last Name' },
			{ key: 'locale', label: 'Language' },
			{ key: 'role', label: 'Role', type: 'badge' },
			{ key: 'created_at', label: 'Created At' },
			{ key: 'updated_at', label: 'Last Updated' },
		],
		editFields: [
			{ key: 'email', label: 'Email', type: 'email' },
			{ key: 'first_name', label: 'First Name', type: 'text' },
			{ key: 'last_name', label: 'Last Name', type: 'text' },
			{ key: 'locale', label: 'Language', type: 'text' },
			{
				key: 'role', label: 'Role', type: 'select', options: [
					{ value: 'USER', label: 'USER' },
					{ value: 'SUPPORT', label: 'SUPPORT' },
					{ value: 'ADMINISTRATOR', label: 'ADMINISTRATOR' }
				]
			},
		],
	},
	plan: {
		viewTitle: 'Plan Details',
		editTitle: 'Edit Plan',
		deleteTitle: 'Delete Plan',
		deleteDescription: 'Are you sure you want to delete this plan?',
		viewFields: [
			{ key: 'id', label: 'ID' },
			{ key: 'name', label: 'Name' },
			{ key: 'description', label: 'Description', type: 'textarea' },
			{ key: 'price', label: 'Price' },
			{ key: 'currency', label: 'Currency' },
			{ key: 'duration_days', label: 'Duration (days)' },
			{
				key: 'is_active', label: 'Is Active', type: 'select', options: [
					{ value: 'true', label: 'Yes' },
					{ value: 'false', label: 'No' }
				]
			},
			{ key: 'created_at', label: 'Created At' },
			{ key: 'updated_at', label: 'Updated At' },
		],
		editFields: [
			{ key: 'name', label: 'Name', type: 'text' },
			{ key: 'description', label: 'Description', type: 'textarea' },
			{ key: 'price', label: 'Price', type: 'number' },
			{
				key: 'currency', label: 'Currency', type: 'select', options: [
					{ value: 'USD', label: 'USD' },
					{ value: 'EUR', label: 'EUR' }
				]
			},
			{ key: 'duration_days', label: 'Duration (days)', type: 'number' },
			{
				key: 'is_active', label: 'Is Active', type: 'select', options: [
					{ value: 'true', label: 'Yes' },
					{ value: 'false', label: 'No' }
				]
			},
		],
	},
	connection: {
		viewTitle: 'Connection Details',
		editTitle: 'Edit Connection',
		deleteTitle: 'Delete Connection',
		deleteDescription: 'Are you sure you want to delete this connection?',
		viewFields: [
			{ key: 'id', label: 'Connection ID' },
			{ key: 'userEmail', label: 'User Email' },
			{ key: 'serverLocation', label: 'Server Location' },
			{ key: 'device_info', label: 'Device Information' },
			{ key: 'status', label: 'Connection Status', type: 'badge' },
			{ key: 'created_at', label: 'Connected At' },
			{ key: 'updated_at', label: 'Last Updated' },
		],
		editFields: [
			{ key: 'userEmail', label: 'User Email', type: 'email' },
			{ key: 'serverLocation', label: 'Server Location', type: 'text' },
			{ key: 'device_info', label: 'Device Information', type: 'text' },
			{
				key: 'status', label: 'Connection Status', type: 'select', options: [
					{ value: 'CONNECTED', label: 'CONNECTED' },
					{ value: 'DISCONNECTED', label: 'DISCONNECTED' },
					{ value: 'TIMEOUT', label: 'TIMEOUT' },
					{ value: 'REJECTED', label: 'REJECTED' },
					{ value: 'ERROR', label: 'ERROR' }
				]
			},
		],
	},
	subscription: {
		viewTitle: 'Subscription Details',
		editTitle: 'Edit Subscription',
		deleteTitle: 'Delete Subscription',
		deleteDescription: 'Are you sure you want to delete this subscription?',
		viewFields: [
			{ key: 'id', label: 'Subscription ID' },
			{ key: 'userEmail', label: 'User Email' },
			{ key: 'planName', label: 'Plan Name' },
			{
				key: 'status', label: 'Status', type: 'select', options: [
					{ value: 'PENDING', label: 'PENDING' },
					{ value: 'ACTIVE', label: 'ACTIVE' },
					{ value: 'CANCELLED', label: 'CANCELLED' },
					{ value: 'EXPIRED', label: 'EXPIRED' }
				]
			},
			{
				key: 'auto_renew', label: 'Auto Renewal', type: 'select', options: [
					{ value: 'true', label: 'Yes' },
					{ value: 'false', label: 'No' }
				]
			},
			{ key: 'created_at', label: 'Created At' },
			{ key: 'updated_at', label: 'Last Updated' },
			{ key: 'expires_at', label: 'Expires At' },
		],
		editFields: [
			{ key: 'userEmail', label: 'User Email', type: 'email' },
			{ key: 'planName', label: 'Plan Name', type: 'text' },
			{
				key: 'status', label: 'Status', type: 'select', options: [
					{ value: 'PENDING', label: 'PENDING' },
					{ value: 'ACTIVE', label: 'ACTIVE' },
					{ value: 'CANCELLED', label: 'CANCELLED' },
					{ value: 'EXPIRED', label: 'EXPIRED' }
				]
			},
			{
				key: 'auto_renew', label: 'Auto Renewal', type: 'select', options: [
					{ value: 'true', label: 'Yes' },
					{ value: 'false', label: 'No' }
				]
			},
			{ key: 'expires_at', label: 'Expires At', type: 'text' },
		],
	},
	server: {
		viewTitle: 'VPN Server Details',
		editTitle: 'Edit VPN Server',
		deleteTitle: 'Delete VPN Server',
		deleteDescription: 'Are you sure you want to delete this VPN server?',
		viewFields: [
			{ key: 'id', label: 'Server ID' },
			{ key: 'country', label: 'Country' },
			{ key: 'city', label: 'City' },
			{ key: 'hostname', label: 'Hostname' },
			{ key: 'ip_address', label: 'IP Address' },
			{ key: 'config_file_url', label: 'Config File URL' },
			{
				key: 'is_free', label: 'Is Free', type: 'select', options: [
					{ value: 'true', label: 'Yes' },
					{ value: 'false', label: 'No' }
				]
			},
			{
				key: 'is_active', label: 'Is Active', type: 'select', options: [
					{ value: 'true', label: 'Yes' },
					{ value: 'false', label: 'No' }
				]
			},
			{ key: 'created_at', label: 'Created At' },
			{ key: 'updated_at', label: 'Last Updated' },
		],
		editFields: [
			{ key: 'country', label: 'Country', type: 'text' },
			{ key: 'city', label: 'City', type: 'text' },
			{ key: 'hostname', label: 'Hostname', type: 'text' },
			{ key: 'ip_address', label: 'IP Address', type: 'text' },
			{ key: 'config_file_url', label: 'Config File URL', type: 'text' },
			{
				key: 'is_free', label: 'Is Free', type: 'select', options: [
					{ value: 'true', label: 'Yes' },
					{ value: 'false', label: 'No' }
				]
			},
			{
				key: 'is_active', label: 'Is Active', type: 'select', options: [
					{ value: 'true', label: 'Yes' },
					{ value: 'false', label: 'No' }
				]
			},
		],
	},
	payment: {
		viewTitle: 'Payment Details',
		editTitle: 'Edit Payment',
		deleteTitle: 'Delete Payment',
		deleteDescription: 'Are you sure you want to delete this payment?',
		viewFields: [
			{ key: 'id', label: 'Payment ID' },
			{ key: 'userEmail', label: 'User Email' },
			{ key: 'planName', label: 'Plan Name' },
			{ key: 'amount', label: 'Amount' },
			{ key: 'currency', label: 'Currency' },
			{ key: 'method', label: 'Payment Method', type: 'badge' },
			{ key: 'status', label: 'Payment Status', type: 'badge' },
			{ key: 'transaction_reference', label: 'Transaction Reference' },
			{ key: 'created_at', label: 'Payment Date' },
			{ key: 'updated_at', label: 'Last Updated' },
		],
		editFields: [
			{ key: 'userEmail', label: 'User Email', type: 'email' },
			{ key: 'planName', label: 'Plan Name', type: 'text' },
			{ key: 'amount', label: 'Amount', type: 'number' },
			{
				key: 'currency', label: 'Currency', type: 'select', options: [
					{ value: 'USD', label: 'USD' },
					{ value: 'EUR', label: 'EUR' }
				]
			},
			{
				key: 'method', label: 'Payment Method', type: 'select', options: [
					{ value: 'CREDIT_CARD', label: 'Credit Card' },
					{ value: 'DEBIT_CARD', label: 'Debit Card' },
					{ value: 'PAYPAL', label: 'PayPal' },
					{ value: 'APPLE_PAY', label: 'Apple Pay' },
					{ value: 'GOOGLE_PAY', label: 'Google Pay' }
				]
			},
			{
				key: 'status', label: 'Payment Status', type: 'select', options: [
					{ value: 'PENDING', label: 'Pending' },
					{ value: 'PAID', label: 'Paid' },
					{ value: 'FAILED', label: 'Failed' },
					{ value: 'CANCELLED', label: 'Cancelled' },
					{ value: 'REFUNDED', label: 'Refunded' }
				]
			},
			{ key: 'transaction_reference', label: 'Transaction Reference', type: 'text' },
		],
	},
};

const TableActions: React.FC<TableActionsProps> = ({
	data,
	entityType,
	onUpdate,
	onDelete
}) => {
	const [viewModalOpen, setViewModalOpen] = useState(false);
	const [editModalOpen, setEditModalOpen] = useState(false);
	const [deleteModalOpen, setDeleteModalOpen] = useState(false);

	const handleView = () => setViewModalOpen(true);
	const handleEdit = () => setEditModalOpen(true);
	const handleDelete = () => setDeleteModalOpen(true);

	const handleUpdate = (updatedData: Record<string, any>) => {
		if (onUpdate) {
			if (entityType === 'server') {
				if (updatedData.is_free !== undefined) {
					updatedData.is_free = updatedData.is_free === 'true';
				}
				if (updatedData.is_active !== undefined) {
					updatedData.is_active = updatedData.is_active === 'true';
				}
			}
			if (entityType === 'plan') {
				if (updatedData.is_active !== undefined) {
					updatedData.is_active = updatedData.is_active === 'true';
				}
			}
			if (entityType === 'subscription') {
				if (updatedData.auto_renew !== undefined) {
					updatedData.auto_renew = updatedData.auto_renew === 'true';
				}
			}
			onUpdate(updatedData);
		}
		console.log('Update data:', updatedData);
	};

	const handleDeleteConfirm = async () => {
		if (onDelete) {
			await onDelete(data.id);
		}
		console.log('Delete data:', data);
	};

	const config = entityConfigs[entityType];

	if (!config) {
		console.error(`No config found for entity type: ${entityType}`);
		return null;
	}

	const getDisplayName = () => {
		if (entityType === 'user') {
			return `${data.first_name} ${data.last_name}`;
		}
		if (entityType === 'server') {
			return 'Server';
		}
		if (entityType === 'connection') {
			return `Connection ${data.id}`;
		}
		return data.name || data.fullName || data.email || `ID: ${data.id}`;
	};

	const getModalData = () => {
		if (entityType === 'server') {
			return {
				...data,
				is_free: data.is_free?.toString(),
				is_active: data.is_active?.toString()
			};
		}
		if (entityType === 'plan') {
			return {
				...data,
				is_active: data.is_active?.toString()
			};
		}
		if (entityType === 'subscription') {
			return {
				...data,
				auto_renew: data.auto_renew?.toString()
			};
		}
		return data;
	};

	return (
		<>
			<div className="flex items-center gap-2">
				<Button
					variant="ghost"
					size="sm"
					onClick={handleView}
					className="text-blue-600 hover:text-blue-700 hover:bg-blue-50 dark:text-blue-400 dark:hover:text-blue-300 dark:hover:bg-blue-900/20"
				>
					<Eye className="h-4 w-4" />
				</Button>
				<Button
					variant="ghost"
					size="sm"
					onClick={handleEdit}
					className="text-yellow-600 hover:text-yellow-700 hover:bg-yellow-50 dark:text-yellow-400 dark:hover:text-yellow-300 dark:hover:bg-yellow-900/20"
				>
					<Edit className="h-4 w-4" />
				</Button>
				<Button
					variant="ghost"
					size="sm"
					onClick={handleDelete}
					className="text-red-600 hover:text-red-700 hover:bg-red-50 dark:text-red-400 dark:hover:text-red-300 dark:hover:bg-red-900/20"
				>
					<Trash2 className="h-4 w-4" />
				</Button>
			</div>

			<FieldModal
				isOpen={viewModalOpen}
				onClose={() => setViewModalOpen(false)}
				title={config.viewTitle}
				data={getModalData()}
				fields={config.viewFields}
				mode="view"
			/>

			<FieldModal
				isOpen={editModalOpen}
				onClose={() => setEditModalOpen(false)}
				title={config.editTitle}
				data={getModalData()}
				fields={config.editFields}
				mode="edit"
				onSave={handleUpdate}
			/>

			<DeleteModal
				isOpen={deleteModalOpen}
				onClose={() => setDeleteModalOpen(false)}
				title={config.deleteTitle}
				description={config.deleteDescription}
				itemName={getDisplayName()}
				onConfirm={handleDeleteConfirm}
			/>
		</>
	);
};

export default TableActions;