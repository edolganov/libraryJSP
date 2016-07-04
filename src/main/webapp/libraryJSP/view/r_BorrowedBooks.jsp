<%@page language="java" contentType="text/html;charset=UTF-8" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.Iterator" %>
<%@page import="com.examples.libraryJSP.beans.*" %>
<%@ page import="java.util.LinkedHashMap" %>
<jsp:useBean id="dataManager" scope="application"
             class="com.examples.libraryJSP.model.DataManager"/>
<% String base = (String) application.getAttribute("base"); %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Borrowed books</title>
    <link rel="stylesheet" href="/libraryJSP/css/library.css" type="text/css"/>
</head>
<body>
<jsp:include page="m_TopMenu.jsp" flush="true"/>
<jsp:include page="m_LeftMenu.jsp" flush="true"/>

<div class="content">
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

            ArrayList<LinkedHashMap<String, String>> borrowedBooks = dataManager.borrowedBooksList(null, false);
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
                <%=dataManager.getUserById(Integer.parseInt(entry.get("user_id"))).getName()%></a></td>
            <td><%=entry.get("expire_date")%></td>
        </tr>
        <%
            }
        %>
    </table>
</div>

</body>
</html>
