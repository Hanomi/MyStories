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
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

public enum DataHelper implements Serializable {
    INSTANCE;

    private SessionFactory sessionFactory;

    DataHelper() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public List<Book> updateBooks(SearchType type, PageController pageController, String query) {
        pageController.updatePager();
        return getAllBooks();
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    // нужно упростить......
    public List<Genre> getAllGenres() {
        getSession().beginTransaction();
        System.out.println("OPEN");
        final CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Genre> criteria = builder.createQuery(Genre.class);
        Root<Genre> root = criteria.from(Genre.class);
        criteria.orderBy(builder.asc(root.get("name")));
        Query<Genre> query = getSession().createQuery(criteria);
        List<Genre> resultList = query.getResultList();
        getSession().getTransaction().commit();
        System.out.println("CLOSE");
        return resultList;
    }

    public List<Book> getAllBooks() {
        getSession().beginTransaction();
        System.out.println("OPEN");
        final CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
        Root<Book> root = criteria.from(Book.class);
        criteria.orderBy(builder.asc(root.get("name")));
        Query<Book> query = getSession().createQuery(criteria);
        query.setFirstResult(0);
        query.setMaxResults(5);
        List<Book> resultList = query.getResultList();
        getSession().getTransaction().commit();
        System.out.println("CLOSE");
        return resultList;
    }

    public byte[] getContent(long id) {
        getSession().beginTransaction();
        System.out.println("OPEN");
        final CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<byte[]> criteria = builder.createQuery(byte[].class);
        Root<BookContent> root = criteria.from(BookContent.class);
        criteria.select(root.get("content"));
        criteria.where(builder.equal(root.get("id"), id));
        Query<byte[]> query = getSession().createQuery(criteria);
        byte[] content = query.uniqueResult();
        getSession().getTransaction().commit();
        System.out.println("CLOSE");
        return content;
    }

    public <T> Long getCount(Class<T> tClass){
        final CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        criteria.select(builder.count(criteria.from(tClass)));
        //criteria.where(builder.equal(root.get("id"), id));
        return getSession().createQuery(criteria).getSingleResult();
    }

    public List<String> getAllLetters() {
        getSession().beginTransaction();
        System.out.println("OPEN");
        final CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<String> criteria = builder.createQuery(String.class);
        Root<Book> root = criteria.from(Book.class);
        criteria.select(root.get("name"));
        criteria.orderBy(builder.asc(root.get("name")));
        Query<String> query = getSession().createQuery(criteria);
        List<String> resultList = query.getResultList();
        getSession().getTransaction().commit();
        System.out.println("CLOSE");
        return resultList;
    }
}
