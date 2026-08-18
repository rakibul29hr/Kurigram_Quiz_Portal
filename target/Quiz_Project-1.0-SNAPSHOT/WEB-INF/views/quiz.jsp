<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${quiz.title} - Kurigram Quiz</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<nav class="navbar">
    <div class="nav-brand">Kurigram Quiz Portal</div>
    <div class="nav-actions">
        <span style="color: var(--text-muted); font-size: 0.9rem;">Student: <strong>${sessionScope.user.name}</strong></span>
    </div>
</nav>

<div class="container" style="max-width: 800px;">

    <!-- Sticky Timer & Progress Header -->
    <div class="quiz-topbar">
        <div>
            <h3 style="margin-bottom: 2px;">${quiz.title}</h3>
            <span style="color: var(--text-muted); font-size: 0.85rem;">Answer all questions before the timer expires</span>
        </div>
        <div id="timerBox" class="timer-pill">
            ⏳ <span id="timer">--:--</span>
        </div>
    </div>

    <!-- Quiz Form -->
    <form id="quizForm" action="${pageContext.request.contextPath}/submit-quiz" method="POST">
        <input type="hidden" name="quizId" value="${quiz.id}">

        <c:forEach var="q" items="${quiz.questions}" varStatus="status">
            <div class="card question-card">
                <div class="question-header">
                    <h4 style="font-size: 1.05rem; line-height: 1.4;">
                        <span style="color: var(--primary);">Q${status.index + 1}.</span> ${q.questionText}
                    </h4>
                    <span class="category-tag">${q.category}</span>
                </div>

                <div class="options-grid">
                    <c:forEach var="opt" items="${q.options}">
                        <label class="option-card">
                            <input type="radio" name="q_${q.id}" value="${opt.id}">
                            <span>${opt.optionText}</span>
                        </label>
                    </c:forEach>
                </div>
            </div>
        </c:forEach>

        <div style="display: flex; justify-content: flex-end; margin-top: 2rem; margin-bottom: 3rem;">
            <button type="submit" class="btn btn-primary" style="padding: 0.875rem 2rem; font-size: 1rem;">
                Submit Quiz Attempt
            </button>
        </div>
    </form>

</div>

<script>
    const TIME_LIMIT_MINUTES = ${quiz.timeLimitMinutes};
</script>
<script src="${pageContext.request.contextPath}/js/quiz.js"></script>
</body>
</html>