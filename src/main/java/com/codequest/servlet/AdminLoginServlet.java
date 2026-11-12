package com.codequest.servlet;

import com.codequest.util.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.sql.*;

@WebServlet("/admin-login")
public class AdminLoginServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException {WebUtil.view(req,resp,"admin-login");}
    protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException {
        if(!WebUtil.validToken(req)){resp.sendError(403);return;}
        String username=req.getParameter("username"),password=req.getParameter("password");
        if(username==null||password==null){WebUtil.error(req,"Enter credentials.");WebUtil.view(req,resp,"admin-login");return;}
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("SELECT admin_id,password_hash FROM admins WHERE username=?")) {
            p.setString(1,username.trim());
            try(ResultSet r=p.executeQuery()) {
                if(r.next() && PasswordUtil.verify(password,r.getString("password_hash"))) {
                    HttpSession old=req.getSession(false);if(old!=null)old.invalidate();
                    HttpSession session=req.getSession(true);session.setAttribute("adminId",r.getInt("admin_id"));
                    session.setAttribute("adminName",username.trim());session.setMaxInactiveInterval(1800);
                    resp.sendRedirect(req.getContextPath()+"/admin");return;
                }
            }
            WebUtil.error(req,"Invalid admin credentials.");
        } catch(SQLException e) {getServletContext().log("Admin login failed",e);WebUtil.error(req,"Database unavailable.");}
        WebUtil.view(req,resp,"admin-login");
    }
}
