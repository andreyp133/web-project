package by.gsu.epamlab.controllers;

import by.gsu.epamlab.Constants;
import by.gsu.epamlab.model.User;
import by.gsu.epamlab.model.factories.FileFactory;
import by.gsu.epamlab.model.factories.TasksFactory;
import by.gsu.epamlab.model.exceptions.DaoException;
import by.gsu.epamlab.model.interfaces.IFileDAO;
import by.gsu.epamlab.model.interfaces.ITaskDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@MultipartConfig(fileSizeThreshold=1024*1024*2)
public class CreateTaskServlet extends AbstractServlet {

    private static final int ONE_DAY_IN_MILLI_SECONDS = 86400000;
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String KEY_SOMEDAY_TASK = "newSomedayTask";
    private static final String KEY_TOMORROW_TASK = "newTomorrowTask";
    private static final String KEY_TODAY_TASK = "newTodayTask";
    private static final String KEY_TASK = "task";
    private static final String EX_MESSAGE_INVALID_DATE = "Invalid date";

    @Override
    protected void performTask(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            HttpSession session = req.getSession(true);
            User user = (User) session.getAttribute(Constants.KEY_USER);

            Date currentDate = new Date(System.currentTimeMillis());
            ITaskDAO taskImpl = TasksFactory.getClassFromFactory();
            String user_id = user.getId();

            req.setCharacterEncoding(Constants.UTF_8);
            String path = getServletConfig().getServletContext().getRealPath("/");
            Part part = req.getPart(Constants.KEY_FILE);
            IFileDAO fileImpl = FileFactory.getClassFromFactory();
            //Возвращает конечное имя файла,
            // с учетом совпадения имени с именем уже существующего файла
            String finalFileName = fileImpl.upload(part, path);

            if(req.getParameter(KEY_TODAY_TASK) != null){
                taskImpl.addTask(user_id, req.getParameter(KEY_TASK),
                        currentDate, finalFileName);
            }
            if(req.getParameter(KEY_TOMORROW_TASK) != null){
                currentDate.setTime(System.currentTimeMillis() +
                        ONE_DAY_IN_MILLI_SECONDS);
                taskImpl.addTask(user_id, req.getParameter(KEY_TASK),
                        currentDate, finalFileName);
            }
            if(req.getParameter(KEY_SOMEDAY_TASK) != null){
                SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
                try{
                    String date = req.getParameter(Constants.KEY_DATE);
                    java.util.Date parsedDate = format.parse(date);
                    Date taskDate = new Date(parsedDate.getTime());
                    taskImpl.addTask(user_id, req.getParameter(KEY_TASK),
                            taskDate, finalFileName);
                }catch (ParseException e){
                    throw new DaoException(EX_MESSAGE_INVALID_DATE);
                }
            }
            resp.sendRedirect(Constants.URL_TO_UPDATE_TASKS);
        } catch (DaoException e){
            req.setAttribute(Constants.KEY_MESSAGE, e.getMessage());
            req.getRequestDispatcher(
                    Constants.TASKS_JSP).forward(req, resp);
        }
    }
}