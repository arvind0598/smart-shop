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
@WebServlet(name = "Register", urlPatterns = {"/serve_register"})
public class Register extends HttpServlet {
    
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

        String useremail = request.getParameter("email");
        String username = request.getParameter("name");
        String password = request.getParameter("password");
        
        Boolean useremail_correct = Helper.regexChecker(Helper.Regex.EMAIL, useremail);
        
        // add password validation here
        Boolean password_correct = true;
        Boolean username_correct = Helper.regexChecker(Helper.Regex.MIN_SIX_ALPHA_SPACES, username);
        
        if(!useremail_correct || !password_correct || !username_correct) {
            try (PrintWriter out = response.getWriter()) {
                JSONObject obj = new JSONObject();
                obj.put("status", -1);
                obj.put("message", "Input provided was not valid.");
                out.println(obj);
                out.close();                
            }             
            return;
        } 
        
        String hashedPassword = Helper.hashPassword(password);
        Boolean status = x.registerUser(username, useremail, hashedPassword);

        try (PrintWriter out = response.getWriter()) {
            HttpSession sess = request.getSession();
            JSONObject obj = new JSONObject();
            
            obj.put("status", status);
            String message = status ? "Registration successful" : "Registration unsuccessful";
            obj.put("message", message);
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
        return "servlet registers a user";
    }// </editor-fold>

}
