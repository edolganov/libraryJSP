<%@ page import="com.examples.libraryJSP.model.SessionDataManager" %>
<%@ page import="com.examples.libraryJSP.beans.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<jsp:useBean id="dataManager" scope="application"
             class="com.examples.libraryJSP.model.DataManager"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
    String base = (String) application.getAttribute("base");
    String imageURL = (String) application.getAttribute("imageURL");
    String httpMethod = (String) application.getAttribute("httpMethod");
    SessionDataManager sdm = (SessionDataManager) session.getAttribute("sessionDataManager");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Login</title>
    <link rel="stylesheet" href="/libraryJSP/css/library.css" type="text/css"/> <!--  -->
</head>
<body>
<jsp:include page="m_TopMenu.jsp" flush="true"/>
<div class="content">

    <div class="box">
        <div class="title">Welcome to library</div>
        <p></p>
        <form style="border: 0px solid; padding: 0; margin: 0;" id="loginForm" action="?action=login"
              method=<%=httpMethod%>>
            <input id="action" type="hidden" name="action" value="login">
            <table>
                <tr>
                    <td>Login:</td>
                    <td><input id="login" type="text" name="login" size="15"></td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td><input id="pass" type="text" name="pass" size="15"><br/></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input id="submit" type="submit" value="Sign in"></td>
                    </td>
                </tr>
            </table>
        </form>
    </div>


   <%--<div class="box">--%>
        <%--<a href="<%=base%>?action=sign&hack=true">--%>
            <%--<mark>hack</mark>--%>
        <%--</a>--%>
        <%--<%--%>
            <%--if (sdm.isHack()) {--%>
                <%--sdm.setHack(false);--%>
        <%--%>--%>
        <%--<table>--%>
            <%--<tr>--%>
                <%--<th>ID</th>--%>
                <%--<th>Name</th>--%>
                <%--<th>Phone</th>--%>
                <%--<th>Details</th>--%>
            <%--</tr>--%>
            <%--<%--%>
                <%--ArrayList<User> users = dataManager.getUserSearchResults("");--%>
                <%--Iterator<User> iterator = users.iterator();--%>
                <%--while (iterator.hasNext()) {--%>
                    <%--User user = (User) iterator.next();--%>
                    <%--int userId = user.getUserId();--%>
            <%--%>--%>
            <%--<tr>--%>
                <%--<td><%=user.getUserId()%>--%>
                <%--</td>--%>
                <%--<td><%=user.getName()%>--%>
                <%--</td>--%>
                <%--<td><%=user.getPhone()%>--%>
                <%--</td>--%>
                <%--<td><a class="link1"--%>
                       <%--href="<%=base%>?action=userDetails&userId=<%=userId%>">--%>
                    <%--Details</a></td>--%>
            <%--</tr>--%>
            <%--<%--%>
                <%--}--%>
            <%--%>--%>
        <%--</table>--%>
        <%--<%}%>--%>
    <%--</div>--%>
</div>
</body>
</html>
