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
public class AddCategoryTest {
    
    public AddCategoryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of processRequest method, of class AddCategory.
     */
    @Test
    public void testProcessRequest() {
        System.out.println("add category");
        AddCategory instance = new AddCategory();

        // admin is not logged in
        assertEquals(-1, Integer.parseInt(instance.processRequest("shoes", "-1").get("status").toString()));
        
        // category name is not valid
        assertEquals(-1, Integer.parseInt(instance.processRequest("1234", "2").get("status").toString()));
        
        // all successful
        assertEquals(1, Integer.parseInt(instance.processRequest("shoes", "2").get("status").toString()));
        
        // sql exception because repeated name
        assertEquals(0, Integer.parseInt(instance.processRequest("shoes", "2").get("status").toString()));
     
    }
    
}
