import api from "@/lib/api";
import { useEffect } from "react";
import { useAuth } from "@/contexts/AuthContext";
import { useRefresh } from "@/contexts/RefreshContext";
import { useGlobalDataContext } from "@/contexts/GlobalDataContext";
import { toast } from "@/hooks/useToast";

export function useGlobalData() {
	const { user } = useAuth();
	const { refreshTrigger } = useRefresh();
	const { setData } = useGlobalDataContext();

	useEffect(() => {
		if (!user) return;

		const fetchAll = async () => {
			try {
				const [users, servers, connections, plans, subscriptions, payments] = await Promise.all([
					api.get("/users"),
					api.get("/vpn-servers"),
					api.get("/connections"),
					api.get("/plans"),
					api.get("/subscriptions"),
					api.get("/payments"),
				]);
				setData({
					users: users.data,
					servers: servers.data,
					connections: connections.data,
					plans: plans.data,
					subscriptions: subscriptions.data,
					payments: payments.data,
				});
				toast({
					title: "Data updated",
					description: "Information has been refreshed successfully",
				});
			} catch (err) {
				toast({
					title: "Error loading data",
					description: "Failed to fetch global data.",
					variant: "destructive",
				});
			}
		};

		fetchAll();
	}, [refreshTrigger, user, setData]);
}