<%@page language="java" contentType="text/html;charset=UTF-8" %>
<%@page import="com.examples.libraryJSP.beans.User" %>
<%@ page import="com.examples.libraryJSP.beans.permissions.Activities" %>
<%@ page import="com.examples.libraryJSP.beans.permissions.Roles" %>
<%@ page import="com.examples.libraryJSP.model.SessionDataManager" %>
<jsp:useBean id="dataManager" scope="application"
             class="com.examples.libraryJSP.model.DataManager"/>
<%
    String base = (String) application.getAttribute("base");
    String imageURL = (String) application.getAttribute("imageURL");
    String httpMethod = (String) application.getAttribute("httpMethod");
    SessionDataManager sdm = (SessionDataManager) session.getAttribute("sessionDataManager");
    User currentUser = sdm == null ? null : sdm.getCurrentSessionUser();
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>User roles</title>
    <link rel="stylesheet" href="/libraryJSP/css/library.css" type="text/css"/>
</head>
<body>
<jsp:include page="m_TopMenu.jsp" flush="true"/>
<jsp:include page="m_LeftMenu.jsp" flush="true"/>

<% if (currentUser.activityAllowed(Activities.USERS_MANAGE)
        || currentUser.activityAllowed(Activities.USERS_SERVE)) {
%>

<div class="content">
    <h2>Profile</h2>
    <%
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            User user = dataManager.getUserById(userId);
            dataManager.getUserRoles(user);
            if (user != null) {
    %>

    <div class="box">
        <div class="title">Edit user roles</div>
        <p></p>
        <form style="border: 0px solid; padding: 0; margin: 0;" id="userUpdateForm" action="?action=applyUserRoles"
              method=<%=httpMethod%>>
            <input type="hidden" name="action" value="applyUserRoles">
            <table>
                <tr>
                    <td><i>picture:</i></td>
                    <td>
                        <table>
                            <tr>
                                <td><b>User ID:  </b><%=user.getUserId()%>
                                    <input id="userId" type="hidden" name="userId" value=<%=user.getUserId()%>></td>
                            </tr>
                            <tr>
                                <td><b>Login:    </b><%=user.getLogin()%></td>
                            </tr>
                            <tr>
                                <td>
                                    <form id="userRoles" action="?action=applyUserRoles" method="httpMethod">
                                        <% for (Roles role : Roles.values()) {
                                            out.println("<input id='" + role +
                                                    "' type='checkbox' name='" + role + "' value='" + role + "' " +
                                                    (user.roleAllowed(role) ? "checked" : "") + ">" + role + "<br>");
                                        }
                                        %>

                                        <input id="submit" type="submit" value="Apply user roles">
                                    </form>
                                </td>
                            </tr>

                        </table>
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
