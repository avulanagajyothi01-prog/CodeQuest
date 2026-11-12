<%@ page contentType="text/html;charset=UTF-8" %><%@ include file="partials/header.jspf" %>
<main class="container"><div class="auth-wrap"><div class="auth"><span class="eyebrow">START YOUR JOURNEY</span><h1>Create Account</h1><p>Join CodeQuest and start earning XP.</p><%@ include file="partials/message.jspf" %>
<form method="post" action="<%=ctx%>/register"><input type="hidden" name="csrf" value="<%=WebUtil.escape(WebUtil.token(request))%>">
<label for="username">Username</label><input required minlength="3" maxlength="30" pattern="[A-Za-z0-9_]+" id="username" name="username" autocomplete="username" value="<%=WebUtil.escape(request.getParameter("username"))%>">
<label for="email">Email address</label><input required type="email" maxlength="100" id="email" name="email" autocomplete="email" value="<%=WebUtil.escape(request.getParameter("email"))%>">
<label for="password">Password (8+ characters)</label><input required type="password" minlength="8" id="password" name="password" autocomplete="new-password">
<label for="confirm">Confirm password</label><input required type="password" minlength="8" id="confirm" name="confirm" autocomplete="new-password">
<button class="btn full" type="submit">Create Account &rarr;</button></form><p class="center">Already a player? <a style="color:var(--blue)" href="<%=ctx%>/login">Sign in</a></p></div></div></main>
<%@ include file="partials/footer.jspf" %>
