<%@ page contentType="text/html;charset=UTF-8" %><%@ include file="partials/header.jspf" %>
<main class="container"><div class="auth-wrap"><div class="auth"><span class="eyebrow">LOCAL INITIAL SETUP</span><h1>Create First Admin</h1><p>Available only from your own computer while the admins table is empty. Create the first admin before public deployment.</p><%@ include file="partials/message.jspf" %>
<form method="post" action="<%=ctx%>/setup-admin"><input type="hidden" name="csrf" value="<%=WebUtil.escape(WebUtil.token(request))%>">
<label>Admin username</label><input name="username" minlength="3" maxlength="30" pattern="[A-Za-z0-9_]+" required>
<label>Admin password (10+ characters)</label><input name="password" type="password" minlength="10" required>
<button class="btn full" type="submit">Create First Admin</button></form></div></div></main>
<%@ include file="partials/footer.jspf" %>
