delimiter $$

drop procedure if exists update_cart_qty$$
drop procedure if exists add_category$$
drop procedure if exists rmv_category$$
drop procedure if exists make_order_from_cart$$
drop procedure if exists add_item$$
drop procedure if exists update_item$$
drop procedure if exists rmv_item$$
drop procedure if exists add_offer$$
drop procedure if exists add_stock$$

create procedure update_cart_qty
	(in a int, in b int, in c int)
begin
	if a <> 0 then
		if exists(select qty from cart where cust_id = b and item_id = c) then
			update cart set qty = a where cust_id = b and item_id = c;
		else
			insert into cust_logs(cust_id, action, details) values(b, 'ADD_TO_CART', c);
			insert into cart(cust_id, item_id) values(b, c);
		end if;
	else
		insert into cust_logs(cust_id, action, details) values(b, 'RMV_FROM_CART', c);
		delete from cart where cust_id = b and item_id = c;
	end if;
end$$

create procedure add_category
	(out status_x int, in cat_x varchar(30), in adm_x int)
begin
	set status_x = 0;
	insert into categories(name) values(cat_x);
	insert into admin_logs(admin_id, action, details) values(adm_x, 'ADD_CATEGORY', cat_x);
	set status_x = 1;
end$$

create procedure rmv_category
	(out status_x int, in cat_x int, in adm_x int)
begin
	declare cat_name varchar(30);

	set status_x = 0;
	select name into cat_name from categories where id = cat_x;
	delete from categories where id = cat_x;
	insert into admin_logs(admin_id, action, details) values(adm_x, 'RMV_CATEGORY', cat_name);
	set status_x = 1;
end$$

create procedure add_offer
	(out status_x int, in item_x int, in offer_x int, in adm_x int)
begin

	declare item_name varchar(30);

	set status_x = 0;
	select name into item_name from items where id = item_x;

	update items set offer = offer_x where id = item_x;
	insert into admin_logs(admin_id, action, details) values(adm_x, 'ADD_OFFER', concat(item_name,' ', offer_x));

	set status_x = 1;
end$$

create procedure add_stock
	(out status_x int, in item_x int, in stock_x int, in adm_x int)
begin

	declare item_name varchar(30);

	set status_x = 0;
	select name into item_name from items where id = item_x;

	update items set stock = stock_x where id = item_x;
	insert into admin_logs(admin_id, action, details) values(adm_x, 'MODIFY_STOCK', concat(item_name,' ', stock_x));

	set status_x = 1;
end$$

create procedure add_item
	(out status_x int, in cat_x int, in name_x varchar(30), in details_x varchar(30), in cost_x int, in keywords_x varchar(200), in stock_x int, in offer_x int, in adm_x int)
begin
	
	declare offer_status_x int default 0;
	declare item_x int;

	set status_x = 0;

	insert into items(cat_id, name, details, cost, keywords, stock) values(cat_x, name_x, details_x, cost_x, keywords_x, stock_x);
	select last_insert_id() into item_x;

	insert into admin_logs(admin_id, action, details) values(adm_x, 'ADD_ITEM', name_x);	

	if offer_x is not null then
		call add_offer(offer_status_x, item_x, offer_x, adm_x);
	end if;

	set status_x = item_x;
end$$

-- create procedure update_item
-- 	(out status_x int, in catname_x varchar(30), in name_x varchar(30), in details_x varchar(30), in cost_x int, in keywords_x varchar(200), in stock_x int, in offer_x int, in adm_x int)
-- begin
-- end$$
	
create procedure make_order_from_cart
	(out order_x int, in cust_x int, in used_points int)
begin
	
	declare done int default 0;
	declare item_x int;
	declare cost_x int;
	declare offer_x int;
	declare qty_x int;
	declare bill_x int default 0;
	declare points_x int;
	declare stock_x int;
	declare points_allowed_x int default 0;

	declare cart_cursor cursor for select item_id, qty from cart where cust_id = cust_x;
	declare continue handler for not found set done = 1;

	open cart_cursor;

	insert into orders(cust_id, bill, paid) values(cust_x, bill_x, 1);
	select last_insert_id() into order_x;

	get_details : loop
		fetch cart_cursor into item_x, qty_x;
		if done = 1 then leave get_details; end if;
		insert into order_items values(order_x, item_x, qty_x);
		select cost, offer into cost_x, offer_x from items where id = item_x;
		if offer_x is null then set offer_x = 0; end if;
		set bill_x = bill_x + ((cost_x - offer_x) * qty_x);
		delete from cart where cust_id = cust_x and item_id = item_x;
		
		update items set stock = stock - qty_x where id = item_x;
		select stock into stock_x from items where id = item_x;
		if stock_x <= 0 then
			update items set stock = 0 where id = item_x;
			update cart set active = 0 where item_id = item_x;
		end if;

	end loop get_details;
	close cart_cursor;

	if used_points = 1 then
		select points into points_x from login where id = cust_x;
		if points_x < (bill_x / 2) then
			set points_allowed_x = points_x;
		else
			set points_allowed_x = bill_x / 2;
		end if;
		set bill_x = bill_x - points_allowed_x;
		update login set points = (points_x - points_allowed_x) where id = cust_x;
	end if;

	update orders set bill = bill_x where id = order_x;
	update login set points = points + (bill_x / 20) where id = cust_x;

	insert into cust_logs(cust_id, action, details) values(cust_x, 'CHECKOUT', order_x);
end$$	

delimiter ;
