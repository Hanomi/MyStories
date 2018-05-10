package ru.invictus.mystories.controller;

import ru.invictus.mystories.beans.Book;
import ru.invictus.mystories.db.Database;
import ru.invictus.mystories.servlets.FileType;
import ru.invictus.mystories.utils.SearchType;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("searchController")
@SessionScoped
public class SearchController implements Serializable {
    private SearchType searchType;
    private static Map<String, SearchType> searchMap = new HashMap<>();
    private List<Book> bookList;
    private static final Logger logger;

    static {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("locale.localisation", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        searchMap.put(resourceBundle.getString("author_name"), SearchType.AUTHOR);
        searchMap.put(resourceBundle.getString("book_name"), SearchType.TITLE);
        logger = Logger.getLogger(SearchController.class.getName());
    }

    public SearchController() {
    }

    private void getBooks(String sql) {
        bookList = new ArrayList<>();
        try (Statement statement = Database.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setName(resultSet.getString("name"));
                book.setAuthor(resultSet.getString("author"));
                book.setGenre(resultSet.getString("genre"));
                book.setIsbn(resultSet.getString("isbn"));
                book.setPageCount(resultSet.getInt("page_count"));
                book.setPublishDate(resultSet.getDate("publish_year"));
                book.setPublisher(resultSet.getString("publisher"));
                book.setDescription(resultSet.getString("description"));
                bookList.add(book);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, null, e);
        }
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public Map<String, SearchType> getSearchMap() {
        return searchMap;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void fillBooksByGenre() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        Long id = Long.parseLong(params.get("genre_id"));
        getBooks("SELECT b.id, b.name, b.isbn, b.page_count,b.publish_year, b.publish_year, b.description, " +
                "a.fio AS author, g.name AS genre, p.name AS publisher " +
                "FROM mystory.book b " +
                " INNER JOIN author a on b.author_id = a.id " +
                " INNER JOIN genre g on b.genre_id = g.id " +
                " INNER JOIN publisher p on b.publisher_id = p.id " +
                "WHERE b.genre_id = " + id + " ORDER BY b.name " +
                "LIMIT 0,6");
    }


    public byte[] getData(String id, FileType fileType) {
        byte[] fyle = null;
        try (Statement statement = Database.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT book." + fileType.getType() + " FROM mystory.book WHERE id=" + id)) {
            while (resultSet.next()) {
                fyle = resultSet.getBytes(fileType.getType());
            }
        } catch (SQLException e) {
            Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, e);
        }
        return fyle;
    }
}
