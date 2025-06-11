import React from 'react';
import { useAuth } from '@/contexts/AuthContext';
import { useTheme } from '@/contexts/ThemeContext';
import { useRefresh } from '@/contexts/RefreshContext';
import { useToast } from '@/hooks/useToast';
import { Sun, Moon, LogOut, RefreshCw } from 'lucide-react';

const Header: React.FC = () => {
	const { user, logout } = useAuth();
	const { isDark, toggleTheme } = useTheme();
	const { triggerRefresh } = useRefresh();
	const { toast } = useToast();

	const handleRefresh = () => {
		triggerRefresh();
		toast({
			title: "Data updated",
			description: "Information has been refreshed successfully"
		});
	};

	const handleToggleTheme = () => {
		toggleTheme();
		toast({
			title: isDark ? "Switched to light mode" : "Switched to dark mode",
			description: isDark
				? "Light mode is now active."
				: "Dark mode is now active."
		});
	};

	const handleLogout = () => {
		logout();
		toast({
			title: "Signed out",
			description: "You have been signed out successfully."
		});
	};

	return (
		<header className="bg-white dark:bg-gray-800 shadow-sm border-b border-gray-200 dark:border-gray-700">
			<div className="max-w-full mx-auto px-6 py-4">
				<div className="flex items-center justify-between">
					{/* Logo and title */}
					<div className="flex items-center space-x-4">
						<div className="w-10 h-10 bg-gradient-to-r from-blue-500 to-indigo-600 rounded-lg flex items-center justify-center">
							<span className="text-white font-bold text-sm">OW</span>
						</div>
						<h1 className="font-bold text-gray-900 dark:text-white text-xl">
							Management Panel
						</h1>
					</div>

					{/* Actions */}
					<div className="flex items-center space-x-4">
						<button
							onClick={handleRefresh}
							className="p-2 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors"
							title="Refresh"
							aria-label="Refresh data"
							type="button"
						>
							<RefreshCw className="w-5 h-5 text-gray-600 dark:text-gray-300" />
						</button>

						<button
							onClick={handleToggleTheme}
							className="p-2 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors"
							title={isDark ? 'Switch to light mode' : 'Switch to dark mode'}
							aria-label="Toggle theme"
							type="button"
						>
							{isDark ? (
								<Sun className="w-5 h-5 text-gray-600 dark:text-gray-300" />
							) : (
								<Moon className="w-5 h-5 text-gray-600 dark:text-gray-300" />
							)}
						</button>

						<button
							onClick={handleLogout}
							className="flex items-center space-x-2 px-4 py-2 rounded-lg transition-colors text-white font-medium bg-red-600 hover:bg-red-500"
							title="Sign out"
							aria-label="Sign out"
							type="button"
						>
							<LogOut className="w-4 h-4" />
							<span>Sign out</span>
						</button>
					</div>
				</div>
			</div>
		</header>
	);
};

export default Header;