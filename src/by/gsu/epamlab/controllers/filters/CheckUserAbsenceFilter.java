package by.gsu.epamlab.controllers.filters;

import by.gsu.epamlab.Constants;
import by.gsu.epamlab.model.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CheckUserAbsenceFilter implements Filter {

    public void init(FilterConfig config) throws ServletException { }

    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute(Constants.KEY_USER);

        if(user != null) {
            request.getRequestDispatcher(
                    Constants.URL_TO_MAIN_SERVLET).forward(request, response);
        }

        chain.doFilter(request, response);
    }

    public void destroy() { }
}