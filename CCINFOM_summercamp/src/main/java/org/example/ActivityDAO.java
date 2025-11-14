package org.example;

import org.example.DBConnection;
import org.example.Activity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityDAO {
    public List<Activity> listAll() throws SQLException {
        String sql = "SELECT activity_id, name, area_id, max_participants FROM activities ORDER BY activity_id";
        List<Activity> out = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new Activity(
                        rs.getInt("activity_id"),
                        rs.getString("name"),
                        rs.getInt("area_id"),
                        rs.getInt("max_participants")
                ));
            }
        }
        return out;
    }

    public int insert(Activity a) throws SQLException {
        String sql = "INSERT INTO activities (name, area_id, max_participants) VALUES (?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getName());
            ps.setInt(2, a.getAreaId());
            ps.setInt(3, a.getMaxParticipants());
            ps.executeUpdate();
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) return gk.getInt(1);
            }
        }
        return -1;
    }

    public void update(Activity a) throws SQLException {
        String sql = "UPDATE activities SET name=?, area_id=?, max_participants=? WHERE activity_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getName());
            ps.setInt(2, a.getAreaId());
            ps.setInt(3, a.getMaxParticipants());
            ps.setInt(4, a.getActivityId());
            ps.executeUpdate();
        }
    }

    public void delete(int activityId) throws SQLException {
        String sql = "DELETE FROM activities WHERE activity_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, activityId);
            ps.executeUpdate();
        }
    }
}
