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
@WebServlet(name = "ChangePassword", urlPatterns = {"/serve_changepass"})
public class ChangePassword extends HttpServlet {

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
        
        String useremail = ((JSONObject)sess.getAttribute("details")).get("email").toString();
        String currPassword = request.getParameter("curr_pass");
        String newPassword = request.getParameter("new_pass");
        
        Boolean useremail_correct = Helper.regexChecker(Helper.Regex.EMAIL, useremail);
        
        // add password validation here
        Boolean curr_password_correct = true;
        Boolean new_password_correct = true;
        
        if(!useremail_correct || !curr_password_correct || !new_password_correct) {
            try (PrintWriter out = response.getWriter()) {
                obj.put("status", -1);
                obj.put("message", "Input provided was not valid.");
                out.println(obj);
                out.close();                
            }             
            return;
        } 
       
        int currUser = x.checkUser(useremail, currPassword, true);
        
        if(currUser <= 0) {
            try (PrintWriter out = response.getWriter()) {
                obj.put("status", 0);
                obj.put("message", "Current password does not match.");
                out.println(obj);
                out.close();                
            }             
            return;
        }
        
        x.changePassword(currUser, newPassword);
        try (PrintWriter out = response.getWriter()) {
            obj.put("status", 1);
            obj.put("message", "Succesfully changed Password");
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
        return "servlet changes password for a user";
    }// </editor-fold>
}
