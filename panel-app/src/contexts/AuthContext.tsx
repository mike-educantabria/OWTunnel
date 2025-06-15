import {
	createContext,
	useContext,
	useState,
	useEffect,
	type ReactNode,
} from "react";
import {
	login,
	saveSession,
	clearSession,
	getUser,
} from "@/lib/auth";
import type { User } from "@/types/auth";

interface AuthContextValue {
	user: User | null;
	isLoading: boolean;
	login: (email: string, password: string) => Promise<boolean>;
	logout: () => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function useAuth(): AuthContextValue {
	const context = useContext(AuthContext);
	if (!context) throw new Error("useAuth must be used within an AuthProvider");
	return context;
}

interface AuthProviderProps {
	children: ReactNode;
}

const allowedRoles = ["SUPPORT", "ADMINISTRATOR"];

export const AuthProvider = ({ children }: AuthProviderProps) => {
	const [user, setUser] = useState<User | null>(null);
	const [isLoading, setIsLoading] = useState(true);

	useEffect(() => {
		const storedUser = getUser();
		if (storedUser) setUser(storedUser);
		setIsLoading(false);
	}, []);

	const handleLogin = async (email: string, password: string): Promise<boolean> => {
		setIsLoading(true);
		try {
			const response = await login({ email, password });

			if (!allowedRoles.includes(response.user.role)) {
				return false;
			}

			saveSession(response);
			setUser(response.user);
			return true;
		} catch {
			return false;
		} finally {
			setIsLoading(false);
		}
	};

	const handleLogout = () => {
		clearSession();
		setUser(null);
	};

	const value: AuthContextValue = {
		user,
		isLoading,
		login: handleLogin,
		logout: handleLogout,
	};

	return (
		<AuthContext.Provider value={value}>
			{children}
		</AuthContext.Provider>
	);
};