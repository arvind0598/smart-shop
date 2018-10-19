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
    
    enum Logs {
        LOGIN_SUCCESS, LOGIN_FAIL;
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
            PreparedStatement stmt = conn.prepareStatement("select count(*) from login where username=? and password=?");
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet res = stmt.executeQuery();
            
            if (res.next()) {
                if(res.getInt(1) == 1) {
                    logAction(conn, username, Logs.LOGIN_SUCCESS);
                    status = true;
                }
                else
                    logAction(conn, username, Logs.LOGIN_FAIL);
            }
            else {
                logAction(conn, username, Logs.LOGIN_FAIL);
            }
            conn.close();            
            
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
        return status;
    }
    
    void logAction(Connection conn, String username, Logs type) {
        
        try {
            PreparedStatement stmt = conn.prepareStatement("insert into logs(username, type) values(?,?)");
            stmt.setString(1, username);
            stmt.setString(2, type.toString());
            stmt.execute();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
}
