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
@WebServlet(name = "Feedback", urlPatterns = {"/serve_feedback"})
public class Feedback extends HttpServlet {

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
        String temp_order_id = request.getParameter("order");
        String feedback = request.getParameter("feedback");

        Boolean cust_id_valid = Helper.regexChecker(Helper.Regex.NUMBERS_ONLY, temp_cust_id);
        Boolean order_id_valid = Helper.regexChecker(Helper.Regex.NUMBERS_ONLY, temp_order_id);

        int order_id = -1;

        if (!cust_id_valid) {
            obj.put("status", -1);
            obj.put("message", "Login to provide feedback.");
            return;
        }
        
        if(!order_id_valid || feedback.length() > 200) {
            obj.put("status", -1);
            obj.put("message", "Invalid request.");
        }
            
        int cust_id = Integer.parseInt(temp_cust_id);

        Boolean status = x.provideFeedback(order_id, feedback, cust_id);
        if (!status) {
            obj.put("status", 0);
            obj.put("message", "There was an error.");
        } else {
            obj.put("status", 1);
            obj.put("message", "Your feedback was submitted.");
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
        return "gets feedback from a customer";
    }// </editor-fold>
}
