package ru.invictus.mystory.web.beans;

import ru.invictus.mystory.web.db.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LetterList {
    private char[] letters;


    public char[] getLetters() {
        if (letters == null) {
            ArrayList<Character> list = new ArrayList<>();
            try (Statement statement = Database.getConnection().createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT book.name from mystory.book order by name")) {
                while (resultSet.next()) {
                    char a = resultSet.getString("name").toLowerCase().charAt(0);
                    if (list.isEmpty() || a != list.get(list.size()-1)) {
                        list.add(a);
                    }
                }
            } catch (SQLException e) {
                Logger.getLogger(LetterList.class.getName()).log(Level.SEVERE, null, e);
            }
            letters = new char[list.size()];
            for (int i = 0; i < list.size(); i++) {
                letters[i] = list.get(i);
            }
            return letters;
        } else return letters;
    }
}
