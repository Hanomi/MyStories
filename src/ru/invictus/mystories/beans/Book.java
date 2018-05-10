package ru.invictus.mystories.beans;

import ru.invictus.mystories.db.Database;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Book implements Serializable {
    private int id;
    private String name;
    private byte[] content;
    private int pageCount;
    private String isbn;
    private String genre;
    private String author;
    private Date publishDate;
    private String publisher;
    private byte[] image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getContent() {
        return content;
    }

    private void setContent(byte[] content) {
        if (this.content == null) {
            this.content = content;
        }
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void readPdfContent() {
        try (Statement statement = Database.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT book.content FROM mystory.book WHERE id=" + this.id)) {
            while (resultSet.next()) {
                this.setContent(resultSet.getBytes("content"));
            }
        } catch (SQLException e) {
            Logger.getLogger(Book.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
