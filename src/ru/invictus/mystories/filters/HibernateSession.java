package ru.invictus.mystories.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.hibernate.SessionFactory;
import ru.invictus.mystories.entity.HibernateUtil;

@WebFilter(filterName = "HibernateSession",
urlPatterns = "/*")
public class HibernateSession implements Filter {

    private SessionFactory sessionFactory;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        sessionFactory.getCurrentSession().beginTransaction();
        filterChain.doFilter(servletRequest, servletResponse);
        sessionFactory.getCurrentSession().getTransaction().commit();
    }

    public void init(FilterConfig filterConfig) {
        sessionFactory = HibernateUtil.getSessionFactory();
    }
}