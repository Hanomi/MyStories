package ru.invictus.mystories.controller;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import ru.invictus.mystories.db.DataHelper;
import ru.invictus.mystories.entity.Book;
import ru.invictus.mystories.utils.SearchType;

import javax.enterprise.context.SessionScoped;
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
    private boolean editMode; // режим редактирования книг
    private SearchType lastType;
    private String lastQuery;
    private String selectedLetter;
    private String selectedGenre;
    private String searchString;
    private SearchType searchType;

    public SearchController() {
        bookList = new ArrayList<>();
        editMode = false;
        dataHelper = DataHelper.INSTANCE;
        lastType = SearchType.UPDATE;
        lastQuery = "";
        bookListDataModel = new LazyDataModel<Book>() {

            @Override
            public List<Book> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
                this.setRowCount(dataHelper.getRowCount(lastType, lastQuery));
                bookList = dataHelper.updateBooks(lastType, lastQuery, first, pageSize);
                return bookList;
            }

            private void setRowCount(long rowCount) {
                this.setRowCount((int) rowCount);
            }
        };
    }

    // книги по жанру
    public void fillBooksByGenre() {
        editMode = false;
        selectedLetter = "";
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedGenre = params.get("genre_id");
        lastType = SearchType.GENRE;
        lastQuery = selectedGenre;
    }

    // книги по алфавитному указателю
    public void fillBooksByLetter() {
        editMode = false;
        selectedGenre = "";
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedLetter = params.get("letter");
        lastType = SearchType.LETTER;
        lastQuery = selectedLetter;
    }

    // книги по поиску
    public void fillBooksBySearch(){
        editMode = false;
        selectedGenre = "";
        selectedLetter = "";
        lastType = searchType;
        lastQuery = searchString;
    }

    //  режим редактирования
    public void enableEditMode() {
        editMode = true;
    }

    // отмена редактирования
    public void cancelEditMode() {
        editMode = false;
        bookList.forEach(f -> f.setEdit(false));
    }

    // обновление книг
    public void updateBooks() {
        DataHelper.INSTANCE.update(bookList);
        cancelEditMode();
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

    public boolean isEditMode() {
        return editMode;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public LazyDataModel getBookListDataModel() {
        return bookListDataModel;
    }
}
