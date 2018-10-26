delimiter $$

drop procedure update_cart_qty$$
drop procedure make_order_from_cart$$

create procedure update_cart_qty(in a int, in b int, in c int)
begin
	if a <> 0 then
		if exists(select qty from cart where cust_id = b and item_id = c) then
			update cart set qty = a where cust_id = b and item_id = c;
			select 'hello';
		else
			insert into cust_logs(cust_id, action, details) values(b, 'ADD_TO_CART', c);
			insert into cart(cust_id, item_id) values(b, c);
			select 'no';
		end if;
	else
		insert into cust_logs(cust_id, action, details) values(b, 'RMV_FROM_CART', c);
		delete from cart where cust_id = b and item_id = c;
	end if;
end$$

create procedure make_order_from_cart(in cust_x int)
begin
	
	declare done int default 0;
	declare item_x int;
	declare cost_x int;
	declare qty_x int;
	declare order_x int;
	declare bill_x int default 0;

	declare cart_cursor cursor for select item_id, qty from cart where cust_id = cust_x;
	declare continue handler for not found set done = 1;

	open cart_cursor;

	insert into orders(cust_id, bill) values(cust_x, bill_x);
	select last_insert_id() into order_x;

	get_details : loop
		fetch cart_cursor into item_x, qty_x;
		if done = 1 then leave get_details; end if;
		insert into order_items values(order_x, item_x, qty_x);
		select cost into cost_x from items where id = item_x;
		set bill_x = bill_x + (cost_x * qty_x);
		delete from cart where cust_id = cust_x and item_id = item_x;
	end loop get_details;
	close cart_cursor;

	update orders set bill = bill_x where id = order_x;
end$$	

delimiter ;
