<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Attempt History - Kurigram Quiz</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<!-- Header Navigation -->
<nav class="navbar">
    <div class="nav-brand"> Kurigram Quiz Portal</div>
    <div class="nav-actions">
        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline">⬅️ Dashboard</a>
        <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger">Logout</a>
    </div>
</nav>

<div class="container">

    <div class="card" style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
        <div>
            <h2>Attempt History</h2>
            <p style="color: var(--text-muted); font-size: 0.9rem;">Review all your past quiz performance records</p>
        </div>
        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary">+ Take New Quiz</a>
    </div>

    <div class="card" style="padding: 0; overflow: hidden;">
        <c:choose>
            <c:when test="${empty attempts}">
                <div style="padding: 3rem; text-align: center; color: var(--text-muted);">
                    <p style="font-size: 1.1rem; margin-bottom: 1rem;">No attempt records found yet.</p>
                    <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline">Start Your First Quiz</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="table-responsive">
                    <table class="data-table">
                        <thead>
                        <tr>
                            <th>Quiz Name</th>
                            <th>Score</th>
                            <th>Percentage</th>
                            <th>Time Elapsed</th>
                            <th>Attempted On</th>
                            <th style="text-align: right;">Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="att" items="${attempts}">
                            <tr>
                                <td><strong>${att.quizTitle}</strong></td>
                                <td>${att.score} / ${att.totalQuestions}</td>
                                <td>
                                            <span class="badge-score ${att.percentage < 50 ? 'low' : ''}">
                                                ${att.percentage}%
                                            </span>
                                </td>
                                <td>${att.formattedTimeTaken}</td>
                                <td style="color: var(--text-muted); font-size: 0.875rem;">${att.submittedAt}</td>
                                <td style="text-align: right;">
                                    <a href="${pageContext.request.contextPath}/result?attemptId=${att.id}" class="btn btn-outline" style="padding: 0.375rem 0.75rem; font-size: 0.8rem;">
                                        View Report
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

</div>
</body>
</html>