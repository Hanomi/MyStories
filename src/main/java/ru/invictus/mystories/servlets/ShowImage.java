package ru.invictus.mystories.servlets;

import ru.invictus.mystories.controller.SearchController;
import ru.invictus.mystories.entity.Book;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet(name = "ShowImage", urlPatterns = {"/image"})
public class ShowImage extends HttpServlet {

    @Inject
    private SearchController searchController;

    public void setSearchController(SearchController searchController) {
        this.searchController = searchController;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("image/jpeg");

        try (OutputStream out = resp.getOutputStream()) {
            long id = Long.parseLong(req.getParameter("id"));
            Book book = searchController.getBookList().stream().filter(f -> f.getId() == id).findFirst().get();
            resp.setContentLength(book.getImage().length);
            out.write(book.getImage());
        }
    }
}
