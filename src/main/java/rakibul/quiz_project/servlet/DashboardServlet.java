package rakibul.quiz_project.servlet;



import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import rakibul.quiz_project.model.Quiz;
import rakibul.quiz_project.model.User;
import rakibul.quiz_project.utils.DBConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        List<Quiz> quizzes = new ArrayList<>();
        Map<String, Object> lastAttempt = null;

        String sqlQuizzes = "SELECT * FROM quizzes";
        String sqlLastAttempt = "SELECT qa.score, qa.total_questions, qa.percentage, qa.submitted_at, q.title as quiz_title " +
                "FROM quiz_attempts qa " +
                "JOIN quizzes q ON qa.quiz_id = q.id " +
                "WHERE qa.user_id = ? " +
                "ORDER BY qa.submitted_at DESC LIMIT 1";

        try (Connection conn = DBConnection.getConnection()) {

            // 1. Fetch All Quizzes
            try (PreparedStatement stmtQ = conn.prepareStatement(sqlQuizzes);
                 ResultSet rsQ = stmtQ.executeQuery()) {
                while (rsQ.next()) {
                    Quiz q = new Quiz();
                    q.setId(rsQ.getInt("id"));
                    q.setTitle(rsQ.getString("title"));
                    q.setDescription(rsQ.getString("description"));
                    q.setTimeLimitMinutes(rsQ.getInt("time_limit_minutes"));
                    quizzes.add(q);
                }
            }

            // 2. Fetch User's Last Attempt
            try (PreparedStatement stmtA = conn.prepareStatement(sqlLastAttempt)) {
                stmtA.setInt(1, user.getId());
                try (ResultSet rsA = stmtA.executeQuery()) {
                    if (rsA.next()) {
                        lastAttempt = new HashMap<>();
                        lastAttempt.put("quizTitle", rsA.getString("quiz_title"));
                        lastAttempt.put("score", rsA.getInt("score"));
                        lastAttempt.put("totalQuestions", rsA.getInt("total_questions"));
                        lastAttempt.put("percentage", rsA.getDouble("percentage"));
                        lastAttempt.put("submittedAt", rsA.getTimestamp("submitted_at"));
                    }
                }
            }

        } catch (SQLException e) {
            throw new ServletException("Database error loading dashboard", e);
        }

        req.setAttribute("quizzes", quizzes);
        req.setAttribute("lastAttempt", lastAttempt);
        req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
    }
}