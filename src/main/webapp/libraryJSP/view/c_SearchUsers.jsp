<%@page language="java" contentType="text/html;charset=UTF-8" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.Iterator" %>
<%@page import="com.examples.libraryJSP.beans.*" %>
<jsp:useBean id="dataManager" scope="application"
             class="com.examples.libraryJSP.model.DataManager"/>
<% String base = (String) application.getAttribute("base"); %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Search users</title>
    <link rel="stylesheet" href="/libraryJSP/css/library.css" type="text/css"/>
</head>
<body>
<jsp:include page="m_TopMenu.jsp" flush="true"/>
<jsp:include page="m_LeftMenu.jsp" flush="true"/>
<%
    String keyword = request.getParameter("keyword");
    if (true) { // keyword != null && !keyword.trim().equals("")) {
%>
<div class="content">
    <h2>Search results</h2>
    <table>
        <tr>
            <th>Avatar</th>
            <th>ID</th>
            <th>Name</th>
            <th>Phone</th>
            <th>Details</th>
        </tr>
        <%
            ArrayList<User> users = dataManager.getUserSearchResults(keyword);
            Iterator<User> iterator = users.iterator();
            while (iterator.hasNext()) {
                User user = (User) iterator.next();
                int userId = user.getUserId();
        %>
        <tr>
            <td><a href="<%=base%>?action=userDetails&userId=<%=userId%>"><img class="user_pic" src=<%="image/avatar/" + userId%>></a>
            </td>
            <td><%=user.getUserId()%>
            </td>
            <td><%=user.getName()%>
            </td>
            <td><%=user.getPhone()%>
            </td>
            <td><a class="link1"
                   href="<%=base%>?action=userDetails&userId=<%=userId%>">
                Details</a></td>
        </tr>
        <%
            }
        %>
    </table>
</div>
<%
} else {
%><p class="error">Invalid search keyword!</p><%
    }
%>
</body>
</html>
