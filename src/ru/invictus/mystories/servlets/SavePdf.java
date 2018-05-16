package ru.invictus.mystories.servlets;

import ru.invictus.mystories.controller.SearchController;
import ru.invictus.mystories.db.DataHelper;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

@WebServlet(name = "SavePdf", urlPatterns = {"/download", "/read"})
public class SavePdf extends HttpServlet {
    @Inject
    private SearchController searchController;

    public void setSearchController(SearchController searchController) {
        this.searchController = searchController;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/pdf");

        try (OutputStream out = resp.getOutputStream()) {
            long id = Long.parseLong(req.getParameter("id"));

            byte[] pdf = DataHelper.INSTANCE.getContent(id);
            resp.setContentLength(pdf.length);
            if ("/download".equals(req.getServletPath())) {
                String filename = URLEncoder.encode(req.getParameter("filename"), "UTF-8").replace("+", " ");
                resp.setHeader("Content-Disposition", "attachment;filename=" + filename + ".pdf");
            }
            out.write(pdf);
        }
    }
}
