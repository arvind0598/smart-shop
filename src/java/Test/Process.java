/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import java.sql.*;
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
    
    enum CustLogs {
        CHECKOUT,
        FEEDBACK;
    }
    
    enum AdminLogs {
        ADD_CATEGORY,
        RMV_CATEGORY,
        ADD_ITEM,
        RMV_ITEM,
        MODIFY_STOCK,
        ADD_OFFER,
        RMV_OFFER,
        CHANGE_DEL_STATUS;
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
            PreparedStatement stmt = conn.prepareStatement("select id from login where email=? and password=?");
            stmt.setString(1, useremail);
            stmt.setString(2, password);

            ResultSet res = stmt.executeQuery();
           
            if(res.next()) {
                status = res.getInt(1);
                if(!internal) logAccessAction(conn, useremail, AccessLogs.LOGIN_SUCCESS);
            } else {
                if(!internal) logAccessAction(conn, useremail, AccessLogs.LOGIN_FAIL);
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
            
            stmt = conn.prepareStatement("insert into login(email, password, name) values(?, ?, ?)");
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setString(3, name);
            stmt.execute();
            
            logAccessAction(conn, email, AccessLogs.REGISTER);
            status = true;
            
            conn.close();
            
        } catch(Exception e) {
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
            
            if(res.next()) {
                String email = res.getString(1);
                logAccessAction(conn, email, AccessLogs.LOGOUT);      
                status = true;
            }
            
            conn.close();
        } catch(Exception e) {
            Helper.handleError(e);
        }
        
        return status;
    }
    
    Boolean changePassword(int customer_id, String password) {
        Boolean status = false;
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("update login set password = ? where id = ?");
            stmt.setString(1, password);
            stmt.setInt(2, customer_id);
            stmt.execute();
            conn.close();
            status = true;            
        }
        
        catch (Exception e) {
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
        } catch(Exception e) {
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
            
            while(res.next()) {
                JSONObject item = new JSONObject();
                item.put("name", res.getString(2));
                item.put("cost", res.getInt(3));
                item.put("offer", res.getInt(4));
                x.put(res.getInt(1), item);
            }
            
            conn.close();             
        }
        catch (Exception e) {
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
            
            if(res.next()) {
                x.put("email", res.getString(1));
                x.put("name", res.getString(2));
                x.put("address", res.getString(3));
                x.put("points", res.getInt(4));
            }
            
            conn.close();
        }
        
        catch (Exception e) {
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
            
            while(res.next()) {
                x.put(res.getInt(1), res.getString(2));
            }
            
            conn.close();
        }
        
        catch (Exception e) {
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
            
            while(res.next()) {
                JSONObject item = new JSONObject();
                item.put("name", res.getString(2));
                item.put("cost", res.getInt(3));
                item.put("offer", res.getInt(4));
                x.put(res.getInt(1), item);
            }
            
            conn.close();
        }
        
        catch (Exception e) {
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
            
            while(res.next()) {
                x.put("name", res.getString(1));
                x.put("details", res.getString(2));
                x.put("cost", res.getInt(3));
                x.put("offer", res.getInt(4));
                x.put("stock", res.getInt(5));
            }
            
            conn.close();
        }
        
        catch (Exception e) {
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
            while(res.next()) {
                JSONObject item = new JSONObject();
                item.put("qty", res.getInt(2));
                item.put("name", res.getString(3));
                item.put("cost", res.getInt(4));
                item.put("offer", res.getInt(5));
                x.put(res.getInt(1), item);
            }
            
            conn.close();
        }
        
        catch(Exception e) {
            Helper.handleError(e);
        }
        
        return x;
    }
    
    Boolean productQuantityViable(int product_id, int qty) {
        Boolean status = false;
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select stock from items where id = ?");
            stmt.setInt(1, product_id);
            ResultSet res = stmt.executeQuery();
            
            if(res.next() && res.getInt(1) >= qty) status = true;
            conn.close();
            
        }
        catch (Exception e) {
            Helper.handleError(e);
        }
        
        return status && (qty >= 0);
    }
   
    Boolean updateCart(int customer_id, int product_id, int qty) {
        Boolean status = productQuantityViable(product_id, qty);
        if(!status) return false;
        try {
            Connection conn = connectSQL();
//            PreparedStatement stmt = conn.prepareStatement("update cart set qty = ? where cust_id = ? and item_id = ?");
            CallableStatement stmt = conn.prepareCall("call update_cart_qty(?, ?, ?)");
            stmt.setInt(1, qty);
            stmt.setInt(2, customer_id);
            stmt.setInt(3, product_id);
            stmt.execute();
            conn.close();
            status = true;
        }
        catch (Exception e) {
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
            
            if(res.next()) status = true;        
        }
        catch (Exception e) {
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
        }
        catch(Exception e) {
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
            
            while(res.next()) {
                JSONObject x = new JSONObject();
                x.put("item_id", res.getInt(1));
                x.put("qty", res.getInt(2));
                arr.add(x);
            }
            
        }
        catch(Exception e) {
            Helper.handleError(e);
        }
        
        return arr;
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
    
    void logCustomerAction(Connection conn, int id, String details, CustLogs type) {
        
        try {
            PreparedStatement stmt = conn.prepareStatement("insert into cust_logs(cust_id, action, details) values(?, ?, ?)");
            stmt.setInt(1, id);
            stmt.setString(2, type.toString());
            stmt.setString(3, details);
            stmt.execute();
            
        } catch (Exception e) {
            Helper.handleError(e);
        }
    }
    
    void logAdminAction(Connection conn, int id, String details, CustLogs type) {
        
        try {
            PreparedStatement stmt = conn.prepareStatement("insert into admin_logs(cust_id, action, details) values(?, ?, ?)");
            stmt.setInt(1, id);
            stmt.setString(2, type.toString());
            stmt.setString(3, details);
            stmt.execute();
            
        } catch (Exception e) {
            Helper.handleError(e);
        }
    }
    
}

class Helper {
    
    public static void handleError(Exception e) {
        try {
            e.printStackTrace();
        } catch(Exception anotherOne) {
            System.out.println(anotherOne.toString());
        }
    }
    
    public static String hashPassword(String pass) {
        return pass;
    }
}
