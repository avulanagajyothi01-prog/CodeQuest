<%@ page contentType="text/html;charset=UTF-8" %><%@ page import="java.util.List,com.codequest.model.Player" %><%@ include file="partials/header.jspf" %>
<main class="container"><div class="pagehead"><span class="eyebrow">THE HALL OF FAME</span><h1>Global Leaderboard</h1><p>Real rankings based on XP earned in programming missions.</p></div><%@ include file="partials/message.jspf" %>
<% List<Player> players=(List<Player>)request.getAttribute("players"); %>
<div class="tablewrap"><table><thead><tr><th>Rank</th><th>Player</th><th>Level</th><th>Missions Solved</th><th>Total XP</th></tr></thead><tbody>
<% if(players!=null)for(int i=0;i<players.size();i++){Player player=players.get(i); %>
<tr><td class="rank"><%=i==0?"★ 1":i==1?"◆ 2":i==2?"● 3":String.valueOf(i+1)%></td><td><strong><%=WebUtil.escape(player.username)%></strong></td><td><span class="pill">Level <%=player.level%></span></td><td><%=player.completed%></td><td><strong><%=player.xp%> XP</strong></td></tr><% } %>
<% if(players==null||players.isEmpty()){ %><tr><td colspan="5" class="empty">No players yet. Be the first to join CodeQuest!</td></tr><% } %>
</tbody></table></div><div class="quicklinks"><a class="btn" href="<%=ctx%>/register">Join the Competition</a><a class="btn ghost" href="<%=ctx%>/missions">Browse Missions</a></div></main><%@ include file="partials/footer.jspf" %>
