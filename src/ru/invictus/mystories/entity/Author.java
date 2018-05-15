package ru.invictus.mystories.entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "author", schema = "mystory")
public class Author {
    private long id;
    private String fio;
    private Date birthday;
    private Set<Book> books = new HashSet<>(0);

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "fio")
    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    @Basic
    @Column(name = "birthday")
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @OneToMany
    @JoinColumn(name = "author_id")
    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Author)) return false;
        Author author = (Author) o;
        return id == author.id &&
                Objects.equals(fio, author.fio) &&
                Objects.equals(birthday, author.birthday) &&
                Objects.equals(books, author.books);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, fio, birthday, books);
    }
}
