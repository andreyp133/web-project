package by.gsu.epamlab.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends AbstractServlet {

    private static final String INDEX_JSP = "/index.jsp";

    @Override
    protected void performTask(
            HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(INDEX_JSP).forward(req, resp);
    }
}