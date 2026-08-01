package model;

import java.sql.Timestamp;

public class Result {
    private int resultId;
    private int userId;
    private String username; // Joined from users table
    private String fullName; // Joined from users table
    private int score;
    private int totalQuestions;
    private double percentage;
    private Timestamp quizDate;

    public Result() {}

    public Result(int resultId, int userId, String username, String fullName, int score, int totalQuestions, double percentage, Timestamp quizDate) {
        this.resultId = resultId;
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.percentage = percentage;
        this.quizDate = quizDate;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public Timestamp getQuizDate() {
        return quizDate;
    }

    public void setQuizDate(Timestamp quizDate) {
        this.quizDate = quizDate;
    }
}
