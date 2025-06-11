type Metric = {
	title: string;
	value: string;
	change: string;
	changeType: string;
	subtitle: string | number | null;
};

const getChangeColor = (type: string) => {
	switch (type) {
		case "positive":
			return "text-green-600 dark:text-green-400";
		case "negative":
			return "text-red-600 dark:text-red-400";
		default:
			return "text-gray-500 dark:text-gray-400";
	}
};

const DashboardMetricCard = ({ metric }: { metric: Metric }) => (
	<div className="bg-white dark:bg-gray-800 rounded-xl p-6 shadow-sm border border-gray-200 dark:border-gray-700">
		<h3 className="text-sm font-medium text-gray-500 dark:text-gray-400 mb-2">{metric.title}</h3>
		<div className="flex flex-col items-start w-full">
			<div className="flex items-center w-full justify-between">
				<span className="text-3xl font-bold text-gray-900 dark:text-white">{metric.value}</span>
				<div className="flex flex-col items-end">
					{metric.change && (
						<span className={`text-sm font-medium ${getChangeColor(metric.changeType)}`}>
							{metric.change}
						</span>
					)}
					{metric.change && metric.subtitle !== null && (
						<span className="block text-xs text-gray-500 dark:text-gray-400 mt-1 lowercase">
							({metric.subtitle})
						</span>
					)}
				</div>
			</div>
		</div>
	</div>
);

export default DashboardMetricCard;