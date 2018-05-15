package ru.invictus.mystories.controller;

import ru.invictus.mystories.beans.Genre;
import ru.invictus.mystories.db.DataHelper;
import ru.invictus.mystories.entity.GenreEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@ApplicationScoped
public class GenreController implements Serializable {
    private List<SelectItem> selectItems = new ArrayList<>();
    private Map<Long, GenreEntity> genreMap;
    private List<GenreEntity> genreList;

    public GenreController() {

        genreMap = new HashMap<>();
        genreList = DataHelper.INSTANCE.getAllGenres();

        for (GenreEntity genre : genreList) {
            genreMap.put(genre.getId(), genre);
            selectItems.add(new SelectItem(genre, genre.getName()));
        }

    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    //
    public List<GenreEntity> getGenreList() {
        return genreList;
    }
}
