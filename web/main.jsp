<%@ page import="ru.invictus.mystory.web.beans.Book" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

    <jsp:useBean id="bookList" class="ru.invictus.mystory.web.beans.BookList"/>

    <div>
        <%if (genreId > 0) {
            int a = 0;
            for (Book book : bookList.getBookListByGenre(genreId)) {
            session.setAttribute("cover"+book.getId(), book);
        %>
            <% if(a % 3 == 0) {%><article class="books booksRow"><%} else {%><article class="books"><%}%>
                <%@include file="WEB-INF/jspf/book.jspf" %>
            </article>
        <%  a++;
            }}%>
    </div>
</div>

<%@include file="WEB-INF/jspf/left_menu.jspf" %>