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
    
    enum CustLogs {
        LOGIN_SUCCESS, 
        LOGIN_FAIL,
        LOGOUT,
        REGISTER,
        ADD_TO_CART,
        RMV_FROM_CART,
        CHECKOUT,
        TXN_SUCCESS,
        TXN_FAILURE,
        FEEDBACK;
    }
    
    enum AdminLogs {
        LOGIN, 
        LOGOUT,
        REGISTER,
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
                if(!internal) logAction(conn, useremail, CustLogs.LOGIN_SUCCESS);
            } else {
                if(!internal) logAction(conn, useremail, CustLogs.LOGIN_FAIL);
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
            
            stmt = conn.prepareStatement("insert into login(email, password, level) values(?, ?, 0)");
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.execute();
            
            int id = checkUser(email, password, true);
            stmt = conn.prepareStatement("insert into customer(id, name) values(?, ?)");
            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.execute();
            
            logAction(conn, email, CustLogs.REGISTER);
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
                logAction(conn, email, CustLogs.LOGOUT);      
                status = true;
            }
            
            conn.close();
        } catch(Exception e) {
            Helper.handleError(e);
        }
        
        return status;
    }
    
    JSONObject getCategories() {
        
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
    
    JSONObject getProducts(int category_id) {
        JSONObject x = new JSONObject();
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select id, name, cost from categories where cat_id = ?");
            stmt.setInt(1, category_id);
            ResultSet res = stmt.executeQuery();
            
            while(res.next()) {
                JSONObject item = new JSONObject();
                item.put("name", res.getString(2));
                item.put("cost", res.getInt(3));
                x.put(res.getInt(1), item);
            }
            
            conn.close();
        }
        
        catch (Exception e) {
            Helper.handleError(e);
        }
        
        return x;
    }
    
    JSONObject getProductDetails(int product_id) {
        JSONObject x = new JSONObject();
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select name, details, cost from items where id = ?");
            stmt.setInt(1, product_id);
            ResultSet res = stmt.executeQuery();
            
            while(res.next()) {
                int offer = getOfferOnProduct(conn, product_id);
                x.put("name", res.getString(1));
                x.put("details", res.getString(2));
                x.put("cost", res.getInt(3));
                x.put("offer", offer);
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
            PreparedStatement stmt = conn.prepareStatement("select amt from offers where id = ?");
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
    
    JSONObject getCartProducts(int customer_id) {
        
        JSONObject x = new JSONObject();
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select p_id, qty from offers where c_id = ?");
            stmt.setInt(1, customer_id);
            
            ResultSet res = stmt.executeQuery();
            while(res.next()) {
                JSONObject item = new JSONObject();
                int p_id = res.getInt(1);
                String name = getNameOfProduct(conn, p_id);
                int cost = getPriceOfProduct(conn, p_id);
                int offer = getOfferOnProduct(conn, p_id);
                item.put("name", name);
                item.put("cost", cost);
                item.put("offer", offer);
                item.put("qty", res.getInt(2));
                x.put(p_id, item);
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
            PreparedStatement stmt = conn.prepareStatement("select qty from cart where c_id = ? and p_id = ?");
            stmt.setInt(1, customer_id);
            stmt.setInt(2, product_id);
            
            ResultSet res = stmt.executeQuery();
            int qty = 0;
            
            if(res.next()) {
                qty = res.getInt(1);
                stmt = conn.prepareStatement("update cart set qty = ? where c_id = ? and p_id = ?");
                stmt.setInt(1, qty + 1);
                stmt.setInt(2, customer_id);
                stmt.setInt(3, product_id);
                
                stmt.execute();
            }
            
            else {
                stmt = conn.prepareStatement("insert into cart(c_id, p_id, qty) values(?, ?, ?)");
                stmt.setInt(1, customer_id);
                stmt.setInt(2, product_id);
                stmt.setInt(3, qty + 1);
                
                stmt.execute();
            }
            
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
            PreparedStatement stmt = conn.prepareStatement("select qty from cart where c_id = ? and p_id = ?");
            stmt.setInt(1, customer_id);
            stmt.setInt(2, product_id);
            
            ResultSet res = stmt.executeQuery();
            
            if(res.next()) {
                int qty = res.getInt(1);
                if(qty > 1) {
                    stmt = conn.prepareStatement("update cart set qty = ? where c_id = ? and p_id = ?");
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
            
            status = true;            
        }
        catch (Exception e) {
            Helper.handleError(e);
        }
        
        return status;
    }
    
    // helpful functions
    
    void logAction(Connection conn, String username, CustLogs type) {
        
        try {
            PreparedStatement stmt = conn.prepareStatement("insert into cust_logs(email, type) values(?,?)");
            stmt.setString(1, username);
            stmt.setString(2, type.toString());
            stmt.execute();
            
        } catch (Exception e) {
            Helper.handleError(e);
        }
        
    }
    
    void logAdminAction(Connection conn, String username, AdminLogs type) {
        
        try {
            PreparedStatement stmt = conn.prepareStatement("insert into admin_logs(email, type) values(?,?)");
            stmt.setString(1, username);
            stmt.setString(2, type.toString());
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
