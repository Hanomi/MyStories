package ru.invictus.mystories.controller;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import ru.invictus.mystories.db.DataHelper;
import ru.invictus.mystories.entity.Book;
import ru.invictus.mystories.utils.SearchType;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

@Named("searchController")
@SessionScoped
public class SearchController implements Serializable {
    private static final Logger logger = Logger.getLogger(SearchController.class.getName());
    private LazyDataModel<Book> bookListDataModel;
    private DataHelper dataHelper;
    private List<Book> bookList; // список книг на странице
    private Book currentBook;
    private SearchType lastType;
    private String lastQuery;
    private String selectedLetter;
    private String selectedGenre;
    private String searchString;
    private SearchType searchType;

    public SearchController() {
        bookList = new ArrayList<>();
        dataHelper = DataHelper.INSTANCE;
        lastType = SearchType.UPDATE;
        lastQuery = "";
        bookListDataModel = new LazyDataModel<Book>() {

            @Override
            public List<Book> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
                bookList = dataHelper.updateBooks(lastType, lastQuery, first, pageSize);
                return bookList;
            }
        };
    }

    // книги по жанру
    public void fillBooksByGenre() {
        selectedLetter = "";
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedGenre = params.get("genre_id");
        lastType = SearchType.GENRE;
        lastQuery = selectedGenre;
        long rows = dataHelper.getRowCount(lastType, lastQuery);
        bookListDataModel.setRowCount((int) rows);
    }

    // книги по алфавитному указателю
    public void fillBooksByLetter() {
        selectedGenre = "";
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedLetter = params.get("letter");
        lastType = SearchType.LETTER;
        lastQuery = selectedLetter;
        long rows = dataHelper.getRowCount(lastType, lastQuery);
        bookListDataModel.setRowCount((int) rows);
    }

    // книги по поиску
    public void fillBooksBySearch(){
        selectedGenre = "";
        selectedLetter = "";
        lastType = searchType;
        lastQuery = searchString;
        long rows = dataHelper.getRowCount(lastType, lastQuery);
        bookListDataModel.setRowCount((int) rows);
    }

    // обновление книг
    public void updateBook() {
        DataHelper.INSTANCE.update(currentBook);
        long rows = dataHelper.getRowCount(lastType, lastQuery);
        bookListDataModel.setRowCount((int) rows);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("locale.localisation", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(resourceBundle.getString("book_updated")));
    }

    public void deleteBook() {
        System.out.println("DELETE: " + currentBook.getName());
        long rows = dataHelper.getRowCount(lastType, lastQuery);
        bookListDataModel.setRowCount((int) rows);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("locale.localisation", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(resourceBundle.getString("book_deleted")));
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    public String getSelectedLetter() {
        return selectedLetter;
    }

    public String getSelectedGenre() {
        return selectedGenre;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public LazyDataModel getBookListDataModel() {
        return bookListDataModel;
    }

    public void setCurrentBook(Book currentBook) {
        this.currentBook = currentBook;
    }

    public Book getCurrentBook() {
        return currentBook;
    }
}
