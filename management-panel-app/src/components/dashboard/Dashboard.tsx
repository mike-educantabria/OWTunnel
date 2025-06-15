import { useGlobalDataContext } from "@/contexts/GlobalDataContext";
import {
	getUsersCountByMonth,
	getCurrentMonthYear,
	getChangeType,
	formatChange,
	getPaymentsSumByMonth
} from "./dashboardUtils";
import DashboardMetricCard from "./DashboardMetricCard";
import DashboardAlerts from "./DashboardAlerts";

const Dashboard = () => {
	const { data } = useGlobalDataContext();

	const totalUsers = data.users.length;
	const { year: thisYear, month: thisMonth } = getCurrentMonthYear();
	const lastMonth = thisMonth === 1 ? 12 : thisMonth - 1;
	const lastMonthYear = thisMonth === 1 ? thisYear - 1 : thisYear;

	const usersThisMonth = getUsersCountByMonth(data.users, thisYear, thisMonth);
	const usersLastMonth = getUsersCountByMonth(data.users, lastMonthYear, lastMonth);
	const usersChange = usersLastMonth > 0 ? ((usersThisMonth - usersLastMonth) / usersLastMonth) * 100 : usersThisMonth > 0 ? 100 : 0;
	const usersChangeType = getChangeType(usersChange);

	const subscribedUsers = data.subscriptions.filter((s: any) => s.status === "ACTIVE").length;
	function getActiveSubscriptionsCountByMonth(subs: any[], year: number, month: number) {
		return subs.filter(s => {
			const start = new Date(s.created_at ?? s.start_at);
			return s.status === "ACTIVE" && start.getFullYear() === year && start.getMonth() + 1 === month;
		}).length;
	}
	const subsThisMonth = getActiveSubscriptionsCountByMonth(data.subscriptions, thisYear, thisMonth);
	const subsLastMonth = getActiveSubscriptionsCountByMonth(data.subscriptions, lastMonthYear, lastMonth);
	const subsChange = subsLastMonth > 0 ? ((subsThisMonth - subsLastMonth) / subsLastMonth) * 100 : subsThisMonth > 0 ? 100 : 0;
	const subsChangeType = getChangeType(subsChange);

	const activeConnections = data.connections.filter((c: any) => c.status === "CONNECTED").length;
	const activeServers = data.servers.filter((s: any) => s.is_active !== undefined ? s.is_active : true).length;

	const revenueThisMonth = getPaymentsSumByMonth(data.payments, thisYear, thisMonth);
	const revenueLastMonth = getPaymentsSumByMonth(data.payments, lastMonthYear, lastMonth);
	const revenueChange = revenueLastMonth > 0 ? ((revenueThisMonth - revenueLastMonth) / revenueLastMonth) * 100 : revenueThisMonth > 0 ? 100 : 0;
	const revenueChangeType = getChangeType(revenueChange);

	const metrics = [
		{
			title: 'Total Users',
			value: totalUsers.toLocaleString(),
			change: formatChange(usersThisMonth, usersLastMonth, usersChange),
			changeType: usersChangeType,
			subtitle: usersThisMonth + ' users this month'
		},
		{
			title: 'Subscribed Users',
			value: subscribedUsers.toLocaleString(),
			change: formatChange(subsThisMonth, subsLastMonth, subsChange),
			changeType: subsChangeType,
			subtitle: subsThisMonth + ' users this month'
		},
		{
			title: 'Active Connections',
			value: activeConnections.toLocaleString(),
			change: 'Real-time',
			changeType: 'neutral',
			subtitle: null
		},
		{
			title: 'Active Servers',
			value: activeServers.toLocaleString(),
			change: 'All servers',
			changeType: 'neutral',
			subtitle: null
		},
		{
			title: 'Total Revenue',
			value: `$${revenueThisMonth.toLocaleString()}`,
			change: formatChange(revenueThisMonth, revenueLastMonth, revenueChange),
			changeType: revenueChangeType,
			subtitle: revenueThisMonth > 0 ? `$${revenueThisMonth.toLocaleString()} earned this month` : null
		},
	];

	const alerts = [
		{
			id: 1,
			type: 'info',
			message: 'Server maintenance scheduled for Tokyo server at 2:00 AM UTC',
			time: '2 hours ago'
		},
		{
			id: 2,
			type: 'warning',
			message: 'High CPU usage detected on US East server',
			time: '5 hours ago'
		},
		{
			id: 3,
			type: 'error',
			message: 'Authentication service temporarily unavailable',
			time: '1 day ago'
		},
	];

	return (
		<div className="space-y-6">
			<div className="grid grid-cols-[repeat(auto-fit,minmax(350px,1fr))] gap-6">
				{metrics.map((metric, index) => (
					<DashboardMetricCard key={index} metric={metric} />
				))}
			</div>
			<DashboardAlerts alerts={alerts} />
		</div>
	);
};

export default Dashboard;