package ru.invictus.mystories.controller;

import ru.invictus.mystories.anotations.Eager;
import ru.invictus.mystories.db.DataHelper;
import ru.invictus.mystories.entity.Genre;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Eager
@Named
@ApplicationScoped
public class GenreController implements Serializable {
    private List<Genre> genreList;

    public GenreController() {
        genreList = DataHelper.INSTANCE.getAllGenres();
    }

    public List<Genre> getGenreList() {
        return genreList;
    }
}
