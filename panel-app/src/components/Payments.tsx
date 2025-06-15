import React, { useState, useMemo } from 'react';
import { Search, FileText } from 'lucide-react';
import TableActions from '@/components/TableActions';
import { useGlobalDataContext } from '@/contexts/GlobalDataContext';
import { toast } from '@/hooks/useToast';
import { useRefresh } from '@/contexts/RefreshContext';
import api from '@/lib/api';

type Payment = {
	id: string;
	user_id: string;
	plan_id: string;
	subscription_id: string;
	userEmail: string;
	planName: string;
	amount: string;
	currency: string;
	method: string;
	status: string;
	transaction_reference: string;
	created_at: string;
	updated_at: string;
};

const paymentFields = [
	{ key: 'userEmail', label: 'User Email', type: 'email' as const },
	{ key: 'planName', label: 'Plan Name', type: 'text' as const },
	{ key: 'amount', label: 'Amount', type: 'number' as const },
	{
		key: 'currency', label: 'Currency', type: 'select' as const, options: [
			{ value: 'USD', label: 'USD' },
			{ value: 'EUR', label: 'EUR' }
		]
	},
	{
		key: 'method', label: 'Payment Method', type: 'select' as const, options: [
			{ value: 'CREDIT_CARD', label: 'Credit Card' },
			{ value: 'DEBIT_CARD', label: 'Debit Card' },
			{ value: 'PAYPAL', label: 'PayPal' },
			{ value: 'APPLE_PAY', label: 'Apple Pay' },
			{ value: 'GOOGLE_PAY', label: 'Google Pay' }
		]
	},
	{
		key: 'status', label: 'Payment Status', type: 'select' as const, options: [
			{ value: 'PENDING', label: 'Pending' },
			{ value: 'PAID', label: 'Paid' },
			{ value: 'FAILED', label: 'Failed' },
			{ value: 'CANCELED', label: 'Canceled' },
			{ value: 'REFUNDED', label: 'Refunded' }
		]
	},
	{ key: 'transaction_reference', label: 'Transaction Reference', type: 'text' as const },
];

const emptyPaymentData = {
	userEmail: '',
	planName: '',
	amount: '',
	currency: 'USD',
	method: 'CREDIT_CARD',
	status: 'PENDING',
	transaction_reference: ''
};

const Payments = () => {
	const [searchTerm, setSearchTerm] = useState('');
	const [statusFilter, setStatusFilter] = useState('all');
	const { data, setData } = useGlobalDataContext();
	const { triggerRefresh } = useRefresh();

	const users = data.users ?? [];
	const plans = data.plans ?? [];
	const subscriptions = data.subscriptions ?? [];

	const payments: Payment[] = (data.payments ?? []).map((p: any) => {
		const user = users.find((u: any) => String(u.id) === String(p.user_id ?? p.userId));
		const plan = plans.find((pl: any) => String(pl.id) === String(p.plan_id ?? p.planId));
		return {
			id: String(p.id),
			user_id: String(p.user_id ?? p.userId),
			plan_id: String(p.plan_id ?? p.planId),
			subscription_id: String(p.subscription_id ?? p.subscriptionId ?? ''),
			userEmail: user?.email ?? '',
			planName: plan?.name ?? '',
			amount: String(p.amount ?? ''),
			currency: p.currency ?? '',
			method: p.method ?? '',
			status: p.status ?? '',
			transaction_reference: p.transaction_reference ?? p.transactionReference ?? '',
			created_at: p.created_at ?? p.createdAt ?? '',
			updated_at: p.updated_at ?? p.updatedAt ?? '',
		};
	});

	const filteredPayments = useMemo(() => {
		return payments.filter(payment => {
			const matchesSearch = searchTerm === '' ||
				payment.userEmail.toLowerCase().includes(searchTerm.toLowerCase()) ||
				payment.planName.toLowerCase().includes(searchTerm.toLowerCase()) ||
				payment.transaction_reference.toLowerCase().includes(searchTerm.toLowerCase()) ||
				payment.amount.includes(searchTerm) ||
				payment.id.includes(searchTerm);

			const matchesStatus = statusFilter === 'all' || payment.status === statusFilter;

			return matchesSearch && matchesStatus;
		});
	}, [payments, searchTerm, statusFilter]);

	const exportToCSV = () => {
		const headers = ['ID', 'User ID', 'Plan ID', 'Subscription ID', 'User Email', 'Plan Name', 'Amount', 'Currency', 'Method', 'Status', 'Transaction Reference', 'Created At', 'Updated At'];
		const csvContent = [
			headers.join(','),
			...filteredPayments.map(payment => [
				payment.id,
				payment.user_id,
				payment.plan_id,
				payment.subscription_id,
				payment.userEmail,
				`"${payment.planName}"`,
				payment.amount,
				payment.currency,
				payment.method,
				payment.status,
				payment.transaction_reference,
				payment.created_at,
				payment.updated_at
			].join(','))
		].join('\n');

		const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
		const link = document.createElement('a');
		const url = URL.createObjectURL(blob);
		link.setAttribute('href', url);
		link.setAttribute('download', 'payments.csv');
		link.style.visibility = 'hidden';
		document.body.appendChild(link);
		link.click();
		document.body.removeChild(link);
		URL.revokeObjectURL(url);
	};

	const handleUpdatePayment = async (updatedData: Record<string, any>) => {
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
			const subscription = subscriptions.find((s: any) => String(s.id) === String(updatedData.subscription_id ?? updatedData.subscriptionId));
			if (!subscription) {
				toast({ title: 'Error', description: 'Subscription not found', variant: 'destructive' });
				return;
			}

			const amount = Number(updatedData.amount);
			if (isNaN(amount)) {
				toast({ title: 'Error', description: 'Amount must be a number.', variant: 'destructive' });
				return;
			}

			const payload = {
				userId: Number(user.id),
				planId: Number(plan.id),
				subscriptionId: Number(subscription.id),
				amount,
				currency: updatedData.currency,
				method: updatedData.method,
				status: updatedData.status,
				transactionReference: updatedData.transaction_reference,
			};

			const response = await api.put(`/payments/${updatedData.id}`, payload);
			triggerRefresh();
			const updated = response.data;
			const normalized: Payment = {
				id: String(updated.id),
				user_id: String(updated.userId ?? updated.user_id),
				plan_id: String(updated.planId ?? updated.plan_id),
				subscription_id: String(updated.subscriptionId ?? updated.subscription_id ?? ''),
				userEmail: user.email,
				planName: plan.name,
				amount: String(updated.amount ?? ''),
				currency: updated.currency ?? '',
				method: updated.method ?? '',
				status: updated.status ?? '',
				transaction_reference: updated.transactionReference ?? updated.transaction_reference ?? '',
				created_at: updated.created_at ?? updated.createdAt ?? '',
				updated_at: updated.updated_at ?? updated.updatedAt ?? '',
			};
			setData(prev => ({
				...prev,
				payments: prev.payments.map((pay: Payment) =>
					pay.id === normalized.id ? { ...pay, ...normalized } : pay
				)
			}));
			toast({ title: 'Success', description: 'Payment updated successfully.' });
		} catch (err) {
			console.error('Error updating payment:', err);
			toast({ title: 'Error', description: 'Failed to update payment.' });
		}
	};

	const handleDeletePayment = async (paymentId: string) => {
		try {
			await api.delete(`/payments/${paymentId}`);
			triggerRefresh();
			setData(prev => ({
				...prev,
				payments: prev.payments.filter((pay: Payment) => pay.id !== paymentId)
			}));
			toast({ title: 'Success', description: 'Payment deleted successfully.' });
		} catch (err) {
			console.error('Error deleting payment:', err);
			toast({ title: 'Error', description: 'Failed to delete payment.' });
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
							placeholder="Search payments..."
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
						<option value="PENDING">Pending</option>
						<option value="PAID">Paid</option>
						<option value="FAILED">Failed</option>
						<option value="CANCELED">Canceled</option>
						<option value="REFUNDED">Refunded</option>
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
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Amount</th>
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Method</th>
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Status</th>
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Actions</th>
							</tr>
						</thead>
						<tbody className="bg-white dark:bg-gray-800 divide-y divide-gray-200 dark:divide-gray-700">
							{filteredPayments.map((payment) => (
								<tr key={payment.id} className="hover:bg-gray-50 dark:hover:bg-gray-700/50">
									<td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-300">{payment.userEmail}</td>
									<td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-300">{payment.planName}</td>
									<td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-300">{payment.currency} {payment.amount}</td>
									<td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-300">{payment.method}</td>
									<td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-300">{payment.status}</td>
									<td className="px-6 py-4 whitespace-nowrap">
										<TableActions
											data={payment}
											entityType="payment"
											onUpdate={handleUpdatePayment}
											onDelete={handleDeletePayment}
										/>
									</td>
								</tr>
							))}
						</tbody>
					</table>
				</div>
			</div>
		</div>
	);
};

export default Payments;