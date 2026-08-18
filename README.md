# 📌 Kurigram Quiz Portal

A modern, responsive Web Application designed to test and improve knowledge about Kurigram District. Built with **Java Servlets, JSP, JDBC, and MySQL**, featuring randomized question sets, real-time exam timers, performance tracking, and user attempt history.

---

## ✨ Features

- 👤 **User Authentication:** Secure registration and login for students.
- 🗺️ **Local District Identity:** Custom Kurigram District silhouette branding and modern glassmorphism UI.
- 🔀 **Randomized Quiz Engine:** Randomly selects 15 questions from a pool of 100+ questions on each quiz attempt.
- ⏱️ **Real-Time Timer:** Countdown timer with visual urgency alerts when under 2 minutes.
- 📊 **Dashboard & Performance Stats:**
  - Displays **Last Attempt Score** and **Last Percentage**.
  - Account status and available quiz listings.
- 📜 **Attempt History:** Comprehensive log of past attempts showing scores, percentages, elapsed time, and timestamps.
- 🔒 **Server-Side Validation:** All scoring and answer verification calculations are performed securely on the server.

---

## 🛠️ Tech Stack & Prerequisites

### Tech Stack
* **Backend:** Java 26, Java Servlets (Jakarta EE)
* **Frontend:** JSP (JavaServer Pages), JSTL, Modern CSS3, JavaScript (Vanilla ES6)
* **Database:** MySQL 8.0+
* **Data Access:** JDBC (Java Database Connectivity)
* **Build/Container:** Apache Tomcat 10+

### Prerequisites
Make sure you have the following installed:
* JDK 17 or higher
* Apache Tomcat 10.x
* MySQL Server & MySQL Workbench
* IDE: IntelliJ IDEA, Eclipse, or NetBeans

---

## 🗄️ Database Setup

Create the database and required tables in MySQL:

```sql
CREATE DATABASE IF NOT EXISTS kurigram_quiz_db;
USE kurigram_quiz_db;

-- 1. Users Table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Quizzes Table
CREATE TABLE quizzes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    time_limit_minutes INT DEFAULT 15,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Questions Table
CREATE TABLE questions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    quiz_id INT NOT NULL,
    question_text TEXT NOT NULL,
    category VARCHAR(50) DEFAULT 'General',
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);

-- 4. Options Table
CREATE TABLE options (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question_id INT NOT NULL,
    option_text TEXT NOT NULL,
    is_correct BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
);

-- 5. Quiz Attempts Table
CREATE TABLE quiz_attempts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    quiz_id INT NOT NULL,
    total_questions INT NOT NULL,
    correct_answers INT NOT NULL,
    wrong_answers INT NOT NULL,
    unanswered INT NOT NULL,
    score INT NOT NULL,
    percentage DOUBLE NOT NULL,
    time_taken_seconds INT NOT NULL,
    started_at TIMESTAMP NULL,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);

-- 6. Attempt Details (User Selected Answers)
CREATE TABLE attempt_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    attempt_id INT NOT NULL,
    question_id INT NOT NULL,
    selected_option_id INT DEFAULT NULL,
    FOREIGN KEY (attempt_id) REFERENCES quiz_attempts(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
);
