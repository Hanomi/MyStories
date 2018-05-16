package ru.invictus.mystories.controller;

import ru.invictus.mystories.db.DataHelper;
import ru.invictus.mystories.entity.Book;
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
    }

    public int getRow(Book book) {
        return bookListPage.indexOf(book);
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
        bookListPage = DataHelper.INSTANCE.getAllBooks();
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
        getBooks(true);
    }

    public void fillBooksByLetter() {
        getBooks(true);
    }



    public void fillBooksBySearch() {
        getBooks(true);
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
