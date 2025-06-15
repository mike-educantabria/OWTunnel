import React, { useState, useMemo } from "react";
import { useAuth } from "@/contexts/AuthContext";
import { useGlobalData } from "@/hooks/useGlobalData";
import { RefreshProvider } from "@/contexts/RefreshContext";
import { GlobalDataProvider } from "@/contexts/GlobalDataContext";
import AuthPage from "@/components/AuthPage";
import Header from "@/components/layout/Header";
import Navigation from "@/components/layout/Navigation";
import Dashboard from "@/components/dashboard/Dashboard";
import Users from "@/components/Users";
import Servers from "@/components/Servers";
import Connections from "@/components/Connections";
import Plans from "@/components/Plans";
import Subscriptions from "@/components/Subscriptions";
import Payments from "@/components/Payments";

type TabKey =
	| "dashboard"
	| "users"
	| "servers"
	| "connections"
	| "plans"
	| "subscriptions"
	| "payments";

const TAB_COMPONENTS: Record<TabKey, React.FC> = {
	dashboard: Dashboard,
	users: Users,
	servers: Servers,
	connections: Connections,
	plans: Plans,
	subscriptions: Subscriptions,
	payments: Payments,
};

const LoadingSpinner: React.FC = () => (
	<div className="min-h-screen flex items-center justify-center">
		<div className="animate-spin rounded-full h-32 w-32 border-b-2 border-blue-600"></div>
	</div>
);

const GlobalDataLoader: React.FC = () => {
	useGlobalData();
	return null;
};

const PanelPage: React.FC = () => {
	const { user, isLoading } = useAuth();
	const [activeTab, setActiveTab] = useState<TabKey>("dashboard");

	const ActiveComponent = useMemo(
		() => TAB_COMPONENTS[activeTab] || Dashboard,
		[activeTab]
	);

	const isValidTab = (tab: string): tab is TabKey => tab in TAB_COMPONENTS;

	const handleTabChange = (tab: string) => {
		if (isValidTab(tab)) {
			setActiveTab(tab);
		} else {
			setActiveTab("dashboard");
		}
	};

	if (isLoading) return <LoadingSpinner />;
	if (!user) return <AuthPage />;

	return (
		<RefreshProvider>
			<GlobalDataProvider>
				<GlobalDataLoader />
				<div className="min-h-screen bg-gray-50 dark:bg-gray-900">
					<Header />
					<Navigation activeTab={activeTab} onTabChange={handleTabChange} />
					<main className="max-w-full mx-auto px-6 py-8">
						<ActiveComponent />
					</main>
				</div>
			</GlobalDataProvider>
		</RefreshProvider>
	);
};

export default PanelPage;