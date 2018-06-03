package by.gsu.epamlab.controllers;

import by.gsu.epamlab.Constants;
import by.gsu.epamlab.model.factories.TasksFactory;
import by.gsu.epamlab.model.exceptions.DaoException;
import by.gsu.epamlab.model.impl.FileImpl;
import by.gsu.epamlab.model.interfaces.ITaskDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class DownloadFileServlet extends AbstractServlet {

    private static final String CONTENT_TYPE = "application/octet-stream";
    private static final String CONTENT_HEADER = "Content-Disposition";
    private static final String EX_MESSAGE_FILE_NOT_FOUND =
            "File is not found. Error deleting file name from DB";

    @Override
    protected void performTask(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try{
            req.setCharacterEncoding(Constants.UTF_8);
            resp.setCharacterEncoding(Constants.UTF_8);

            //определение истинного маршрута файла относительно каталога, в котором сервер хранит документы
            String path = getServletConfig().getServletContext().getRealPath("/");
            path = FileImpl.getPathToDir(path); //и получение из него корня

            String fileName = req.getParameter(Constants.KEY_FILE_NAME);
            String filePath = path + File.separator + fileName;
            File downloadFile = new File(filePath);
            if(downloadFile.exists()){
                resp.setContentType(CONTENT_TYPE);
                resp.setContentLength((int) downloadFile.length());

                String headerKey = CONTENT_HEADER;
                String headerValue = String.format("attachment; filename=\"%s\"",
                        downloadFile.getName());
                resp.setHeader(headerKey, headerValue);

                FileInputStream fileInputStream = new FileInputStream(filePath);
                OutputStream outStream = resp.getOutputStream();
                int i = 1;
                while((i = fileInputStream.read()) != -1){
                    outStream.write(i);
                }
                fileInputStream.close();
                outStream.close();
            } else {
                try{
                    ITaskDAO taskImpl = TasksFactory.getClassFromFactory();
                    taskImpl.deleteFileName(req.getParameter(Constants.KEY_ID_TASK));
                    resp.sendRedirect(Constants.URL_TO_UPDATE_TASKS);
                } catch (DaoException e){
                    req.setAttribute(Constants.KEY_MESSAGE,
                            EX_MESSAGE_FILE_NOT_FOUND);
                    req.getRequestDispatcher(
                            Constants.TASKS_JSP).forward(req, resp);
                }
            }
        } catch (IOException e){
            req.setAttribute(Constants.KEY_MESSAGE, e.getMessage());
            req.getRequestDispatcher(
                    Constants.TASKS_JSP).forward(req, resp);
        }
    }
}