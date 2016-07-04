<%@ page import="com.examples.libraryJSP.model.SessionDataManager" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Welcome</title>
    <link rel="stylesheet" href="/libraryJSP/css/library.css" type="text/css"/> <!--  -->
</head>
<body>
<jsp:include page="m_TopMenu.jsp" flush="true"/>
<jsp:include page="m_LeftMenu.jsp" flush="true"/>
<div class="content">
    <!--<h1>Missing necessary permissions for operation</h1> -->
    <p class="error"><%=((SessionDataManager)session.getAttribute("sessionDataManager")).getOperationErrorMessage()%></p>
</div>
</body>
</html>
