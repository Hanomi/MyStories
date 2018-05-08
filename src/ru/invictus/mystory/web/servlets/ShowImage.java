package ru.invictus.mystory.web.servlets;

import ru.invictus.mystory.web.beans.Book;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

public class ShowImage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("image/jpeg");

        try (OutputStream out = resp.getOutputStream()) {
            Enumeration<String> parameterNames = req.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String parameter = parameterNames.nextElement();
                if (parameter.startsWith("cover")) {
                    Book book = (Book) req.getSession(false).getAttribute(parameter);
                    resp.setContentLength(book.getImage().length);
                    out.write(book.getImage());
                }
            }
        }
    }
}
