<%@page language="java" contentType="text/html;charset=UTF-8" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.Iterator" %>
<%@page import="com.examples.libraryJSP.beans.Book" %>
<jsp:useBean id="dataManager" scope="application"
             class="com.examples.libraryJSP.model.DataManager"/>
<% String base = (String) application.getAttribute("base"); %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Browse Catalog</title>
    <link rel="stylesheet" href="/libraryJSP/css/library.css" type="text/css"/>
</head>
<body>
<jsp:include page="m_TopMenu.jsp" flush="true"/>
<jsp:include page="m_LeftMenu.jsp" flush="true"/>
<%
    String categoryId = request.getParameter("id");
    String categoryName = null;
    if (categoryId != null && !categoryId.trim().equals("")) {
        try {
            categoryName = dataManager.getCategoryName(categoryId);
        } catch (NumberFormatException e) {
        }
    }
    if (categoryName != null) {
%>
<div class="content">
    <h2>Select Catalog</h2>
    <p>Category: <strong><%=categoryName%>
    </strong></p>
    <table>
        <tr>
            <th>Cover</th>
            <th>Title</th>
            <th>Author</th>
            <th>Year</th>
            <th>Details</th>
        </tr>
        <%
            ArrayList<Book> books = dataManager.getBooksInCategory(categoryId);
            Iterator<Book> iterator = books.iterator();
            while (iterator.hasNext()) {
                Book book = (Book) iterator.next();
                String pId = book.getId();
        %>
        <tr>
            <td><a href="<%=base%>?action=bookDetails&bookId=<%=book.getId()%>"><img class="book_pic"
                                                                               src=<%="image/cover/" + book.getId()%>></a>
            </td>
            <td><%=book.getTitle()%>
            </td>
            <td><%=book.getAuthor()%>
            </td>
            <td><%=book.getYear()%>
            </td>
            <td><a class="link1"
                   href="<%=base%>?action=bookDetails&bookId=<%=pId%>">
                Details</a></td>
        </tr>
        <%
            }
        %>
    </table>
</div>
<%
} else {
%><p class="error">Invalid category!</p><%
    }
%>
</body>
</html>
