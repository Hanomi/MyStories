package ru.invictus.mystories.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.invictus.mystories.entity.Genre;
import ru.invictus.mystories.entity.HibernateUtil;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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

    public List<Genre> getAllGenres() {
        final CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Genre> criteriaQuery = builder.createQuery(Genre.class);
        criteriaQuery.from(Genre.class);
        Query<Genre> query = getSession().createQuery(criteriaQuery);
        return query.getResultList();
    }
}
