package rakibul.quiz_project.servlet;



import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import rakibul.quiz_project.dao.QuizDAO;
import rakibul.quiz_project.model.Quiz;

import java.io.IOException;

@WebServlet("/quiz")
public class QuizServlet extends HttpServlet {

    private final QuizDAO quizDAO = new QuizDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String quizIdParam = req.getParameter("quizId");
        int quizId = (quizIdParam != null && !quizIdParam.isEmpty()) ? Integer.parseInt(quizIdParam) : 1;

        Quiz quiz = quizDAO.getQuizById(quizId);

        if (quiz == null) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        req.setAttribute("quiz", quiz);
        req.getRequestDispatcher("/WEB-INF/views/quiz.jsp").forward(req, resp);
    }
}