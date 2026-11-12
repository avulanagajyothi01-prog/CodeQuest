package com.codequest.servlet;

import com.codequest.util.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.sql.*;

@WebServlet("/admin/questions/new")
public class AddQuestionServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException {
        if(WebUtil.admin(req,resp))WebUtil.view(req,resp,"add-question");
    }
    protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException {
        if(!WebUtil.admin(req,resp))return;
        if(!WebUtil.validToken(req)){resp.sendError(403);return;}
        req.setCharacterEncoding("UTF-8");
        String title=v(req,"title"),description=v(req,"description"),category=v(req,"category"),difficulty=v(req,"difficulty");
        String a=v(req,"a"),b=v(req,"b"),c=v(req,"c"),d=v(req,"d"),correct=v(req,"correct");
        int reward;
        try{reward=Integer.parseInt(v(req,"xp"));}catch(NumberFormatException e){reward=-1;}
        if(title.isEmpty()||description.isEmpty()||a.isEmpty()||b.isEmpty()||c.isEmpty()||d.isEmpty()
            || !java.util.Set.of("Java","SQL","DSA").contains(category)
            || !java.util.Set.of("Easy","Medium","Hard").contains(difficulty)
            || !java.util.Set.of("A","B","C","D").contains(correct)
            || reward<1||reward>500) {
            WebUtil.error(req,"Complete all fields. XP must be between 1 and 500.");WebUtil.view(req,resp,"add-question");return;
        }
        String sql="INSERT INTO missions(title,description,category,difficulty,option_a,option_b,option_c,option_d,correct_answer,xp_reward) VALUES(?,?,?,?,?,?,?,?,?,?)";
        try(Connection con=DBConnection.getConnection();PreparedStatement p=con.prepareStatement(sql)) {
            String[] items={title,description,category,difficulty,a,b,c,d,correct};
            for(int i=0;i<items.length;i++)p.setString(i+1,items[i]);p.setInt(10,reward);
            p.executeUpdate();resp.sendRedirect(req.getContextPath()+"/admin/questions?added=1");
        } catch(SQLException e){getServletContext().log("Add question failed",e);WebUtil.error(req,"Could not save question.");WebUtil.view(req,resp,"add-question");}
    }
    private String v(HttpServletRequest r,String k){String x=r.getParameter(k);return x==null?"":x.trim();}
}
