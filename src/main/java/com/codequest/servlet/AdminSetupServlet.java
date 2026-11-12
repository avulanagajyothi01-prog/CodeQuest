package com.codequest.servlet;

import com.codequest.util.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.sql.*;

@WebServlet("/setup-admin")
public class AdminSetupServlet extends HttpServlet {
    private boolean local(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        return "127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip);
    }
    private boolean adminExists() throws SQLException {
        try(Connection c = DBConnection.getConnection(); PreparedStatement p=c.prepareStatement("SELECT 1 FROM admins LIMIT 1");
            ResultSet r=p.executeQuery()) { return r.next(); }
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!local(req)) {resp.sendError(403);return;}
        try { if(adminExists()) {resp.sendRedirect(req.getContextPath()+"/admin-login");return;} }
        catch(SQLException e) {getServletContext().log("Admin setup failed",e);WebUtil.error(req,"Database unavailable.");}
        WebUtil.view(req,resp,"setup-admin");
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!local(req) || !WebUtil.validToken(req)) {resp.sendError(403);return;}
        String username = req.getParameter("username"), password = req.getParameter("password");
        if(username==null || !username.matches("[A-Za-z0-9_]{3,30}") || password==null || password.length()<10) {
            WebUtil.error(req,"Enter a username (3–30 characters) and password (at least 10 characters).");
            WebUtil.view(req,resp,"setup-admin");return;
        }
        try(Connection c=DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try(PreparedStatement lock=c.prepareStatement("SELECT admin_id FROM admins LIMIT 1 FOR UPDATE");
                ResultSet r=lock.executeQuery()) {
                if(r.next()) {c.rollback();resp.sendRedirect(req.getContextPath()+"/admin-login");return;}
            }
            try(PreparedStatement p=c.prepareStatement("INSERT INTO admins(username,password_hash) VALUES(?,?)")) {
                p.setString(1,username);p.setString(2,PasswordUtil.hash(password));p.executeUpdate();c.commit();
            } catch(SQLException ex) {c.rollback();throw ex;}
            resp.sendRedirect(req.getContextPath()+"/admin-login?created=1");
        } catch(SQLException ex) {getServletContext().log("Admin creation failed",ex);WebUtil.error(req,"Could not create admin. Check database or existing account.");WebUtil.view(req,resp,"setup-admin");}
    }
}
