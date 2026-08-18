package rakibul.quiz_project.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import rakibul.quiz_project.model.Option;
import rakibul.quiz_project.model.Question;
import rakibul.quiz_project.model.Quiz;
import rakibul.quiz_project.utils.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/start-quiz")
public class StartQuizServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {
        

        String quizIdParam = req.getParameter("quizId");

        if (quizIdParam == null || quizIdParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        int quizId;

        try {
            quizId = Integer.parseInt(quizIdParam);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }



        Quiz quiz = new Quiz();

        List<Question> questions = new ArrayList<>();


        String sqlQuiz =
                "SELECT * FROM quizzes WHERE id = ?";

        String sqlQuestions =
                "SELECT * FROM questions " +
                        "WHERE quiz_id = ? " +
                        "ORDER BY RAND() " +
                        "LIMIT 15";


        // Get options for each question
        String sqlOptions =
                "SELECT * FROM options " +
                        "WHERE question_id = ?";



        try (Connection conn = DBConnection.getConnection()) {




            try (PreparedStatement stmtQuiz =
                         conn.prepareStatement(sqlQuiz)) {

                stmtQuiz.setInt(1, quizId);

                try (ResultSet rsQuiz =
                             stmtQuiz.executeQuery()) {

                    if (!rsQuiz.next()) {

                        // Quiz does not exist
                        resp.sendRedirect(
                                req.getContextPath()
                                        + "/dashboard"
                        );

                        return;
                    }

                    quiz.setId(
                            rsQuiz.getInt("id")
                    );

                    quiz.setTitle(
                            rsQuiz.getString("title")
                    );

                    quiz.setDescription(
                            rsQuiz.getString("description")
                    );

                    quiz.setTimeLimitMinutes(
                            rsQuiz.getInt("time_limit_minutes")
                    );
                }
            }




            try (PreparedStatement stmtQuestions =
                         conn.prepareStatement(sqlQuestions)) {

                stmtQuestions.setInt(1, quizId);

                try (ResultSet rsQuestions =
                             stmtQuestions.executeQuery()) {

                    while (rsQuestions.next()) {

                        Question question = new Question();

                        question.setId(
                                rsQuestions.getInt("id")
                        );

                        question.setQuestionText(
                                rsQuestions.getString("question_text")
                        );

                        question.setCategory(
                                rsQuestions.getString("category")
                        );




                        List<Option> options = new ArrayList<>();

                        try (PreparedStatement stmtOptions =
                                     conn.prepareStatement(sqlOptions)) {

                            stmtOptions.setInt(
                                    1,
                                    question.getId()
                            );

                            try (ResultSet rsOptions =
                                         stmtOptions.executeQuery()) {

                                while (rsOptions.next()) {

                                    Option option = new Option();

                                    option.setId(
                                            rsOptions.getInt("id")
                                    );

                                    option.setOptionText(
                                            rsOptions.getString(
                                                    "option_text"
                                            )
                                    );

                                    option.setCorrect(
                                            rsOptions.getBoolean(
                                                    "is_correct"
                                            )
                                    );

                                    options.add(option);
                                }
                            }
                        }


                        question.setOptions(options);

                        questions.add(question);
                    }
                }
            }

        } catch (SQLException e) {

            throw new ServletException(
                    "Error loading randomized quiz questions",
                    e
            );
        }




        if (questions.isEmpty()) {

            throw new ServletException(
                    "No questions found for this quiz."
            );
        }


        quiz.setQuestions(questions);



        HttpSession session = req.getSession();



        session.setAttribute(
                "quiz_questions_" + quizId,
                questions
        );



        List<Integer> selectedQuestionIds = new ArrayList<>();

        for (Question question : questions) {

            selectedQuestionIds.add(
                    question.getId()
            );
        }

        session.setAttribute(
                "quiz_question_ids_" + quizId,
                selectedQuestionIds
        );



        session.setAttribute(
                "quiz_start_time_" + quizId,
                LocalDateTime.now()
        );


        req.setAttribute(
                "quiz",
                quiz
        );

        req.getRequestDispatcher(
                "/WEB-INF/views/quiz.jsp"
        ).forward(req, resp);
    }
}