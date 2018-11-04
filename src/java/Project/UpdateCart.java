/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;

/**
 *
 * @author de-arth
 */
@WebServlet(name = "UpdateCart", urlPatterns = {"/serve_updatecart"})
public class UpdateCart extends HttpServlet {

    Process x = new Process();

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            out.println("{\"status\":-1,\"message\":\"send post request\"}");
            out.close();
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession sess = request.getSession();
        JSONObject obj = new JSONObject();
        
        String temp_cust_id = sess.getAttribute("login") == null ? "" : sess.getAttribute("login").toString();
        String temp_item_id = request.getParameter("id");
        String temp_qty = request.getParameter("qty");
        
        if(temp_qty == null) temp_qty = "1";
        
        Boolean cust_id_valid = Helper.regexChecker(Helper.Regex.NUMBERS_ONLY, temp_cust_id);
        Boolean item_id_valid = Helper.regexChecker(Helper.Regex.NUMBERS_ONLY, temp_item_id);
        Boolean qty_valid = Helper.regexChecker(Helper.Regex.NUMBERS_ONLY, temp_qty);

        if(!cust_id_valid) {
            obj.put("status", -1);
            obj.put("message", "Login to add to cart.");
        }
        else if(!item_id_valid || !qty_valid) {
            obj.put("status", -1);
            obj.put("message", "Invalid Request");
        }
        else {
            int cust_id = Integer.parseInt(temp_cust_id);
            int item_id = Integer.parseInt(temp_item_id);
            int qty = Integer.parseInt(temp_qty);
                    
            Boolean status = x.updateCart(cust_id, item_id, qty);
            if(!status) {
                obj.put("status", 0);
                obj.put("message", "Unable to update cart.");
            }
            
            else {
                obj.put("status", 1);
                obj.put("message", "Succesfully updated.");
            }
            
        }
        try (PrintWriter out = response.getWriter()) {
            out.println(obj);
            out.close();
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "updates an item to a given qty in the cart";
    }// </editor-fold>
}
