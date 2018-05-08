<%@ page import="ru.invictus.mystory.web.beans.Book" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.invictus.mystory.web.soft.SearchType" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>

<div class="general">
    <jsp:useBean id="bookList" class="ru.invictus.mystory.web.beans.BookList"/>
    <%  List<Book> list = null;
        if (request.getParameter("genre_id") != null) {
            int genreId = Integer.parseInt(request.getParameter("genre_id"));
            list = bookList.getBookListByGenre(genreId);
        } else if (request.getParameter("letter") != null) {
            String letter = request.getParameter("letter");
            list = bookList.getBookListByLetter(letter);
        } else if (request.getParameter("search_string") != null) {
            String searchString = request.getParameter("search_string").trim();
            SearchType searchType = SearchType.TITLE;
            if (request.getParameter("search_value").equals("Автор")) {
                searchType = SearchType.AUTHOR;
            }
            if (!searchString.isEmpty()) {
                list = bookList.getBookListBySearch(searchType, searchString);
            }
        }
    %>

    <div>
        <%@include file="WEB-INF/jspf/letters.jspf" %>
        <h5>Books fonded: <%=list == null ? 0 : list.size()%></h5>
        <%if (list != null && list.size() > 0) {
            int a = 0;
            for (Book book : list) {
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