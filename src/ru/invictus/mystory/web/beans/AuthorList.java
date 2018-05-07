package ru.invictus.mystory.web.beans;

import ru.invictus.mystory.web.db.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthorList {
    private static List<Author> authorList = new ArrayList<>();

    private static void getAuthors() {
        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * from mystory.author order by fio")) {
            while (resultSet.next()) {
                Author author = new Author();
                author.setId(resultSet.getInt("id"));
                author.setFio(resultSet.getString("fio"));
                author.setBirthday(resultSet.getDate("birthday"));
                authorList.add(author);
            }
        } catch (SQLException e) {
            Logger.getLogger(AuthorList.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static List<Author> getAuthorList() {
        if (authorList.isEmpty()) {
            getAuthors();
        }
        return authorList;
    }
}
