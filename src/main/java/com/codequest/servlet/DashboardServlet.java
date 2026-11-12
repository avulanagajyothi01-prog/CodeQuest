package com.codequest.servlet;

import com.codequest.util.*;
import com.codequest.model.Player;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.sql.*;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException {
        if(!WebUtil.student(req,resp))return;
        int id=(Integer)req.getSession(false).getAttribute("userId");
        String sql="SELECT u.user_id,u.username,u.xp,u.level,COUNT(CASE WHEN s.is_correct=1 THEN 1 END) AS completed "
          +"FROM users u LEFT JOIN mission_submissions s ON u.user_id=s.user_id WHERE u.user_id=? "
          +"GROUP BY u.user_id,u.username,u.xp,u.level";
        try(Connection con=DBConnection.getConnection();PreparedStatement p=con.prepareStatement(sql)){
            p.setInt(1,id);
            try(ResultSet r=p.executeQuery()) {
                if(r.next()) {Player player=new Player();player.id=id;player.username=r.getString("username");player.xp=r.getInt("xp");player.level=r.getInt("level");player.completed=r.getInt("completed");req.setAttribute("player",player);}
                else {req.getSession().invalidate();resp.sendRedirect(req.getContextPath()+"/login");return;}
            }
        } catch(SQLException e){getServletContext().log("Dashboard failed",e);WebUtil.error(req,"Dashboard currently unavailable.");}
        WebUtil.view(req,resp,"dashboard");
    }
}
