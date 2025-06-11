import React, { useState, useMemo } from 'react';
import { Search, FileText } from 'lucide-react';
import TableActions from '@/components/TableActions';
import { useGlobalDataContext } from '@/contexts/GlobalDataContext';
import { toast } from '@/hooks/useToast';
import { useRefresh } from '@/contexts/RefreshContext';
import api from '@/lib/api';
import FieldModal from '@/components/FieldModal';

type Subscription = {
	id: string;
	user_id: string;
	plan_id: string;
	userEmail: string;
	planName: string;
	status: string;
	auto_renew: boolean;
	created_at: string;
	updated_at: string;
	expires_at?: string;
};

const subscriptionFields = [
	{ key: 'userEmail', label: 'User Email', type: 'email' as const },
	{ key: 'planName', label: 'Plan Name', type: 'text' as const },
	{
		key: 'status', label: 'Status', type: 'select' as const, options: [
			{ value: 'PENDING', label: 'Pending' },
			{ value: 'ACTIVE', label: 'Active' },
			{ value: 'CANCELLED', label: 'Cancelled' },
			{ value: 'EXPIRED', label: 'Expired' }
		]
	},
	{
		key: 'auto_renew', label: 'Auto Renew', type: 'select' as const, options: [
			{ value: 'true', label: 'Yes' },
			{ value: 'false', label: 'No' }
		]
	},
];

const emptySubscriptionData = {
	userEmail: '',
	planName: '',
	status: 'PENDING',
	auto_renew: 'true'
};

const Subscriptions = () => {
	const [searchTerm, setSearchTerm] = useState('');
	const [statusFilter, setStatusFilter] = useState('all');
	const [newSubscriptionModalOpen, setNewSubscriptionModalOpen] = useState(false);
	const { data, setData } = useGlobalDataContext();
	const { triggerRefresh } = useRefresh();

	const users = data.users ?? [];
	const plans = data.plans ?? [];
	const subscriptions: Subscription[] = (data.subscriptions ?? []).map((s: any) => {
		const user = users.find((u: any) => String(u.id) === String(s.user_id ?? s.userId));
		const plan = plans.find((p: any) => String(p.id) === String(s.plan_id ?? s.planId));
		return {
			id: String(s.id),
			user_id: String(s.user_id ?? s.userId),
			plan_id: String(s.plan_id ?? s.planId),
			userEmail: user?.email ?? '',
			planName: plan?.name ?? '',
			status: s.status ?? '',
			auto_renew: !!(s.auto_renew ?? s.autoRenew),
			created_at: s.created_at ?? s.createdAt ?? '',
			updated_at: s.updated_at ?? s.updatedAt ?? '',
			expires_at: s.expires_at ?? s.expiresAt ?? '',
		};
	});

	const filteredSubscriptions = useMemo(() => {
		return subscriptions.filter(sub => {
			const matchesSearch = searchTerm === '' ||
				sub.userEmail.toLowerCase().includes(searchTerm.toLowerCase()) ||
				sub.planName.toLowerCase().includes(searchTerm.toLowerCase()) ||
				sub.id.includes(searchTerm);

			const matchesStatus = statusFilter === 'all' || sub.status === statusFilter;

			return matchesSearch && matchesStatus;
		});
	}, [subscriptions, searchTerm, statusFilter]);

	const exportToCSV = () => {
		const headers = ['ID', 'User Email', 'Plan Name', 'Status', 'Auto Renew', 'Created At', 'Updated At', 'Expires At'];
		const csvContent = [
			headers.join(','),
			...filteredSubscriptions.map(sub => [
				sub.id,
				sub.userEmail,
				sub.planName,
				sub.status,
				sub.auto_renew ? 'Yes' : 'No',
				sub.created_at,
				sub.updated_at,
				sub.expires_at ?? ''
			].join(','))
		].join('\n');

		const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
		const link = document.createElement('a');
		const url = URL.createObjectURL(blob);
		link.href = url;
		link.download = 'subscriptions.csv';
		link.click();
		document.body.removeChild(link);
		URL.revokeObjectURL(url);
	};

	const handleUpdateSubscription = async (updatedData: Record<string, any>) => {
		try {
			const user = users.find((u: any) => u.email === updatedData.userEmail);
			if (!user) {
				toast({ title: 'Error', description: 'User not found', variant: 'destructive' });
				return;
			}
			const plan = plans.find((p: any) => p.name === updatedData.planName);
			if (!plan) {
				toast({ title: 'Error', description: 'Plan not found', variant: 'destructive' });
				return;
			}
			const autoRenew = updatedData.auto_renew === true || updatedData.auto_renew === 'true' || updatedData.auto_renew === 1;

			const expiresAt = updatedData.expires_at ?? updatedData.expiresAt;
			if (!expiresAt) {
				toast({ title: 'Error', description: 'Expiration date is required.', variant: 'destructive' });
				return;
			}

			const payload = {
				userId: Number(user.id),
				planId: Number(plan.id),
				status: updatedData.status,
				autoRenew,
				expiresAt: typeof expiresAt === 'string' ? expiresAt : new Date(expiresAt).toISOString(),
			};

			console.log('Payload sent:', payload);

			const response = await api.put(`/subscriptions/${updatedData.id}`, payload);
			triggerRefresh();
			const updated = response.data;
			const normalized: Subscription = {
				id: String(updated.id),
				user_id: String(updated.userId ?? updated.user_id),
				plan_id: String(updated.planId ?? updated.plan_id),
				userEmail: user.email,
				planName: plan.name,
				status: updated.status ?? '',
				auto_renew: !!(updated.autoRenew ?? updated.auto_renew),
				created_at: updated.created_at ?? updated.createdAt ?? '',
				updated_at: updated.updated_at ?? updated.updatedAt ?? '',
				expires_at: updated.expires_at ?? updated.expiresAt ?? '',
			};
			setData(prev => ({
				...prev,
				subscriptions: prev.subscriptions.map((sub: Subscription) =>
					sub.id === normalized.id ? { ...sub, ...normalized } : sub
				)
			}));
			toast({ title: 'Success', description: 'Subscription updated successfully.' });
		} catch (err) {
			console.error('Error updating subscription:', err);
			toast({ title: 'Error', description: 'Failed to update subscription.' });
		}
	};

	const handleDeleteSubscription = async (subscriptionId: string) => {
		try {
			await api.delete(`/subscriptions/${subscriptionId}`);
			triggerRefresh();
			setData(prev => ({
				...prev,
				subscriptions: prev.subscriptions.filter((sub: Subscription) => sub.id !== subscriptionId)
			}));
			toast({ title: 'Success', description: 'Subscription deleted successfully.' });
		} catch (err) {
			console.error('Error deleting subscription:', err);
			toast({ title: 'Error', description: 'Failed to delete subscription.' });
		}
	};

	const handleCreateSubscription = async (newData: Record<string, any>) => {
		try {
			const user = users.find((u: any) => u.email === newData.userEmail);
			if (!user) {
				toast({ title: 'Error', description: 'User not found', variant: 'destructive' });
				return;
			}
			const plan = plans.find((p: any) => p.name === newData.planName);
			if (!plan) {
				toast({ title: 'Error', description: 'Plan not found', variant: 'destructive' });
				return;
			}
			const autoRenew = newData.auto_renew === true || newData.auto_renew === 'true' || newData.auto_renew === 1;

			const payload = {
				userId: Number(user.id),
				planId: Number(plan.id),
				status: newData.status,
				autoRenew,
			};

			const response = await api.post('/subscriptions', payload);
			triggerRefresh();
			const created = response.data;
			const normalized: Subscription = {
				id: String(created.id),
				user_id: String(created.userId ?? created.user_id),
				plan_id: String(created.planId ?? created.plan_id),
				userEmail: user.email,
				planName: plan.name,
				status: created.status ?? '',
				auto_renew: !!(created.autoRenew ?? created.auto_renew),
				created_at: created.created_at ?? created.createdAt ?? '',
				updated_at: created.updated_at ?? created.updatedAt ?? '',
				expires_at: created.expires_at ?? created.expiresAt ?? '',
			};
			setData(prev => ({
				...prev,
				subscriptions: [...prev.subscriptions, normalized]
			}));
			toast({ title: 'Success', description: 'Subscription created successfully.' });
		} catch (err) {
			console.error('Error creating subscription:', err);
			toast({ title: 'Error', description: 'Failed to create subscription.' });
		}
	};

	return (
		<div className="space-y-6">
			<div className="flex items-center justify-between gap-3">
				<div className="flex items-center gap-3">
					<div className="relative w-[400px]">
						<Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
						<input
							type="text"
							placeholder="Search subscriptions..."
							value={searchTerm}
							onChange={(e) => setSearchTerm(e.target.value)}
							className="w-full pl-10 pr-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-700 dark:text-white"
						/>
					</div>
					<select
						value={statusFilter}
						onChange={(e) => setStatusFilter(e.target.value)}
						className="w-[180px] px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-700 dark:text-white"
					>
						<option value="all">All Status</option>
						<option value="ACTIVE">Active</option>
						<option value="EXPIRED">Expired</option>
						<option value="PENDING">Pending</option>
						<option value="CANCELLED">Cancelled</option>
					</select>
				</div>
				<button
					onClick={exportToCSV}
					className="inline-flex items-center justify-center w-[150px] px-4 py-2 bg-green-600 hover:bg-green-700 text-white rounded-lg transition-colors"
				>
					<FileText className="w-4 h-4 mr-2" />
					Export CSV
				</button>
			</div>

			<div className="bg-white dark:bg-gray-800 rounded-xl shadow-sm border border-gray-200 dark:border-gray-700 overflow-hidden">
				<div className="overflow-x-auto">
					<table className="w-full divide-y divide-gray-200 dark:divide-gray-700">
						<thead className="bg-gray-50 dark:bg-gray-900/50">
							<tr>
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">User Email</th>
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Plan Name</th>
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Status</th>
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Auto Renew</th>
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Actions</th>
							</tr>
						</thead>
						<tbody className="bg-white dark:bg-gray-800 divide-y divide-gray-200 dark:divide-gray-700">
							{filteredSubscriptions.map((sub) => (
								<tr key={sub.id} className="hover:bg-gray-50 dark:hover:bg-gray-700/50">
									<td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-gray-300">{sub.userEmail}</td>
									<td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-gray-300">{sub.planName}</td>
									<td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-300">{sub.status}</td>
									<td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-300">
										{sub.auto_renew ? 'Yes' : 'No'}
									</td>
									<td className="px-6 py-4 whitespace-nowrap">
										<TableActions
											data={sub}
											entityType="subscription"
											onUpdate={handleUpdateSubscription}
											onDelete={handleDeleteSubscription}
										/>
									</td>
								</tr>
							))}
						</tbody>
					</table>
				</div>
			</div>

			<FieldModal
				isOpen={newSubscriptionModalOpen}
				onClose={() => setNewSubscriptionModalOpen(false)}
				title="Create New Subscription"
				data={emptySubscriptionData}
				fields={subscriptionFields}
				mode="edit"
				onSave={handleCreateSubscription}
			/>
		</div>
	);
};

export default Subscriptions;