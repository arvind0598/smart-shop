-- note: run using source command 
-- example: source ~/Desktop/projects/smart-shop/sql/setup.sql

drop database shop;

create database shop;

use shop;

create table login(
	id int primary key auto_increment,
	email varchar(50) unique not null,
	password varchar(30) not null,
	level int
);

-- management

create table cust_logs(
	time_performed timestamp not null default current_timestamp,
	email varchar(50) references login on delete cascade,
	type varchar(20) not null
);

create table admin_logs like cust_logs;

-- customer tables

create table customer(
	id int primary key references login on delete cascade,
	name varchar(30) not null,
	address varchar(100) not null,
	birth_date date not null,
	registered_time timestamp not null default current_timestamp
);

create table points(
	id int primary key references customer on delete cascade,
	num int not null
);

-- items

create table categories(
	id int primary key auto_increment,
	name varchar(30) not null
);

create table items(
	id int primary key auto_increment,
	cat_id int references categories(id) on delete cascade,
	name varchar(30) not null,
	details varchar(200),
	cost int not null
);

create table stock(
	id int references items on delete cascade,
	amt int not null,
	primary key(id, amt)
);

create table offers(
	id int references item on delete cascade,
	amt int not null,
	primary key(id, amt)
);

create table cart(
	id int primary key references customer on delete cascade,
	p_id int references items(id) on delete cascade,
	cost int not null,
	qty int not null
);

-- orders

create table orders(
	id int primary key auto_increment,
	bill int not null,
	cus_id int references customer(id) on delete cascade,
	time_performed timestamp not null default current_timestamp
);

create table order_items(
	order_id int references orders(id) on delete cascade,
	product_id int references items(id) on delete cascade,
	qty int not null,
	primary key(order_id, product_id)
);

create table del_status(
	order_id int primary key references orders(id) on delete cascade,
	status int not null
);

create table feedback(
	order_id int references orders(id) on delete cascade,
	message_id varchar(200) not null,
	primary key(order_id, message_id)
);
