import { app, BrowserWindow } from "electron";
import path from "path";
import { fileURLToPath } from "url";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const isDev = !app.isPackaged;

function createWindow() {
	const win = new BrowserWindow({
		width: 1024,
		height: 768,
		minWidth: 1024,
		minHeight: 768,
		webPreferences: {
			contextIsolation: true,
			nodeIntegration: false
		}
	});

	const url = isDev
		? "http://localhost:8081"
		: `file://${path.join(__dirname, "../dist/index.html")}`;

	win.removeMenu();
	win.loadURL(url);
}

app.whenReady().then(() => {
	createWindow();

	app.on("activate", () => {
		if (BrowserWindow.getAllWindows().length === 0) createWindow();
	});
});

app.on("window-all-closed", () => {
	if (process.platform !== "darwin") app.quit();
});