/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project;

import Project.Helper.Regex;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.json.simple.JSONObject;

/**
 *
 * @author de-arth
 */
@WebServlet(name = "ModifyProduct", urlPatterns = {"/serve_modproduct"})
@MultipartConfig
public class ModifyProduct extends HttpServlet {

    Process x = new Process();
    Credentials cred = new Credentials();

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
        String temp_cat_id = request.getParameter("cat_id") == null ? "-1" : request.getParameter("cat_id");
        String temp_item_id = request.getParameter("item_id") == null ? "-1" : request.getParameter("item_id");
        String product_name = request.getParameter("product_name");
        String desc = request.getParameter("desc");
        String keywords = request.getParameter("keywords");
        String temp_cost = request.getParameter("cost");
        String temp_offer = request.getParameter("offer");
        String temp_stock = request.getParameter("stock");

        Boolean stock_ok = Helper.regexChecker(Regex.NUMBERS_ONLY, temp_stock);
        Boolean offer_ok = Helper.regexChecker(Regex.NUMBERS_ONLY, temp_offer);
        Boolean cost_ok = Helper.regexChecker(Regex.NUMBERS_ONLY, temp_cost);
        Boolean admin_ok = Helper.regexChecker(Regex.NUMBERS_ONLY, temp_admin_id);
        Boolean product_name_ok = Helper.regexChecker(Regex.MIN_SIX_ALPHANUM_SPACES, product_name);
        Boolean desc_ok = Helper.regexChecker(Regex.DESCRIPTION, desc);
        Boolean keywords_ok = Helper.regexChecker(Regex.MIN_SIX_LOWERCASE_SPACES, keywords);

        Boolean cat_id_ok = Helper.regexChecker(Regex.NUMBERS_ONLY, temp_cat_id);
        Boolean item_id_ok = Helper.regexChecker(Regex.NUMBERS_ONLY, temp_item_id);

        Boolean options_ok = cat_id_ok || item_id_ok;

        if (!admin_ok) {
            obj.put("status", -1);
            obj.put("message", "Login to continue.");
            return;
        } else if (!stock_ok || !offer_ok || !cost_ok || !admin_ok || !product_name_ok || !desc_ok || !keywords_ok || !options_ok) {
            obj.put("status", -1);
            obj.put("message", "Invalid Request");
            return;
        } else {

            int cost = Integer.parseInt(temp_cost);
            int offer = Integer.parseInt(temp_offer);

            if (cost <= offer) {
                obj.put("status", -1);
                obj.put("message", "cost cannot be less than offer.");
            } else {
                JSONObject product = new JSONObject();
                product.put("cost", cost);
                product.put("offer", offer);
                product.put("name", product_name);
                product.put("desc", desc);
                product.put("keywords", keywords);
                product.put("stock", Integer.parseInt(temp_stock));

                int status = -1;

                // first check if product id is valid then its updating
                if (cat_id_ok) {
                    status = x.addProduct(Integer.parseInt(temp_cat_id), product, Integer.parseInt(temp_admin_id));
                    // check here if status is more than zero and add image

                    if (status > 0) {
                        Part filePart = request.getPart("image");
                        OutputStream os = null;
                        InputStream fileContent = null;

                        obj.put("status", status);

                        try {
                            os = new FileOutputStream(new File(cred.FILE_PATH + status + ".png"));
                            fileContent = filePart.getInputStream();

                            System.out.println(cred.FILE_PATH + status + ".png");

                            int read = 0;
                            final byte[] bytes = new byte[1024];

                            while ((read = fileContent.read(bytes)) != -1) {
                                os.write(bytes, 0, read);
                            }

                            obj.put("message", "Item added with image.");

                        } catch (Exception e) {
                            e.printStackTrace();
                            obj.put("message", "Item added without image.");
                        }

                    } else {
                        obj.put("message", "Item added without image.");
                    }
                } else {
                    obj.put("status", status);
                    obj.put("message", "There was an error.");
                }

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
        return "updates an item or adds a new item";
    }// </editor-fold>
}
