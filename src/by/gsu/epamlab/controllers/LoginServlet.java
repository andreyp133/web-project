package by.gsu.epamlab.controllers;

import by.gsu.epamlab.Constants;
import by.gsu.epamlab.model.User;
import by.gsu.epamlab.model.factories.UserFactory;
import by.gsu.epamlab.model.exceptions.DaoException;
import by.gsu.epamlab.model.exceptions.InvalidLoginOrPasswordException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginServlet extends AbstractServlet {

    private static final int MAX_COOKIE_AGE = 60*60*24*31;
    private static final String LOGIN_JSP = "login.jsp";

    @Override
    protected void performTask(HttpServletRequest req,
                               HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        try{
            String login = req.getParameter(Constants.KEY_LOGIN);
            String password = req.getParameter(Constants.KEY_PASSWORD);
            User user = UserFactory.getClassFromFactory().getUser(login,
                    password);
            session.setAttribute(Constants.KEY_USER, user);

            List<Cookie> cookies = new ArrayList<Cookie>();
            cookies.add(new Cookie(Constants.KEY_LOGIN, login));
            cookies.add(new Cookie(Constants.KEY_PASSWORD, password));
            for(Cookie cookie : cookies){
                cookie.setMaxAge(MAX_COOKIE_AGE);
                cookie.setPath(req.getContextPath());
                resp.addCookie(cookie);
            }
        }catch (InvalidLoginOrPasswordException e){
            errorJump(e, req, resp);
        }catch (DaoException e){
            errorJump(e, req, resp);
        }
        resp.sendRedirect(Constants.URL_TO_UPDATE_TASKS);
    }

    private void errorJump(DaoException e, HttpServletRequest req,
                           HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute(Constants.KEY_MESSAGE, e.getMessage());
        req.getRequestDispatcher(
                LOGIN_JSP).forward(req, resp);
    }
}