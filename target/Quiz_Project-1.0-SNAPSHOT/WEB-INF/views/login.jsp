<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login - Kurigram Quiz Portal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="auth-wrapper">
    <!-- Sidebar with Title and Kurigram District Map -->
    <div class="auth-sidebar">
        <h1>Kurigram Quiz Portal</h1>
        <p>Test and expand your knowledge of Kurigram District</p>

        <div class="map-container">
            <!-- Kurigram District Map Silhouette -->
            <svg viewBox="0 0 300 300" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M120 40 L160 20 L210 45 L240 80 L220 120 L250 160 L210 220 L170 250 L130 230 L90 260 L60 210 L80 160 L50 110 L90 70 Z"
                      fill="#0284c7" opacity="0.3" stroke="#38bdf8" stroke-width="3" stroke-dasharray="4 2"/>
                <circle cx="160" cy="130" r="6" fill="#f59e0b" />
                <text x="175" y="135" fill="#ffffff" font-size="12" font-weight="bold">Kurigram Sadar</text>
            </svg>
        </div>
    </div>

    <!-- Form Section -->
    <div class="auth-form-container">
        <div class="auth-card">
            <h2>Welcome Back</h2>
            <p style="color: var(--text-muted); margin-bottom: 1.5rem;">Please log in to continue</p>

            <c:if test="${not empty error}">
                <div style="background: #fee2e2; color: #b91c1c; padding: 0.75rem; border-radius: 8px; margin-bottom: 1rem;">
                        ${error}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/login" method="POST">
                <div class="form-group">
                    <label>Email Address</label>
                    <input type="email" name="email" class="form-control" required placeholder="student@example.com">
                </div>
                <div class="form-group">
                    <label>Password</label>
                    <input type="password" name="password" class="form-control" required placeholder="••••••••">
                </div>
                <button type="submit" class="btn btn-primary" style="width: 100%;">Sign In</button>
            </form>

            <p style="text-align: center; margin-top: 1.5rem; color: var(--text-muted);">
                Don't have an account? <a href="${pageContext.request.contextPath}/register" style="color: var(--primary); text-decoration: none; font-weight: 600;">Register</a>
            </p>
        </div>
    </div>
</div>
</body>
</html>