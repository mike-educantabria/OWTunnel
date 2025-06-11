-- 1. users
INSERT INTO users (id, email, password_hash, first_name, last_name, locale, role, created_at, updated_at) VALUES
(1, 'admin@owtunnel.com', '$2a$10$Ose6zZm8j0fINMDldK9tj.igBPyC8COf.SuuDUilBzcGABhwFXt1a', 'Miguel', 'Gutiérrez Caso', 'es_ES', 'ADMINISTRATOR', '2025-04-12 00:00:00', '2025-06-10 00:42:04'),
(2, 'kevin.thomas@example.com', '$2a$10$Ose6zZm8j0fINMDldK9tj.igBPyC8COf.SuuDUilBzcGABhwFXt1a', 'Kevin', 'Thomas', 'de_DE', 'SUPPORT', '2025-04-12 00:00:00', '2025-06-10 00:42:04'),
(3, 'matthew.devil@example.com', '$2a$10$Ose6zZm8j0fINMDldK9tj.igBPyC8COf.SuuDUilBzcGABhwFXt1a', 'Matthew', 'Gay', 'de_DE', 'SUPPORT', '2025-04-12 00:00:00', '2025-06-10 00:42:04'),
(4, 'mary.long@example.com', '$2a$10$Ose6zZm8j0fINMDldK9tj.igBPyC8COf.SuuDUilBzcGABhwFXt1a', 'Mary', 'Long', 'es_ES', 'USER', '2025-04-12 00:00:00', '2025-06-10 00:42:04'),
(5, 'anthony.jackson@example.com', '$2a$10$Ose6zZm8j0fINMDldK9tj.igBPyC8COf.SuuDUilBzcGABhwFXt1a', 'Anthony', 'Jackson', 'es_ES', 'USER', '2025-06-11 00:00:00', '2025-06-10 00:42:04'),
(6, 'wayne.jackson@example.com', '$2a$10$Ose6zZm8j0fINMDldK9tj.igBPyC8COf.SuuDUilBzcGABhwFXt1a', 'Wayne', 'Jackson', 'es_ES', 'USER', '2025-06-11 00:00:00', '2025-06-10 00:42:04'),
(7, 'ryan.oconnor@example.com', '$2a$10$Ose6zZm8j0fINMDldK9tj.igBPyC8COf.SuuDUilBzcGABhwFXt1a', 'Ryan', 'Oconnor', 'fr_FR', 'USER', '2025-06-11 00:00:00', '2025-06-10 00:42:04'),
(8, 'richard.krause@example.com', '$2a$10$Ose6zZm8j0fINMDldK9tj.igBPyC8COf.SuuDUilBzcGABhwFXt1a', 'Richard', 'Krause', 'de_DE', 'USER', '2025-06-11 00:00:00', '2025-06-10 00:42:04');

-- 2. password_resets
INSERT INTO password_resets (id, user_id, reset_token, created_at, expires_at) VALUES
(1, 1, 'reset_token_1', '2025-06-06 00:42:04', '2025-06-12 00:42:04'),
(2, 2, 'reset_token_2', '2025-06-06 00:42:04', '2025-06-12 00:42:04'),
(3, 3, 'reset_token_3', '2025-06-06 00:42:04', '2025-06-12 00:42:04'),
(4, 4, 'reset_token_4', '2025-06-06 00:42:04', '2025-06-12 00:42:04'),
(5, 5, 'reset_token_5', '2025-06-06 00:42:04', '2025-06-12 00:42:04'),
(6, 6, 'reset_token_6', '2025-06-06 00:42:04', '2025-06-12 00:42:04'),
(7, 7, 'reset_token_7', '2025-06-06 00:42:04', '2025-06-12 00:42:04'),
(8, 8, 'reset_token_8', '2025-06-06 00:42:04', '2025-06-12 00:42:04');

-- 3. login_attempts
INSERT INTO login_attempts (id, ip_address, attempts_count, blocked_until, created_at, updated_at) VALUES
(1, '192.168.142.2', 2, NULL, '2025-05-27 00:42:04', '2025-06-10 00:42:04'),
(2, '192.168.39.4', 3, NULL, '2025-05-27 00:42:04', '2025-06-10 00:42:04'),
(3, '192.168.173.128', 4, NULL, '2025-05-27 00:42:04', '2025-06-10 00:42:04'),
(4, '192.168.69.160', 1, NULL, '2025-05-27 00:42:04', '2025-06-10 00:42:04'),
(5, '192.168.221.86', 5, NULL, '2025-05-27 00:42:04', '2025-06-10 00:42:04'),
(6, '192.168.69.56', 3, NULL, '2025-05-27 00:42:04', '2025-06-10 00:42:04'),
(7, '192.168.215.18', 2, NULL, '2025-05-27 00:42:04', '2025-06-10 00:42:04'),
(8, '192.168.83.135', 1, NULL, '2025-05-27 00:42:04', '2025-06-10 00:42:04');

-- 4. plans
INSERT INTO plans (id, name, description, price, currency, duration_days, is_active, created_at, updated_at) VALUES
(1, 'Basic Plan 1', 'VPN plan level 1', 38.00, 'USD', 90, 1, '2025-04-12 00:42:04', '2025-06-10 00:42:04'),
(2, 'Basic Plan 2', 'VPN plan level 2', 39.00, 'USD', 90, 1, '2025-04-12 00:42:04', '2025-06-10 00:42:04'),
(3, 'Basic Plan 3', 'VPN plan level 3', 11.00, 'USD', 180, 1, '2025-04-12 00:42:04', '2025-06-10 00:42:04'),
(4, 'Basic Plan 4', 'VPN plan level 4', 45.00, 'USD', 180, 1, '2025-04-12 00:42:04', '2025-06-10 00:42:04'),
(5, 'Basic Plan 5', 'VPN plan level 5', 13.00, 'USD', 180, 1, '2025-04-12 00:42:04', '2025-06-10 00:42:04'),
(6, 'Basic Plan 6', 'VPN plan level 6', 44.00, 'USD', 180, 1, '2025-04-12 00:42:04', '2025-06-10 00:42:04'),
(7, 'Basic Plan 7', 'VPN plan level 7', 40.00, 'USD', 365, 1, '2025-04-12 00:42:04', '2025-06-10 00:42:04'),
(8, 'Basic Plan 8', 'VPN plan level 8', 49.00, 'USD', 180, 1, '2025-04-12 00:42:04', '2025-06-10 00:42:04');

-- 5. subscriptions
INSERT INTO subscriptions (id, user_id, plan_id, status, auto_renew, created_at, updated_at, expires_at) VALUES
(1, 1, 1, 'ACTIVE', 1, '2025-05-12 00:00:00', '2025-05-12 00:00:00', '2025-08-10 00:00:00'),
(2, 2, 2, 'ACTIVE', 1, '2025-05-12 00:00:00', '2025-05-12 00:00:00', '2025-08-10 00:00:00'),
(3, 3, 3, 'ACTIVE', 1, '2025-05-12 00:00:00', '2025-05-12 00:00:00', '2025-08-10 00:00:00'),
(4, 4, 4, 'ACTIVE', 1, '2025-05-12 00:00:00', '2025-05-12 00:00:00', '2025-08-10 00:00:00'),
(5, 5, 5, 'ACTIVE', 1, '2025-06-11 00:00:00', '2025-06-11 00:00:00', '2025-09-09 00:00:00'),
(6, 6, 6, 'ACTIVE', 1, '2025-06-11 00:00:00', '2025-06-11 00:00:00', '2025-09-09 00:00:00');

-- 6. payments
INSERT INTO payments (id, user_id, plan_id, subscription_id, amount, currency, method, status, transaction_reference, created_at, updated_at) VALUES
(1, 1, 1, 1, 38.00, 'USD', 'CREDIT_CARD', 'PAID', 'TXN1', '2025-05-12 00:00:00', '2025-05-12 00:00:00'),
(2, 2, 2, 2, 39.00, 'USD', 'PAYPAL', 'PAID', 'TXN2', '2025-05-12 00:00:00', '2025-05-12 00:00:00'),
(3, 3, 3, 3, 11.00, 'USD', 'GOOGLE_PAY', 'PAID', 'TXN3', '2025-05-12 00:00:00', '2025-05-12 00:00:00'),
(4, 4, 4, 4, 45.00, 'USD', 'APPLE_PAY', 'PAID', 'TXN4', '2025-05-12 00:00:00', '2025-05-12 00:00:00'),
(5, 5, 5, 5, 13.00, 'USD', 'DEBIT_CARD', 'PAID', 'TXN5', '2025-06-11 00:00:00', '2025-06-11 00:00:00'),
(6, 6, 6, 6, 44.00, 'USD', 'PAYPAL', 'PAID', 'TXN6', '2025-06-11 00:00:00', '2025-06-11 00:00:00');

-- 7. vpn_servers
INSERT INTO vpn_servers (id, country, city, hostname, ip_address, config_file_url, is_free, is_active, created_at, updated_at) VALUES
(1, 'Country1', 'City1', 'vpn1.owtunnel.com', '10.0.0.1', 'http://configs/vpn1.ovpn', 1, 1, '2025-03-03 00:42:04', '2025-06-10 00:42:04'),
(2, 'Country2', 'City2', 'vpn2.owtunnel.com', '10.0.0.2', 'http://configs/vpn2.ovpn', 0, 1, '2025-03-03 00:42:04', '2025-06-10 00:42:04'),
(3, 'Country3', 'City3', 'vpn3.owtunnel.com', '10.0.0.3', 'http://configs/vpn3.ovpn', 1, 1, '2025-03-03 00:42:04', '2025-06-10 00:42:04'),
(4, 'Country4', 'City4', 'vpn4.owtunnel.com', '10.0.0.4', 'http://configs/vpn4.ovpn', 0, 1, '2025-03-03 00:42:04', '2025-06-10 00:42:04'),
(5, 'Country5', 'City5', 'vpn5.owtunnel.com', '10.0.0.5', 'http://configs/vpn5.ovpn', 1, 1, '2025-03-03 00:42:04', '2025-06-10 00:42:04'),
(6, 'Country6', 'City6', 'vpn6.owtunnel.com', '10.0.0.6', 'http://configs/vpn6.ovpn', 1, 1, '2025-03-03 00:42:04', '2025-06-10 00:42:04'),
(7, 'Country7', 'City7', 'vpn7.owtunnel.com', '10.0.0.7', 'http://configs/vpn7.ovpn', 0, 1, '2025-03-03 00:42:04', '2025-06-10 00:42:04'),
(8, 'Country8', 'City8', 'vpn8.owtunnel.com', '10.0.0.8', 'http://configs/vpn8.ovpn', 1, 1, '2025-03-03 00:42:04', '2025-06-10 00:42:04');

-- 8. connections
INSERT INTO connections (id, user_id, vpn_server_id, device_info, status, created_at, updated_at) VALUES
(1, 1, 1, 'Device 1 info', 'CONNECTED', '2025-05-12 00:00:00', '2025-05-12 00:00:00'),
(2, 2, 2, 'Device 2 info', 'DISCONNECTED', '2025-05-12 00:00:00', '2025-05-12 00:00:00'),
(3, 3, 3, 'Device 3 info', 'CONNECTED', '2025-05-12 00:00:00', '2025-05-12 00:00:00'),
(4, 4, 4, 'Device 4 info', 'TIMEOUT', '2025-05-12 00:00:00', '2025-05-12 00:00:00'),
(5, 5, 5, 'Device 5 info', 'REJECTED', '2025-06-11 00:00:00', '2025-06-11 00:00:00'),
(6, 6, 6, 'Device 6 info', 'CONNECTED', '2025-06-11 00:00:00', '2025-06-11 00:00:00');
