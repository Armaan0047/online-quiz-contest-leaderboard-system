-- Online Quiz Contest & Leaderboard System Database Schema
-- Target Database: MySQL 8.0+

DROP DATABASE IF EXISTS quiz_db;
CREATE DATABASE quiz_db;
USE quiz_db;

-- --------------------------------------------------------
-- Table Structure for `users`
-- --------------------------------------------------------
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'USER') NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- --------------------------------------------------------
-- Table Structure for `questions`
-- --------------------------------------------------------
CREATE TABLE questions (
    question_id INT AUTO_INCREMENT PRIMARY KEY,
    question_text TEXT NOT NULL,
    option_a VARCHAR(255) NOT NULL,
    option_b VARCHAR(255) NOT NULL,
    option_c VARCHAR(255) NOT NULL,
    option_d VARCHAR(255) NOT NULL,
    correct_option CHAR(1) NOT NULL
);

-- --------------------------------------------------------
-- Table Structure for `results`
-- --------------------------------------------------------
CREATE TABLE results (
    result_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    score INT NOT NULL,
    total_questions INT NOT NULL,
    percentage DOUBLE NOT NULL,
    quiz_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_results_users FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- ========================================================
-- Sample Seed Data
-- ========================================================

-- Seed Users
INSERT INTO users (username, password, full_name, role) VALUES
('Admin_Armaan', 'admin123', 'Armaan', 'ADMIN'),
('Achint', 'user123', 'Achint', 'USER'),
('Vansh', 'user123', 'Ayush', 'USER'),
('Jitwar', 'user123', 'Jitwar', 'USER'),
('BAUNNI', 'user123', 'Lakshika', 'USER');

-- Seed Sample Quiz Questions
INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, correct_option) VALUES
('Which keyword is used to declare a class in Java?', 'struct', 'class', 'interface', 'object', 'B'),
('What is the default value of a boolean variable in Java?', 'true', 'false', '0', 'null', 'B'),
('Which data structure uses LIFO (Last In First Out) principle?', 'Queue', 'Array', 'Stack', 'Linked List', 'C'),
('Which of the following is NOT a feature of OOP in Java?', 'Inheritance', 'Polymorphism', 'Pointers', 'Encapsulation', 'C'),
('Which SQL clause is used to filter records in a database query?', 'ORDER BY', 'GROUP BY', 'WHERE', 'SELECT', 'C'),
('What does JDBC stand for?', 'Java Database Connectivity', 'Java Data Binding Connection', 'Java Database Component', 'Joint Database Connection', 'A'),
('Which package contains the Java Swing components?', 'java.awt', 'javax.swing', 'java.util', 'java.io', 'B'),
('Which interface is implemented by the Thread class in Java?', 'Callable', 'Runnable', 'Serializable', 'Cloneable', 'B'),
('Which command is used to remove a table permanently from MySQL?', 'DELETE', 'REMOVE', 'DROP', 'TRUNCATE', 'C'),
('What is the size of an int data type in Java?', '2 bytes', '4 bytes', '8 bytes', 'Depends on OS', 'B');

-- Seed Sample Quiz Results for Leaderboard Demo
INSERT INTO results (user_id, score, total_questions, percentage) VALUES
(2, 9, 10, 90.0),
(3, 8, 10, 80.0),
(4, 7, 10, 70.0),
(5, 9, 10, 90.0);
