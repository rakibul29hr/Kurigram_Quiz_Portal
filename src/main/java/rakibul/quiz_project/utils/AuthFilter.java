package rakibul.quiz_project.utils;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/", "/login", "/register", "/quiz", "/start-quiz", "/submit-quiz", "/result", "/dashboard", "/history"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getServletPath();
        HttpSession session = httpRequest.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        // Handle Root URL '/'
        if ("/".equals(path) || path.isEmpty()) {
            if (isLoggedIn) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/dashboard");
            } else {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            }
            return;
        }

        // Prevent logged-in users from viewing Login or Register pages again
        if (isLoggedIn && ("/login".equals(path) || "/register".equals(path))) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/dashboard");
            return;
        }

        // Protect secured routes
        if (!isLoggedIn && !"/login".equals(path) && !"/register".equals(path)) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }

        chain.doFilter(request, response);
    }
}