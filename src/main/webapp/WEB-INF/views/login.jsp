<%@ page contentType="text/html;charset=UTF-8" %><%@ include file="partials/header.jspf" %>
<main class="container"><div class="auth-wrap"><div class="auth"><span class="eyebrow">WELCOME BACK</span><h1>Student Login</h1><p>Continue learning, solving, and leveling up.</p>
<% if("1".equals(request.getParameter("registered"))) { %><div class="message success">Account created! Sign in to get started.</div><% } %><%@ include file="partials/message.jspf" %>
<form method="post" action="<%=ctx%>/login"><input type="hidden" name="csrf" value="<%=WebUtil.escape(WebUtil.token(request))%>">
<label for="identifier">Username or email</label><input required id="identifier" name="identifier" autocomplete="username">
<label for="password">Password</label><input required type="password" id="password" name="password" autocomplete="current-password">
<button class="btn full" type="submit">Sign In &rarr;</button></form><p class="center">New here? <a style="color:var(--blue)" href="<%=ctx%>/register">Create an account</a></p></div></div></main>
<%@ include file="partials/footer.jspf" %>
