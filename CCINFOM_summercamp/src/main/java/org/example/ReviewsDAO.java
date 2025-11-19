package org.example;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReviewsDAO {
    public List<Reviews> listAll() throws SQLException {
        String sql = "SELECT review_id, person_id, rating, comments, review_date FROM reviews ORDER BY review_id";
        List<Reviews> out = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Reviews r = new Reviews();
                r.setReviewID(rs.getInt("review_id"));
                r.setPersonID(rs.getInt("person_id"));
                r.setRating(rs.getInt("rating"));
                r.setComments(rs.getString("comments"));
                java.sql.Date sqlDate = rs.getDate("review_date");
                if (sqlDate != null) {
                    r.setReviewDate(sqlDate.toLocalDate());
                }

                out.add(r);
            }
        }
        return out;
    }

    // insert review

    public static int insert(Reviews r) throws SQLException {
        String insertReview = "INSERT INTO reviews (person_id, rating, comments, review_date) VALUES (?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(insertReview, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, r.getPersonID());
            ps.setInt(2, r.getRating());
            ps.setString(3, r.getComments());
            
            // Changed to use LocalDate
            if (r.getReviewDate() != null) {
                ps.setDate(4, java.sql.Date.valueOf(r.getReviewDate()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            
            ps.executeUpdate();
            
            // Simplified - just return the generated ID
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) {
                    return gk.getInt(1);
                }
            }
        }
        return -1;
    }


    public void update(Reviews r) throws SQLException {
        String sql = "UPDATE reviews SET person_id=?, rating=?, comments=?, review_date=? WHERE review_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, r.getPersonID());
            ps.setInt(2, r.getRating());
            ps.setString(3, r.getComments());
            ps.setDate(4, java.sql.Date.valueOf(r.getReviewDate()));
            ps.setInt(5, r.getReviewID());
            ps.executeUpdate();
        }
    }

    public void delete(int reviewID) throws SQLException {
        String sql = "DELETE FROM reviews WHERE review_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, reviewID);
            ps.executeUpdate();
        }
    }
}
