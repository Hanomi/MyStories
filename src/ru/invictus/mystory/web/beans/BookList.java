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

public class BookList {
    private static List<Book> bookList = new ArrayList<>();

    private static void getBooks() {
        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * from mystory.book order by name")) {
            while (resultSet.next()) {
                Book book = new Book();
                book.setName(resultSet.getString("name"));
                book.setGenre(resultSet.getString("genre"));
                book.setIsbn(resultSet.getString("isbn"));
                book.setPageCount(resultSet.getInt("page_count"));
                book.setPublishDate(resultSet.getDate("publish_date"));
                book.setPublisher(resultSet.getString("publisher"));
                bookList.add(book);
            }
        } catch (SQLException e) {
            Logger.getLogger(AuthorList.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static List<Book> getBookList() {
        if (bookList.isEmpty()) {
            getBooks();
        }
        return bookList;
    }
}
