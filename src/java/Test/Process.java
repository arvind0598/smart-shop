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
        ADD_ADDRESS,
        CHECKOUT,
        ADD_TO_CART,
        RMV_FROM_CART,
        TXN_SUCCESS,
        TXN_FAILURE,
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
            
            stmt = conn.prepareStatement("insert into login(email, password, name) values(?, ?)");
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setString(3, name);
            
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
            PreparedStatement stmt = conn.prepareStatement("select name, details, cost, offer from items where id = ? and stock > 0");
            stmt.setInt(1, product_id);
            ResultSet res = stmt.executeQuery();
            
            while(res.next()) {
                x.put("name", res.getString(1));
                x.put("details", res.getString(2));
                x.put("cost", res.getInt(3));
                x.put("offer", res.getInt(4));
            }
            
            conn.close();
        }
        
        catch (Exception e) {
            Helper.handleError(e);
        }
        
        return x;
    }
    
    int getOfferOnProduct(Connection conn, int product_id) {
        int offer = -1;
        try {
            PreparedStatement stmt = conn.prepareStatement("select offer from items where id = ?");
            stmt.setInt(1, product_id);
            ResultSet res = stmt.executeQuery();
            
            if(res.next()) {
                offer = res.getInt(1);
            }
        }
        
        catch (Exception e) {
            Helper.handleError(e);
        }
        
        return offer;
    }
    
    String getNameOfProduct(Connection conn, int product_id) {
        String name = null;
        try {
            PreparedStatement stmt = conn.prepareStatement("select name from items where id = ?");
            stmt.setInt(1, product_id);
            ResultSet res = stmt.executeQuery();
            
            if(res.next()) {
                name = res.getString(1);
            }
        }
        
        catch (Exception e) {
            Helper.handleError(e);
        }
        
        return name;
    }
    
    int getPriceOfProduct(Connection conn, int product_id) {
        
        int cost = -1;
        
        try {
            PreparedStatement stmt = conn.prepareStatement("select cost from items where id = ?");
            stmt.setInt(1, product_id);
            
            ResultSet res = stmt.executeQuery();
            
            if(res.next()) {
                cost = res.getInt(1);
            }
            
        } catch (Exception e) {
            Helper.handleError(e);
        }
        
        return cost;
    }
    
    JSONObject getDetailsForCart(Connection conn, int product_id) {
        JSONObject x = new JSONObject();
        try {
            PreparedStatement stmt = conn.prepareStatement("select name, cost, offer from items where id = ? and stock > 0");
            stmt.setInt(1, product_id);
            ResultSet res = stmt.executeQuery();
            
            while(res.next()) {
                x.put("name", res.getString(1));
                x.put("cost", res.getInt(2));
                x.put("offer", res.getInt(3));
            }
            
            conn.close();
        }
        
        catch (Exception e) {
            Helper.handleError(e);
        }
        
        return x;
    }
    
    JSONObject getCartProducts(int customer_id) {
        
        JSONObject x = new JSONObject();
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select item_id, qty from offers where cust_id = ?");
            stmt.setInt(1, customer_id);
            
            ResultSet res = stmt.executeQuery();
            while(res.next()) {
                int item_id = res.getInt(1);
                JSONObject item = getDetailsForCart(conn, item_id);
                item.put("qty", res.getInt(2));
                x.put(item_id, item);
            }
            
            conn.close();
        }
        
        catch(Exception e) {
            Helper.handleError(e);
        }
        
        return x;
    }
    
    Boolean addToCart(int customer_id, int product_id) {
        Boolean status = false;
        
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select qty from cart where cust_id = ? and item_id = ?");
            stmt.setInt(1, customer_id);
            stmt.setInt(2, product_id);
            
            ResultSet res = stmt.executeQuery();
            int qty = 0;
            
            if(res.next()) {
                qty = res.getInt(1);
                stmt = conn.prepareStatement("update cart set qty = ? where cust_id = ? and item_id = ?");
                stmt.setInt(1, qty + 1);
                stmt.setInt(2, customer_id);
                stmt.setInt(3, product_id);
                
                stmt.execute();
            }
            
            else {
                stmt = conn.prepareStatement("insert into cart(cust_id, item_id, qty) values(?, ?, ?)");
                stmt.setInt(1, customer_id);
                stmt.setInt(2, product_id);
                stmt.setInt(3, qty + 1);
                
                stmt.execute();
            }
            
            logCustomerAction(conn, customer_id, String.valueOf(product_id), CustLogs.ADD_TO_CART);
            status = true;            
        }
        catch (Exception e) {
            Helper.handleError(e);
        }
        
        return status;
    }
    
    Boolean removeFromCart(int customer_id, int product_id) {
        Boolean status = false;
        
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select qty from cart where cust_id = ? and item_id = ?");
            stmt.setInt(1, customer_id);
            stmt.setInt(2, product_id);
            
            ResultSet res = stmt.executeQuery();
            
            if(res.next()) {
                int qty = res.getInt(1);
                if(qty > 1) {
                    stmt = conn.prepareStatement("update cart set qty = ? where cust_id = ? and item_id = ?");
                    stmt.setInt(1, qty - 1);
                    stmt.setInt(2, customer_id);
                    stmt.setInt(3, product_id);

                    stmt.execute();
                }
                
                else {
                    stmt = conn.prepareStatement("delete from cart where c_id = ? and p_id = ?");
                    stmt.setInt(1, customer_id);
                    stmt.setInt(2, product_id);

                    stmt.execute();
                }
            }
            
            logCustomerAction(conn, customer_id, String.valueOf(product_id), CustLogs.RMV_FROM_CART);
            status = true;            
        }
        catch (Exception e) {
            Helper.handleError(e);
        }
        
        return status;
    }
    
    Boolean checkInCart(int customer_id, int product_id) {
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
        
        return status;
        
    }
    
    Boolean setUserDetails(int id, String address) {
        Boolean status = false;
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("update login set address = ? where id = ?");
            stmt.setString(1, address);
            stmt.setInt(2, id);
            stmt.execute();
            
            logCustomerAction(conn, id, null, CustLogs.ADD_ADDRESS);
            status = true;
            
            conn.close();
            
        } catch(Exception e) {
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
    // implement password hashing here as a public static method
    // further, call it in registerUser before storing password
    
    public static void handleError(Exception e) {
        try {
            e.printStackTrace();
        } catch(Exception anotherOne) {
            System.out.println(anotherOne.toString());
        }
    }
}
