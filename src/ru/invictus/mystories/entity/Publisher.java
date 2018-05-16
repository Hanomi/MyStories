package ru.invictus.mystories.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "publisher", schema = "mystory")
public class Publisher {
    private long id;
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Publisher)) return false;
        Publisher publisher = (Publisher) o;
        return id == publisher.id &&
                Objects.equals(name, publisher.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name);
    }
}
