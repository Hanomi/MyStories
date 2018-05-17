package ru.invictus.mystories.controller;

import ru.invictus.mystories.anotations.Eager;
import ru.invictus.mystories.db.DataHelper;
import ru.invictus.mystories.entity.Author;
import ru.invictus.mystories.utils.NameComparator;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

@Eager
@Named
@ApplicationScoped
public class AuthorController implements Serializable, Converter {
    private List<SelectItem> selectItems = new ArrayList<>();
    private Map<Long, Author> map;
    private List<Author> list;


    public AuthorController() {
        map = new HashMap<>();
        list = DataHelper.INSTANCE.getAllAuthors();
        list.sort(new NameComparator<>());

        for (Author author : list) {
            map.put(author.getId(), author);
            selectItems.add(new SelectItem(author, author.getFio()));
        }
    }

    public List<Author> getAuthorList(){
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
        return ((Author)value).getId() + "";
    }
}
