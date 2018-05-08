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

    <table class="bookTable">
        <%if (genreId > 0) {
            for (Book book : bookList.getBookListByGenre(genreId)) {
            session.setAttribute("cover"+book.getId(), book);%>
        <tr>
            <td>
                <p><%=book.getName()%></p>
                <img src="<%=request.getContextPath()%>/image?cover<%=book.getId()%>" width="190" alt="Cover"/>
                <br>isbn: <%=book.getIsbn()%>
                <br>page count: <%=book.getPageCount()%>
                <br>author: <%=book.getAuthor()%>
                <br>published: <%=book.getPublishDate()%>
                <br>publisher: <%=book.getPublisher()%>
                <p><a href="#">Read.</a> </p>
            </td>
        </tr>
        <%}}%>
    </table>
</div>

<%@include file="WEB-INF/jspf/left_menu.jspf" %>