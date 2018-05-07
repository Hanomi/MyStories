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
    private List<Author> authorList;

    public AuthorList() {
        authorList = new ArrayList<>();
    }

    private void getAuthors() {
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * from mystory.author order by fio");
            while (resultSet.next()) {
                Author author = new Author();
                author.setName(resultSet.getString("fio"));
                authorList.add(author);
            }
        } catch (SQLException e) {
            Logger.getLogger(AuthorList.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public List<Author> getAuthorList() {
        if (authorList.isEmpty()) {
            getAuthors();
        }
        return authorList;
    }
}
