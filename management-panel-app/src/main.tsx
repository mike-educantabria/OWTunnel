import { createRoot } from 'react-dom/client';
import React from 'react';
import App from '@/App';
import './globals.css';

const rootElement = document.getElementById('root');
if (!rootElement) throw new Error('Root element not found');

createRoot(rootElement).render(
	<React.StrictMode>
		<App />
	</React.StrictMode>
);