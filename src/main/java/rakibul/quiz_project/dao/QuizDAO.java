package rakibul.quiz_project.dao;




import rakibul.quiz_project.model.Option;
import rakibul.quiz_project.model.Question;
import rakibul.quiz_project.model.Quiz;
import rakibul.quiz_project.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDAO {

    public Quiz getQuizById(int quizId) {
        Quiz quiz = null;
        String sqlQuiz = "SELECT * FROM quizzes WHERE id = ?";
        String sqlQuestions = "SELECT * FROM questions WHERE quiz_id = ?";
        String sqlOptions = "SELECT * FROM options WHERE question_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmtQuiz = conn.prepareStatement(sqlQuiz)) {

            stmtQuiz.setInt(1, quizId);
            ResultSet rsQuiz = stmtQuiz.executeQuery();

            if (rsQuiz.next()) {
                quiz = new Quiz();
                quiz.setId(rsQuiz.getInt("id"));
                quiz.setTitle(rsQuiz.getString("title"));
                quiz.setDescription(rsQuiz.getString("description"));
                quiz.setTimeLimitMinutes(rsQuiz.getInt("time_limit_minutes"));

                List<Question> questions = new ArrayList<>();
                try (PreparedStatement stmtQ = conn.prepareStatement(sqlQuestions)) {
                    stmtQ.setInt(1, quizId);
                    ResultSet rsQ = stmtQ.executeQuery();

                    while (rsQ.next()) {
                        Question q = new Question();
                        q.setId(rsQ.getInt("id"));
                        q.setQuizId(quizId);
                        q.setQuestionText(rsQ.getString("question_text"));
                        q.setCategory(rsQ.getString("category"));

                        List<Option> options = new ArrayList<>();
                        try (PreparedStatement stmtOpt = conn.prepareStatement(sqlOptions)) {
                            stmtOpt.setInt(1, q.getId());
                            ResultSet rsOpt = stmtOpt.executeQuery();
                            while (rsOpt.next()) {
                                Option opt = new Option();
                                opt.setId(rsOpt.getInt("id"));
                                opt.setQuestionId(q.getId());
                                opt.setOptionText(rsOpt.getString("option_text"));
                                opt.setCorrect(rsOpt.getBoolean("is_correct"));
                                options.add(opt);
                            }
                        }
                        q.setOptions(options);
                        questions.add(q);
                    }
                }
                quiz.setQuestions(questions);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quiz;
    }
}