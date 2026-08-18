package rakibul.quiz_project.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import rakibul.quiz_project.dao.AttemptDAO;
import rakibul.quiz_project.dao.QuizDAO;
import rakibul.quiz_project.model.Option;
import rakibul.quiz_project.model.Question;
import rakibul.quiz_project.model.Quiz;
import rakibul.quiz_project.model.QuizAttempt;
import rakibul.quiz_project.model.User;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/submit-quiz")
public class SubmitQuizServlet extends HttpServlet {

    private final QuizDAO quizDAO = new QuizDAO();
    private final AttemptDAO attemptDAO = new AttemptDAO();


    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {


        // ---------------------------------------------------------
        // 1. Get session
        // ---------------------------------------------------------

        HttpSession session = req.getSession();


        // ---------------------------------------------------------
        // 2. Get logged-in user
        // ---------------------------------------------------------

        User user = (User) session.getAttribute("user");

        if (user == null) {

            resp.sendRedirect(
                    req.getContextPath() + "/login"
            );

            return;
        }


        // ---------------------------------------------------------
        // 3. Get quiz ID
        // ---------------------------------------------------------

        String quizIdParam = req.getParameter("quizId");

        if (quizIdParam == null || quizIdParam.trim().isEmpty()) {

            resp.sendRedirect(
                    req.getContextPath() + "/dashboard"
            );

            return;
        }

        int quizId;

        try {

            quizId = Integer.parseInt(quizIdParam);

        } catch (NumberFormatException e) {

            resp.sendRedirect(
                    req.getContextPath() + "/dashboard"
            );

            return;
        }


        // ---------------------------------------------------------
        // 4. Get quiz information
        // ---------------------------------------------------------

        Quiz quiz = quizDAO.getQuizById(quizId);

        if (quiz == null) {

            resp.sendRedirect(
                    req.getContextPath() + "/dashboard"
            );

            return;
        }


        // ---------------------------------------------------------
        // 5. Get EXACT 15 questions from session
        // ---------------------------------------------------------

        @SuppressWarnings("unchecked")
        List<Question> questions =
                (List<Question>) session.getAttribute(
                        "quiz_questions_" + quizId
                );


        /*
         * If there are no questions in session, then this
         * submission does not belong to an active quiz attempt.
         */
        if (questions == null || questions.isEmpty()) {

            resp.sendRedirect(
                    req.getContextPath() + "/dashboard"
            );

            return;
        }


        // ---------------------------------------------------------
        // 6. Get quiz start time
        // ---------------------------------------------------------

        LocalDateTime startTime =
                (LocalDateTime) session.getAttribute(
                        "quiz_start_time_" + quizId
                );

        if (startTime == null) {

            throw new ServletException(
                    "Quiz start time was not found."
            );
        }


        // ---------------------------------------------------------
        // 7. Submission time
        // ---------------------------------------------------------

        LocalDateTime submissionTime =
                LocalDateTime.now();


        // ---------------------------------------------------------
        // 8. Calculate time taken
        // ---------------------------------------------------------

        long secondsTaken =
                Duration.between(
                        startTime,
                        submissionTime
                ).getSeconds();


        /*
         * Prevent negative time in case system clock changes.
         */
        if (secondsTaken < 0) {
            secondsTaken = 0;
        }


        // ---------------------------------------------------------
        // 9. Check time limit
        // ---------------------------------------------------------

        long allowedSeconds =
                quiz.getTimeLimitMinutes() * 60L;


        /*
         * If the user submits after the time limit,
         * we still calculate the result here.
         *
         * You can instead reject the submission if you want.
         */
        boolean timeExpired =
                secondsTaken > allowedSeconds;


        // ---------------------------------------------------------
        // 10. Score calculation
        // ---------------------------------------------------------

        int totalQuestions = questions.size();

        int correctCount = 0;

        int wrongCount = 0;

        int unansweredCount = 0;


        /*
         * Stores:
         *
         * questionId -> selectedOptionId
         *
         * null means unanswered.
         */
        Map<Integer, Integer> userAnswers =
                new HashMap<>();


        // ---------------------------------------------------------
        // 11. Process ONLY the selected 15 questions
        // ---------------------------------------------------------

        for (Question question : questions) {


            String parameterName =
                    "q_" + question.getId();


            String paramValue =
                    req.getParameter(parameterName);


            // =====================================================
            // UNANSWERED
            // =====================================================

            if (paramValue == null ||
                    paramValue.trim().isEmpty()) {

                unansweredCount++;

                userAnswers.put(
                        question.getId(),
                        null
                );

                continue;
            }


            // =====================================================
            // CONVERT OPTION ID
            // =====================================================

            int selectedOptionId;

            try {

                selectedOptionId =
                        Integer.parseInt(
                                paramValue
                        );

            } catch (NumberFormatException e) {

                /*
                 * Invalid option submitted.
                 *
                 * Treat it as unanswered instead of trusting
                 * the client.
                 */
                unansweredCount++;

                userAnswers.put(
                        question.getId(),
                        null
                );

                continue;
            }


            // =====================================================
            // CHECK THAT OPTION BELONGS TO QUESTION
            // =====================================================

            Option selectedOption = null;

            for (Option option :
                    question.getOptions()) {

                if (option.getId() ==
                        selectedOptionId) {

                    selectedOption = option;

                    break;
                }
            }


            /*
             * The submitted option does not belong to this
             * question.
             *
             * Never trust the option ID sent by the browser.
             */
            if (selectedOption == null) {

                wrongCount++;

                userAnswers.put(
                        question.getId(),
                        null
                );

                continue;
            }


            // =====================================================
            // STORE USER ANSWER
            // =====================================================

            userAnswers.put(
                    question.getId(),
                    selectedOptionId
            );


            // =====================================================
            // CHECK CORRECTNESS
            // =====================================================

            if (selectedOption.isCorrect()) {

                correctCount++;

            } else {

                wrongCount++;
            }
        }


        // ---------------------------------------------------------
        // 12. Calculate score
        // ---------------------------------------------------------

        int score = correctCount;


        double percentage;

        if (totalQuestions > 0) {

            percentage =
                    ((double) score /
                            totalQuestions) * 100.0;

        } else {

            percentage = 0.0;
        }


        /*
         * Round to 2 decimal places.
         */
        percentage =
                Math.round(
                        percentage * 100.0
                ) / 100.0;


        // ---------------------------------------------------------
        // 13. Create QuizAttempt
        // ---------------------------------------------------------

        QuizAttempt attempt =
                new QuizAttempt();


        attempt.setUserId(
                user.getId()
        );


        attempt.setQuizId(
                quizId
        );


        attempt.setTotalQuestions(
                totalQuestions
        );


        attempt.setCorrectAnswers(
                correctCount
        );


        attempt.setWrongAnswers(
                wrongCount
        );


        attempt.setUnanswered(
                unansweredCount
        );


        attempt.setScore(
                score
        );


        attempt.setPercentage(
                percentage
        );


        attempt.setTimeTakenSeconds(
                (int) secondsTaken
        );


        attempt.setStartedAt(
                startTime
        );


        attempt.setSubmittedAt(
                submissionTime
        );


        // ---------------------------------------------------------
        // 14. Save attempt + answers
        // ---------------------------------------------------------

        int attemptId =
                attemptDAO.saveAttempt(
                        attempt,
                        userAnswers
                );


        // ---------------------------------------------------------
        // 15. Clean session
        // ---------------------------------------------------------

        session.removeAttribute(
                "quiz_questions_" + quizId
        );


        session.removeAttribute(
                "quiz_question_ids_" + quizId
        );


        session.removeAttribute(
                "quiz_start_time_" + quizId
        );


        // ---------------------------------------------------------
        // 16. Redirect to result page
        // ---------------------------------------------------------

        resp.sendRedirect(
                req.getContextPath()
                        + "/result?attemptId="
                        + attemptId
        );
    }
}