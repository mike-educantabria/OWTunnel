import React, { useState, useMemo } from 'react';
import { Search, FileText } from 'lucide-react';
import { useRefresh } from '@/contexts/RefreshContext';
import TableActions from '@/components/TableActions';
import { useGlobalDataContext } from '@/contexts/GlobalDataContext';
import api from '@/lib/api';

type User = {
	id: string;
	email: string;
	first_name: string;
	last_name: string;
	locale: string;
	role: string;
	created_at: string;
	updated_at: string;
};

const Users = () => {
	const [searchTerm, setSearchTerm] = useState('');
	const [roleFilter, setRoleFilter] = useState('all');
	const { refreshTrigger, triggerRefresh } = useRefresh();
	const { data, setData } = useGlobalDataContext();

	const users: User[] = data.users.map((u: any) => ({
		id: u.id,
		email: u.email ?? '',
		first_name: u.first_name ?? u.firstName ?? '',
		last_name: u.last_name ?? u.lastName ?? '',
		locale: u.locale ?? '',
		role: u.role ?? '',
		created_at: u.created_at ?? u.createdAt ?? '',
		updated_at: u.updated_at ?? u.updatedAt ?? '',
	}));

	const filteredUsers = useMemo(() => {
		const lowerSearch = searchTerm.toLowerCase();
		return users.filter(user => {
			const email = user.email ?? '';
			const role = user.role ?? '';
			const matchesSearch = email.toLowerCase().includes(lowerSearch);
			const matchesRole = roleFilter === 'all' || role === roleFilter;
			return matchesSearch && matchesRole;
		});
	}, [users, searchTerm, roleFilter]);

	const exportToCSV = () => {
		const headers = ['ID', 'Email', 'First Name', 'Last Name', 'Locale', 'Role', 'Created At', 'Updated At'];
		const csvContent = [
			headers.join(','),
			...filteredUsers.map(user =>
				[
					user.id,
					user.email,
					user.first_name,
					user.last_name,
					user.locale,
					user.role,
					user.created_at,
					user.updated_at,
				].join(',')
			)
		].join('\n');

		const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
		const link = document.createElement('a');
		const url = URL.createObjectURL(blob);
		link.href = url;
		link.download = 'users.csv';
		link.style.display = 'none';
		document.body.appendChild(link);
		link.click();
		document.body.removeChild(link);
		URL.revokeObjectURL(url);
	};

	const handleUpdateUser = async (updatedUser: User & { password?: string }) => {
		try {
			const payload: any = {
				email: updatedUser.email,
				firstName: updatedUser.first_name,
				lastName: updatedUser.last_name,
				locale: updatedUser.locale,
				role: updatedUser.role,
			};
			if (updatedUser.password && updatedUser.password.trim() !== '') {
				payload.password = updatedUser.password;
			}
			const response = await api.put(`/users/${updatedUser.id}`, payload);
			triggerRefresh();
			const updated = response.data;
			const normalizedUpdated: User = {
				id: updated.id,
				email: updated.email,
				first_name: updated.firstName,
				last_name: updated.lastName,
				locale: updated.locale,
				role: updated.role,
				created_at: updated.createdAt,
				updated_at: updated.updatedAt,
			};
			setData(prev => ({
				...prev,
				users: prev.users.map(user => user.id === normalizedUpdated.id ? { ...user, ...normalizedUpdated } : user)
			}));
		} catch (error) {
			console.error('Error updating user:', error);
		}
	};

	const handleDeleteUser = async (userId: string) => {
		try {
			await api.delete(`/users/${userId}`);
			triggerRefresh();
			setData(prev => ({
				...prev,
				users: prev.users.filter(user => user.id !== userId)
			}));
		} catch (error) {
			throw error;
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
							placeholder="Search users..."
							value={searchTerm}
							onChange={e => setSearchTerm(e.target.value)}
							className="w-full pl-10 pr-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-700 dark:text-white"
						/>
					</div>
					<select
						value={roleFilter}
						onChange={e => setRoleFilter(e.target.value)}
						className="w-[180px] pl-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-700 dark:text-white"
					>
						<option value="all">All Roles</option>
						<option value="USER">User</option>
						<option value="SUPPORT">Support</option>
						<option value="ADMINISTRATOR">Administrator</option>
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
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Email</th>
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Full Name</th>
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Locale</th>
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Role</th>
								<th className="px-6 py-3 text-left text-xs font-medium text-gray-900 dark:text-gray-100 uppercase tracking-wider">Actions</th>
							</tr>
						</thead>
						<tbody className="bg-white dark:bg-gray-800 divide-y divide-gray-200 dark:divide-gray-700">
							{filteredUsers.map(user => (
								<tr key={user.id} className="hover:bg-gray-50 dark:hover:bg-gray-700/50">
									<td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-300">{user.email}</td>
									<td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-300">{user.first_name} {user.last_name}</td>
									<td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-300">{user.locale}</td>
									<td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-300">{user.role}</td>
									<td className="px-6 py-4 whitespace-nowrap">
										<TableActions
											data={user}
											entityType="user"
											onUpdate={updatedData => {
												const userToUpdate: User & { password?: string } = {
													...user,
													...updatedData,
												};
												handleUpdateUser(userToUpdate);
											}}
											onDelete={handleDeleteUser}
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

export default Users;