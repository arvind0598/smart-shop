delimiter $$

drop procedure update_cart_qty$$

create procedure update_cart_qty(in a int, in b int, in c int)
begin
	if a <> 0 then
		update cart set qty = a where cust_id = b and item_id = c;
	else
		delete from cart where cust_id = b and item_id = c;
	end if;
end$$

delimiter ;
