package com.codequest.servlet;

import com.codequest.util.WebUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin-logout")
public class AdminLogoutServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!WebUtil.validToken(req)) { resp.sendError(403); return; }
        HttpSession session = req.getSession(false);
        if (session != null) session.invalidate();
        resp.sendRedirect(req.getContextPath() + "/admin-login");
    }
}
