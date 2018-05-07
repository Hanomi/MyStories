<%@ page import="test.JdbcTest" %>
<%@ page import="ru.invictus.mystory.web.beans.AuthorList" %>
<%@ page import="ru.invictus.mystory.web.beans.Author" %><%--
  Created by IntelliJ IDEA.
  User: Naera
  Date: 04.05.2018
  Time: 18:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>MyStory</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="css/style.css"/>
    <link rel="stylesheet" href="css/main_style.css"/>
</head>
<body>
<div class="box">
    <header>
        <img alt="place for logo" name="logo" width="1024" height="100"/>
        <form name="search_form" method="post">
            <img src="images/search.png" width="16" height="16"/>
            <input type="text" name="search_string" value="" size="100"/>
            <input type="submit" name="search_button" value="Search"/>
            <select name="search_value">
                <option>Название</option>
                <option>Автор</option>
            </select>
        </form>
    </header>

    <div class="left_sidebar">
        <h3>Список авторов</h3>
        <ul>
            <%
                for(Author author : AuthorList.getAuthorList()) {%>
            <li><a href="#"><%=author.getName()%></a></li>
            <%};%>
        </ul>
    </div>

    <div class="content">

    </div>

    <footer>
        <p class="gray">@code by freefpdie 2018</p>
    </footer>
</div>
</body>
</html>



<%--    <% request.setCharacterEncoding("UTF-8"); %>
        <h3> ${param["username"]}</h3>
        <h3> ${param["password"]}</h3>

        <%
            JdbcTest jdbcTest = new JdbcTest();
            jdbcTest.check();
        %>--%>