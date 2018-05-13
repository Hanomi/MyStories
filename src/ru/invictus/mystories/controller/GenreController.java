package ru.invictus.mystories.controller;

import ru.invictus.mystories.beans.Genre;
import ru.invictus.mystories.db.Database;
import ru.invictus.mystories.anotations.Eager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@Eager
@ApplicationScoped
public class GenreController implements Serializable {
    private static final List<Genre> genreList;

    static {
        genreList = new ArrayList<>();
        getGenres();
    }

    public GenreController() {

    }

    private static void getGenres() {
        try (Statement statement = Database.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * from mystory.genre order by name")) {
            while (resultSet.next()) {
                Genre genre = new Genre();
                genre.setId(resultSet.getInt("id"));
                genre.setName(resultSet.getString("name"));
                genreList.add(genre);
            }
        } catch (SQLException e) {
            Logger.getLogger(GenreController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public List<Genre> getGenreList() {
        return genreList;
    }
}
