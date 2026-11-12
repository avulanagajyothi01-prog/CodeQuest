package com.codequest.servlet;

import com.codequest.util.*;
import com.codequest.model.Player;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/leaderboard")
public class LeaderboardServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException {
        List<Player> players=new ArrayList<>();
        String sql="SELECT u.user_id,u.username,u.xp,u.level,COUNT(CASE WHEN s.is_correct=1 THEN 1 END) AS completed "
            +"FROM users u LEFT JOIN mission_submissions s ON u.user_id=s.user_id "
            +"GROUP BY u.user_id,u.username,u.xp,u.level ORDER BY u.xp DESC,u.username ASC LIMIT 100";
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){
            while(r.next()){Player x=new Player();x.id=r.getInt(1);x.username=r.getString(2);x.xp=r.getInt(3);x.level=r.getInt(4);x.completed=r.getInt(5);players.add(x);}
        } catch(SQLException e){getServletContext().log("Leaderboard failed",e);WebUtil.error(req,"Leaderboard unavailable.");}
        req.setAttribute("players",players);WebUtil.view(req,resp,"leaderboard");
    }
}
