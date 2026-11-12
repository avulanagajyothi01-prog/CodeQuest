package com.codequest.servlet;

import com.codequest.util.*;
import com.codequest.model.Mission;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/missions")
public class MissionServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException {
        if(!WebUtil.student(req,resp))return;
        String category=req.getParameter("category"),difficulty=req.getParameter("difficulty");
        if (category == null ||
                !Set.of("Java", "SQL", "DSA").contains(category)) {
            category = "";
        }

        if (difficulty == null ||
                !Set.of("Easy", "Medium", "Hard").contains(difficulty)) {
            difficulty = "";
        }
     
        req.setAttribute("category",category);req.setAttribute("difficulty",difficulty);
        Cookie categoryCookie = new Cookie("cq_last_category", category);
        categoryCookie.setHttpOnly(true);
        categoryCookie.setMaxAge(60 * 60 * 24 * 7);
        categoryCookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
        resp.addCookie(categoryCookie);
        List<Mission> missions=new ArrayList<>();
        String sql="SELECT m.mission_id,m.title,m.description,m.category,m.difficulty,m.option_a,m.option_b,m.option_c,m.option_d,m.xp_reward, "
            +"(s.submission_id IS NOT NULL) attempted FROM missions m LEFT JOIN mission_submissions s "
            +"ON s.mission_id=m.mission_id AND s.user_id=? WHERE (?='' OR m.category=?) AND (?='' OR m.difficulty=?) ORDER BY m.mission_id DESC";
        try(Connection con=DBConnection.getConnection();PreparedStatement p=con.prepareStatement(sql)){
            p.setInt(1,(Integer)req.getSession(false).getAttribute("userId"));
            p.setString(2,category);p.setString(3,category);p.setString(4,difficulty);p.setString(5,difficulty);
            try(ResultSet r=p.executeQuery()){
                while(r.next()) {
                    Mission m=new Mission();m.id=r.getInt(1);m.title=r.getString(2);m.description=r.getString(3);m.category=r.getString(4);
                    m.difficulty=r.getString(5);m.a=r.getString(6);m.b=r.getString(7);m.c=r.getString(8);m.d=r.getString(9);m.xp=r.getInt(10);m.attempted=r.getBoolean(11);missions.add(m);
                }
            }
        } catch(SQLException e){getServletContext().log("Missions failed",e);WebUtil.error(req,"Unable to load missions.");}
        req.setAttribute("missions",missions);WebUtil.view(req,resp,"missions");
    }
}
