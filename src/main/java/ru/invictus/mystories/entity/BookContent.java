package ru.invictus.mystories.entity;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "book", schema = "mystory")
public class BookContent {
    private long id;
    private byte[] content;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "content")
    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookContent)) return false;
        BookContent that = (BookContent) o;
        return id == that.id &&
                Arrays.equals(content, that.content);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(id);
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }
}
