<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="com.examples.libraryJSP.beans.User" %>
<%@ page import="com.examples.libraryJSP.beans.permissions.Activities" %>
<%@ page import="com.examples.libraryJSP.model.SessionDataManager" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.examples.libraryJSP.beans.Book" %>
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
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Book details</title>
    <link rel="stylesheet" href="/libraryJSP/css/library.css" type="text/css">
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

<div class="content">
    <h2>Book details</h2>
    <%
        try {
            Book book = dataManager.getBookById(request.getParameter("bookId"));
            if (book != null) {
    %>

    <table>
        <tr>
            <td>
                <table>
                    <tr>
                        <td>
                            <img class="small_image" src=<%="image/cover/" + book.getId()%>>
                            <!--  "$ {pageContext.request.contextPath}/images/avatar"  <img src="<%//=(imageURL + user.getUserId())%>.gif"/> -->
                            <!--<i>picture:</i> -->
                        </td>
                        <td>
                            <table>
                                <tr>
                                    <td><b>ISBN</b></td>
                                    <td><%=book.getId()%>
                                    </td>
                                </tr>
                                <tr>
                                    <td><b>Author</b></td>
                                    <td><%=book.getAuthor()%>
                                    </td>
                                </tr>
                                <tr>
                                    <td><b>Title</b></td>
                                    <td><%=book.getTitle()%>
                                    </td>
                                </tr>
                                <tr>
                                    <td><b>Year</b></td>
                                    <td><%=book.getYear()%>
                                    </td>
                                </tr>
                                <tr>
                                    <td><b>Category</b></td>
                                    <td><%=dataManager.getCategoryName(Integer.toString(book.getCategory_id()))%>
                                    </td>
                                </tr>
                                <tr>
                                    <td><b>About</b></td>
                                    <td><%=(book.getAbout() == null) ? "" : book.getAbout()%>
                                    </td>
                                </tr>
                                <tr>
                                    <td><b>Active</b></td>
                                    <td><%=book.isActive()%>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <% if (currentUser.activityAllowed(Activities.BOOKS_MANAGE)) {%>
                    <tr>
                        <td>
                        </td>
                        <td colspan="2" align="right">
                            <a class="link1"
                               href="<%=base%>?action=handleBook&bookId=<%=book.getId()%>">
                                Edit book details</a><br>
                            <a class="link1"
                               href="<%=base%><%=((currentUser.activityAllowed(Activities.USERS_SERVE))?
                        ((dataManager.bookAccessForIssue(book))?
                                "?action=issueReturnBook&bookId=":(dataManager.bookBorrowed(book))?
                                "?action=issueReturnBook&bookId=":"?action=bookDetails&bookId="):"?action=bookDetails&bookId=")%><%=book.getId()%>">
                                <%=(currentUser.activityAllowed(Activities.USERS_SERVE)) ?
                                        ((dataManager.bookAccessForIssue(book)) ?
                                                "Issue book" : (dataManager.bookBorrowed(book)) ?
                                                "Return book" : "Book non active") : ""%>
                            </a>
                            <br>
                            <a class="link1"
                               href="<%=base%>?action=deleteBook&bookId=<%=book.getId()%>">
                                <mark>delete book</mark>
                            </a>
                        </td>
                    </tr>
                    <%}%>
                </table>
            </td>
        </tr>
    </table>
    <%
        }
    } catch (Exception e) {
    %><p class="error">Invalid book identifier!</p><%
    }
%>
</div>

</body>
</html>
