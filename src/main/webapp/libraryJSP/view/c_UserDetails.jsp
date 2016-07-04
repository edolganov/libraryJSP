<%@page language="java" contentType="text/html;charset=UTF-8" %>
<%@page import="com.examples.libraryJSP.beans.User" %>
<%@ page import="com.examples.libraryJSP.beans.permissions.Activities" %>
<%@ page import="com.examples.libraryJSP.model.SessionDataManager" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<jsp:useBean id="dataManager" scope="application"
             class="com.examples.libraryJSP.model.DataManager"/>
<%
    String base = (String) application.getAttribute("base");
    String imageURL = (String) application.getAttribute("imageURL");
    SessionDataManager sdm = (SessionDataManager) session.getAttribute("sessionDataManager");
    User currentUser = sdm == null ? null : sdm.getCurrentSessionUser();
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>User details</title>
    <link rel="stylesheet" href="/libraryJSP/css/library.css" type="text/css"/>
    <style>
        mark {
            color: red;
            background-color: white;
        }
    </style>
</head>
<body>
<jsp:include page="m_TopMenu.jsp" flush="true"/>
<jsp:include page="m_LeftMenu.jsp" flush="true"/>

<% if (currentUser != null && (Integer.parseInt(request.getParameter("userId")) == currentUser.getUserId() ||
        (currentUser.activityAllowed(Activities.USERS_MANAGE)
                || currentUser.activityAllowed(Activities.USERS_SERVE)))) {
%>

<div class="content">
    <h2>Profile</h2>
    <%
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            User user = dataManager.getUserById(userId);
            if (user != null) {
    %>

    <table>
        <tr>
            <td>
                <table>
                    <tr>
                        <td>
                            <img class="small_image" src=<%="image/avatar/" + userId%>>  <!--  "$ {pageContext.request.contextPath}/images/avatar"  <img src="<%//=(imageURL + user.getUserId())%>.gif"/> -->
                            <!--<i>picture:</i> -->
                        </td>
                        <td>
                            <table>
                                <tr>
                                    <td><b>User ID</b></td>
                                    <td><%=user.getUserId()%>
                                    </td>
                                </tr>
                                <tr>
                                    <td><b>Login</b></td>
                                    <td><%=user.getLogin()%>
                                    </td>
                                </tr>
                                <tr>
                                    <td><b>Name</b></td>
                                    <td><%=user.getName()%>
                                    </td>
                                </tr>
                                <tr>
                                    <td><b>Phone</b></td>
                                    <td><%=user.getPhone()%>
                                    </td>
                                </tr>
                                <tr>
                                    <td><b>Address</b></td>
                                    <td><%=user.getAddress()%>
                                    </td>
                                </tr>
                                <tr>
                                    <td><b>Comments</b></td>
                                    <td><%=user.getComments()%>
                                    </td>
                                </tr>
                                <tr>
                                    <td><b>Active account</b></td>
                                    <td><%=user.isActive()%>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <tr>
                        <td>
                        </td>
                        <td colspan="2" align="right">
                            <a class="link1"
                               href="<%=base%>?action=handleUser&userId=<%=user.getUserId()%>">
                                Edit user details</a><br>
                            <% if (currentUser.activityAllowed(Activities.USERS_MANAGE)) {%>
                            <a class="link1"
                               href="<%=base%>?action=userRoles&userId=<%=user.getUserId()%>">
                                Edit access roles</a><br>
                            <a class="link1"
                               href="<%=base%>?action=startServe&userId=<%=user.getUserId()%>">
                                Start serve user</a><br>
                            <a class="link1"
                               href="<%=base%>?action=endServe&userId=<%=user.getUserId()%>">
                                End serve user</a><br>
                            <a class="link1"
                               href="<%=base%>?action=deleteUser&userId=<%=user.getUserId()%>">
                                <mark>delete user</mark></a>
                            <%}%>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <h2>Borrowed books</h2>
                <table>
                    <tr>
                        <th>Title</th>
                        <th>Author</th>
                        <th>Year</th>
                        <th>Details</th>
                        <th>User</th>
                        <th>Expire date</th>
                    </tr>
                    <%

                        ArrayList<LinkedHashMap<String, String>> borrowedBooks = dataManager.borrowedBooksList(user, false);
                        Iterator<LinkedHashMap<String, String>> iterator = borrowedBooks.iterator();
                        while (iterator.hasNext()) {
                            LinkedHashMap<String, String> entry = (LinkedHashMap<String, String>) iterator.next();
                    %>
                    <tr>
                        <td><%=entry.get("title")%>
                        </td>
                        <td><%=entry.get("author")%>
                        </td>
                        <td><%=entry.get("year")%>
                        </td>
                        <td><a class="link1"
                               href="<%=base%>?action=bookDetails&bookId=<%=entry.get("book_id")%>">
                            Details</a></td>
                        <td><a class="link1"
                               href="<%=base%>?action=userDetails&userId=<%=entry.get("user_id")%>">
                            <%=dataManager.getUserById(Integer.parseInt(entry.get("user_id"))).getName()%>
                        </a></td>
                        <td><%=entry.get("expire_date")%>
                        </td>
                    </tr>
                    <%
                        }
                    %>
                </table>
            </td>
        </tr>
    </table>
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
