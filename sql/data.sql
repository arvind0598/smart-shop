-- insert queries go here, remember to do in order to avoid foreign key issues

insert into login(email, password, level) values("arvind0598@gmail.com", "pass", 0);
insert into login(email, password, level) values("admin@admin.com", "admin1", 1);
insert into customer(id, name, address) values(1, 'Arvind S', 'Apt.-32 Sugar Lane');
insert into points(id, num) values(1, 23);

insert into categories(name) values('Footwear');
insert into items(cat_id, name, details, cost, keywords) values(1, 'Nike superun', 'Black sneakers, Rain wear',1000,'shoes, sports');
insert into items(cat_id, name, details, cost, keywords) values(1, 'Catwalk wedges', 'Brown peep toes, Platform- 2 inches, Heel- 2 inches, Faux Leather',1500,'heels, party');

insert into categories(name) values('Food');
insert into items(cat_id, name, details, cost, keywords) values(2, 'Amul cheese', 'Full fat, white cheese',100,'food, dairy');
insert into items(cat_id, name, details, cost, keywords) values(2, 'Britto garlic bread', 'Amaze garlic amaze bread',200,'baked, food');

insert into categories(name) values('Toilet');
insert into items(cat_id, name, details, cost, keywords) values(3, 'Lux rose', 'Bodywash, dryskin',90,'cosmetic');
insert into items(cat_id, name, details, cost, keywords) values(3, 'Colgate', 'Maxfresh, Flavour- Mint',70,'Hygiene, toothpaste');

-- stock for all 6 items
insert into stock(id, amt) values(1, 105);
insert into stock(id, amt) values(2, 70);
insert into stock(id, amt) values(3, 50);
insert into stock(id, amt) values(4, 50);
insert into stock(id, amt) values(5, 70);
insert into stock(id, amt) values(6, 100);

insert into offers(id, amt) values(1, 100); 
insert into offers(id, amt) values(2, 250); 

insert into cart(c_id, p_id, qty) values(1, 1, 1);
insert into cart(c_id, p_id, qty) values(1, 3, 2);

insert into orders(bill, cus_id) values(1100, 1);
insert into order_items(order_id, product_id, qty) values(1, 1, 1);
insert into order_items(order_id, product_id, qty) values(1, 3, 2);

insert into feedback(order_id, message_id) values(1, 'Very good service');
