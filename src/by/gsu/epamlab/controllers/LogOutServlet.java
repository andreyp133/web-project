package by.gsu.epamlab.controllers;

import by.gsu.epamlab.Constants;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogOutServlet extends AbstractServlet {

    private static final int COOKIE_AGE_DEAD = 60*60*24*31;
    private static final String INDEX_JSP = "index.jsp";

    @Override
    protected void performTask(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.getSession().invalidate();
        Cookie loginCookie = new Cookie(Constants.KEY_LOGIN, Constants.EMPTY_STRING);
        loginCookie.setMaxAge(COOKIE_AGE_DEAD);
        resp.addCookie(loginCookie);
        Cookie tokenCookie = new Cookie(Constants.KEY_PASSWORD, Constants.EMPTY_STRING);
        tokenCookie.setMaxAge(COOKIE_AGE_DEAD);
        resp.addCookie(tokenCookie);
        resp.sendRedirect(INDEX_JSP);
    }
}