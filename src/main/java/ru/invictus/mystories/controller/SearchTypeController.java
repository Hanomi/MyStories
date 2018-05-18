package ru.invictus.mystories.controller;

import ru.invictus.mystories.utils.SearchType;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Named
@RequestScoped
public class SearchTypeController {
    private Map<String, SearchType> searchMap;

    public SearchTypeController() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("locale.localisation", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        searchMap = new HashMap<>();
        searchMap.put(resourceBundle.getString("author_name"), SearchType.AUTHOR);
        searchMap.put(resourceBundle.getString("book_name"), SearchType.TITLE);
    }

    public Map<String, SearchType> getSearchMap() {
        return searchMap;
    }
}
