package ru.invictus.mystories.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.invictus.mystories.entity.*;
import ru.invictus.mystories.utils.SearchType;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public enum DataHelper implements Serializable {
    INSTANCE;

    private SearchType lastType;
    private String lastQuery;
    private Long lastResult;
    private SessionFactory sessionFactory;

    DataHelper() {
        sessionFactory = HibernateUtil.getSessionFactory();
        lastResult = 0L;
    }

    public List<Book> updateBooks(SearchType type, String query, int first, int pageSize) {
        switch (type) {
            case GENRE:
                return getBooksByGenre(query, first, pageSize);
            case LETTER:
                return getBooksByLetter(query, first, pageSize);
            case TITLE:
                return getBooksByTitle(query, first, pageSize);
            case AUTHOR:
                return getBooksByAuthor(query, first, pageSize);
            default:
                return Collections.emptyList();
        }
    }

    public Long getRowCount(SearchType type, String query) {
        if (type == lastType && query.equals(lastQuery)) return lastResult;

        lastType = type;
        lastQuery = query;

        switch (type) {
            case GENRE:
                return getRowCountByGenre(query);
            case LETTER:
                return getRowCountByLetter(query);
            case TITLE:
                return getRowCountByTitle(query);
            case AUTHOR:
                return getRowCountByAuthor(query);
            default:
                return 0L;
        }
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    private List<Book> getBooksByGenre(String genre, int first, int pageSize) {
        getSession().beginTransaction();
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
        Root<Book> root = criteria.from(Book.class);
        Join<Book, Genre> join = root.join("genre");
        criteria.select(root);
        criteria.where(builder.equal(join.get("id"), genre));
        criteria.orderBy(builder.asc(root.get("name")));
        Query<Book> query = getSession().createQuery(criteria);
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        List<Book> resultList = query.getResultList();
        getSession().getTransaction().commit();
        return resultList;
    }

    private Long getRowCountByGenre(String genre) {
        getSession().beginTransaction();
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Book> root = criteria.from(Book.class);
        Join<Book, Genre> join = root.join("genre");
        criteria.select(builder.count(root));
        criteria.where(builder.equal(join.get("id"), genre));
        lastResult = getSession().createQuery(criteria).getSingleResult();
        getSession().getTransaction().commit();
        return lastResult;
    }

    private List<Book> getBooksByTitle(String title, int first, int pageSize) {
        getSession().beginTransaction();
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
        Root<Book> root = criteria.from(Book.class);
        criteria.select(root);
        criteria.where(builder.like(root.get("name"), "%" + title + "%"));
        criteria.orderBy(builder.asc(root.get("name")));
        Query<Book> query = getSession().createQuery(criteria);
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        List<Book> resultList = query.getResultList();
        getSession().getTransaction().commit();
        return resultList;
    }

    private Long getRowCountByTitle(String title) {
        getSession().beginTransaction();
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Book> root = criteria.from(Book.class);
        criteria.select(builder.count(root));
        criteria.where(builder.like(root.get("name"), "%" + title + "%"));
        lastResult = getSession().createQuery(criteria).getSingleResult();
        getSession().getTransaction().commit();
        return lastResult;
    }

    private List<Book> getBooksByAuthor(String author, int first, int pageSize) {
        getSession().beginTransaction();
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
        Root<Book> root = criteria.from(Book.class);
        Join<Book, Author> join = root.join("author");
        criteria.select(root);
        criteria.where(builder.like(join.get("fio"), "%" + author + "%"));
        criteria.orderBy(builder.asc(root.get("name")));
        Query<Book> query = getSession().createQuery(criteria);
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        List<Book> resultList = query.getResultList();
        getSession().getTransaction().commit();
        return resultList;
    }

    private Long getRowCountByAuthor(String author) {
        getSession().beginTransaction();
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Book> root = criteria.from(Book.class);
        Join<Book, Author> join = root.join("author");
        criteria.select(builder.count(root));
        criteria.where(builder.like(join.get("fio"), "%" + author + "%"));
        lastResult = getSession().createQuery(criteria).getSingleResult();
        getSession().getTransaction().commit();
        return lastResult;
    }

    private List<Book> getBooksByLetter(String letter, int first, int pageSize) {
        getSession().beginTransaction();
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
        Root<Book> root = criteria.from(Book.class);
        criteria.select(root);
        criteria.where(builder.like(root.get("name"), letter + "%"));
        criteria.orderBy(builder.asc(root.get("name")));
        Query<Book> query = getSession().createQuery(criteria);
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        List<Book> resultList = query.getResultList();
        getSession().getTransaction().commit();
        return resultList;
    }

    private Long getRowCountByLetter(String letter) {
        getSession().beginTransaction();
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Book> root = criteria.from(Book.class);
        criteria.select(builder.count(root));
        criteria.where(builder.like(root.get("name"), letter + "%"));
        lastResult = getSession().createQuery(criteria).getSingleResult();
        getSession().getTransaction().commit();
        return lastResult;
    }

    // нужно упростить......
    public List<Genre> getAllGenres() {
        getSession().beginTransaction();
        final CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Genre> criteria = builder.createQuery(Genre.class);
        Root<Genre> root = criteria.from(Genre.class);
        criteria.orderBy(builder.asc(root.get("name")));
        Query<Genre> query = getSession().createQuery(criteria);
        List<Genre> resultList = query.getResultList();
        getSession().getTransaction().commit();
        return resultList;
    }

    public byte[] getContent(long id) {
        getSession().beginTransaction();
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<byte[]> criteria = builder.createQuery(byte[].class);
        Root<BookContent> root = criteria.from(BookContent.class);
        criteria.select(root.get("content"));
        criteria.where(builder.equal(root.get("id"), id));
        Query<byte[]> query = getSession().createQuery(criteria);
        byte[] content = query.uniqueResult();
        getSession().getTransaction().commit();
        return content;
    }

    public List<String> getAllLetters() {
        getSession().beginTransaction();
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<String> criteria = builder.createQuery(String.class);
        Root<Book> root = criteria.from(Book.class);
        criteria.select(root.get("name"));
        criteria.orderBy(builder.asc(root.get("name")));
        Query<String> query = getSession().createQuery(criteria);
        List<String> resultList = query.getResultList();
        getSession().getTransaction().commit();
        return resultList;
    }

    public void update(List<Book> bookList) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            bookList.forEach(f -> {
                if (f.isEdit()) session.update(f);
            });
            transaction.commit();
            //session.flush();
        }
    }

    public List<Author> getAllAuthors() {
        getSession().beginTransaction();
        final CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Author> criteria = builder.createQuery(Author.class);
        Root<Author> root = criteria.from(Author.class);
        criteria.orderBy(builder.asc(root.get("fio")));
        Query<Author> query = getSession().createQuery(criteria);
        List<Author> resultList = query.getResultList();
        getSession().getTransaction().commit();
        return resultList;
    }

    public List<Publisher> getAllPublisher() {
        getSession().beginTransaction();
        final CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Publisher> criteria = builder.createQuery(Publisher.class);
        Root<Publisher> root = criteria.from(Publisher.class);
        criteria.orderBy(builder.asc(root.get("name")));
        Query<Publisher> query = getSession().createQuery(criteria);
        List<Publisher> resultList = query.getResultList();
        getSession().getTransaction().commit();
        return resultList;
    }
}
