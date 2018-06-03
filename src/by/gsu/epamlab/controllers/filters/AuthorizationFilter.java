package by.gsu.epamlab.controllers.filters;

import by.gsu.epamlab.Constants;
import by.gsu.epamlab.model.User;
import by.gsu.epamlab.model.factories.UserFactory;
import by.gsu.epamlab.model.exceptions.DaoException;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthorizationFilter implements Filter {

    private static final String EX_MESSAGE_INVALID_LOG_OR_PASS_IN_COOKIES =
            "Invalid login or password in cookies!";
    private static final String EX_MESSAGE_NO_COOKIES = "No cookies";

    public void init(FilterConfig config) throws ServletException { }

    public void doFilter(ServletRequest req,
                         ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        try{
            HttpSession session = request.getSession(true);
            User user = (User) session.getAttribute(Constants.KEY_USER);
            if(user == null) {
                 uploadUserFromCookies(request, session);
            }
        } catch (DaoException e){
            System.err.println(EX_MESSAGE_INVALID_LOG_OR_PASS_IN_COOKIES);
        } finally {
            chain.doFilter(request, response);
        }
    }

    protected void uploadUserFromCookies(HttpServletRequest request,
                                         HttpSession session) throws DaoException{
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            String login = null;
            String password = null;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(Constants.KEY_LOGIN)) {
                    login = cookie.getValue();
                }
                if (cookie.getName().equals(Constants.KEY_PASSWORD)) {
                    password = cookie.getValue();
                }
            }
            if (login != null && password != null) {
                User user = UserFactory.getClassFromFactory().getUser(login,
                        password);
                session.setAttribute(Constants.KEY_USER, user);
            }
        } else {
            throw new DaoException(EX_MESSAGE_NO_COOKIES);
        }
    }

    public void destroy() { }
}