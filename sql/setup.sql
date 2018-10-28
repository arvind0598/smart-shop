-- note: run using source command 
-- example: source ~/Desktop/projects/smart-shop/sql/setup.sql

drop database `shop`;

create database `shop`;

use shop;

CREATE TABLE `login` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
	`email` varchar(50) NOT NULL UNIQUE,
	`password` varchar(30) NOT NULL,
	`level` int NOT NULL DEFAULT '0',
	`name` varchar(30),
	`address` varchar(100),
	`registered_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`points` int DEFAULT '0',
	PRIMARY KEY (`id`)
);

CREATE TABLE `categories` (
	`id` int NOT NULL AUTO_INCREMENT,
	`name` varchar(30) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `items` (
	`id` int NOT NULL AUTO_INCREMENT,
	`cat_id` int NOT NULL,
	`name` varchar(30) NOT NULL,
	`details` varchar(200),
	`cost` int NOT NULL,
	`keywords` varchar(200),
	`stock` int NOT NULL DEFAULT '1',
	`offer` int ,
	PRIMARY KEY (`id`)
);

CREATE TABLE `cart` (
	`cust_id` int NOT NULL,
	`item_id` int NOT NULL,
	`qty` int NOT NULL DEFAULT '1',
	`active` int NOT NULL DEFAULT '1'
);

CREATE TABLE `orders` (
	`id` int NOT NULL AUTO_INCREMENT,
	`cust_id` int NOT NULL,
	`bill` int NOT NULL,
	`paid` int NOT NULL DEFAULT '0',
	`status` int NOT NULL DEFAULT '0',
	`time_performed` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`feedback` varchar(200),
	PRIMARY KEY (`id`)
);

CREATE TABLE `order_items` (
	`order_id` int NOT NULL,
	`item_id` int NOT NULL,
	`qty` int NOT NULL DEFAULT '1',
	PRIMARY KEY (`order_id`,`item_id`)
);

CREATE TABLE `admin_logs` (
	`id` int NOT NULL AUTO_INCREMENT,
	`admin_id` int NOT NULL,
	`action` varchar(30) NOT NULL,
	`details` varchar(30) NOT NULL,
	`time_performed` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
);

CREATE TABLE `cust_logs` (
	`id` int NOT NULL AUTO_INCREMENT,
	`cust_id` int NOT NULL,
	`action` varchar(30) NOT NULL,
	`details` varchar(30) NOT NULL,
	`time_performed` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
);

CREATE TABLE `access_logs` (
	`id` int NOT NULL AUTO_INCREMENT,
	`email` varchar(50) NOT NULL,
	`type` varchar(20) NOT NULL,
	`time_performed` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
);

ALTER TABLE `items` ADD CONSTRAINT `items_fk0` FOREIGN KEY (`cat_id`) REFERENCES `categories`(`id`);

ALTER TABLE `cart` ADD CONSTRAINT `cart_fk0` FOREIGN KEY (`cust_id`) REFERENCES `login`(`id`);

ALTER TABLE `cart` ADD CONSTRAINT `cart_fk1` FOREIGN KEY (`item_id`) REFERENCES `items`(`id`);

ALTER TABLE `orders` ADD CONSTRAINT `orders_fk0` FOREIGN KEY (`cust_id`) REFERENCES `login`(`id`);

ALTER TABLE `order_items` ADD CONSTRAINT `order_items_fk0` FOREIGN KEY (`order_id`) REFERENCES `orders`(`id`);

ALTER TABLE `order_items` ADD CONSTRAINT `order_items_fk1` FOREIGN KEY (`item_id`) REFERENCES `items`(`id`);

ALTER TABLE `admin_logs` ADD CONSTRAINT `admin_logs_fk0` FOREIGN KEY (`admin_id`) REFERENCES `login`(`id`);

ALTER TABLE `cust_logs` ADD CONSTRAINT `cust_logs_fk0` FOREIGN KEY (`cust_id`) REFERENCES `login`(`id`);

source ~/Desktop/projects/smart-shop/sql/data.sql
source ~/Desktop/projects/smart-shop/sql/more.sql

