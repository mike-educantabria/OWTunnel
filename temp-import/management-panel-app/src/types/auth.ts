export interface AuthCredentials {
	email: string;
	password: string;
}

export interface AuthResponse {
	token: string;
	user: User;
}

export interface User {
	id: number;
	email: string;
	firstName: string;
	lastName: string;
	locale: string;
	role: "USER" | "SUPPORT" | "ADMINISTRATOR";
}