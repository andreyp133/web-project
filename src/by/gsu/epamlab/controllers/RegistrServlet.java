package by.gsu.epamlab.controllers;

import by.gsu.epamlab.Constants;
import by.gsu.epamlab.model.factories.UserFactory;
import by.gsu.epamlab.model.exceptions.DaoException;
import by.gsu.epamlab.model.exceptions.InvalidLoginOrPasswordException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegistrServlet extends AbstractServlet {

    private static final String SUCCESSFUL_REGISTRATION_JSP =
            "successfulRegistration.jsp";
    private static final String REGISTRATION_JSP = "registration.jsp";
    private static final String KEY_IS_SUCCESS = "isSuccess";
    public static final String EX_MESSAGE_EMPTY_LOGIN_OR_PASSWORD =
            "Login or password is empty";

    @Override
    protected void performTask(HttpServletRequest req,
                               HttpServletResponse resp)
            throws ServletException, IOException {
        try{
            String login = req.getParameter(Constants.KEY_LOGIN);
            String password = req.getParameter(Constants.KEY_PASSWORD);
            if(login == null && password == null) {
                req.setAttribute(Constants.KEY_MESSAGE,
                        EX_MESSAGE_EMPTY_LOGIN_OR_PASSWORD);
                req.getRequestDispatcher(
                        REGISTRATION_JSP).forward(req, resp);
            } else {
                UserFactory.getClassFromFactory().addUser(login, password);
            }
            req.setAttribute(KEY_IS_SUCCESS, true);
            req.getRequestDispatcher(
                    SUCCESSFUL_REGISTRATION_JSP).forward(req, resp);
        }catch (InvalidLoginOrPasswordException e){
            errorJump(e, req, resp);
        } catch (DaoException e){
            errorJump(e, req, resp);
        }
    }

    private void errorJump(DaoException e, HttpServletRequest req,
                           HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute(Constants.KEY_MESSAGE, e.getMessage());
        req.getRequestDispatcher(
                REGISTRATION_JSP).forward(req, resp);
    }
}