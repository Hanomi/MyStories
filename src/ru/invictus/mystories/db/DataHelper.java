package ru.invictus.mystories.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import ru.invictus.mystories.entity.GenreEntity;
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

    public List<GenreEntity> getAllGenres() {
        final CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<GenreEntity> criteriaQuery = builder.createQuery(GenreEntity.class);
        criteriaQuery.from(GenreEntity.class);
        Query<GenreEntity> query = getSession().createQuery(criteriaQuery);
        return query.getResultList();
    }
}
