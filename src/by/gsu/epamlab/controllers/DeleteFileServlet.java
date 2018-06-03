package by.gsu.epamlab.controllers;

import by.gsu.epamlab.Constants;
import by.gsu.epamlab.model.factories.FileFactory;
import by.gsu.epamlab.model.factories.TasksFactory;
import by.gsu.epamlab.model.exceptions.DaoException;
import by.gsu.epamlab.model.interfaces.IFileDAO;
import by.gsu.epamlab.model.interfaces.ITaskDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteFileServlet extends AbstractServlet {

    @Override
    protected void performTask(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            //Удаление самого файла
            IFileDAO fileImpl = FileFactory.getClassFromFactory();
            req.setCharacterEncoding(Constants.UTF_8);
            String path = getServletConfig().getServletContext().getRealPath("/");
            fileImpl.delete(req.getParameter(Constants.KEY_FILE_NAME), path);
            //Удаление имени файла из базы данных
            ITaskDAO taskImpl = TasksFactory.getClassFromFactory();
            taskImpl.deleteFileName(req.getParameter(Constants.KEY_ID_TASK));
            //Обновление тасков
            resp.sendRedirect(Constants.URL_TO_UPDATE_TASKS);
        } catch (DaoException e){
            req.setAttribute(Constants.KEY_MESSAGE, e.getMessage());
            req.getRequestDispatcher(
                    Constants.TASKS_JSP).forward(req, resp);
        }
    }
}