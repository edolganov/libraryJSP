<%@page language="java" contentType="text/html;charset=UTF-8" %>
<%@page import="com.examples.libraryJSP.beans.User" %>
<%@ page import="com.examples.libraryJSP.beans.permissions.Activities" %>
<%@ page import="com.examples.libraryJSP.model.SessionDataManager" %>
<%@ page import="com.examples.libraryJSP.beans.Book" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.HashMap" %>
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
    <title>Book details</title>
    <link rel="stylesheet" href="/libraryJSP/css/library.css" type="text/css"/>
</head>
<body>
<jsp:include page="m_TopMenu.jsp" flush="true"/>
<jsp:include page="m_LeftMenu.jsp" flush="true"/>

<% if (currentUser.activityAllowed(Activities.USERS_MANAGE)) {
%>

<div class="content">
    <h2>Book details</h2>
    <%
        try {
            Book book = dataManager.getBookById(request.getParameter("bookId"));
            if (book != null) {
    %>

    <div class="box">
        <div class="title">Edit book details</div>
        <p></p>
        <form style="border: 0px solid; padding: 0; margin: 0;" id="bookUpdateForm" action="?action=updateBookData"
              method=<%=httpMethod%> enctype="multipart/form-data">
            <input type="hidden" name="action" value="updateBookData"/>
            <table>
                <tr>
                    <td></td>
                    <td><i>Cover:</i></td>
                    <td><input id="cover" type="file" name="cover" size="15" accept="image"></td>
                </tr>
                <tr>
                    <td></td>
                    <td><b>ISBN:</b></td>
                    <td><input id="bookId" type="text" name="bookId" value="<%=book.getId()%>">
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td><b>Author:</b></td>
                    <td><input id="author" type="text" name="author" size="15" value="<%=book.getAuthor()%>"></td>
                </tr>
                <tr>
                    <td></td>
                    <td><b>Title:</b></td>
                    <td><input id="title" type="text" name="title" size="15" value="<%=book.getTitle()%>"><br/></td>
                </tr>


                <tr>
                    <td></td>
                    <td><b>Year:</b></td>
                    <td><input id="year" type="text" name="year" size="15" value="<%=book.getYear()%>"></td>
                </tr>
                <tr>
                    <td></td>
                    <td><b>About:</b></td>
                    <td><textarea id="about" type="text/plain" name="about" value="<%=book.getAbout()%>"></textarea><br/></td>
                </tr>
                <tr>
                    <td></td>
                    <td><b>Category:</b></td>
                    <td>
                        <!--<form id="bookCategory">-->
                            <%
                                HashMap<String, String> categories = dataManager.getCategories();
                                Set<String> categoryIds = categories.keySet();
                                for (Object categoryId : categoryIds) {
                                    out.println("<input id='category' type='radio'" +
                                            " name='categoryId' value='" +
                                            categoryId.toString() + "' size='15'" +
                                            ((book.getCategory_id() == Integer.parseInt((String) categoryId)) ? "checked" : "") +
                                            ">" + categories.get(categoryId) + "<br>");

//                                            out.println("<p><a href=" + base + "?action=selectCatalog&id="
//                                            + categoryId.toString() + ">" + categories.get(categoryId) + "</a></p>"
//                                    );
                                }
                            %>
                        <!--</form>-->
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td><b>Active:</b></td>
                    <td>
                        <!--<form id="active">-->
                            <input id="activeTrue" type="radio" name="active" value="true"
                                   size="15" <%=book.isActive() ? "checked" : ""%>>active<br>
                            <input id="activeFalse" type="radio" name="active" value="false"
                                   size="15" <%=book.isActive() ? "" : "checked"%>>nonactive
                        <!--</form>-->
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td></td>
                    <td><input id="submit" type="submit" value="Update book data"></td>
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
