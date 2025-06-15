import React from 'react';

interface Tab {
	id: string;
	label: string;
}

interface NavigationProps {
	activeTab: string;
	onTabChange: (tab: string) => void;
}

const tabs: Tab[] = [
	{ id: 'dashboard', label: 'Dashboard' },
	{ id: 'users', label: 'Users' },
	{ id: 'servers', label: 'VPN Servers' },
	{ id: 'connections', label: 'Connections' },
	{ id: 'plans', label: 'Plans' },
	{ id: 'subscriptions', label: 'Subscriptions' },
	{ id: 'payments', label: 'Payments' },
];

const Navigation: React.FC<NavigationProps> = ({ activeTab, onTabChange }) => (
	<nav
		className="bg-white dark:bg-gray-800 border-b border-gray-200 dark:border-gray-700"
		aria-label="Main navigation"
	>
		<div className="max-w-full mx-auto px-6">
			<div className="flex" role="tablist">
				{tabs.map((tab) => {
					const isActive = activeTab === tab.id;
					return (
						<button
							key={tab.id}
							role="tab"
							aria-selected={isActive}
							aria-controls={`tabpanel-${tab.id}`}
							id={`tab-${tab.id}`}
							tabIndex={isActive ? 0 : -1}
							onClick={() => onTabChange(tab.id)}
							className={`flex-1 px-6 py-4 text-sm font-medium border-b-2 transition-colors focus:outline-none ${isActive
								? 'border-blue-500 text-blue-600 dark:text-blue-400'
								: 'border-transparent text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-300 hover:border-gray-300'
								}`}
						>
							{tab.label}
						</button>
					);
				})}
			</div>
		</div>
	</nav>
);

export default Navigation;