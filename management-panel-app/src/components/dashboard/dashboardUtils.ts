export function getUsersCountByMonth(users: any[], year: number, month: number) {
	return users.filter(u => {
		const date = new Date(u.created_at);
		return date.getFullYear() === year && date.getMonth() + 1 === month;
	}).length;
}

export function getCurrentMonthYear() {
	const now = new Date();
	return { year: now.getFullYear(), month: now.getMonth() + 1 };
}

export const getChangeType = (value: number) => {
	if (value > 0) return "positive";
	if (value < 0) return "negative";
	return "neutral";
};

export const formatChange = (current: number, last: number, change: number) => {
	if (last === 0 && current === 0) return "No data";
	if (last === 0 && current > 0) return "+100% vs last month";
	if (last > 0 && current === 0) return "-100% vs last month";
	return `${change >= 0 ? "+" : ""}${change.toFixed(1)}% vs last month`;
};

export function getPaymentsSumByMonth(payments: any[], year: number, month: number) {
	return payments
		.filter(p => {
			const date = new Date(p.created_at ?? p.createdAt);
			const status = (p.status || "").toUpperCase();
			return (
				date.getFullYear() === year &&
				date.getMonth() + 1 === month &&
				(status === "COMPLETED" || status === "PAID")
			);
		})
		.reduce((sum, p) => sum + (Number(p.amount) || 0), 0);
}