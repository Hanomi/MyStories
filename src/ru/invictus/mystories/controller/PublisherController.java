package ru.invictus.mystories.controller;

import ru.invictus.mystories.anotations.Eager;
import ru.invictus.mystories.db.DataHelper;
import ru.invictus.mystories.entity.Genre;
import ru.invictus.mystories.entity.Publisher;
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

@Eager
@Named
@ApplicationScoped
public class PublisherController implements Serializable, Converter {
    private List<SelectItem> selectItems = new ArrayList<>();
    private Map<Long, Publisher> map;
    private List<Publisher> list;

    public PublisherController() {
        map = new HashMap<>();
        list = DataHelper.INSTANCE.getAllPublisher();
        list.sort(new NameComparator<>());

        for (Publisher publisher : list) {
            map.put(publisher.getId(), publisher);
            selectItems.add(new SelectItem(publisher, publisher.getName()));
        }
    }

    public List<Publisher> getGenreList() {
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
        return ((Publisher)value).getId() + "";
    }
}
