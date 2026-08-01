package dao;

import database.DBConnection;
import model.Result;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultDAO {

    /**
     * Saves a student's quiz attempt result to MySQL.
     * @param result Result object containing score details
     * @return true if saved, false otherwise
     */
    public boolean saveResult(Result result) {
        String sql = "INSERT INTO results (user_id, score, total_questions, percentage) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, result.getUserId());
            pstmt.setInt(2, result.getScore());
            pstmt.setInt(3, result.getTotalQuestions());
            pstmt.setDouble(4, result.getPercentage());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Gets the overall quiz leaderboard sorted by highest score and percentage.
     * @return List of Result objects with student user details
     */
    public List<Result> getLeaderboard() {
        List<Result> list = new ArrayList<>();
        String sql = "SELECT r.*, u.username, u.full_name FROM results r " +
                     "JOIN users u ON r.user_id = u.user_id " +
                     "ORDER BY r.percentage DESC, r.score DESC, r.quiz_date ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToResult(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Retrieves quiz attempts for a specific student.
     * @param userId ID of the student
     * @return List of Result objects for the user
     */
    public List<Result> getResultsByUserId(int userId) {
        List<Result> list = new ArrayList<>();
        String sql = "SELECT r.*, u.username, u.full_name FROM results r " +
                     "JOIN users u ON r.user_id = u.user_id " +
                     "WHERE r.user_id = ? " +
                     "ORDER BY r.quiz_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToResult(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Gets total number of quiz attempts recorded in MySQL.
     * @return total quiz attempts count
     */
    public int getTotalQuizzesTakenCount() {
        String sql = "SELECT COUNT(*) FROM results";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Result mapResultSetToResult(ResultSet rs) throws SQLException {
        return new Result(
                rs.getInt("result_id"),
                rs.getInt("user_id"),
                rs.getString("username"),
                rs.getString("full_name"),
                rs.getInt("score"),
                rs.getInt("total_questions"),
                rs.getDouble("percentage"),
                rs.getTimestamp("quiz_date")
        );
    }
}
