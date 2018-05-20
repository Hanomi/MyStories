package ru.invictus.mystories.controller;

import ru.invictus.mystories.db.DataHelper;
import ru.invictus.mystories.entity.Genre;
import ru.invictus.mystories.utils.NameComparator;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@ApplicationScoped
public class GenreController implements Serializable, Converter {
    private List<SelectItem> selectItems = new ArrayList<>();
    private Map<Long, Genre> map;
    private List<Genre> list;

    public GenreController() {
        map = new HashMap<>();
        list = DataHelper.INSTANCE.getAllGenres();
        list.sort(new NameComparator<>());

        for (Genre genre : list) {
            map.put(genre.getId(), genre);
            selectItems.add(new SelectItem(genre, genre.getName()));
        }
    }

    public List<Genre> getGenreList() {
        return list;
    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return map.get(Long.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((Genre)value).getId() + "";
    }
}
