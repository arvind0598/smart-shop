-- insert queries go here, remember to do in order to avoid foreign key issues

insert into login(email, password, name, address, points) values("arvind0598@gmail.com", "9D4E1E23BD5B727046A9E3B4B7DB57BD8D6EE684", 'Arvind S', 'Apt.-32 Sugar Lane', 23);
insert into login(email, password, level) values("admin@admin.com", "6C7CA345F63F835CB353FF15BD6C5E052EC08E7A", 1);

insert into categories(name) values('Footwear');
insert into items(cat_id, name, details, cost, keywords, stock, offer) values(1, 'Nike superun', 'Black sneakers, Rain wear',1000,'shoes sports', 10, 100);
insert into items(cat_id, name, details, cost, keywords, stock, offer) values(1, 'Catwalk wedges', 'Brown peep toes',1500,'heels party', 10, 250);

insert into categories(name) values('Food');
insert into items(cat_id, name, details, cost, keywords, stock) values(2, 'Amul cheese', 'Full fat, white cheese',100,'food dairy', 10);
insert into items(cat_id, name, details, cost, keywords, stock) values(2, 'Britto garlic bread', 'Amaze garlic amaze bread',200,'baked food', 10);

insert into categories(name) values('Toiletries');
insert into items(cat_id, name, details, cost, keywords, stock) values(3, 'Lux rose', 'Bodywash, dryskin',90,'cosmetic', 10);
insert into items(cat_id, name, details, cost, keywords, stock) values(3, 'Colgate', 'Maxfresh, Flavour- Mint',70,'hygiene toothpaste', 10);

insert into cart(cust_id, item_id, qty) values(1, 1, 4);
insert into cart(cust_id, item_id, qty) values(1, 3, 2);
