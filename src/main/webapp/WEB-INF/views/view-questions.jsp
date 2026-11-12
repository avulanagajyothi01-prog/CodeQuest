<%@ page contentType="text/html;charset=UTF-8" %><%@ page import="java.util.List,com.codequest.model.Mission" %><%@ include file="partials/header.jspf" %>
<main class="container"><div class="pagehead"><span class="eyebrow">ADMIN / MISSION LIBRARY</span><h1>Question Management</h1><p>All programming questions currently stored in MySQL.</p></div><%@ include file="partials/message.jspf" %>
<div class="quicklinks"><a class="btn" href="<%=ctx%>/admin/questions/new">+ Add a Question</a><a class="btn ghost" href="<%=ctx%>/admin">Back to Admin</a></div>
<% List<Mission> missions=(List<Mission>)request.getAttribute("missions"); if("1".equals(request.getParameter("added"))){ %><div class="message success">Mission saved successfully.</div><% } %>
<div class="tablewrap"><table><thead><tr><th>ID</th><th>Title</th><th>Category</th><th>Difficulty</th><th>XP</th></tr></thead><tbody>
<% if(missions!=null)for(Mission m:missions){ %><tr><td>#<%=m.id%></td><td><%=WebUtil.escape(m.title)%></td><td><%=WebUtil.escape(m.category)%></td><td><span class="pill"><%=WebUtil.escape(m.difficulty)%></span></td><td><%=m.xp%> XP</td></tr><% } %>
<% if(missions==null||missions.isEmpty()){ %><tr><td colspan="5" class="empty">No missions yet. Add your first question.</td></tr><% } %>
</tbody></table></div></main><%@ include file="partials/footer.jspf" %>
