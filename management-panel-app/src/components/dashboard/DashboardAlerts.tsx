type Alert = {
	id: number;
	type: string;
	message: string;
	time: string;
};

const getAlertColor = (type: string) => {
	switch (type) {
		case 'info':
			return 'text-blue-600 dark:text-blue-400';
		case 'warning':
			return 'text-yellow-600 dark:text-yellow-400';
		case 'error':
			return 'text-red-600 dark:text-red-400';
		default:
			return 'text-gray-600 dark:text-gray-400';
	}
};

const DashboardAlerts = ({ alerts }: { alerts: Alert[] }) => (
	<div className="bg-white dark:bg-gray-800 rounded-xl shadow-sm border border-gray-200 dark:border-gray-700">
		<div className="p-6">
			<h2 className="text-xl font-semibold text-gray-900 dark:text-white mb-4">Recent Alerts</h2>
			<div className="space-y-4">
				{alerts.map((alert) => (
					<div key={alert.id} className="flex items-center space-x-4 p-4 rounded-lg bg-gray-50 dark:bg-gray-700/50">
						<span className={`w-20 h-8 px-2 py-1 text-xs font-medium rounded-full text-center flex items-center justify-center ${getAlertColor(alert.type)}`}>
							{alert.type.toUpperCase()}
						</span>
						<div className="flex-1">
							<p className="text-gray-900 dark:text-white">{alert.message}</p>
							<p className="text-sm text-gray-500 dark:text-gray-400 mt-1">{alert.time}</p>
						</div>
					</div>
				))}
			</div>
		</div>
	</div>
);

export default DashboardAlerts;