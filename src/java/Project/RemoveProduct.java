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
@WebServlet(name = "RemoveProduct", urlPatterns = {"/serve_rmvitem"})
public class RemoveProduct extends HttpServlet {

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

        String temp_admin_id = sess.getAttribute("admlogin") == null ? "-1" : sess.getAttribute("admlogin").toString();
        String temp_id = request.getParameter("id");
        
        Boolean admin_ok = Helper.regexChecker(Helper.Regex.NUMBERS_ONLY, temp_admin_id);
        Boolean id_ok = Helper.regexChecker(Helper.Regex.NUMBERS_ONLY, temp_id);
        
        if(!admin_ok) {
            try (PrintWriter out = response.getWriter()) {
                obj.put("status", -1);
                obj.put("message", "Login to continue.");
                out.println(obj);
                out.close();                
            }             
            return;
        }        
        
        else if(!id_ok) {
            try (PrintWriter out = response.getWriter()) {
                obj.put("status", -1);
                obj.put("message", "Input provided was not valid.");
                out.println(obj);
                out.close();                
            }             
            return;
        } 
       
        int product_id = Integer.parseInt(temp_id);
        int admin_id = Integer.parseInt(temp_admin_id);
        
        Boolean status = x.removeProduct(product_id, admin_id);
        obj.put("status", status);
        obj.put("message", status ? "Succesfully removed." : "Unable to remove.");
                   
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
        return "removes a product";
    }// </editor-fold>

}
