import React, { useState, useMemo } from 'react';
import { Plus, Search, FileText } from 'lucide-react';
import { useRefresh } from '@/contexts/RefreshContext';
import TableActions from '@/components/TableActions';
import FieldModal from './FieldModal';
import { useGlobalDataContext } from '@/contexts/GlobalDataContext';
import { toast } from '@/hooks/useToast';
import api from '@/lib/api';

type Server = {
	id: number;
	country: string;
	city: string;
	hostname: string;
	ip_address: string;
	config_file_url: string;
	is_free: boolean;
	is_active: boolean;
	created_at: string;
	updated_at: string;
};

const Servers = () => {
	const { refreshTrigger, triggerRefresh } = useRefresh();
	const { data, setData } = useGlobalDataContext();
	const [searchTerm, setSearchTerm] = useState('');
	const [statusFilter, setStatusFilter] = useState('all');
	const [newServerModalOpen, setNewServerModalOpen] = useState(false);

	const servers: Server[] = (data.servers ?? []).map((s: any) => ({
		id: s.id,
		country: s.country ?? '',
		city: s.city ?? '',
		hostname: s.hostname ?? '',
		ip_address: s.ip_address ?? s.ipAddress ?? '',
		config_file_url: s.config_file_url ?? s.configFileUrl ?? '',
		is_free: typeof s.is_free !== 'undefined'
			? !!s.is_free
			: typeof s.isFree !== 'undefined'
				? !!s.isFree
				: false,
		is_active: typeof s.is_active !== 'undefined'
			? !!s.is_active
			: typeof s.isActive !== 'undefined'
				? !!s.isActive
				: false,
		created_at: s.created_at ?? s.createdAt ?? '',
		updated_at: s.updated_at ?? s.updatedAt ?? '',
	}));

	const filteredServers = useMemo(() => {
		return servers.filter(server => {
			const location = `${server.city}, ${server.country}`;
			const matchesSearch =
				searchTerm === '' ||
				location.toLowerCase().includes(searchTerm.toLowerCase()) ||
				server.hostname.toLowerCase().includes(searchTerm.toLowerCase()) ||
				server.ip_address.includes(searchTerm) ||
				server.id.toString().includes(searchTerm);

			const matchesStatus =
				statusFilter === 'all' ||
				(statusFilter === 'active' && server.is_active) ||
				(statusFilter === 'inactive' && !server.is_active);

			return matchesSearch && matchesStatus;
		});
	}, [servers, searchTerm, statusFilter]);

	const exportToCSV = () => {
		const headers = ['ID', 'Country', 'City', 'Hostname', 'IP Address', 'Config File URL', 'Is Free', 'Is Active', 'Created At', 'Updated At'];
		const csvContent = [
			headers.join(','),
			...filteredServers.map(server => [
				server.id,
				server.country,
				server.city,
				server.hostname,
				server.ip_address,
				server.config_file_url,
				server.is_free ? 'Yes' : 'No',
				server.is_active ? 'Yes' : 'No',
				server.created_at,
				server.updated_at
			].join(','))
		].join('\n');

		const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
		const link = document.createElement('a');
		link.href = URL.createObjectURL(blob);
		link.download = 'servers.csv';
		document.body.appendChild(link);
		link.click();
		document.body.removeChild(link);
	};

	const handleUpdateServer = async (updatedData: Record<string, any>) => {
		try {
			const updatedServer: Server = {
				id: Number(updatedData.id),
				country: updatedData.country ?? '',
				city: updatedData.city ?? '',
				hostname: updatedData.hostname ?? '',
				ip_address: updatedData.ip_address ?? '',
				config_file_url: updatedData.config_file_url ?? '',
				is_free: updatedData.is_free === true || updatedData.is_free === 'true',
				is_active: updatedData.is_active === true || updatedData.is_active === 'true',
				created_at: updatedData.created_at ?? '',
				updated_at: updatedData.updated_at ?? '',
			};
			const payload = {
				country: updatedServer.country,
				city: updatedServer.city,
				hostname: updatedServer.hostname,
				ipAddress: updatedServer.ip_address,
				configFileUrl: updatedServer.config_file_url,
				is_free: updatedServer.is_free,
				is_active: updatedServer.is_active,
			};
			const response = await api.put(`/vpn-servers/${updatedServer.id}`, payload);
			triggerRefresh();
			const updated = response.data;
			const normalized: Server = {
				id: updated.id,
				country: updated.country ?? '',
				city: updated.city ?? '',
				hostname: updated.hostname ?? '',
				ip_address: updated.ip_address ?? updated.ipAddress ?? '',
				config_file_url: updated.config_file_url ?? updated.configFileUrl ?? '',
				is_free: !!updated.is_free,
				is_active: !!updated.is_active,
				created_at: updated.created_at ?? updated.createdAt ?? '',
				updated_at: updated.updated_at ?? updated.updatedAt ?? '',
			};
			setData(prev => ({
				...prev,
				servers: prev.servers.map((server: Server) =>
					server.id === normalized.id ? { ...server, ...normalized } : server
				)
			}));
			toast({ title: 'Success', description: 'Server updated successfully.' });
		} catch (err) {
			console.error('Update failed:', err);
			toast({ title: 'Error', description: 'Failed to update server.' });
		}
	};

	const handleDeleteServer = async (id: string) => {
		try {
			const serverId = Number(id);
			await api.delete(`/vpn-servers/${serverId}`);
			triggerRefresh();
			setData(prev => ({
				...prev,
				servers: prev.servers.filter((server: Server) => server.id !== serverId)
			}));
		} catch (error) {
			throw error;
		}
	};

	const handleCreateServer = async (newServerData: Record<string, any>) => {
		try {
			const payload = {
				country: newServerData.country || '',
				city: newServerData.city || '',
				hostname: newServerData.hostname || '',
				ipAddress: newServerData.ip_address || '',
				configFileUrl: newServerData.config_file_url || '',
				is_free: newServerData.is_free === true || newServerData.is_free === 'true',
				is_active: newServerData.is_active === true || newServerData.is_active === 'true',
			};
			const response = await api.post('/vpn-servers', payload);
			triggerRefresh();
			const created = response.data;
			const normalizedServer: Server = {
				id: created.id,
				country: created.country ?? '',
				city: created.city ?? '',
				hostname: created.hostname ?? '',
				ip_address: created.ip_address ?? created.ipAddress ?? '',
				config_file_url: created.config_file_url ?? created.configFileUrl ?? '',
				is_free: !!created.is_free,
				is_active: !!created.is_active,
				created_at: created.created_at ?? created.createdAt ?? '',
				updated_at: created.updated_at ?? created.updatedAt ?? '',
			};
			setData(prev => ({
				...prev,
				servers: [...prev.servers, normalizedServer]
			}));
			toast({ title: 'Success', description: 'Server created successfully.' });
		} catch (err) {
			console.error('Creation failed:', err);
			toast({ title: 'Error', description: 'Failed to create server.' });
		}
	};

	const newServerFields = [
		{ key: 'country', label: 'Country', type: 'text' as const },
		{ key: 'city', label: 'City', type: 'text' as const },
		{ key: 'hostname', label: 'Hostname', type: 'text' as const },
		{ key: 'ip_address', label: 'IP Address', type: 'text' as const },
		{ key: 'config_file_url', label: 'Config File URL', type: 'text' as const },
		{
			key: 'is_free', label: 'Free', type: 'select' as const, options: [
				{ value: 'true', label: 'Yes' },
				{ value: 'false', label: 'No' }
			]
		},
		{
			key: 'is_active', label: 'Active', type: 'select' as const, options: [
				{ value: 'true', label: 'Yes' },
				{ value: 'false', label: 'No' }
			]
		},
	];

	const emptyServerData = {
		country: '', city: '', hostname: '', ip_address: '', config_file_url: '',
		is_free: 'true', is_active: 'true',
	};

	return (
		<div className="space-y-6">
			<div className="flex items-center justify-between gap-3">
				<div className="flex items-center gap-3">
					<div className="relative w-[400px]">
						<Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
						<input
							type="text"
							placeholder="Search servers..."
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
						onClick={() => setNewServerModalOpen(true)}
						className="inline-flex items-center justify-center w-[150px] px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg transition-colors"
					>
						<Plus className="w-4 h-4 mr-2" />
						New Server
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
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Location</th>
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Hostname</th>
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">IP Address</th>
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Is Free</th>
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Is Active</th>
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Actions</th>
							</tr>
						</thead>
						<tbody className="bg-white dark:bg-gray-800 divide-y divide-gray-200 dark:divide-gray-700">
							{filteredServers.map(server => (
								<tr key={server.id} className="hover:bg-gray-50 dark:hover:bg-gray-700/50">
									<td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-300">
										{server.city}, {server.country}
									</td>
									<td className="px-6 py-4 text-sm text-gray-500 dark:text-gray-300">{server.hostname}</td>
									<td className="px-6 py-4 text-sm text-gray-500 dark:text-gray-300">{server.ip_address}</td>
									<td className="px-6 py-4 text-sm text-gray-500 dark:text-gray-300">{server.is_free ? 'Yes' : 'No'}</td>
									<td className="px-6 py-4 text-sm text-gray-500 dark:text-gray-300">{server.is_active ? 'Yes' : 'No'}</td>
									<td className="px-6 py-4">
										<TableActions
											data={server}
											entityType="server"
											onUpdate={handleUpdateServer}
											onDelete={handleDeleteServer}
										/>
									</td>
								</tr>
							))}
						</tbody>
					</table>
				</div>
			</div>
			<FieldModal
				isOpen={newServerModalOpen}
				onClose={() => setNewServerModalOpen(false)}
				title="Create New Server"
				data={emptyServerData}
				fields={newServerFields}
				mode="edit"
				onSave={handleCreateServer}
			/>
		</div>
	);
};

export default Servers;