-- insert queries go here, remember to do in order to avoid foreign key issues

insert into login(email, password, name, address, points) values("arvind0598@gmail.com", "pass", 'Arvind S', 'Apt.-32 Sugar Lane', 23);
insert into login(email, password, level) values("admin@admin.com", "admin1", 1);

insert into categories(name) values('Footwear');
insert into items(cat_id, name, details, cost, keywords, stock, offer) values(1, 'Nike superun', 'Black sneakers, Rain wear',1000,'shoes sports', 105, 100);
insert into items(cat_id, name, details, cost, keywords, stock, offer) values(1, 'Catwalk wedges', 'Brown peep toes, Platform- 2 inches, Heel- 2 inches, Faux Leather',1500,'heels party', 70, 250);

insert into categories(name) values('Food');
insert into items(cat_id, name, details, cost, keywords, stock) values(2, 'Amul cheese', 'Full fat, white cheese',100,'food dairy', 50);
insert into items(cat_id, name, details, cost, keywords, stock) values(2, 'Britto garlic bread', 'Amaze garlic amaze bread',200,'baked food', 50);

insert into categories(name) values('Toilet');
insert into items(cat_id, name, details, cost, keywords, stock) values(3, 'Lux rose', 'Bodywash, dryskin',90,'cosmetic', 70);
insert into items(cat_id, name, details, cost, keywords, stock) values(3, 'Colgate', 'Maxfresh, Flavour- Mint',70,'hygiene toothpaste', 100);

insert into cart(cust_id, item_id, qty) values(1, 1, 1);
insert into cart(cust_id, item_id, qty) values(1, 3, 2);

insert into orders(bill, cust_id, feedback) values(1100, 1, 'Very good service'	);
insert into order_items(order_id, item_id, qty) values(1, 1, 1);
insert into order_items(order_id, item_id, qty) values(1, 3, 2);
