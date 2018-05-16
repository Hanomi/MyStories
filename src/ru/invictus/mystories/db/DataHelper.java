package ru.invictus.mystories.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.invictus.mystories.entity.Book;
import ru.invictus.mystories.entity.BookContent;
import ru.invictus.mystories.entity.Genre;
import ru.invictus.mystories.entity.HibernateUtil;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public enum DataHelper {
    INSTANCE;

    private SessionFactory sessionFactory;

    DataHelper() {
        sessionFactory = HibernateUtil.getSessionFactory();
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
        query.setFirstResult(2);
        query.setMaxResults(4);
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
}
