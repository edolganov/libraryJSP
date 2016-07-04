<%@page language="java" contentType="text/html;charset=UTF-8" %>
<%@page import="com.examples.libraryJSP.beans.User" %>
<%@ page import="com.examples.libraryJSP.beans.permissions.Activities" %>
<%@ page import="com.examples.libraryJSP.model.SessionDataManager" %>
<jsp:useBean id="dataManager" scope="application"
             class="com.examples.libraryJSP.model.DataManager"/>
<%
    String base = (String) application.getAttribute("base");
    String imageURL = (String) application.getAttribute("imageURL");
    String httpMethod = (String)application.getAttribute("httpMethod");
    SessionDataManager sdm = (SessionDataManager)session.getAttribute("sessionDataManager");
    User currentUser = sdm==null?null:sdm.getCurrentSessionUser();
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>User details</title>
    <link rel="stylesheet" href="/libraryJSP/css/library.css" type="text/css"/>
</head>
<body>
<jsp:include page="m_TopMenu.jsp" flush="true"/>
<jsp:include page="m_LeftMenu.jsp" flush="true"/>

<% if (currentUser.activityAllowed(Activities.USERS_MANAGE) || (currentUser.getUserId() == ((request.getParameter("userId")==null)? -1: Integer.parseInt(request.getParameter("userId"))))) {
%>

<div class="content">
    <h2>Profile</h2>
    <%
        try {
            int userId = ((request.getParameter("userId")==null)? -1: Integer.parseInt(request.getParameter("userId"))); //Integer.parseInt(request.getParameter("userId"));
            User user = dataManager.getUserById(userId);
            if (user != null) {
    %>

    <div class="box">
        <div class="title">Edit user details</div>
        <p></p>
        <form style="border: 0px solid; padding: 0; margin: 0;" id="userUpdateForm" action="?action=updateUserData"
              method=<%=httpMethod%> enctype="multipart/form-data">
            <input type="hidden" name="action" value="updateUserData"/>
            <table>
                <tr>
                    <td></td>
                    <td><i>Avatar:</i></td>
                    <td><input id="avatar" type="file" name="avatar" size="15" accept="image"></td>
                </tr>
                <tr>
                    <td></td>
                    <td><b>User ID:</b></td>
                    <td><%=user.getUserId()%>
                        <input id="userId" type="hidden" name="userId" value="<%=user.getUserId()%>">
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td><b>Login:</b></td>
                    <td><input id="login" type="text" name="login" size="15" value="<%=user.getLogin()%>"></td>
                </tr>
                <tr>
                    <td></td>
                    <td><b>Password:</b></td>
                    <td><input id="pass" type="text" name="pass" size="15"><br/></td>
                </tr>


                <tr>
                    <td></td>
                    <td><b>Name:</b></td>
                    <td><input id="name" type="text" name="name" size="15" value="<%=user.getName()%>"></td>
                </tr>
                <tr>
                    <td></td>
                    <td><b>Phone:</b></td>
                    <td><input id="phone" type="text" name="phone" size="15" value="<%=user.getPhone()%>"></td>
                </tr>
                <tr>
                    <td></td>
                    <td><b>Address:</b></td>
                    <td><input id="address" type="text" name="address" size="15" value="<%=user.getAddress()%>"><br/></td>
                </tr>
                <tr>
                    <td></td>
                    <td><b>Comments:</b></td>
                    <td><input id="comments" type="text" name="comments" size="15" value="<%=user.getComments()%>"></td>
                </tr>
                <tr>
                    <td></td>
                    <td><b>Active account:</b></td>
                    <td>
                        <form id="activeUser">
                            <input id="activeTrue" type="radio" name="active" value="true"
                                   size="15" <%=user.isActive() ? "checked" : ""%>>active<br>
                            <input id="activeFalse" type="radio" name="active" value="false"
                                   size="15" <%=user.isActive() ? "" : "checked"%>>nonactive
                        </form>
                    </td>
                </tr>


                <tr>
                    <td></td>
                    <td></td>
                    <td><input id="submit" type="submit" value="Update user data"></td>
                    </td>
                </tr>
            </table>
        </form>
    </div>

    <%
        }
    } catch (Exception e) {
    %><p class="error">Invalid user identifier!</p><%
    }
%>
</div>

<% }
%>

</body>
</html>
