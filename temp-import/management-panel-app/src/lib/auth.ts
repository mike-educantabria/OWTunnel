import api from "@/lib/api";
import type { AuthCredentials, AuthResponse, User } from "@/types/auth";

const TOKEN_KEY = "owtunnel_token";
const USER_KEY = "owtunnel_user";

export const login = async (
	credentials: AuthCredentials
): Promise<AuthResponse> => {
	try {
		const response = await api.post<AuthResponse>(
			"/auth/login",
			credentials
		);
		return response.data;
	} catch (error) {
		console.error("Login error:", error);
		throw new Error("Invalid credentials or server error.");
	}
};

export function saveSession(response: AuthResponse): void {
	localStorage.setItem(TOKEN_KEY, response.token);
	localStorage.setItem(USER_KEY, JSON.stringify(response.user));
}

export function clearSession(): void {
	localStorage.removeItem(TOKEN_KEY);
	localStorage.removeItem(USER_KEY);
}

export function getUser(): User | null {
	const userData = localStorage.getItem(USER_KEY);
	return userData ? JSON.parse(userData) : null;
}