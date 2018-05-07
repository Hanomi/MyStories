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
    private List<Book> getBooks(String sql) {
        List<Book> bookList = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
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
        return bookList;
    }

    public List<Book> getBookList() {
        return getBooks("SELECT * from mystory.book order by name");
    }

    public List<Book> getBookListByGenre(int id) {
        return getBooks("SELECT b.id, b.name, b.isbn, b.page_count,b.publish_year, b.publish_year, b.image " +
                "a.fio AS author, g.name AS genre, p.name AS publisher " +
                "FROM mystory.book b " +
                "INNER JOIN author a on b.author_id = a.id " +
                "INNER JOIN genre g on b.genre_id = g.id " +
                "INNER JOIN publisher p on b.publisher_id = p.id " +
                "WHERE genre_id = " + id + " ORDER BY b.name " +
                "LIMIT 0,5");
    }
}
