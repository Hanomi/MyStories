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
    private String searchString;
    private static final Logger logger;
    private static Set<Character> letters;

    static {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("locale.localisation", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        searchMap.put(resourceBundle.getString("author_name"), SearchType.AUTHOR);
        searchMap.put(resourceBundle.getString("book_name"), SearchType.TITLE);
        logger = Logger.getLogger(SearchController.class.getName());
        letters = new TreeSet<>();

        if (letters.isEmpty()) {
            try (Statement statement = Database.getConnection().createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT book.name from mystory.book order by name")) {
                while (resultSet.next()) {
                    char a = resultSet.getString("name").toUpperCase().charAt(0);
                    letters.add(a);
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, null, e);
            }
        }
    }

    public SearchController() {
    }

    public Set<Character> getLetters() {
        return letters;
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



    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String  getSearchString() {
        return searchString;
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
        getBooks("SELECT b.id, b.name, b.isbn, b.page_count,b.publish_year, b.publish_year, b.description, " +
                "a.fio AS author, g.name AS genre, p.name AS publisher " +
                "FROM mystory.book b " +
                " INNER JOIN author a on b.author_id = a.id " +
                " INNER JOIN genre g on b.genre_id = g.id " +
                " INNER JOIN publisher p on b.publisher_id = p.id " +
                "WHERE b.genre_id = " + params.get("genre_id") + " ORDER BY b.name " +
                "LIMIT 0,6");
    }

    public void fillBooksByLetter() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        getBooks("SELECT b.id, b.name, b.isbn, b.page_count,b.publish_year, b.publish_year, b.description, " +
                "a.fio AS author, g.name AS genre, p.name AS publisher " +
                "FROM mystory.book b " +
                " INNER JOIN author a on b.author_id = a.id " +
                " INNER JOIN genre g on b.genre_id = g.id " +
                " INNER JOIN publisher p on b.publisher_id = p.id " +
                "WHERE b.name REGEXP '^" + params.get("letter") + "' ORDER BY b.name " +
                "LIMIT 0,6");
    }



    public void fillBooksBySearch() {
        if (searchString.isEmpty()) {
            return;
        }
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        StringBuilder builder = new StringBuilder("SELECT b.id, b.name, b.isbn, b.page_count,b.publish_year, b.publish_year, b.description, " +
                "a.fio AS author, g.name AS genre, p.name AS publisher " +
                "FROM mystory.book b " +
                "INNER JOIN author a on b.author_id = a.id " +
                "INNER JOIN genre g on b.genre_id = g.id " +
                "INNER JOIN publisher p on b.publisher_id = p.id " +
                "WHERE");
        if (searchType == SearchType.AUTHOR) {
            builder.append(" a.fio ");
        } else {
            builder.append(" b.name ");
        }
        builder.append("REGEXP '");
        builder.append(searchString);
        builder.append("' ORDER BY b.name LIMIT 0, 6");
        getBooks(builder.toString());
    }


    public byte[] getData(String id, FileType fileType) {
        byte[] file = null;
        try (Statement statement = Database.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT book." + fileType.getType() + " FROM mystory.book WHERE id=" + id)) {
            while (resultSet.next()) {
                file = resultSet.getBytes(fileType.getType());
            }
        } catch (SQLException e) {
            Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, e);
        }
        return file;
    }
}
