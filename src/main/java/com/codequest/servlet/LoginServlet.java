package com.codequest.servlet;

import com.codequest.util.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.sql.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebUtil.view(req, resp, "login");
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!WebUtil.validToken(req)) { resp.sendError(403); return; }
        String identifier = req.getParameter("identifier"), password = req.getParameter("password");
        if (identifier == null || password == null) { WebUtil.error(req,"Enter your login details."); WebUtil.view(req,resp,"login"); return; }
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT user_id,username,password_hash FROM users WHERE username=? OR email=?")) {
            ps.setString(1,identifier.trim()); ps.setString(2,identifier.trim());
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next() && PasswordUtil.verify(password,rs.getString("password_hash"))) {
                    HttpSession old = req.getSession(false); if(old != null) old.invalidate();
                    HttpSession session = req.getSession(true); session.setAttribute("userId",rs.getInt("user_id"));
                    session.setAttribute("username",rs.getString("username")); session.setMaxInactiveInterval(1800);
                    resp.sendRedirect(req.getContextPath()+"/dashboard"); return;
                }
            }
            WebUtil.error(req,"Incorrect username/email or password.");
        } catch(SQLException ex) { getServletContext().log("Student login failed",ex); WebUtil.error(req,"Database error. Please try again."); }
        WebUtil.view(req,resp,"login");
    }
}
