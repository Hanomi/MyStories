package ru.invictus.mystories.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "SessionFilter", urlPatterns = "/books.xhtml")
public class SessionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        HttpSession session = httpServletRequest.getSession(false);
        if (session == null || session.isNew()){
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/index.xhtml");
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
