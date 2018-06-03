package by.gsu.epamlab.controllers;

import by.gsu.epamlab.Constants;
import by.gsu.epamlab.model.factories.FileFactory;
import by.gsu.epamlab.model.factories.TasksFactory;
import by.gsu.epamlab.model.exceptions.DaoException;
import by.gsu.epamlab.model.interfaces.IFileDAO;
import by.gsu.epamlab.model.interfaces.ITaskDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

@MultipartConfig(fileSizeThreshold=1024*1024*2)
public class UploadFileServlet extends AbstractServlet {

    private static final String EX_MESSAGE_FILE_TO_UPLOAD_NOT_AVAILABLE =
            "File is impossible to upload, please delete the old file";
    private static final String EX_MESSAGE_FILE_TO_UPLOAD_NOT_FOUND =
            "File to upload is not found";

    @Override
    protected void performTask(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try{
            req.setCharacterEncoding(Constants.UTF_8);
            ITaskDAO taskImpl = TasksFactory.getClassFromFactory();
            String taskId = req.getParameter(Constants.KEY_ID_TASK);
            String oldFileName = taskImpl.getFileName(taskId);
            if(oldFileName.equals(Constants.EMPTY_STRING)){
                try{
                    Part part = req.getPart(Constants.KEY_FILE);
                    IFileDAO fileImpl = FileFactory.getClassFromFactory();
                    String path = getServletConfig().getServletContext().getRealPath("/");
                    //Возвращает конечное имя файла, с учетом совпадения имени с именем уже существующего файла
                    String finalFileName = fileImpl.upload(part, path);
                    taskImpl.addFileName(finalFileName, taskId);
                    resp.sendRedirect(Constants.URL_TO_UPDATE_TASKS);
                }catch (ServletException e){
                    throw new DaoException(EX_MESSAGE_FILE_TO_UPLOAD_NOT_FOUND);
                }
            } else {
                throw new DaoException(EX_MESSAGE_FILE_TO_UPLOAD_NOT_AVAILABLE);
            }
        } catch (DaoException e){
        req.setAttribute(Constants.KEY_MESSAGE, e.getMessage());
        req.getRequestDispatcher(
                Constants.TASKS_JSP).forward(req, resp);
        }
    }
}