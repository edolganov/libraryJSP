<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@page import="java.util.HashMap"%>
<%@ page import="java.util.Set" %>
<%@ page import="com.examples.libraryJSP.beans.User" %>
<%@ page import="com.examples.libraryJSP.beans.permissions.Activities" %>
<%@ page import="com.examples.libraryJSP.model.SessionDataManager" %>
<%
    String base = (String) application.getAttribute("base");
    String imageURL = (String) application.getAttribute("imageURL");
    String httpMethod = (String)application.getAttribute("httpMethod");
    SessionDataManager sdm = (SessionDataManager)session.getAttribute("sessionDataManager");
    User currentUser = sdm==null?null:sdm.getCurrentSessionUser();
%>
<jsp:useBean id="dataManager" scope="application"
  class="com.examples.libraryJSP.model.DataManager"/>
<div class="menu">

    <% if (currentUser != null
            && (currentUser.activityAllowed(Activities.USERS_MANAGE)
            || currentUser.activityAllowed(Activities.USERS_SERVE))) {
    %>
    <div class="box">
        <div class="title">Search users</div>
        <p>User ID/Login/Name:</p>
        <form style="border: 0px solid; padding: 0; margin: 0;">
            <input type="hidden" name="action" value="searchUsers"/>
            <input id="text" type="text" name="keyword" size="15"/>
            <input id="submit" type="submit" value="Search"/>
        </form>
    </div>
    <%
        }
    %>

    <% if (currentUser.activityAllowed(Activities.USERS_MANAGE)) {
    %>
    <div class="box">
        <div class="title">Manage users</div>
        <p><a href="<%=base%>?action=newUser">Add user</a></p>
    </div>
    <%
        }
    %>

    <div class="box">
    <div class="title">Quick Search</div>
    <p>Book Title/Author:</p>
    <form style="border: 0px solid; padding: 0; margin: 0;">
      <input type="hidden" name="action" value="searchBooks"/>
      <input id="text" type="text" name="keyword" size="15"/>
      <input id="submit" type="submit" value="Search"/>
      </form>
    </div>

  <div class="box">
    <div class="title">Categories</div>
<%
    HashMap<String, String> categories = dataManager.getCategories();
    Set<String> categoryIds = categories.keySet();
    for (Object categoryId:categoryIds){
      out.println("<p><a href=" + base + "?action=selectCatalog&id="
        + categoryId.toString() + ">" + categories.get(categoryId) + "</a></p>"
        );
      }
  %>
    </div>

    <% if (currentUser.activityAllowed(Activities.REPORTS)) {
    %>
    <div class="box">
        <div class="title">Reports</div>
        <p><a href="<%=base%>?action=reportExpiredBooks&expired=true">Expired books</a></p>
        <p><a href="<%=base%>?action=reportBorrowedBooks&expired=false">Borrowed books</a></p>
    </div>
    <%
        }
    %>

    <% if (currentUser.activityAllowed(Activities.BOOKS_MANAGE)) {
    %>
    <div class="box">
        <div class="title">Manage books</div>
        <p><a href="<%=base%>?action=newBook">Add Book</a></p>
    </div>
    <%
        }
    %>

</div>
