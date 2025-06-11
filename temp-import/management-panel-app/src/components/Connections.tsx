import React, { useState, useMemo } from 'react';
import { Search, FileText } from 'lucide-react';
import TableActions from '@/components/TableActions';
import { useRefresh } from '@/contexts/RefreshContext';
import { useGlobalDataContext } from '@/contexts/GlobalDataContext';
import { toast } from '@/hooks/useToast';
import api from '@/lib/api';

type Connection = {
	id: string;
	user_id: string;
	vpn_server_id: string;
	userEmail: string;
	serverLocation: string;
	device_info: string;
	status: string;
	created_at: string;
	updated_at: string;
};

const Connections = () => {
	const [searchTerm, setSearchTerm] = useState('');
	const [statusFilter, setStatusFilter] = useState('all');
	const { triggerRefresh } = useRefresh();
	const { data, setData } = useGlobalDataContext();

	const users = data.users ?? [];
	const servers = data.servers ?? [];

	const connections: Connection[] = (data.connections ?? []).map((c: any) => {
		const user = users.find((u: any) => String(u.id) === String(c.user_id ?? c.userId));
		const server = servers.find((s: any) => String(s.id) === String(c.vpn_server_id ?? c.vpnServerId));
		return {
			id: String(c.id),
			user_id: String(c.user_id ?? c.userId),
			vpn_server_id: String(c.vpn_server_id ?? c.vpnServerId),
			userEmail: user?.email ?? '',
			serverLocation: server ? `${server.city}, ${server.country}` : '',
			device_info: c.device_info ?? c.deviceInfo ?? '',
			status: c.status ?? '',
			created_at: c.created_at ?? c.createdAt ?? '',
			updated_at: c.updated_at ?? c.updatedAt ?? '',
		};
	});

	const filteredConnections = useMemo(() => {
		const search = searchTerm.toLowerCase();
		return connections.filter(connection =>
			(connection.userEmail.toLowerCase().includes(search) ||
				connection.serverLocation.toLowerCase().includes(search) ||
				connection.device_info.toLowerCase().includes(search) ||
				connection.id.toString().includes(search)) &&
			(statusFilter === 'all' || connection.status === statusFilter)
		);
	}, [connections, searchTerm, statusFilter]);

	const exportToCSV = () => {
		const headers = ['ID', 'User ID', 'VPN Server ID', 'User Email', 'Server Location', 'Device Info', 'Status', 'Created At', 'Updated At'];
		const csvContent = [
			headers.join(','),
			...filteredConnections.map(conn => [
				conn.id,
				conn.user_id,
				conn.vpn_server_id,
				conn.userEmail,
				`"${conn.serverLocation}"`,
				`"${conn.device_info}"`,
				conn.status,
				conn.created_at,
				conn.updated_at,
			].join(','))
		].join('\n');

		const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
		const link = document.createElement('a');
		const url = URL.createObjectURL(blob);
		link.href = url;
		link.download = 'connections.csv';
		link.click();
		document.body.removeChild(link);
		URL.revokeObjectURL(url);
	};

	const handleUpdateConnection = async (updatedData: Record<string, any>) => {
		try {
			const user = users.find((u: any) => u.email === updatedData.userEmail);
			if (!user) {
				toast({ title: 'Error', description: 'User not found', variant: 'destructive' });
				return;
			}
			const server = servers.find((s: any) => `${s.city}, ${s.country}` === updatedData.serverLocation);
			if (!server) {
				toast({ title: 'Error', description: 'Server not found', variant: 'destructive' });
				return;
			}

			const payload = {
				userId: Number(user.id),
				vpnServerId: Number(server.id),
				deviceInfo: updatedData.device_info ?? updatedData.deviceInfo ?? '',
				status: updatedData.status,
			};

			await api.put(`/connections/${updatedData.id}`, payload);
			triggerRefresh();
			toast({ title: 'Success', description: 'Connection updated successfully.' });
		} catch (err) {
			console.error('Error updating connection:', err);
			toast({ title: 'Error', description: 'Failed to update connection.', variant: 'destructive' });
		}
	};

	const handleDeleteConnection = async (connectionId: string) => {
		try {
			await api.delete(`/connections/${connectionId}`);
			triggerRefresh();
			setData(prev => ({
				...prev,
				connections: prev.connections.filter((c: Connection) => c.id !== connectionId)
			}));
		} catch (err) {
			throw err;
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
							placeholder="Search connections..."
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
						<option value="CONNECTED">CONNECTED</option>
						<option value="DISCONNECTED">DISCONNECTED</option>
						<option value="TIMEOUT">TIMEOUT</option>
						<option value="REJECTED">REJECTED</option>
						<option value="ERROR">ERROR</option>
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
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">User</th>
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Server Location</th>
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Device Info</th>
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Status</th>
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Actions</th>
							</tr>
						</thead>
						<tbody className="bg-white dark:bg-gray-800 divide-y divide-gray-200 dark:divide-gray-700">
							{filteredConnections.map((connection) => (
								<tr key={connection.id} className="hover:bg-gray-50 dark:hover:bg-gray-700/50">
									<td className="px-6 py-4 text-sm text-gray-500 dark:text-gray-300">{connection.userEmail}</td>
									<td className="px-6 py-4 text-sm text-gray-500 dark:text-gray-300">{connection.serverLocation}</td>
									<td className="px-6 py-4 text-sm text-gray-500 dark:text-gray-300">{connection.device_info}</td>
									<td className="px-6 py-4 text-sm text-gray-500 dark:text-gray-300">{connection.status}</td>
									<td className="px-6 py-4">
										<TableActions
											data={connection}
											entityType="connection"
											onUpdate={handleUpdateConnection}
											onDelete={handleDeleteConnection}
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

export default Connections;