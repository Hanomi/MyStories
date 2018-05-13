package ru.invictus.mystories.controller;

import ru.invictus.mystories.beans.Book;
import ru.invictus.mystories.db.Database;
import ru.invictus.mystories.servlets.FileType;
import ru.invictus.mystories.utils.SearchType;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import java.io.Serializable;
import java.sql.PreparedStatement;
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
    private List<Book> bookListPage;
    private String searchString;
    private static final Logger logger;
    private static Set<Character> letters;
    private int booksOnPage = 3;
    private Integer selectedPage;
    private String selectedLetter;
    private String selectedGenre;
    private boolean editMode;
    private String lastSql;
    private int bookListSize = 0;

    static {
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

    public int getBooksOnPage() {
        return booksOnPage;
    }

    public void setBooksOnPage(int booksOnPage) {
        this.booksOnPage = booksOnPage;
    }

    public int getBookListSize() {
        return bookListSize;
    }

    public SearchController() {
        bookListPage = new ArrayList<>();
        editMode = false;
    }

    public boolean isEditMode() {
        return editMode;
    }


    public List<Book> getBookListPage() {
        return bookListPage;
    }

    public Set<Character> getLetters() {
        return letters;
    }

    private void getBooks(boolean newSearch) {
        if (newSearch) {
            try (Statement statement = Database.getConnection().createStatement();
                 ResultSet resultSet = statement.executeQuery(lastSql)) {
                resultSet.last();
                bookListSize = resultSet.getRow();
            } catch (SQLException e) {
                logger.log(Level.SEVERE, null, e);
            }
        }

        editMode = false;
        bookListPage.clear();
        String sql = lastSql + " LIMIT " + (booksOnPage *selectedPage - booksOnPage) + "," + booksOnPage;
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
                bookListPage.add(book);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, null, e);
        }
    }

    public boolean showPager() {
        return bookListSize > booksOnPage;
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

    public void fillBooksByGenre() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedGenre = params.get("genre_id");
        selectedLetter = "";
        selectedPage = 1;
        lastSql = "SELECT b.id, b.name, b.isbn, b.page_count,b.publish_year, b.publish_year, b.description, " +
                "a.fio AS author, g.name AS genre, p.name AS publisher " +
                "FROM mystory.book b " +
                " INNER JOIN author a on b.author_id = a.id " +
                " INNER JOIN genre g on b.genre_id = g.id " +
                " INNER JOIN publisher p on b.publisher_id = p.id " +
                "WHERE b.genre_id = " + selectedGenre + " ORDER BY b.name";
        getBooks(true);
    }

    public void fillBooksByLetter() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedLetter = params.get("letter");
        selectedGenre = "";
        selectedPage = 1;
        lastSql = "SELECT b.id, b.name, b.isbn, b.page_count,b.publish_year, b.publish_year, b.description, " +
                "a.fio AS author, g.name AS genre, p.name AS publisher " +
                "FROM mystory.book b " +
                " INNER JOIN author a on b.author_id = a.id " +
                " INNER JOIN genre g on b.genre_id = g.id " +
                " INNER JOIN publisher p on b.publisher_id = p.id " +
                "WHERE b.name REGEXP '^" + selectedLetter + "' ORDER BY b.name";
        getBooks(true);
    }



    public void fillBooksBySearch() {
        if (searchString.isEmpty()) {
            return;
        }

        selectedLetter = "";
        selectedGenre = "";
        selectedPage = 1;

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
        builder.append("' ORDER BY b.name");
        lastSql = builder.toString();
        getBooks(true);
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

    public List<Integer> pageNumber() {
        int size = (int) Math.ceil((double) bookListSize / booksOnPage);
        List<Integer> list = new ArrayList<>();
        list.add(1);
        for (int i = 1; i < size; i++) {
            list.add(i + 1);
        }
        return list;
    }

    public void selectPage() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedPage = Integer.parseInt(params.get("page_number"));
        getBooks(false);
    }

    public void changeBooksOnPage(ValueChangeEvent event) {
        selectedPage = 1;
        booksOnPage = Integer.parseInt(event.getNewValue().toString());
        if (lastSql != null) getBooks(true);
    }

    public String updateBooks() {
        String sql = "UPDATE mystory.book SET name=?, isbn=?, page_count=?, description=? where id=?";
        try (PreparedStatement preparedStatement = Database.getConnection().prepareStatement(sql)) {
            for (Book book : bookListPage) {
                if (!book.getEdit()) continue;
                preparedStatement.setString(1, book.getName());
                preparedStatement.setString(2, book.getIsbn());
                preparedStatement.setInt(3, book.getPageCount());
                preparedStatement.setString(4, book.getDescription());
                preparedStatement.setInt(5, book.getId());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, e);
        }
        cancelEditMode();
        return "books";
    }

    public void enableEditMode() {
        editMode = true;
    }

    public void cancelEditMode() {
        editMode = false;
        bookListPage.forEach(book -> book.setEdit(false));
    }

    public Integer getSelectedPage() {
        return selectedPage;
    }

    public String getSelectedLetter() {
        return selectedLetter;
    }

    public String getSelectedGenre() {
        return selectedGenre;
    }
}
