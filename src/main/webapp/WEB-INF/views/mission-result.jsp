<%@ page contentType="text/html;charset=UTF-8" %><%@ include file="partials/header.jspf" %>
<main class="container"><div class="auth-wrap"><div class="auth center"><span class="eyebrow">MISSION RESULT</span><h1><%=Boolean.TRUE.equals(request.getAttribute("correct"))?"Mission Complete!":"Submission Result"%></h1>
<p><%=WebUtil.escape(request.getAttribute("resultMessage"))%></p>
<% if(Boolean.TRUE.equals(request.getAttribute("correct"))){ %><div class="stat"><span>Reward Earned</span><strong>+<%=request.getAttribute("gained")%> XP</strong><small>Progress saved to MySQL</small></div><% } %>
<div class="quicklinks" style="justify-content:center"><a class="btn" href="<%=ctx%>/missions">More Missions</a><a class="btn ghost" href="<%=ctx%>/dashboard">My Dashboard</a></div></div></div></main><%@ include file="partials/footer.jspf" %>
