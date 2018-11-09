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
public class FeedbackTest {
    
    public FeedbackTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of processRequest method, of class Feedback.
     */
    @Test
    public void testProcessRequest() {
        System.out.println("feedback");
        Feedback instance = new Feedback();
        
        // reaches the end of code
        assertEquals(1, Integer.parseInt(instance.processRequest("1", "1", "kitna amazing product").get("status").toString()));
        
        // order id is invalid
        assertEquals(-1, Integer.parseInt(instance.processRequest("xyz", "1", "kitna amazing product").get("status").toString()));
        
        // customer is not logged in
        assertEquals(-1, Integer.parseInt(instance.processRequest("1", "-1", "kitna amazing product").get("status").toString()));
        
        // feedback is too long
        assertEquals(-1, Integer.parseInt(instance.processRequest("1", "1", "kitna amazing product kitna amazing product kitna amazing product kitna amazing product kitna amazing product kitna amazing product kitna amazing product kitna amazing product kitna amazing product kitna amazing product").get("status").toString()));
    }
    
}
