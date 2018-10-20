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
    
    Boolean checkUser(String username, String password) {
        
        Boolean status = false;
                
        try {
            Connection conn = connectSQL();
            PreparedStatement stmt = conn.prepareStatement("select level from login where username=? and password=?");
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet res = stmt.executeQuery();
            
            if (res.next()) {
                status = true;
                System.out.println(res.getInt(1));
                if(res.getInt(1) == 0) logAction(conn, username, CustLogs.LOGIN_SUCCESS);
                else logAdminAction(conn, username, AdminLogs.LOGIN);
            }
            else {
                logAction(conn, username, CustLogs.LOGIN_FAIL);
            }
            conn.close();            
            
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
        return status;
    }
    
    void logAction(Connection conn, String username, CustLogs type) {
        
        try {
            PreparedStatement stmt = conn.prepareStatement("insert into cust_logs(username, type) values(?,?)");
            stmt.setString(1, username);
            stmt.setString(2, type.toString());
            stmt.execute();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    void logAdminAction(Connection conn, String username, AdminLogs type) {
        
        try {
            PreparedStatement stmt = conn.prepareStatement("insert into admin_logs(username, type) values(?,?)");
            stmt.setString(1, username);
            stmt.setString(2, type.toString());
            stmt.execute();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
}
