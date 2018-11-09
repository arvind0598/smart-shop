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
public class UpdateCartTest {
    
    public UpdateCartTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of processRequest method, of class UpdateCart.
     */
    @Test
    public void testProcessRequest() {
        System.out.println("update cart");
        String temp_cust_id = "";
        String temp_item_id = "";
        String temp_qty = "";
        UpdateCart instance = new UpdateCart();
    
        // customer is not logged in
        assertEquals(-1, Integer.parseInt(instance.processRequest("-1", "5", "6").get("status").toString()));
        
        // invalid item id format
        assertEquals(-1, Integer.parseInt(instance.processRequest("1", "23basd", "6").get("status").toString()));
        
        // invalid quantity format
        assertEquals(-1, Integer.parseInt(instance.processRequest("1", "5", "a lot").get("status").toString()));
        
        // item does not exist
        assertEquals(0, Integer.parseInt(instance.processRequest("1", "56", "4").get("status").toString()));
        
        // item succesfully added
        assertEquals(1, Integer.parseInt(instance.processRequest("1", "2", "3").get("status").toString()));     
    }
    
}
