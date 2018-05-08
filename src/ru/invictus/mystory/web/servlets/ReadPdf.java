package ru.invictus.mystory.web.servlets;

import ru.invictus.mystory.web.beans.Book;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

public class ReadPdf extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/pdf");

        try (OutputStream out = resp.getOutputStream()) {
            String bookId = req.getParameter("index");
            Book book = (Book) req.getSession(false).getAttribute("book" + bookId);
            book.readPdfContent();
            resp.setContentLength(book.getContent().length);
            out.write(book.getContent());
        }
    }
}
