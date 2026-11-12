package com.codequest.servlet;

import com.codequest.util.*;
import com.codequest.model.Mission;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/admin/questions")
public class ViewQuestionsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException {
        if(!WebUtil.admin(req,resp))return;
        List<Mission> missions=new ArrayList<>();
        try(Connection con=DBConnection.getConnection();PreparedStatement p=con.prepareStatement(
            "SELECT mission_id,title,category,difficulty,xp_reward FROM missions ORDER BY mission_id DESC");ResultSet r=p.executeQuery()) {
            while(r.next()) { Mission m=new Mission();m.id=r.getInt(1);m.title=r.getString(2);m.category=r.getString(3);m.difficulty=r.getString(4);m.xp=r.getInt(5);missions.add(m); }
        } catch(SQLException e){getServletContext().log("Question list failed",e);WebUtil.error(req,"Unable to load questions.");}
        req.setAttribute("missions",missions);WebUtil.view(req,resp,"view-questions");
    }
}
