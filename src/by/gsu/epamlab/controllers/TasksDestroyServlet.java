package by.gsu.epamlab.controllers;

import by.gsu.epamlab.Constants;
import by.gsu.epamlab.model.User;
import by.gsu.epamlab.model.factories.TasksFactory;
import by.gsu.epamlab.model.exceptions.DaoException;
import by.gsu.epamlab.model.interfaces.ITaskDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class TasksDestroyServlet extends AbstractServlet {

    private static final String KEY_DESTROY_ALL = "destroyAll";

    @Override
    protected void performTask(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            HttpSession session = req.getSession(true);
            User user = (User) session.getAttribute(Constants.KEY_USER);

            String[] tasksId = req.getParameterValues(Constants.KEY_PICKER);

            ITaskDAO taskImpl = TasksFactory.getClassFromFactory();
            String path = getServletConfig().getServletContext().getRealPath("/");
            if((req.getParameter(KEY_DESTROY_ALL)) != null){
                taskImpl.tasksDestroy(tasksId, user.getId(), path, true);
            } else {
                taskImpl.tasksDestroy(tasksId, user.getId(),path, false);
            }
            resp.sendRedirect(Constants.URL_TO_UPDATE_TASKS);
        } catch (DaoException e){
            req.setAttribute(Constants.KEY_MESSAGE, e.getMessage());
            req.getRequestDispatcher(
                    Constants.TASKS_JSP).forward(req, resp);
        }
    }
}