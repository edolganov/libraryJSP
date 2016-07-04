<%@page import="com.examples.libraryJSP.beans.User"%>
<%@ page import="com.examples.libraryJSP.model.SessionDataManager" %>
<%@page language="java" contentType="text/html;charset=UTF-8" %>
<%
    String base = (String) application.getAttribute("base");
    String imageURL = (String) application.getAttribute("imageURL");
    String httpMethod = (String)application.getAttribute("httpMethod");
    SessionDataManager sdm = (SessionDataManager)session.getAttribute("sessionDataManager");
    User currentUser = sdm==null?null:sdm.getCurrentSessionUser();
%>
<div class="header">
    <div class="logo">
        <p>The Open Library<i><%=(currentUser==null?"":"   ("+currentUser.getName()+")")
                + ((sdm!=null && sdm.getServeUser()!=null)?
                (":  serve user  " + sdm.getServeUser().getName()):"") %></i></p>
    </div>
    <div class="borrowedBooks">

        <table>
            <tr>
                <td>
                    <table>
                        <tr>
                            <td>
                                <a class="link2" href="<%=base%>?action=userDetails&userId=<%=(currentUser==null)?null:currentUser.getUserId()%>">
                                    Profile</a>
                            </td>
                        </tr>
                        <!--<tr>
                            <td>
                                <a class="link3" href="<%=base%>?action=myBooks">My books</a>
                            </td>
                        </tr> -->
                        <tr>
                            <td>
                                <a class="link3" href="<%=base%>?action=sign">
<%  if (currentUser == null) {
        out.println("Sign in");
    } else {
        out.println("Sign out");
    }
%>
                                </a>
                            </td>
                        </tr>
                    </table>
                </td>
                <td>
                    <a class="link2" href="<%=base%>?action=userDetails&userId=<%=(currentUser==null)?null:currentUser.getUserId()%>">
                        <img src="<%=imageURL%>profile_couple_image.gif" border="0"/></a>
                </td>
            </tr>
        </table>
    </div>
</div>