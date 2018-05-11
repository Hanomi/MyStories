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
    private static Map<String, SearchType> searchMap;
    private List<Book> bookList;
    private List<Book> bookListPage;
    private String searchString;
    private static final Logger logger;
    private static Set<Character> letters;
    private static final int BOOKS_ON_PAGE = 3;

    static {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("locale.localisation", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        searchMap = new HashMap<>();
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

    private String selectedPage;
    private String selectedLetter;
    private String selectedGenre;

    public SearchController() {
        bookList = new ArrayList<>();
        bookListPage = new ArrayList<>(BOOKS_ON_PAGE);
    }

    public List<Book> getBookListPage() {
        return bookListPage;
    }

    public Set<Character> getLetters() {
        return letters;
    }

    private void getBooks(String sql) {
        bookList.clear();
        bookListPage.clear();
        selectedPage = "1";
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
        if (bookList.size() > BOOKS_ON_PAGE) {
            for (int i = 0; i < BOOKS_ON_PAGE; i++) {
                bookListPage.add(bookList.get(i));
            }
        } else {
            bookListPage.addAll(bookList);
        }
    }

    public boolean showPager() {
        return bookList.size() > BOOKS_ON_PAGE;
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

    public int booksFounded() {
        return bookList.size();
    }

    public void fillBooksByGenre() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedGenre = params.get("genre_id");
        selectedLetter = "";
        getBooks("SELECT b.id, b.name, b.isbn, b.page_count,b.publish_year, b.publish_year, b.description, " +
                "a.fio AS author, g.name AS genre, p.name AS publisher " +
                "FROM mystory.book b " +
                " INNER JOIN author a on b.author_id = a.id " +
                " INNER JOIN genre g on b.genre_id = g.id " +
                " INNER JOIN publisher p on b.publisher_id = p.id " +
                "WHERE b.genre_id = " + selectedGenre + " ORDER BY b.name");
    }

    public void fillBooksByLetter() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedLetter = params.get("letter");
        selectedGenre = "";
        getBooks("SELECT b.id, b.name, b.isbn, b.page_count,b.publish_year, b.publish_year, b.description, " +
                "a.fio AS author, g.name AS genre, p.name AS publisher " +
                "FROM mystory.book b " +
                " INNER JOIN author a on b.author_id = a.id " +
                " INNER JOIN genre g on b.genre_id = g.id " +
                " INNER JOIN publisher p on b.publisher_id = p.id " +
                "WHERE b.name REGEXP '^" + selectedLetter + "' ORDER BY b.name");
    }



    public void fillBooksBySearch() {
        selectedLetter = "";
        selectedGenre = "";

        if (searchString.isEmpty()) {
            fillAllBooks();
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
        builder.append("' ORDER BY b.name");
        getBooks(builder.toString());
    }

    private void fillAllBooks() {
        getBooks("SELECT b.id, b.name, b.isbn, b.page_count,b.publish_year, b.publish_year, b.description, " +
                "a.fio AS author, g.name AS genre, p.name AS publisher " +
                "FROM mystory.book b " +
                " INNER JOIN author a on b.author_id = a.id " +
                " INNER JOIN genre g on b.genre_id = g.id " +
                " INNER JOIN publisher p on b.publisher_id = p.id " +
                "ORDER BY b.name");
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
        int size = bookList.size() / BOOKS_ON_PAGE + 1;
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i + 1);
        }
        return list;
    }

    public void selectPage() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedPage = params.get("page_number");
        int page_number = Integer.parseInt(selectedPage);
        bookListPage.clear();
        try {
            for (int i = 0; i < BOOKS_ON_PAGE; i++) {
                bookListPage.add(bookList.get(i+BOOKS_ON_PAGE*(page_number-1)));
            }
        } catch (IndexOutOfBoundsException e) {
            // ignore limit
        }
    }

    public String getSelectedPage() {
        return selectedPage;
    }

    public String getSelectedLetter() {
        return selectedLetter;
    }

    public String getSelectedGenre() {
        return selectedGenre;
    }
}
