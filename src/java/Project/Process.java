/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.regex.Pattern;
import javax.net.ssl.HttpsURLConnection;
import org.json.simple.*;

/**
 *
 * @author de-arth
 */
public class Process {

    enum AccessLogs {
        LOGIN_SUCCESS,
        LOGIN_FAIL,
        LOGOUT,
        REGISTER;
    }

    enum DeliveryStatus {
        DEL_RECEIVED,
        DEL_DISPATCHED,
        DEL_DELIVERED;
    }

    Connection connectSQL()
            throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Credentials cred = new Credentials();
        Connection conn = DriverManager.getConnection(cred.DB_URL, cred.DB_USER, cred.DB_PASS);
        return conn;
    }

    int checkUser(String useremail, String password, Boolean internal) {

        int status = -1;

        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select id from login where email=? and password=sha(?) and level = 0");
            stmt.setString(1, useremail);
            stmt.setString(2, password);

            ResultSet res = stmt.executeQuery();

            if (res.next()) {
                status = res.getInt(1);
                if (!internal) {
                    logAccessAction(conn, useremail, AccessLogs.LOGIN_SUCCESS);
                }
            } else {
                if (!internal) {
                    logAccessAction(conn, useremail, AccessLogs.LOGIN_FAIL);
                }
            }

            conn.close();

        } catch (Exception e) {
            Helper.handleError(e);
        }

        return status;
    }

    Boolean registerUser(String name, String email, String password) {
        Boolean status = false;
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt;

            // check if user id exists not done because duplicate entry violates constraint and hence returns false
            stmt = conn.prepareStatement("insert into login(email, password, name) values(?, sha(?), ?)");
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setString(3, name);
            stmt.execute();

            logAccessAction(conn, email, AccessLogs.REGISTER);
            status = true;

            conn.close();

        } catch (Exception e) {
            Helper.handleError(e);
        }

        return status;
    }

    Boolean logoutUser(int id) {
        Boolean status = false;
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select email from login where id=?");
            stmt.setInt(1, id);

            ResultSet res = stmt.executeQuery();

            if (res.next()) {
                String email = res.getString(1);
                logAccessAction(conn, email, AccessLogs.LOGOUT);
                status = true;
            }

            conn.close();
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return status;
    }

    Boolean changePassword(int customer_id, String password) {
        Boolean status = false;
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("update login set password = sha(?) where id = ?");
            stmt.setString(1, password);
            stmt.setInt(2, customer_id);
            stmt.execute();
            conn.close();
            status = true;
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return status;
    }

    Boolean changeAddress(int customer_id, String address) {
        Boolean status = false;
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("update login set address = ? where id = ?");
            stmt.setString(1, address);
            stmt.setInt(2, customer_id);
            stmt.execute();
            conn.close();
            status = true;
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return status;
    }

    public JSONObject searchProducts(String str) {
        JSONObject x = new JSONObject();
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select items.id, items.name, cost, offer from items join categories on(cat_id = categories.id) and keywords like ?");
            stmt.setString(1, "%" + str + "%");
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                JSONObject item = new JSONObject();
                item.put("name", res.getString(2));
                item.put("cost", res.getInt(3));
                item.put("offer", res.getInt(4));
                x.put(res.getInt(1), item);
            }

            conn.close();
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return x;
    }

    public JSONObject getCustomerDetails(int customer_id) {
        JSONObject x = new JSONObject();
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select email, name, address, points from login where id = ?");
            stmt.setInt(1, customer_id);
            ResultSet res = stmt.executeQuery();

            if (res.next()) {
                x.put("email", res.getString(1));
                x.put("name", res.getString(2));
                x.put("address", res.getString(3));
                x.put("points", res.getInt(4));
            }

            conn.close();
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return x;
    }

    public JSONObject getCategories() {

        JSONObject x = new JSONObject();
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select * from categories");
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                x.put(res.getInt(1), res.getString(2));
            }

            conn.close();
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return x;
    }

    public String getCategoryName(int cat_id) {
        String x = new String();
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select name from categories where id = ?");
            stmt.setInt(1, cat_id);
            ResultSet res = stmt.executeQuery();

            if (res.next()) {
                x = res.getString(1);
            }

            conn.close();
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return x;
    }

    public JSONObject getProducts(int category_id) {
        JSONObject x = new JSONObject();
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select id, name, cost, offer from items where cat_id = ? and stock > 0");
            stmt.setInt(1, category_id);
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                JSONObject item = new JSONObject();
                item.put("name", res.getString(2));
                item.put("cost", res.getInt(3));
                item.put("offer", res.getInt(4));
                x.put(res.getInt(1), item);
            }

            conn.close();
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return x;
    }

    public JSONObject getProductDetails(int product_id) {
        JSONObject x = new JSONObject();
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select name, details, cost, offer, stock from items where id = ? and stock > 0");
            stmt.setInt(1, product_id);
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                x.put("name", res.getString(1));
                x.put("details", res.getString(2));
                x.put("cost", res.getInt(3));
                x.put("offer", res.getInt(4));
                x.put("stock", res.getInt(5));
            }

            conn.close();
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return x;
    }

    public JSONObject getCartProducts(int customer_id) {

        JSONObject x = new JSONObject();

        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select item_id, qty, name, cost, offer from cart join items on(item_id = id)  where cust_id = ? and active = 1");
            stmt.setInt(1, customer_id);

            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                JSONObject item = new JSONObject();
                item.put("qty", res.getInt(2));
                item.put("name", res.getString(3));
                item.put("cost", res.getInt(4));
                item.put("offer", res.getInt(5));
                x.put(res.getInt(1), item);
            }

            conn.close();
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return x;
    }

    int productQuantityViable(int product_id, int qty) {
        int stock = -1;
        if (qty < 0) {
            return stock;
        }

        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select stock from items where id = ?");
            stmt.setInt(1, product_id);
            ResultSet res = stmt.executeQuery();

            if (res.next()) {
                System.out.print(res.getInt(1));
                int allowedStock = res.getInt(1);
                if (allowedStock < qty) {
                    stock = allowedStock;
                } else {
                    stock = qty;
                }
            }

            conn.close();
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return stock;
    }

    Boolean updateCart(int customer_id, int product_id, int qty) {
        Boolean status = false;
        int allowedQty = productQuantityViable(product_id, qty);
        if (allowedQty == -1) {
            return false;
        }

        System.out.println(allowedQty);
        try {
            Connection conn = connectSQL();
            CallableStatement stmt = conn.prepareCall("call update_cart_qty(?, ?, ?)");
            stmt.setInt(1, allowedQty);
            stmt.setInt(2, customer_id);
            stmt.setInt(3, product_id);
            stmt.execute();
            conn.close();
            status = true;
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return status;
    }

    public Boolean checkInCart(int customer_id, int product_id) {
        Boolean status = false;

        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select qty from cart where cust_id = ? and item_id = ?");
            stmt.setInt(1, customer_id);
            stmt.setInt(2, product_id);

            ResultSet res = stmt.executeQuery();

            if (res.next()) {
                status = true;
            }
            conn.close();
        } catch (Exception e) {
            Helper.handleError(e);
        }

        System.out.println(product_id + " " + status);
        return status;

    }

    int checkoutOrder(int customer_id, Boolean points) {
        int order_id = -1;
        try {
            Connection conn = connectSQL();
            CallableStatement stmt = conn.prepareCall("call make_order_from_cart(?,?,?)");
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setInt(2, customer_id);
            stmt.setInt(3, points ? 1 : 0);
            stmt.execute();

            order_id = stmt.getInt(1);
            conn.close();
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return order_id;
    }

    public JSONArray getOrderItems(int order_id) {
        JSONArray arr = new JSONArray();
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select item_id, qty from order_items where order_id = ?");
            stmt.setInt(1, order_id);
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                JSONObject x = new JSONObject();
                x.put("item_id", res.getInt(1));
                x.put("qty", res.getInt(2));
                arr.add(x);
            }
            conn.close();
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return arr;
    }

    public JSONObject getProductDetailsForAdmin(int product_id) {
        JSONObject x = new JSONObject();
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select name, stock from items where id = ?");
            stmt.setInt(1, product_id);
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                x.put("name", res.getString(1));
                x.put("stock", res.getInt(2));
            }

            conn.close();
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return x;
    }

    public JSONArray getOrderItemsForAdmin(int order_id) {
        JSONArray arr = new JSONArray();
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select item_id, qty from order_items where order_id = ?");
            stmt.setInt(1, order_id);
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                JSONObject x = new JSONObject();
                int item_id = res.getInt(1);
                JSONObject item = getProductDetailsForAdmin(item_id);
                x.put("item", item);
                x.put("qty", res.getInt(2));
                arr.add(x);
            }
            conn.close();
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return arr;
    }

    public JSONObject getOrderHistory(int cust_id) {
        System.out.println(cust_id + " hehehehe");
        JSONObject x = new JSONObject();
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select id, bill, status, case when feedback is null then 0 else 1 end as feedback from orders where cust_id = ?");
            stmt.setInt(1, cust_id);
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                JSONObject item = new JSONObject();
                int order_id = res.getInt(1);
//                JSONArray orderItems = getOrderItems(order_id);
                item.put("bill", res.getInt(2));
//                item.put("details", getOrderItemsForAdmin(order_id));
                item.put("status", res.getInt(3));
                item.put("feedback", res.getInt(4));

//                item.put("items", orderItems);
                x.put(order_id, item);
            }

            conn.close();
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return x;
    }

    Boolean provideFeedback(int order_id, String feedback, int cust_id) {
        System.out.println(order_id + " " + feedback + " " + cust_id);
        Boolean status = false;
        try {
            Connection conn = connectSQL();
            CallableStatement stmt = conn.prepareCall("call add_feedback(?,?,?,?)");
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setInt(2, order_id);
            stmt.setString(3, feedback);
            stmt.setInt(4, cust_id);
            stmt.execute();
            status = stmt.getInt(1) == 1;
            conn.close();
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return status;
    }

    void logAccessAction(Connection conn, String username, AccessLogs type) {

        try {
            PreparedStatement stmt = conn.prepareStatement("insert into access_logs(email, type) values(?,?)");
            stmt.setString(1, username);
            stmt.setString(2, type.toString());
            stmt.execute();

        } catch (Exception e) {
            Helper.handleError(e);
        }

    }

    //    admin
    int checkAdmin(String useremail, String password, Boolean internal) {

        int status = -1;

        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select id from login where email=? and password=sha(?) and level=1");
            stmt.setString(1, useremail);
            stmt.setString(2, password);

            ResultSet res = stmt.executeQuery();

            if (res.next()) {
                status = res.getInt(1);
                if (!internal) {
                    logAccessAction(conn, useremail, AccessLogs.LOGIN_SUCCESS);
                }
            } else {
                if (!internal) {
                    logAccessAction(conn, useremail, AccessLogs.LOGIN_FAIL);
                }
            }

            conn.close();

        } catch (Exception e) {
            Helper.handleError(e);
        }

        return status;
    }

    Boolean addCategory(String category, int admin_id) {
        Boolean status = false;
        try {
            Connection conn = connectSQL();
            CallableStatement stmt = conn.prepareCall("call add_category(?,?,?)");
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setString(2, category);
            stmt.setInt(3, admin_id);
            stmt.execute();
            status = stmt.getInt(1) == 1;
            conn.close();
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return status;
    }

    Boolean removeCategory(int cat_id, int admin_id) {
        Boolean status = false;
        try {
            Connection conn = connectSQL();
            CallableStatement stmt = conn.prepareCall("call rmv_category(?,?,?)");
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setInt(2, cat_id);
            stmt.setInt(3, admin_id);
            stmt.execute();
            status = stmt.getInt(1) == 1;
            conn.close();
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return status;
    }

    int addProduct(int cat_id, JSONObject product, int admin_id) {
        int status = -1;
        try {
            Connection conn = connectSQL();
            CallableStatement stmt = conn.prepareCall("call add_item(?,?,?,?,?,?,?,?,?)");
            stmt.registerOutParameter(1, Types.INTEGER);
//            System.out.println(product.get("cat_id"));
            stmt.setInt(2, cat_id);
            stmt.setString(3, product.get("name").toString());
//            System.out.println(product.get("stock"));
            stmt.setString(4, product.get("desc").toString());
            stmt.setInt(5, (int) product.get("cost"));
            stmt.setString(6, product.get("keywords").toString());
            stmt.setInt(7, (int) product.get("stock"));
            stmt.setInt(8, (int) product.get("offer"));
            stmt.setInt(9, admin_id);

            stmt.execute();
            status = stmt.getInt(1);
            conn.close();
        } catch (Exception e) {
            Helper.handleError(e);
            status = -1;
        }

        return status;
    }

    Boolean removeProduct(int product_id, int admin_id) {
        Boolean status = false;
        try {
            Connection conn = connectSQL();
            CallableStatement stmt = conn.prepareCall("call rmv_item(?,?,?)");
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setInt(2, product_id);
            stmt.setInt(3, admin_id);
            stmt.execute();
            status = stmt.getInt(1) == 1;
            conn.close();
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return status;
    }

    public JSONObject getProductsForAdmin(int category_id) {
        JSONObject x = new JSONObject();
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select id, name, cost, offer, stock from items where cat_id = ?");
            stmt.setInt(1, category_id);
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                JSONObject item = new JSONObject();
                item.put("name", res.getString(2));
                item.put("cost", res.getInt(3));
                item.put("offer", res.getInt(4));
                item.put("stock", res.getInt(5));
                x.put(res.getInt(1), item);
            }

            conn.close();
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return x;
    }

    Boolean updateOffer(int product_id, int offer, int admin_id) {
        Boolean status = false;
        try {
            Connection conn = connectSQL();
            CallableStatement stmt = conn.prepareCall("call add_offer(?,?,?,?)");
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setInt(2, product_id);
            stmt.setInt(3, offer);
            stmt.setInt(4, admin_id);
            stmt.execute();
            status = stmt.getInt(1) == 1;
            conn.close();
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return status;
    }

    Boolean updateStock(int product_id, int stock, int admin_id) {
        Boolean status = false;
        try {
            Connection conn = connectSQL();
            CallableStatement stmt = conn.prepareCall("call add_stock(?,?,?,?)");
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setInt(2, product_id);
            stmt.setInt(3, stock);
            stmt.setInt(4, admin_id);
            stmt.execute();
            status = stmt.getInt(1) == 1;
            conn.close();
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return status;
    }

    public JSONObject getActiveOrders() {
        JSONObject x = new JSONObject();
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select id, cust_id, bill, status from orders where status <> 2");
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                JSONObject item = new JSONObject();
                int order_id = res.getInt(1);
                JSONArray orderItems = getOrderItemsForAdmin(order_id);
                item.put("cust_id", res.getInt(2));
                item.put("bill", res.getInt(3));
                item.put("status", res.getInt(4));
                item.put("items", orderItems);
                x.put(order_id, item);
            }

            conn.close();
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return x;
    }

    Boolean updateDeliveryStatus(int order_id, int del_status, int admin_id) {
        Boolean status = false;
        try {
            Connection conn = connectSQL();
            CallableStatement stmt = conn.prepareCall("call update_del_status(?,?,?,?)");
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setInt(2, order_id);
            stmt.setInt(3, del_status);
            stmt.setInt(4, admin_id);
            stmt.execute();
            status = stmt.getInt(1) == 1;
            conn.close();
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return status;
    }
    
    public JSONObject getAllFeedback() {
        JSONObject x = new JSONObject();
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select orders.id, name, bill, status, feedback from orders join login on(cust_id = login.id) where feedback is not null");
            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                JSONObject item = new JSONObject();
                int order_id = res.getInt(1);
                item.put("name", res.getString(2));
                item.put("bill", res.getInt(3));
                item.put("status", res.getInt(4));
                item.put("feedback", res.getString(5));
                x.put(order_id, item);
            }

            conn.close();
        } catch (Exception e) {
            Helper.handleError(e);
        }

        return x;
    }
}

class Helper {

    enum Regex {
        NUMBERS_ONLY("^[0-9]+$"),
        MIN_FOUR_ALPHA_ONLY("^[a-zA-Z]{4,}$"),
        EMAIL("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$"),
        MIN_SIX_ALPHANUM("^[a-zA-Z0-9]{6,}$"),
        MIN_SIX_ALPHA_SPACES("^[a-zA-Z ]{6,}$"),
        MIN_SIX_ALPHANUM_SPACES("^[a-zA-Z0-9 ]{6,}$"),
        MIN_SIX_LOWERCASE_SPACES("^[a-z ]{6,}$"),
        DESCRIPTION("[a-zA-Z0-9.?! ]{6,}"),
        SIX_TO_TWELVE(".{6,12}");

        private final String str;

        public String getRegex() {
            return this.str;
        }

        Regex(String str) {
            this.str = str;
        }
    }

    public static void handleError(Exception e) {
        try {
            e.printStackTrace();
        } catch (Exception anotherOne) {
            System.out.println(anotherOne.toString());
        }
    }

    public static Boolean regexChecker(Regex r, String str) {
        Boolean status = false;
        try {
            Pattern p = Pattern.compile(r.getRegex());
            status = p.matcher(str).matches();
//            System.out.println(status + " " + str);
        } catch (Exception e) {
            status = false;
        }

        return status;
    }
    
    public static void sendEmail(String email, int order, String name) 
            throws MalformedURLException, IOException {

        String preURL = String.format("https://script.google.com/macros/s/AKfycbwZm6E2OzyHqnjwQAe10TgAobIyH1tmhk3nWpt_E3ahlMIajm8/exec?email=%s&order=%d&name=%s", email, order, name.split(" (?!.* )")[0]);

        URL url = new URL(preURL);

        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        System.out.println("GET " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder lol = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                lol.append(inputLine);
            }

            in.close();
        } else {
            System.out.println("GET request not worked");
        }
    }
}
