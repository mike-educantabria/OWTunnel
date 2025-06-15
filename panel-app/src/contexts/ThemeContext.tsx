import {
	createContext,
	useContext,
	useState,
	useEffect,
	type ReactNode,
} from "react";

interface ThemeContextType {
	isDark: boolean;
	toggleTheme: () => void;
}

const ThemeContext = createContext<ThemeContextType | undefined>(undefined);

export function useTheme(): ThemeContextType {
	const context = useContext(ThemeContext);
	if (!context) {
		throw new Error("useTheme must be used within a ThemeProvider");
	}
	return context;
}

interface ThemeProviderProps {
	children: ReactNode;
}

export function ThemeProvider({ children }: ThemeProviderProps) {
	const [isDark, setIsDark] = useState(false);

	useEffect(() => {
		if (typeof window === "undefined") return;
		const saved = window.localStorage.getItem("owtunnel_theme");
		const prefersDark = window.matchMedia("(prefers-color-scheme: dark)").matches;
		setIsDark(saved ? saved === "dark" : prefersDark);
	}, []);

	useEffect(() => {
		if (typeof document === "undefined") return;
		const classList = document.documentElement.classList;
		if (isDark) {
			classList.add("dark");
		} else {
			classList.remove("dark");
		}
		window.localStorage.setItem("owtunnel_theme", isDark ? "dark" : "light");
	}, [isDark]);

	const toggleTheme = () => setIsDark(prev => !prev);

	return (
		<ThemeContext.Provider value={{ isDark, toggleTheme }}>
			{children}
		</ThemeContext.Provider>
	);
}