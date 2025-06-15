import React, { useState, useMemo } from 'react';
import { Plus, Search, FileText } from 'lucide-react';
import TableActions from '@/components/TableActions';
import FieldModal from '@/components/FieldModal';
import { useGlobalDataContext } from '@/contexts/GlobalDataContext';
import { useRefresh } from '@/contexts/RefreshContext';
import { toast } from '@/hooks/useToast';
import api from '@/lib/api';

type Plan = {
	id: string;
	name: string;
	description: string;
	price: string;
	currency: string;
	duration_days: number;
	is_active: boolean;
	created_at: string;
	updated_at: string;
};

const planFields = [
	{ key: 'name', label: 'Name', type: 'text' as const },
	{ key: 'description', label: 'Description', type: 'textarea' as const },
	{ key: 'price', label: 'Price', type: 'number' as const },
	{
		key: 'currency', label: 'Currency', type: 'select' as const, options: [
			{ value: 'USD', label: 'USD' },
			{ value: 'EUR', label: 'EUR' }
		]
	},
	{ key: 'duration_days', label: 'Duration (days)', type: 'number' as const },
	{
		key: 'is_active', label: 'Is Active', type: 'select' as const, options: [
			{ value: 'true', label: 'Yes' },
			{ value: 'false', label: 'No' }
		]
	},
];

const emptyPlanData = {
	name: '',
	description: '',
	price: '',
	currency: 'USD',
	duration_days: '',
	is_active: 'true'
};

const Plans = () => {
	const [searchTerm, setSearchTerm] = useState('');
	const [statusFilter, setStatusFilter] = useState('all');
	const [newPlanModalOpen, setNewPlanModalOpen] = useState(false);
	const { data, setData } = useGlobalDataContext();
	const { triggerRefresh } = useRefresh();

	const plans: Plan[] = (data.plans ?? []).map((p: any) => ({
		id: String(p.id),
		name: p.name ?? '',
		description: p.description ?? '',
		price: String(p.price ?? ''),
		currency: p.currency ?? '',
		duration_days: Number(p.duration_days ?? p.durationDays ?? 0),
		is_active: !!p.is_active,
		created_at: p.created_at ?? p.createdAt ?? '',
		updated_at: p.updated_at ?? p.updatedAt ?? '',
	}));

	const filteredPlans = useMemo(() => {
		return plans.filter(plan => {
			const matchesSearch = searchTerm === '' ||
				plan.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
				plan.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
				plan.price.includes(searchTerm) ||
				plan.id.includes(searchTerm);

			const matchesStatus = statusFilter === 'all' ||
				(statusFilter === 'active' && plan.is_active) ||
				(statusFilter === 'inactive' && !plan.is_active);

			return matchesSearch && matchesStatus;
		});
	}, [plans, searchTerm, statusFilter]);

	const exportToCSV = () => {
		const headers = ['ID', 'Name', 'Description', 'Price', 'Currency', 'Duration Days', 'Is Active', 'Created At', 'Updated At'];
		const csvContent = [
			headers.join(','),
			...filteredPlans.map(plan => [
				plan.id,
				`"${plan.name}"`,
				`"${plan.description}"`,
				plan.price,
				plan.currency,
				plan.duration_days,
				plan.is_active ? 'Yes' : 'No',
				plan.created_at,
				plan.updated_at
			].join(','))
		].join('\n');

		const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
		const link = document.createElement('a');
		const url = URL.createObjectURL(blob);
		link.href = url;
		link.download = 'plans.csv';
		link.click();
		document.body.removeChild(link);
		URL.revokeObjectURL(url);
	};

	const formatPrice = (price: string, currency: string) => {
		return `${currency} ${price}`;
	};

	const handleUpdatePlan = async (updatedData: Record<string, any>) => {
		try {
			const price = Number(updatedData.price);
			const durationDays = Number(updatedData.duration_days);
			const isActive = updatedData.is_active === true || updatedData.is_active === 'true' || updatedData.is_active === 1;

			if (
				!updatedData.name ||
				!updatedData.currency ||
				isNaN(price) ||
				isNaN(durationDays)
			) {
				toast({ title: 'Error', description: 'Todos los campos son obligatorios y deben ser válidos.' });
				return;
			}

			const payload = {
				name: updatedData.name,
				description: updatedData.description,
				price,
				currency: updatedData.currency,
				durationDays,
				isActive,
			};

			const response = await api.put(`/plans/${updatedData.id}`, payload);
			triggerRefresh();
			const updated = response.data;
			const normalized: Plan = {
				id: String(updated.id),
				name: updated.name ?? '',
				description: updated.description ?? '',
				price: String(updated.price ?? ''),
				currency: updated.currency ?? '',
				duration_days: Number(updated.durationDays ?? updated.duration_days ?? 0),
				is_active: !!updated.isActive,
				created_at: updated.created_at ?? updated.createdAt ?? '',
				updated_at: updated.updated_at ?? updated.updatedAt ?? '',
			};
			setData(prev => ({
				...prev,
				plans: prev.plans.map((plan: Plan) =>
					plan.id === normalized.id ? { ...plan, ...normalized } : plan
				)
			}));
			toast({ title: 'Success', description: 'Plan updated successfully.' });
		} catch (err) {
			console.error('Update failed:', err);
			toast({ title: 'Error', description: 'Failed to update plan.' });
		}
	};

	const handleDeletePlan = async (planId: string) => {
		try {
			await api.delete(`/plans/${planId}`);
			triggerRefresh();
			setData(prev => ({
				...prev,
				plans: prev.plans.filter((plan: Plan) => plan.id !== planId)
			}));
		} catch (err) {
			throw err;
		}
	};

	const handleCreatePlan = async (newPlan: Record<string, any>) => {
		try {
			const price = Number(newPlan.price);
			const durationDays = Number(newPlan.duration_days);
			const isActive = newPlan.is_active === true || newPlan.is_active === 'true' || newPlan.is_active === 1;

			if (
				!newPlan.name ||
				!newPlan.currency ||
				isNaN(price) ||
				isNaN(durationDays)
			) {
				toast({ title: 'Error', description: 'Todos los campos son obligatorios y deben ser válidos.' });
				return;
			}

			const payload = {
				name: newPlan.name,
				description: newPlan.description,
				price,
				currency: newPlan.currency,
				durationDays,
				isActive,
			};

			const response = await api.post('/plans', payload);
			triggerRefresh();
			const created = response.data;
			const normalized: Plan = {
				id: String(created.id),
				name: created.name ?? '',
				description: created.description ?? '',
				price: String(created.price ?? ''),
				currency: created.currency ?? '',
				duration_days: Number(created.durationDays ?? created.duration_days ?? 0),
				is_active: !!created.isActive,
				created_at: created.created_at ?? created.createdAt ?? '',
				updated_at: created.updated_at ?? created.updatedAt ?? '',
			};
			setData(prev => ({
				...prev,
				plans: [...prev.plans, normalized]
			}));
			toast({ title: 'Success', description: 'Plan created successfully.' });
		} catch (err) {
			console.error('Creation failed:', err);
			toast({ title: 'Error', description: 'Failed to create plan.' });
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
							placeholder="Search plans..."
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
						<option value="active">Active</option>
						<option value="inactive">Inactive</option>
					</select>
					<button
						onClick={() => setNewPlanModalOpen(true)}
						className="inline-flex items-center justify-center w-[150px] px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg transition-colors"
					>
						<Plus className="w-4 h-4 mr-2" />
						New Plan
					</button>
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
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Name</th>
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Price</th>
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Duration Days</th>
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Is Active</th>
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Actions</th>
							</tr>
						</thead>
						<tbody className="bg-white dark:bg-gray-800 divide-y divide-gray-200 dark:divide-gray-700">
							{filteredPlans.map((plan) => (
								<tr key={plan.id} className="hover:bg-gray-50 dark:hover:bg-gray-700/50">
									<td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-300">{plan.name}</td>
									<td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-300">{formatPrice(plan.price, plan.currency)}</td>
									<td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-300">{plan.duration_days}</td>
									<td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-300">
										{plan.is_active ? 'Yes' : 'No'}
									</td>
									<td className="px-6 py-4 whitespace-nowrap">
										<TableActions
											data={plan}
											entityType="plan"
											onUpdate={handleUpdatePlan}
											onDelete={handleDeletePlan}
										/>
									</td>
								</tr>
							))}
						</tbody>
					</table>
				</div>
			</div>

			<FieldModal
				isOpen={newPlanModalOpen}
				onClose={() => setNewPlanModalOpen(false)}
				title="Create New Plan"
				data={emptyPlanData}
				fields={planFields}
				mode="edit"
				onSave={handleCreatePlan}
			/>
		</div>
	);
};

export default Plans;