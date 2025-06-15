import React from "react";
import { Toaster } from "@/components/ui/toaster";
import { TooltipProvider } from "@/components/ui/tooltip";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import {
	BrowserRouter,
	HashRouter,
	Routes,
	Route,
	useNavigate,
} from "react-router-dom";
import { ThemeProvider } from "@/contexts/ThemeContext";
import { AuthProvider } from "@/contexts/AuthContext";
import { clearSession } from "@/lib/auth";
import PanelPage from "@/components/PanelPage";

const Router = import.meta.env.MODE === "development" ? BrowserRouter : HashRouter;

const queryClient = new QueryClient();

function NotFoundRedirect() {
	const navigate = useNavigate();
	React.useEffect(() => {
		clearSession();
		navigate("/", { replace: true });
	}, [navigate]);
	return null;
}

const App = () => (
	<ThemeProvider>
		<QueryClientProvider client={queryClient}>
			<TooltipProvider>
				<AuthProvider>
					<Toaster />
					<Router>
						<Routes>
							<Route path="/" element={<PanelPage />} />
							<Route path="*" element={<NotFoundRedirect />} />
						</Routes>
					</Router>
				</AuthProvider>
			</TooltipProvider>
		</QueryClientProvider>
	</ThemeProvider>
);

export default App;