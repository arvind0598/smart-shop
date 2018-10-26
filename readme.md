# Smart Shopping

A basic online shopping client, with customer and admin end points.

---

#### Setup required

Download Netbeans for JAVA EE from https://netbeans.org/downloads/
From the downloads folder:
```
sudo apt update
sudo apt upgrade
sudo apt install git default-jdk curl
sudo ./{{ netbeans filename (extension is .sh) }}

git clone https://github.com/arvind0598/smart-shop.git
```
Install the Tomcat server from the Netbeans installation when prompted.
Open the smart-shop file that is created as a project in Netbeans.
https://www.digitalocean.com/community/tutorials/how-to-install-the-latest-mysql-on-ubuntu-16-04
After installing MySQL, open the terminal in the smart-shop folder.

```
mysql -u {{ username }} -p
>> then enter password

mysql > source ~/{{ path to smart-shop }}/sql/setup.sql
mysql > source ~/{{ path to smart-shop }}/sql/data.sql
mysql > source ~/{{ path to smart-shop }}/sql/more.sql
```

Edit Credentials.java with your own MySQL credentials.
Run project.

---

#### Working with temporary images (only supports PNG)

From the smart-shop folder, execute ``` chmod 777 addimage.sh download.sh ```

Example: 
Adding an image https://nsbpictures.com/wp-content/uploads/2018/07/sunglass-png-nsbpictures-6.png for an item with item_id = 12;

```
./addimage.sh 12 https://nsbpictures.com/wp-content/uploads/2018/07/sunglass-png-nsbpictures-6.png
./download.sh
```

This allows us to save space on the repo, by adding links instead of actual images.