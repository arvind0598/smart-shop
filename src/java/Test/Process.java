/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import java.sql.*;

/**
 *
 * @author de-arth
 */
public class Process {
    
    static final String DB_URL = "jdbc:mysql://localhost/shop";
    static final String DB_USER = "root";
    static final String DB_PASS = "password";
    
    enum CustLogs {
        LOGIN_SUCCESS, 
        LOGIN_FAIL,
        LOGOUT,
        REGISTER,
        ADD_TO_CART,
        RMV_FROM_CART,
        CHECKOUT,
        TXN_SUCCESS,
        TXN_FAILURE;
    }
    
    enum AdminLogs {
        LOGIN, 
        LOGOUT,
        REGISTER,
        ADD_CATEGORY,
        ADD_ITEM,
        MODIFY_STOCK,
        ADD_OFFER;
    }
    
    enum DeliveryStatus {
        DEL_RECEIVED,
        DEL_DISPATCHED,
        DEL_DELIVERED;
    }
    
    Connection connectSQL() 
            throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        return conn;
    }
    
    int checkUser(String useremail, String password) {
        
        int status = -1;
                
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select id from login where email=? and password=?");
            stmt.setString(1, useremail);
            stmt.setString(2, password);

            ResultSet res = stmt.executeQuery();
            
            if(res.next()) {
                status = res.getInt(1);
                logAction(conn, useremail, CustLogs.LOGIN_SUCCESS);
            } else {
                logAction(conn, useremail, CustLogs.LOGIN_FAIL);
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
            
            stmt = conn.prepareStatement("insert into login(email, password, level) values(?, ?, 0)");
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.execute();
            
            int id = checkUser(email, password);
            stmt = conn.prepareStatement("insert into customer(id, name) values(?, ?)");
            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.execute();
            
            logAction(conn, email, CustLogs.REGISTER);
            status = true;
            
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
        } catch(Exception e) {
            Helper.handleError(e);
        }
        
        return status;
    }
    
    
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
