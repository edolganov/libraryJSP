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
    <title>Search books</title>
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
            <th>Cover</th>
            <th>Title</th>
            <th>Author</th>
            <th>Year</th>
            <th>Details</th>
            <th>Status</th>
        </tr>
        <%
            ArrayList<Book> books = dataManager.getBookSearchResults(keyword);
            Iterator<Book> iterator = books.iterator();
            while (iterator.hasNext()) {
                Book book = (Book) iterator.next();
                String bookId = book.getId();
        %>
        <tr>
            <td><a href="<%=base%>?action=bookDetails&bookId=<%=bookId%>"><img class="book_pic" src=<%="image/cover/" + book.getId()%>></a>
            </td>
            <td><%=book.getTitle()%>
            </td>
            <td><%=book.getAuthor()%>
            </td>
            <td><%=book.getYear()%>
            </td>
            <td><a class="link1"
                   href="<%=base%>?action=bookDetails&bookId=<%=bookId%>">
                Details</a>
            </td>
            <td><%=((book.isActive()) ? ((dataManager.bookBorrowed(book)) ? "borrowed" : "free") : "non active")%>
            </td>
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
