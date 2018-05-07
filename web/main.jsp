<%@ page import="test.JdbcTest" %><%--
  Created by IntelliJ IDEA.
  User: Naera
  Date: 04.05.2018
  Time: 18:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <% request.setCharacterEncoding("UTF-8"); %>
    <h3> ${param["username"]}</h3>
    <h3> ${param["password"]}</h3>

    <%
        JdbcTest jdbcTest = new JdbcTest();
        jdbcTest.check();
    %>
</body>
</html>
