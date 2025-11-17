package org.example;

import org.example.DBConnection;
import org.example.Reviews;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewsDAO{
    public List<Reviews> listAll() throws SQLException{
        String sql = "SELECT review_id, camper_id, counselor_id, rating, comments, review_date FROM reviews ORDER BY review_id";
        List<Reviews> out = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Reviews r = new Reviews(); 
                r.setReviewID(rs.getInt("review_id"));
                r.setCamperID(rs.getInt("camper_id"));
                r.setCounselorID(rs.getInt("counselor_id"));
                r.setRating(rs.getInt("rating"));
                r.setComments(rs.getString("comments")); 
                out.add(r); 
            }
        
        }
        return out; 
    }
    
    // insert review
    public int insert(Reviews r) throws SQLException{
        String insertReview = "INSERT INTO reviews (review_id, camper_id, counselor_id, rating, comments) VALUES (?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(insertReview, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, r.getReviewID());
            ps.setInt(2, r.getCamperID());
            ps.setInt(3, r.getCounselorID());
            ps.setInt(4, r.getRating());
            ps.setString(5, r.getComments()); 
            ps.executeUpdate();
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) return gk.getInt(1);
            }
        }
        return -1;
    }

    public void update(Reviews r) throws SQLException {
        String sql = "UPDATE reviews SET review_id=?, camper_id=?, counselor_id=?, rating=?, comments=?, review_date=? WHERE review_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, r.getReviewID());
            ps.setInt(2, r.getCamperID()); 
            ps.setInt(3, r.getCounselorID());
            ps.setInt(4, r.getRating());
            ps.setString(5, r.getComments());
            ps.setTimestamp(6, Timestamp.valueOf(r.getReviewDate()));
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