<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
            <jsp:useBean id="authorList" scope="application" class="ru.invictus.mystory.web.beans.AuthorList"/>
            <c:forEach var="author" items="${authorList.getAuthorList()}">
                <li><a href="#">${author.fio}</a></li>
            </c:forEach>
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