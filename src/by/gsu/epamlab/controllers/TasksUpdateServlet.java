package by.gsu.epamlab.controllers;

import by.gsu.epamlab.Constants;
import by.gsu.epamlab.model.Task;
import by.gsu.epamlab.model.User;
import by.gsu.epamlab.model.database.Sections;
import by.gsu.epamlab.model.factories.TasksFactory;
import by.gsu.epamlab.model.exceptions.DaoException;
import by.gsu.epamlab.model.interfaces.ITaskDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

public class TasksUpdateServlet extends AbstractServlet {
    private List<Task> tasks;
    private static final String SECTION_TODAY = "Today";
    private static final String SECTION_TOMORROW = "Tomorrow";
    private static final String SECTION_SOMEDAY = "Someday";
    private static final String SECTION_FIXED = "Fixed";
    private static final String SECTION_BIN = "Bin";
    private static final String KEY_BIN = "bin";
    private static final String KEY_TODAY = "today";
    private static final String KEY_TOMORROW = "tomorrow";
    private static final String KEY_SOMEDAY = "someday";
    private static final String KEY_FIXED = "fixed";
    private static final String KEY_IS_EMPTY = "isEmpty";
    private static final String KEY_TASKS = "tasks";
    private static final String KEY_SECTION = "section";

    @Override
    protected void performTask(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try{
            HttpSession session = req.getSession(true);
            User user = (User) session.getAttribute(Constants.KEY_USER);

            session.setAttribute(Constants.KEY_DATE,
                    new Date(System.currentTimeMillis()));
            String user_id = user.getId();

            ITaskDAO taskImpl = TasksFactory.getClassFromFactory();
            String section = (String)session.getAttribute(KEY_SECTION);
            if(section == null){ section = Constants.EMPTY_STRING;}
            if((req.getParameter(KEY_TODAY)) != null || tasks == null
                    || section.equals(SECTION_TODAY)){
                section = Constants.EMPTY_STRING;
                session.setAttribute(KEY_SECTION, SECTION_TODAY);
                tasks = taskImpl.getTasks(user_id, Sections.getQueryEnd(
                        KEY_TODAY));
            }
            if((req.getParameter(KEY_TOMORROW)) != null
                    || section.equals(SECTION_TOMORROW)){
                section = Constants.EMPTY_STRING;
                session.setAttribute(KEY_SECTION, SECTION_TOMORROW);
                tasks = taskImpl.getTasks(user_id, Sections.getQueryEnd(
                        KEY_TOMORROW));
            }
            if((req.getParameter(KEY_SOMEDAY)) != null
                    || section.equals(SECTION_SOMEDAY)){
                section = Constants.EMPTY_STRING;
                session.setAttribute(KEY_SECTION, SECTION_SOMEDAY);
                tasks = taskImpl.getTasks(user_id, Sections.getQueryEnd(
                        KEY_SOMEDAY));
            }
            if((req.getParameter(KEY_FIXED)) != null
                    || section.equals(SECTION_FIXED)){
                section = Constants.EMPTY_STRING;
                session.setAttribute(KEY_SECTION, SECTION_FIXED);
                tasks = taskImpl.getTasks(user_id, Sections.getQueryEnd(
                        KEY_FIXED));
            }
            if((req.getParameter(KEY_BIN)) != null
                    || section.equals(SECTION_BIN)){
                section = Constants.EMPTY_STRING;
                session.setAttribute(KEY_SECTION, SECTION_BIN);
                tasks = taskImpl.getTasks(user_id, Sections.getQueryEnd(
                        KEY_BIN));
            }

            if(tasks.size() == 0){
                session.setAttribute(KEY_IS_EMPTY, true);
                req.getRequestDispatcher(
                        Constants.TASKS_JSP).forward(req, resp);
                return;
            }

            session.setAttribute(KEY_TASKS, tasks);
            session.setAttribute(KEY_IS_EMPTY, false);
            req.getRequestDispatcher(
                    Constants.TASKS_JSP).forward(req, resp);
        } catch (DaoException e){
            req.setAttribute(Constants.KEY_MESSAGE, e.getMessage());
            req.getRequestDispatcher(
                    Constants.TASKS_JSP).forward(req, resp);
        }
    }
}