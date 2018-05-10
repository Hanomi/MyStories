package ru.invictus.mystories.servlets;

import ru.invictus.mystories.controller.SearchController;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

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
            String id = req.getParameter("id");
            byte[] image = searchController.getData(id, FileType.JPEG);
            resp.setContentLength(image.length);
            out.write(image);
        }
    }
}
