<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>

<div class="general">

    <% request.setCharacterEncoding("UTF-8");
        int genreId = 0;

        try {
            genreId = Integer.parseInt(request.getParameter("genre_id"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    %>

    <ul>
        <jsp:useBean id="bookList" class="ru.invictus.mystory.web.beans.BookList"/>
        <c:forEach var="book" items="${bookList.getBookList()}">
            <li>${book.name}</li>
        </c:forEach>
    </ul>
</div>

<%@include file="WEB-INF/jspf/left_menu.jspf" %>