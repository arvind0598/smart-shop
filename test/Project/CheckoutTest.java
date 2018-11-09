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
public class CheckoutTest {
    
    public CheckoutTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of processRequest method, of class Checkout.
     */
    @Test
    public void testProcessRequest() throws Exception {
        System.out.println("checkout");
        Checkout instance = new Checkout();
        
        // customer is not logged in
        assertEquals(-1, Integer.parseInt(instance.processRequest("-1", "true").get("status").toString()));

        // unknown customer
        assertEquals(0, Integer.parseInt(instance.processRequest("50", "false").get("status").toString()));
        
        // random value was passed instead of boolean but is considered false
        assertEquals(1 , Integer.parseInt(instance.processRequest("1", "asdasd").get("status").toString()));
        
        // all successful
        assertEquals(1, Integer.parseInt(instance.processRequest("1", "true").get("status").toString()));
    }
    
}
