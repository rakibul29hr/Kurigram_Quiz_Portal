<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dashboard - Kurigram Quiz</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<!-- Header Navigation -->
<nav class="navbar">
    <div class="nav-brand">
         Kurigram Quiz Portal
    </div>
    <div class="nav-actions">
        <!-- History Button -->
        <a href="${pageContext.request.contextPath}/history" class="btn btn-outline">📜 View History</a>
        <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger">Logout</a>
    </div>
</nav>

<div class="container">

    <div class="card">
        <h2>Welcome back, ${sessionScope.user.name}! 👋</h2>
        <p style="color: var(--text-muted);">Track your progress and attempt available quizzes below.</p>
    </div>

    <div class="stats-grid">

        <div class="stat-card">
            <span style="color: var(--text-muted); font-size: 0.85rem; font-weight: 600;">LAST ATTEMPT</span>
            <div class="stat-value">
                <c:choose>
                    <c:when test="${not empty lastAttempt}">
                        ${lastAttempt.score} / ${lastAttempt.totalQuestions}
                    </c:when>
                    <c:otherwise>
                        N/A
                    </c:otherwise>
                </c:choose>
            </div>
            <small style="color: var(--text-muted);">
                <c:choose>
                    <c:when test="${not empty lastAttempt}">
                        ${lastAttempt.quizTitle}
                    </c:when>
                    <c:otherwise>
                        No attempts yet
                    </c:otherwise>
                </c:choose>
            </small>
        </div>


        <div class="stat-card">
            <span style="color: var(--text-muted); font-size: 0.85rem; font-weight: 600;">LAST PERCENTAGE</span>
            <div class="stat-value" style="color: var(--primary);">
                <c:choose>
                    <c:when test="${not empty lastAttempt}">
                        ${lastAttempt.percentage}%
                    </c:when>
                    <c:otherwise>
                        0%
                    </c:otherwise>
                </c:choose>
            </div>
            <small style="color: var(--text-muted);">
                <c:choose>
                    <c:when test="${not empty lastAttempt}">
                        Score percentage
                    </c:when>
                    <c:otherwise>
                        Take a quiz to see result
                    </c:otherwise>
                </c:choose>
            </small>
        </div>


        <div class="stat-card">
            <span style="color: var(--text-muted); font-size: 0.85rem; font-weight: 600;">ACCOUNT STATUS</span>
            <div class="stat-value" style="color: var(--success); font-size: 1.25rem; margin-top: 6px;">
                Active Student
            </div>
            <small style="color: var(--text-muted);">Ready to take quizzes</small>
        </div>

    </div>

    <h3 style="margin-bottom: 1rem; font-size: 1.25rem;">Available Quizzes</h3>

    <c:choose>
        <c:when test="${empty quizzes}">
            <div class="card" style="text-align: center; padding: 2.5rem; color: var(--text-muted);">
                <p style="font-size: 1rem;">No quizzes available right now. Please check back later!</p>
            </div>
        </c:when>
        <c:otherwise>
            <c:forEach var="quiz" items="${quizzes}">
                <div class="card" style="display: flex; justify-content: space-between; align-items: center; gap: 1rem;">
                    <div>
                        <h4 style="font-size: 1.1rem; margin-bottom: 0.25rem;">${quiz.title}</h4>
                        <p style="color: var(--text-muted); font-size: 0.9rem;">${quiz.description}</p>
                        <small style="color: var(--primary); font-weight: 600; margin-top: 0.5rem; display: block;">
                            ⏱️ Time Limit: ${quiz.timeLimitMinutes} Minutes
                        </small>
                    </div>
                    <a href="${pageContext.request.contextPath}/start-quiz?quizId=${quiz.id}" class="btn btn-primary" style="white-space: nowrap;">
                        Start Quiz
                    </a>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>

</div>
</body>
</html>