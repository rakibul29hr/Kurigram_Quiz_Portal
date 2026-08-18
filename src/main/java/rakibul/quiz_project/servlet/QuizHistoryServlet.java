package rakibul.quiz_project.servlet;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import rakibul.quiz_project.model.QuizAttempt;
import rakibul.quiz_project.model.User;
import rakibul.quiz_project.utils.DBConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/history")
public class QuizHistoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        List<QuizAttempt> attempts = new ArrayList<>();
        String sql = "SELECT qa.*, q.title as quiz_title " +
                "FROM quiz_attempts qa " +
                "JOIN quizzes q ON qa.quiz_id = q.id " +
                "WHERE qa.user_id = ? " +
                "ORDER BY qa.submitted_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, user.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                QuizAttempt attempt = new QuizAttempt();
                attempt.setId(rs.getInt("id"));
                attempt.setQuizTitle(rs.getString("quiz_title"));
                attempt.setScore(rs.getInt("score"));
                attempt.setTotalQuestions(rs.getInt("total_questions"));
                attempt.setPercentage(rs.getDouble("percentage"));
                attempt.setTimeTakenSeconds(rs.getInt("time_taken_seconds"));
                attempt.setSubmittedAt(rs.getTimestamp("submitted_at").toLocalDateTime());
                attempts.add(attempt);
            }

        } catch (SQLException e) {
            throw new ServletException(e);
        }

        req.setAttribute("attempts", attempts);
        req.getRequestDispatcher("/WEB-INF/views/history.jsp").forward(req, resp);
    }
}