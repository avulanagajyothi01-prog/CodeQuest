package com.codequest.servlet;

import com.codequest.util.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebInitParam;
import java.io.IOException;
import java.sql.*;

@WebServlet(value="/admin", initParams=@WebInitParam(name="dashboardVersion", value="1.0"))
public class AdminDashboardServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        getServletContext().setAttribute("appTitle", "CodeQuest");
        getServletContext().log("Admin dashboard initialized: " + getInitParameter("dashboardVersion"));
    }
    protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException {
        if(!WebUtil.admin(req,resp))return;
        try(Connection c=DBConnection.getConnection();Statement s=c.createStatement()) {
            try(ResultSet r=s.executeQuery("SELECT COUNT(*) FROM missions")){if(r.next())req.setAttribute("questionCount",r.getInt(1));}
            try(ResultSet r=s.executeQuery("SELECT COUNT(*) FROM users")){if(r.next())req.setAttribute("studentCount",r.getInt(1));}
        } catch(SQLException e){getServletContext().log("Admin stats failed",e);WebUtil.error(req,"Unable to load statistics.");}
        WebUtil.view(req,resp,"admin-dashboard");
    }
}
