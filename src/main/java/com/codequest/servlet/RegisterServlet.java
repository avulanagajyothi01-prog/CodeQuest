package com.codequest.servlet;

import com.codequest.util.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.sql.*;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebUtil.view(req, resp, "register");
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if (!WebUtil.validToken(req)) { resp.sendError(403); return; }
        String username = value(req, "username"), email = value(req, "email").toLowerCase();
        String password = req.getParameter("password"), confirm = req.getParameter("confirm");
        if (!username.matches("[A-Za-z0-9_]{3,30}") || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
                || password == null || password.length() < 8 || !password.equals(confirm)) {
            WebUtil.error(req, "Use a username of 3–30 letters/numbers/underscores, a valid email, and matching passwords of at least 8 characters.");
            WebUtil.view(req, resp, "register"); return;
        }
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO users(username,email,password_hash) VALUES(?,?,?)")) {
            ps.setString(1, username); ps.setString(2, email); ps.setString(3, PasswordUtil.hash(password));
            ps.executeUpdate();
            resp.sendRedirect(req.getContextPath() + "/login?registered=1");
        } catch (SQLIntegrityConstraintViolationException e) {
            WebUtil.error(req, "Username or email already exists."); WebUtil.view(req, resp, "register");
        } catch (SQLException e) {
            getServletContext().log("Registration database error", e);
            WebUtil.error(req, "Registration unavailable. Check database configuration."); WebUtil.view(req, resp, "register");
        }
    }
    private String value(HttpServletRequest req, String key) {
        String v = req.getParameter(key); return v == null ? "" : v.trim();
    }
}
