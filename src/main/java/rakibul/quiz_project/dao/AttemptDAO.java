package rakibul.quiz_project.dao;



import rakibul.quiz_project.model.QuizAttempt;
import rakibul.quiz_project.utils.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Map;

public class AttemptDAO {

    public int saveAttempt(QuizAttempt attempt, Map<Integer, Integer> userAnswers) {
        String insertAttempt = "INSERT INTO quiz_attempts " +
                "(user_id, quiz_id, total_questions, correct_answers, wrong_answers, unanswered, score, percentage, time_taken_seconds, started_at, submitted_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String insertDetail = "INSERT INTO attempt_details (attempt_id, question_id, selected_option_id, is_correct) VALUES (?, ?, ?, ?)";
        String checkOption = "SELECT is_correct FROM options WHERE id = ? AND question_id = ?";

        Connection conn = null;
        int generatedAttemptId = -1;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Transaction

            try (PreparedStatement stmt = conn.prepareStatement(insertAttempt, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, attempt.getUserId());
                stmt.setInt(2, attempt.getQuizId());
                stmt.setInt(3, attempt.getTotalQuestions());
                stmt.setInt(4, attempt.getCorrectAnswers());
                stmt.setInt(5, attempt.getWrongAnswers());
                stmt.setInt(6, attempt.getUnanswered());
                stmt.setInt(7, attempt.getScore());
                stmt.setDouble(8, attempt.getPercentage());
                stmt.setInt(9, attempt.getTimeTakenSeconds());
                stmt.setTimestamp(10, Timestamp.valueOf(attempt.getStartedAt()));
                stmt.setTimestamp(11, Timestamp.valueOf(attempt.getSubmittedAt()));

                stmt.executeUpdate();

                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    generatedAttemptId = keys.getInt(1);
                }
            }

            try (PreparedStatement stmtDetail = conn.prepareStatement(insertDetail);
                 PreparedStatement stmtCheck = conn.prepareStatement(checkOption)) {

                for (Map.Entry<Integer, Integer> entry : userAnswers.entrySet()) {
                    int questionId = entry.getKey();
                    Integer optionId = entry.getValue();

                    boolean isCorrect = false;
                    if (optionId != null) {
                        stmtCheck.setInt(1, optionId);
                        stmtCheck.setInt(2, questionId);
                        ResultSet rs = stmtCheck.executeQuery();
                        if (rs.next()) {
                            isCorrect = rs.getBoolean("is_correct");
                        }
                    }

                    stmtDetail.setInt(1, generatedAttemptId);
                    stmtDetail.setInt(2, questionId);
                    if (optionId != null) {
                        stmtDetail.setInt(3, optionId);
                    } else {
                        stmtDetail.setNull(3, Types.INTEGER);
                    }
                    stmtDetail.setBoolean(4, isCorrect);
                    stmtDetail.addBatch();
                }
                stmtDetail.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return generatedAttemptId;
    }

    public QuizAttempt getAttemptById(int attemptId) {
        String sql = "SELECT qa.*, u.name as student_name, q.title as quiz_title " +
                "FROM quiz_attempts qa " +
                "JOIN users u ON qa.user_id = u.id " +
                "JOIN quizzes q ON qa.quiz_id = q.id " +
                "WHERE qa.id = ?";
        QuizAttempt attempt = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, attemptId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                attempt = new QuizAttempt();
                attempt.setId(rs.getInt("id"));
                attempt.setUserId(rs.getInt("user_id"));
                attempt.setQuizId(rs.getInt("quiz_id"));
                attempt.setStudentName(rs.getString("student_name"));
                attempt.setQuizTitle(rs.getString("quiz_title"));
                attempt.setTotalQuestions(rs.getInt("total_questions"));
                attempt.setCorrectAnswers(rs.getInt("correct_answers"));
                attempt.setWrongAnswers(rs.getInt("wrong_answers"));
                attempt.setUnanswered(rs.getInt("unanswered"));
                attempt.setScore(rs.getInt("score"));
                attempt.setPercentage(rs.getDouble("percentage"));
                attempt.setTimeTakenSeconds(rs.getInt("time_taken_seconds"));
                attempt.setStartedAt(rs.getTimestamp("started_at").toLocalDateTime());
                attempt.setSubmittedAt(rs.getTimestamp("submitted_at").toLocalDateTime());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attempt;
    }
}