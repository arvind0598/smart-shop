/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;
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
@WebServlet(name = "Checkout", urlPatterns = {"/serve_checkout"})
public class Checkout extends HttpServlet {

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
        String temp_point_status = request.getParameter("points");
        
        Pattern numbers_only = Pattern.compile("^[0-9]+$");
        Boolean cust_id_valid = numbers_only.matcher(temp_cust_id).matches();
        Boolean point_status = Boolean.valueOf(temp_point_status);
        
        int order_id = -1;

        if(!cust_id_valid) {
            obj.put("status", -1);
            obj.put("message", "Login to add to cart.");
        }
        else {
            int cust_id = Integer.parseInt(temp_cust_id);
            System.out.println(cust_id + " " + point_status);
                    
            order_id = x.checkoutOrder(cust_id, point_status);
            if(order_id < 1) {
                obj.put("status", 0);
                obj.put("message", "There was an error.");
            }
            
            else {
                obj.put("status", 1);
                obj.put("message", "Succesfully placed order. Pay to continue.");
            }
            
        }
        try (PrintWriter out = response.getWriter()) {
            out.println(obj);
            out.close();
        }
        
        sess.setAttribute("order_id", order_id);
        sess.setAttribute("used_points", point_status);
        sess.removeAttribute("details");
        sess.removeAttribute("products");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "checks out a customer";
    }// </editor-fold>

}
