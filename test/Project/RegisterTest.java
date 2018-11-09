/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author de-arth
 */
public class RegisterTest {
    
    public RegisterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of processRequest method, of class Register.
     */
    @Test
    public void testProcessRequest() {
        System.out.println("processRequest");
        String useremail = "";
        String username = "";
        String password = "";
        Register instance = new Register();
        
        //invalid email
        assertEquals(-1, Integer.parseInt(instance.processRequest("arx", "arvind suresh", "password").get("status").toString()));
        
        //invalid password
        assertEquals(-1, Integer.parseInt(instance.processRequest("arv@arvind.com", "arvind suresh", "asd").get("status").toString()));
        
        //invalid name of user
        assertEquals(-1, Integer.parseInt(instance.processRequest("arv@arvind.com", "21st century cool", "password").get("status").toString()));
        
        //duplicate email id
        assertEquals(0, Integer.parseInt(instance.processRequest("arvind0598@gmail.com", "arvind suresh", "password").get("status").toString()));
        
        // succesful registration
        assertEquals(1, Integer.parseInt(instance.processRequest("arv@arvind.com", "arvind suresh", "password").get("status").toString()));
    }
    
}
