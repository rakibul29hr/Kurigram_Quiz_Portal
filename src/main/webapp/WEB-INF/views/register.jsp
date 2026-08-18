<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register - Kurigram Quiz</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container" style="max-width: 400px; margin-top: 50px;">
    <div class="card">
        <h2>Student Registration</h2>
        <c:if test="${not empty error}">
            <p style="color: red;">${error}</p>
        </c:if>
        <form action="${pageContext.request.contextPath}/register" method="POST">
            <div style="margin-bottom: 15px;">
                <label>Full Name:</label>
                <input type="text" name="name" required style="width: 100%; padding: 8px;">
            </div>
            <div style="margin-bottom: 15px;">
                <label>Email:</label>
                <input type="email" name="email" required style="width: 100%; padding: 8px;">
            </div>
            <div style="margin-bottom: 15px;">
                <label>Password:</label>
                <input type="password" name="password" required style="width: 100%; padding: 8px;">
            </div>
            <button type="submit" class="btn btn-primary" style="width: 100%;">Register</button>
        </form>
        <p style="margin-top: 15px;">Already have an account? <a href="${pageContext.request.contextPath}/login">Login here</a></p>
    </div>
</div>
</body>
</html>