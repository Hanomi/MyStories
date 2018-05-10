package ru.invictus.mystories.controller;

import ru.invictus.mystories.utils.SearchType;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Named
@SessionScoped
public class SearchController implements Serializable {
    private SearchType searchType;
    private static Map<String, SearchType> searchMap = new HashMap<>();

    static {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("locale.localisation", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        searchMap.put(resourceBundle.getString("author_name"), SearchType.AUTHOR);
        searchMap.put(resourceBundle.getString("book_name"), SearchType.TITLE);
    }

    public SearchController() {
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
}
