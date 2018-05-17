package ru.invictus.mystories.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.invictus.mystories.controller.PageController;
import ru.invictus.mystories.entity.Book;
import ru.invictus.mystories.entity.BookContent;
import ru.invictus.mystories.entity.Genre;
import ru.invictus.mystories.entity.HibernateUtil;
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
                return getAllBooks();
            case TITLE:
                return getAllBooks();
            case AUTHOR:
                return getAllBooks();
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

    public List<Book> getAllBooks() {
        getSession().beginTransaction();
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
        Root<Book> root = criteria.from(Book.class);
        criteria.orderBy(builder.asc(root.get("name")));
        Query<Book> query = getSession().createQuery(criteria);
        query.setFirstResult(0);
        query.setMaxResults(5);
        List<Book> resultList = query.getResultList();
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

    public <T> Long getCount(Class<T> tClass, String field, String search){
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<T> root = criteria.from(tClass);
        criteria.where(builder.equal(root.get(field), search));
        criteria.select(builder.count(criteria.from(tClass)));
        return getSession().createQuery(criteria).getSingleResult();
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
}
