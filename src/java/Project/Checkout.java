/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
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

        String temp_cust_id = sess.getAttribute("login") == null ? "-1" : sess.getAttribute("login").toString();
        String temp_point_status = request.getParameter("points");

        JSONObject obj = processRequest(temp_cust_id, temp_point_status);

        try (PrintWriter out = response.getWriter()) {
            out.println(obj);
            out.close();
        }

        Boolean success = Integer.parseInt(obj.get("status").toString()) == 1;
        if (success) {
            sess.setAttribute("order_id", obj.get("order"));
            sess.setAttribute("used_points", obj.get("used_points"));
            sess.removeAttribute("details");
            sess.removeAttribute("products");
        }

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

    private void sendEmail(String email, int order, String name) throws MalformedURLException, IOException {

        String preURL = String.format("https://script.google.com/macros/s/AKfycbwZm6E2OzyHqnjwQAe10TgAobIyH1tmhk3nWpt_E3ahlMIajm8/exec?email=%s&order=%d&name=%s", email, order, name.split(" (?!.* )")[0]);

        URL url = new URL(preURL);

        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        System.out.println("GET " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder lol = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                lol.append(inputLine);
            }

            in.close();
        } else {
            System.out.println("GET request not worked");
        }
    }

    private JSONObject processRequest(String temp_cust_id, String temp_point_status) throws IOException {
        JSONObject obj = new JSONObject();
        Boolean cust_id_valid = Helper.regexChecker(Helper.Regex.NUMBERS_ONLY, temp_cust_id);
        Boolean point_status = Boolean.valueOf(temp_point_status);

        if (!cust_id_valid) {
            obj.put("status", -1);
            obj.put("message", "Login to add to cart.");
            return obj;
        }

        int cust_id = Integer.parseInt(temp_cust_id);
        int order_id = x.checkoutOrder(cust_id, point_status);

        obj.put("status", order_id >= 1 ? 1 : 0);
        obj.put("message", order_id >= 1 ? "Succesfully placed order." : "There was an error.");

        if (order_id >= 1) {
            obj.put("order", order_id);
            obj.put("used_points", point_status);
        }

        JSONObject cust = x.getCustomerDetails(cust_id);
        sendEmail(cust.get("email").toString(), order_id, cust.get("name").toString());

        return obj;
    }
}
