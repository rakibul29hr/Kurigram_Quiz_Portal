package rakibul.quiz_project.servlet;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import rakibul.quiz_project.dao.AttemptDAO;
import rakibul.quiz_project.model.QuizAttempt;

import java.io.IOException;

@WebServlet("/result")
public class QuizResultServlet extends HttpServlet {

    private final AttemptDAO attemptDAO = new AttemptDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String attemptIdParam = req.getParameter("attemptId");

        if (attemptIdParam == null) {
            resp.sendRedirect(req.getContextPath() + "/dashboard.jsp");
            return;
        }

        int attemptId = Integer.parseInt(attemptIdParam);
        QuizAttempt attempt = attemptDAO.getAttemptById(attemptId);

        if (attempt == null) {
            resp.sendRedirect(req.getContextPath() + "/dashboard.jsp");
            return;
        }

        req.setAttribute("attempt", attempt);
        req.getRequestDispatcher("/WEB-INF/views/result.jsp").forward(req, resp);
    }
}
