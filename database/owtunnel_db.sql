-- MySQL Workbench Forward Engineering
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';


-- -----------------------------------------------------
-- Schema `owtunnel_db`
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `owtunnel_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `owtunnel_db`;


-- -----------------------------------------------------
-- Table `users`
-- -----------------------------------------------------
CREATE TABLE `users` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	`email` VARCHAR(90) NOT NULL,
	`password_hash` TEXT NOT NULL,
	`first_name` VARCHAR(45),
	`last_name` VARCHAR(45),
	`locale` VARCHAR(45),
	`role` ENUM('USER', 'SUPPORT', 'ADMINISTRATOR') NOT NULL DEFAULT 'USER',
	`created_at` TIMESTAMP NOT NULL,
	`updated_at` TIMESTAMP NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `email_unique` (`email`)
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `password_resets`
-- -----------------------------------------------------
CREATE TABLE `password_resets` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	`user_id` BIGINT UNSIGNED NOT NULL,
	`reset_token` TEXT NOT NULL,
	`created_at` TIMESTAMP NOT NULL,
	`expires_at` TIMESTAMP NOT NULL,
	PRIMARY KEY (`id`),
	KEY `user_id_idx` (`user_id`),
	CONSTRAINT `fk_password_resets_user`
		FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `login_attempts`
-- -----------------------------------------------------
CREATE TABLE `login_attempts` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	`ip_address` VARCHAR(45) NOT NULL,
	`attempts_count` INT NOT NULL DEFAULT 1,
	`blocked_until` TIMESTAMP,
	`created_at` TIMESTAMP NOT NULL,
	`updated_at` TIMESTAMP NOT NULL,
	PRIMARY KEY (`id`)
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `plans`
-- -----------------------------------------------------
CREATE TABLE `plans` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(90) NOT NULL,
	`description` TEXT,
	`price` DECIMAL(10,2) NOT NULL,
	`currency` ENUM('USD', 'EUR') NOT NULL DEFAULT 'USD',
	`duration_days` INT NOT NULL,
	`is_active` TINYINT NOT NULL DEFAULT 1,
	`created_at` TIMESTAMP NOT NULL,
	`updated_at` TIMESTAMP NOT NULL,
	PRIMARY KEY (`id`)
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `subscriptions`
-- -----------------------------------------------------
CREATE TABLE `subscriptions` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	`user_id` BIGINT UNSIGNED NOT NULL,
	`plan_id` BIGINT UNSIGNED NOT NULL,
	`status` ENUM('PENDING', 'ACTIVE', 'CANCELLED', 'EXPIRED') NOT NULL,
	`auto_renew` TINYINT NOT NULL DEFAULT 1,
	`created_at` TIMESTAMP NOT NULL,
	`updated_at` TIMESTAMP NOT NULL,
	`expires_at` TIMESTAMP NOT NULL,
	PRIMARY KEY (`id`),
	KEY `user_id_idx` (`user_id`),
	KEY `plan_id_idx` (`plan_id`),
	CONSTRAINT `fk_subscriptions_user`
		FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION,
	CONSTRAINT `fk_subscriptions_plan`
		FOREIGN KEY (`plan_id`) REFERENCES `plans` (`id`)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `payments`
-- -----------------------------------------------------
CREATE TABLE `payments` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	`user_id` BIGINT UNSIGNED NOT NULL,
	`plan_id` BIGINT UNSIGNED NOT NULL,
	`subscription_id` BIGINT UNSIGNED NOT NULL,
	`amount` DECIMAL(10,2) NOT NULL,
	`currency` ENUM('USD', 'EUR') NOT NULL,
	`method` ENUM('CREDIT_CARD', 'DEBIT_CARD', 'PAYPAL', 'APPLE_PAY', 'GOOGLE_PAY') NOT NULL,
	`status` ENUM('PENDING', 'PAID', 'FAILED', 'CANCELLED', 'REFUNDED') NOT NULL,
	`transaction_reference` TEXT NOT NULL,
	`created_at` TIMESTAMP NOT NULL,
	`updated_at` TIMESTAMP NOT NULL,
	PRIMARY KEY (`id`),
	KEY `user_id_idx` (`user_id`),
	KEY `plan_id_idx` (`plan_id`),
	KEY `subscription_id_idx` (`subscription_id`),
	CONSTRAINT `fk_payments_user`
		FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION,
	CONSTRAINT `fk_payments_plan`
		FOREIGN KEY (`plan_id`) REFERENCES `plans` (`id`)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION,
	CONSTRAINT `fk_payments_subscription`
		FOREIGN KEY (`subscription_id`) REFERENCES `subscriptions` (`id`)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `vpn_servers`
-- -----------------------------------------------------
CREATE TABLE `vpn_servers` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	`country` VARCHAR(45) NOT NULL,
	`city` VARCHAR(45) NOT NULL,
	`hostname` VARCHAR(45) NOT NULL,
	`ip_address` VARCHAR(45) NOT NULL,
	`config_file_url` TEXT NOT NULL,
	`is_free` TINYINT NOT NULL DEFAULT 0,
	`is_active` TINYINT NOT NULL DEFAULT 1,
	`created_at` TIMESTAMP NOT NULL,
	`updated_at` TIMESTAMP NOT NULL,
	PRIMARY KEY (`id`)
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `connections`
-- -----------------------------------------------------
CREATE TABLE `connections` (
	`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
	`user_id` BIGINT UNSIGNED NOT NULL,
	`vpn_server_id` BIGINT UNSIGNED NOT NULL,
	`device_info` TEXT NOT NULL,
	`status` ENUM('CONNECTED', 'DISCONNECTED', 'TIMEOUT', 'REJECTED', 'ERROR') NOT NULL,
	`created_at` TIMESTAMP NOT NULL,
	`updated_at` TIMESTAMP NOT NULL,
	PRIMARY KEY (`id`),
	KEY `user_id_idx` (`user_id`),
	KEY `vpn_server_id_idx` (`vpn_server_id`),
	CONSTRAINT `fk_connections_user`
		FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION,
	CONSTRAINT `fk_connections_vpn_server`
		FOREIGN KEY (`vpn_server_id`) REFERENCES `vpn_servers` (`id`)
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;