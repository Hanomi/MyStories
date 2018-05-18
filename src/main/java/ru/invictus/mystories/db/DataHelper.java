package ru.invictus.mystories.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.invictus.mystories.controller.PageController;
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

    private SessionFactory sessionFactory;
    private SearchType lastType;
    private String lastQuery;

    DataHelper() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public List<Book> updateBooks(SearchType type, PageController pageController, String query) {
        boolean updatePG = false;

        if (type != SearchType.UPDATE) {
            lastType = type;
            lastQuery = query;
            updatePG = true;
        }
        switch (lastType) {
            case GENRE:
                return getBooksByGenre(lastQuery, pageController, updatePG);
            case LETTER:
                return getBooksByLetter(lastQuery, pageController, updatePG);
            case TITLE:
                return getBooksByTitle(lastQuery, pageController, updatePG);
            case AUTHOR:
                return getBooksByAuthor(lastQuery, pageController, updatePG);
            default:
                return Collections.emptyList();
        }
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    private List<Book> getBooksByGenre(String genre, PageController pageController, boolean updatePG) {
        getSession().beginTransaction();
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        if (updatePG) {
            CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
            Root<Book> root = criteria.from(Book.class);
            Join<Book, Genre> join = root.join("genre");
            criteria.select(builder.count(root));
            criteria.where(builder.equal(join.get("id"), genre));
            pageController.setFoundBooks(getSession().createQuery(criteria).getSingleResult());
        }

        CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
        Root<Book> root = criteria.from(Book.class);
        Join<Book, Genre> join = root.join("genre");
        criteria.select(root);
        criteria.where(builder.equal(join.get("id"), genre));
        criteria.orderBy(builder.asc(root.get("name")));
        Query<Book> query = getSession().createQuery(criteria);
        query.setFirstResult(pageController.getFirstResult());
        query.setMaxResults(pageController.getMaxResults());
        List<Book> resultList = query.getResultList();
        getSession().getTransaction().commit();
        return resultList;
    }

    private List<Book> getBooksByTitle(String title, PageController pageController, boolean updatePG) {
        getSession().beginTransaction();
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        if (updatePG) {
            CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
            Root<Book> root = criteria.from(Book.class);
            criteria.select(builder.count(root));
            criteria.where(builder.like(root.get("name"), "%" + title + "%"));
            pageController.setFoundBooks(getSession().createQuery(criteria).getSingleResult());
        }

        CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
        Root<Book> root = criteria.from(Book.class);
        criteria.select(root);
        criteria.where(builder.like(root.get("name"), "%" + title + "%"));
        criteria.orderBy(builder.asc(root.get("name")));
        Query<Book> query = getSession().createQuery(criteria);
        query.setFirstResult(pageController.getFirstResult());
        query.setMaxResults(pageController.getMaxResults());
        List<Book> resultList = query.getResultList();
        getSession().getTransaction().commit();
        return resultList;
    }

    private List<Book> getBooksByAuthor(String author, PageController pageController, boolean updatePG) {
        getSession().beginTransaction();
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        if (updatePG) {
            CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
            Root<Book> root = criteria.from(Book.class);
            Join<Book, Author> join = root.join("author");
            criteria.select(builder.count(root));
            criteria.where(builder.like(join.get("fio"), "%" + author + "%"));
            pageController.setFoundBooks(getSession().createQuery(criteria).getSingleResult());
        }

        CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
        Root<Book> root = criteria.from(Book.class);
        Join<Book, Author> join = root.join("author");
        criteria.select(root);
        criteria.where(builder.like(join.get("fio"), "%" + author + "%"));
        criteria.orderBy(builder.asc(root.get("name")));
        Query<Book> query = getSession().createQuery(criteria);
        query.setFirstResult(pageController.getFirstResult());
        query.setMaxResults(pageController.getMaxResults());
        List<Book> resultList = query.getResultList();
        getSession().getTransaction().commit();
        return resultList;
    }

    private List<Book> getBooksByLetter(String letter, PageController pageController, boolean updatePG) {
        getSession().beginTransaction();
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        if (updatePG) {
            CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
            Root<Book> root = criteria.from(Book.class);
            criteria.select(builder.count(root));
            criteria.where(builder.like(root.get("name"), letter + "%"));
            pageController.setFoundBooks(getSession().createQuery(criteria).getSingleResult());
        }

        CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
        Root<Book> root = criteria.from(Book.class);
        criteria.select(root);
        criteria.where(builder.like(root.get("name"), letter + "%"));
        criteria.orderBy(builder.asc(root.get("name")));
        Query<Book> query = getSession().createQuery(criteria);
        query.setFirstResult(pageController.getFirstResult());
        query.setMaxResults(pageController.getMaxResults());
        List<Book> resultList = query.getResultList();
        getSession().getTransaction().commit();
        return resultList;
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