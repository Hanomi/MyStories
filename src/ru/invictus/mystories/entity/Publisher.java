package ru.invictus.mystories.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "publisher", schema = "mystory")
public class Publisher {
    private long id;
    private String name;
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
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany
    @JoinColumn(name = "publisher_id")
    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Publisher)) return false;
        Publisher publisher = (Publisher) o;
        return id == publisher.id &&
                Objects.equals(name, publisher.name) &&
                Objects.equals(books, publisher.books);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, books);
    }
}
