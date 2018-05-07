<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<div class="general">
    <h3>Welcome to MyStory</h3>
    <p>Project under construction... </p>
    <form name="login" action="main.jsp" method="post">
        <p><b>Enter you name:</b><br>
            <input type="text" name="username" placeholder="name" maxlength="20" size="20" required/></p>
        <p><b>Password[placeholder]</b><br>
            <input type="password" name="password" placeholder="password" maxlength="20" size="20" required/></p>
        <p><input class="blue" type="submit" value="Enter"/></p>
    </form>
</div>

<%@include file="WEB-INF/jspf/left_menu.jspf"%>