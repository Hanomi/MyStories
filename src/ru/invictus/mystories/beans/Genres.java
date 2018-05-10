package ru.invictus.mystories.beans;

import ru.invictus.mystories.db.Database;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ApplicationScoped
public class Genres {
    private List<Genre> genreList = new ArrayList<>();

    private void getGenres() {
        try (Statement statement = Database.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * from mystory.genre order by name")) {
            while (resultSet.next()) {
                Genre genre = new Genre();
                genre.setId(resultSet.getInt("id"));
                genre.setName(resultSet.getString("name"));
                genreList.add(genre);
            }
        } catch (SQLException e) {
            Logger.getLogger(Genres.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public List<Genre> getGenreList() {
        if (genreList.isEmpty()) {
            getGenres();
        }
        return genreList;
    }
}
