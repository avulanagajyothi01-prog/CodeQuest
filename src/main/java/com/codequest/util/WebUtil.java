package com.codequest.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

public final class WebUtil {
    private static final SecureRandom RANDOM = new SecureRandom();
    private WebUtil() { }

    public static String escape(Object value) {
        if (value == null) return "";
        return value.toString().replace("&", "&amp;").replace("<", "&lt;")
            .replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
    }

    public static String token(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String token = (String) session.getAttribute("csrf");
        if (token == null) {
            byte[] bytes = new byte[32]; RANDOM.nextBytes(bytes);
            token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
            session.setAttribute("csrf", token);
        }
        return token;
    }

    public static boolean validToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String expected = session == null ? null : (String) session.getAttribute("csrf");
        String actual = request.getParameter("csrf");
        return expected != null && actual != null && java.security.MessageDigest.isEqual(
                expected.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                actual.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    public static boolean student(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("userId") instanceof Integer) return true;
        resp.sendRedirect(req.getContextPath() + "/login"); return false;
    }

    public static boolean admin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("adminId") instanceof Integer) return true;
        resp.sendRedirect(req.getContextPath() + "/admin-login"); return false;
    }

    public static void view(HttpServletRequest req, HttpServletResponse resp, String name)
            throws jakarta.servlet.ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/" + name + ".jsp").forward(req, resp);
    }

    public static void error(HttpServletRequest req, String message) { req.setAttribute("error", message); }
}
