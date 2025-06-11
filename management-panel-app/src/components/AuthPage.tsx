import React, { useState, useCallback } from "react";
import { useAuth } from "@/contexts/AuthContext";
import { toast } from "@/hooks/useToast";

const AuthPage = () => {
	const { login } = useAuth();
	const [email, setEmail] = useState("");
	const [password, setPassword] = useState("");
	const [isLoading, setIsLoading] = useState(false);

	const validateForm = (): boolean => {
		if (!email || !password) {
			toast({
				title: "Missing Fields",
				description: "Please fill in all fields.",
				variant: "destructive",
			});
			return false;
		}

		if (!email.includes("@")) {
			toast({
				title: "Invalid Email",
				description: "Please enter a valid email address.",
				variant: "destructive",
			});
			return false;
		}

		return true;
	};

	const handleSubmit = useCallback(
		async (e: React.FormEvent<HTMLFormElement>) => {
			e.preventDefault();
			if (!validateForm()) return;

			setIsLoading(true);
			const success = await login(email, password);

			if (!success) {
				toast({
					title: "Login Failed",
					description: "Invalid email or password.",
					variant: "destructive",
				});
			}
			setIsLoading(false);
		},
		[email, password, login]
	);

	return (
		<div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-indigo-100 dark:from-gray-900 dark:to-gray-800">
			<div className="max-w-md w-full mx-4">
				<div className="bg-white dark:bg-gray-800 rounded-2xl shadow-xl p-8">
					<div className="text-center mb-8">
						<div className="w-16 h-16 bg-gradient-to-r from-blue-500 to-indigo-600 rounded-xl mx-auto mb-4 flex items-center justify-center">
							<span className="text-white font-bold text-xl">OW</span>
						</div>
						<h1 className="text-3xl font-bold text-gray-900 dark:text-white mb-2">OWTunnel</h1>
						<p className="text-gray-600 dark:text-gray-300">
							Sign in to access the management panel
						</p>
					</div>

					<form onSubmit={handleSubmit} className="space-y-6">
						<div>
							<label
								htmlFor="email"
								className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2"
							>
								Email Address
							</label>
							<input
								id="email"
								type="text"
								value={email}
								onChange={(e) => setEmail(e.target.value)}
								className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:outline-none dark:bg-gray-700 dark:text-white transition-colors"
								placeholder="Enter your email"
								autoComplete="email"
							/>
						</div>

						<div>
							<label
								htmlFor="password"
								className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2"
							>
								Password
							</label>
							<input
								id="password"
								type="password"
								value={password}
								onChange={(e) => setPassword(e.target.value)}
								className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:outline-none dark:bg-gray-700 dark:text-white transition-colors"
								placeholder="Enter your password"
								autoComplete="current-password"
							/>
						</div>

						<button
							type="submit"
							disabled={isLoading}
							className="w-full bg-gradient-to-r from-blue-500 to-indigo-600 text-white px-4 py-2 rounded-lg font-medium hover:from-blue-600 hover:to-indigo-700 focus:outline-none dark:focus:ring-blue-800 transition-all disabled:opacity-50"
						>
							{isLoading ? "Signing in..." : "Sign In"}
						</button>
					</form>
				</div>
			</div>
		</div>
	);
};

export default AuthPage;