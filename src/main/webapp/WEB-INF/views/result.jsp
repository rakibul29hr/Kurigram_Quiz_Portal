<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Quiz Result - Kurigram Quiz</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <div class="card result-card">
        <h2>Quiz Result</h2>
        <div class="result-item"><strong>Student:</strong> <span>${attempt.studentName}</span></div>
        <hr>
        <div class="result-item"><strong>Total Questions:</strong> <span>${attempt.totalQuestions}</span></div>
        <div class="result-item text-success"><strong>Correct Answers:</strong> <span>${attempt.correctAnswers}</span></div>
        <div class="result-item text-danger"><strong>Wrong Answers:</strong> <span>${attempt.wrongAnswers}</span></div>
        <div class="result-item text-warning"><strong>Unanswered:</strong> <span>${attempt.unanswered}</span></div>
        <hr>
        <div class="result-item"><strong>Score:</strong> <span>${attempt.score} / ${attempt.totalQuestions}</span></div>
        <div class="result-item"><strong>Percentage:</strong> <span>${attempt.percentage}%</span></div>
        <hr>
        <div class="result-item"><strong>Time Taken:</strong> <span>${attempt.formattedTimeTaken}</span></div>
        <div class="result-item"><strong>Attempted At:</strong> <span>${attempt.submittedAt}</span></div>

        <div class="actions">
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary">Back to Dashboard</a>
        </div>
    </div>
</div>
</body>
</html>