package ru.invictus.mystories.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.sql.Date;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "book", schema = "mystory")
public class Book {
    private long id;
    private String name;
    private Author author;
    private Genre genre;
    private byte[] content;
    private int pageCount;
    private String isbn;
    private Date publishYear;
    private Publisher publisher;
    private byte[] image;
    private String description;
    private boolean edit;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    @Fetch(FetchMode.JOIN)
    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @ManyToOne
    @Fetch(FetchMode.JOIN)
    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @ManyToOne
    @Fetch(FetchMode.JOIN)
    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    @Basic
    @Column(name = "content")
    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Basic
    @Column(name = "page_count")
    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    @Basic
    @Column(name = "isbn")
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Basic
    @Column(name = "publish_year")
    public Date getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(Date publishYear) {
        this.publishYear = publishYear;
    }

    @Basic
    @Column(name = "image")
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return id == book.id &&
                pageCount == book.pageCount &&
                edit == book.edit &&
                Objects.equals(name, book.name) &&
                Objects.equals(author, book.author) &&
                Objects.equals(genre, book.genre) &&
                Arrays.equals(content, book.content) &&
                Objects.equals(isbn, book.isbn) &&
                Objects.equals(publishYear, book.publishYear) &&
                Objects.equals(publisher, book.publisher) &&
                Arrays.equals(image, book.image) &&
                Objects.equals(description, book.description);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(id, name, author, genre, pageCount, isbn, publishYear, publisher, description, edit);
        result = 31 * result + Arrays.hashCode(content);
        result = 31 * result + Arrays.hashCode(image);
        return result;
    }
}
