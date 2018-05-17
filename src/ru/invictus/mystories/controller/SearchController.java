package ru.invictus.mystories.controller;

import ru.invictus.mystories.db.DataHelper;
import ru.invictus.mystories.entity.Book;
import ru.invictus.mystories.utils.SearchType;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

@Named("searchController")
@SessionScoped
public class SearchController implements Serializable {
    private static final Logger logger = Logger.getLogger(SearchController.class.getName());
    @Inject
    private PageController pageController;
    private DataHelper dataHelper;
    private List<Book> bookList; // список книг на странице
    private boolean editMode; // режим редактирования книг

    private String selectedLetter;
    private String selectedGenre;
    private String searchString;
    private SearchType searchType;

    public SearchController() {
        bookList = new ArrayList<>();
        editMode = false;
        dataHelper = DataHelper.INSTANCE;
    }

    public void setPageController(PageController pageController) {
        this.pageController = pageController;
    }

    // книги по жанру
    public void fillBooksByGenre() {
        pageController.changePage(1);
        editMode = false;
        selectedLetter = "";
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedGenre = params.get("genre_id");
        bookList = dataHelper.updateBooks(SearchType.GENRE, pageController, selectedGenre);
        pageController.updatePager();
    }

    // книги по алфавитному указателю
    public void fillBooksByLetter() {
        pageController.changePage(1);
        editMode = false;
        selectedGenre = "";
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedLetter = params.get("letter");
        bookList = dataHelper.updateBooks(SearchType.LETTER, pageController, selectedLetter);
        pageController.updatePager();
    }

    // книги по поиску
    public void fillBooksBySearch(){
        pageController.changePage(1);
        editMode = false;
        selectedGenre = "";
        selectedLetter = "";
        bookList = dataHelper.updateBooks(searchType, pageController, searchString);
        pageController.updatePager();
    }

    // смена страницы в списке найденых книг
    public void changePage() {
        editMode = false;
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        pageController.changePage(Integer.parseInt(params.get("page_number")));
        bookList = dataHelper.updateBooks(SearchType.UPDATE, pageController, null);
    }

    // смена кол-ва книг на странице
    public void changeBooksOnPage(ValueChangeEvent event) {
        pageController.changePage(1);
        editMode = false;
        pageController.setBooksOnPage(Integer.parseInt(event.getNewValue().toString()));
        pageController.updatePager();
        bookList = dataHelper.updateBooks(SearchType.UPDATE, pageController, null);
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
        for (Book book : bookList) {
            if (book.isEdit()) {
                // todo update books
            }
        }
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
}
