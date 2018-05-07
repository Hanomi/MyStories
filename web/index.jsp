<%--
  Created by IntelliJ IDEA.
  User: Naera
  Date: 04.05.2018
  Time: 18:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Welcome to MyStory</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="css/style.css"/>
</head>
<body>
<div class="box">
    <h3><img src="images/12956-200.png" width="100" height="100"/><br>
        Welcome to MyStory</h3>
    <p>Project under construction... </p>
    <form name="login" action="main.jsp" method="post">
        <p><b>Enter you name:</b><br>
            <input type="text" name="username" placeholder="name" maxlength="20" size="20" required/></p>
        <p><b>Password[placeholder]</b><br>
            <input type="password" name="password" placeholder="password" maxlength="20" size="20" required/></p>
        <p><input class="blue" type="submit" value="Enter"/></p>
    </form>
    <p class="gray">@code by freefpdie 2018</p>
</div>
</body>
</html>
