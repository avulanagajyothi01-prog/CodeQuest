package com.codequest.servlet;

import com.codequest.util.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.sql.*;

@WebServlet("/submit-answer")
public class SubmitAnswerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException {
        if(!WebUtil.student(req,resp))return;
        if(!WebUtil.validToken(req)){resp.sendError(403);return;}
        int missionId;
        try{missionId=Integer.parseInt(req.getParameter("missionId"));}catch(Exception e){resp.sendRedirect(req.getContextPath()+"/missions");return;}
        String selected=req.getParameter("answer");
        if(selected==null||!java.util.Set.of("A","B","C","D").contains(selected)){
            req.setAttribute("resultMessage","Choose an answer before submitting.");WebUtil.view(req,resp,"mission-result");return;
        }
        int userId=(Integer)req.getSession(false).getAttribute("userId");
        try(Connection con=DBConnection.getConnection()){
            con.setAutoCommit(false);
            try {
                try(PreparedStatement p=con.prepareStatement("SELECT user_id,xp FROM users WHERE user_id=? FOR UPDATE")) {
                    p.setInt(1,userId);
                    try(ResultSet r=p.executeQuery()){if(!r.next()){con.rollback();resp.sendRedirect(req.getContextPath()+"/login");return;}}
                }
                String correct;int reward;
                try(PreparedStatement p=con.prepareStatement("SELECT correct_answer,xp_reward FROM missions WHERE mission_id=?")) {
                    p.setInt(1,missionId);
                    try(ResultSet r=p.executeQuery()){
                        if(!r.next()){con.rollback();resp.sendRedirect(req.getContextPath()+"/missions");return;}
                        correct=r.getString(1);reward=r.getInt(2);
                    }
                }
                try(PreparedStatement p=con.prepareStatement("SELECT submission_id FROM mission_submissions WHERE user_id=? AND mission_id=?")){
                    p.setInt(1,userId);p.setInt(2,missionId);
                    try(ResultSet r=p.executeQuery()){
                        if(r.next()) {con.rollback();req.setAttribute("resultMessage","You already attempted this mission. One attempt is permitted.");WebUtil.view(req,resp,"mission-result");return;}
                    }
                }
                boolean isCorrect=correct.equals(selected);int gained=isCorrect?reward:0;
                try(PreparedStatement p=con.prepareStatement("INSERT INTO mission_submissions(user_id,mission_id,selected_answer,is_correct,xp_earned) VALUES(?,?,?,?,?)")){
                    p.setInt(1,userId);p.setInt(2,missionId);p.setString(3,selected);p.setBoolean(4,isCorrect);p.setInt(5,gained);p.executeUpdate();
                }
                if(isCorrect){
                    try(PreparedStatement p=con.prepareStatement("UPDATE users SET xp=xp+?,level=FLOOR((xp+?)/100)+1 WHERE user_id=?")){
                        p.setInt(1,gained);p.setInt(2,gained);p.setInt(3,userId);p.executeUpdate();
                    }
                }
                con.commit();req.setAttribute("correct",isCorrect);req.setAttribute("gained",gained);
                req.setAttribute("resultMessage",isCorrect?"Correct answer! You earned XP.":"Not quite. No XP awarded this time.");
            } catch(SQLException e){con.rollback();throw e;}
        } catch(SQLException e){getServletContext().log("Submit answer transaction failed",e);req.setAttribute("resultMessage","Unable to save your submission. Please try again.");}
        WebUtil.view(req,resp,"mission-result");
    }
}
