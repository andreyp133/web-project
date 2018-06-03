package by.gsu.epamlab.controllers;

import by.gsu.epamlab.Constants;
import by.gsu.epamlab.model.database.TaskAction;
import by.gsu.epamlab.model.factories.TasksFactory;
import by.gsu.epamlab.model.exceptions.DaoException;
import by.gsu.epamlab.model.interfaces.ITaskDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FixingTasksServlet extends AbstractServlet {

    @Override
    protected void performTask(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String[] tasksId = req.getParameterValues(Constants.KEY_PICKER);
            ITaskDAO taskImpl = TasksFactory.getClassFromFactory();
            taskImpl.tasksAction(tasksId, TaskAction.FIX);
            resp.sendRedirect(Constants.URL_TO_UPDATE_TASKS);
        } catch (DaoException e){
            req.setAttribute(Constants.KEY_MESSAGE, e.getMessage());
            req.getRequestDispatcher(
                    Constants.TASKS_JSP).forward(req, resp);
        }
    }
}