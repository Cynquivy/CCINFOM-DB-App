package org.example;

import org.example.DBConnection;
import org.example.ActivityReg;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ActivityRegDAO {
    public List<ActivityReg> listAll() throws SQLException {
        String sql = "SELECT registration_id, activity_id, person_id, registered_at, status FROM activity_registration ORDER BY registration_id";
        List<ActivityReg> out = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ActivityReg reg = new ActivityReg(); 
                reg.setRegistrationId(rs.getInt("registration_id"));
                reg.setActivityId(rs.getInt("activity_id"));
                reg.setPersonId(rs.getInt("person_id"));
                Date cd = rs.getDate("registered_at");
                reg.setRegisteredAt(reg != null ? cd.toLocalDate() : null);
                reg.setStatus(rs.getString("status")); 
                out.add(reg); 
            }

        }
        return out;
    }

    public int insert(ActivityReg aReg) throws SQLException {
        String sql = "INSERT INTO activity_registration (activity_id, person_id, registered_at, status) VALUES (?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
             
            ps.setInt(1, aReg.getActivityId());
            ps.setInt(2, aReg.getPersonId());

            if (aReg.getRegisteredAt() != null)
                ps.setDate(3, Date.valueOf(aReg.getRegisteredAt()));
            else
                ps.setNull(3, Types.DATE);
            
            ps.setString(4, aReg.getStatus()); 
            ps.executeUpdate();
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) return gk.getInt(1);
            }
        }
        return -1;
    }

    public void update(ActivityReg aReg) throws SQLException {
        String sql = "UPDATE activity_registration SET activity_id=?, person_id=?, registered_at=?, status=? WHERE registration_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, aReg.getActivityId());
            ps.setInt(2, aReg.getPersonId());

            if (aReg.getRegisteredAt() != null)
                ps.setDate (3, Date.valueOf(aReg.getRegisteredAt()));
            else
                ps.setNull(3, Types.DATE);

            ps.setString(4, aReg.getStatus()); 
            ps.setInt(5, aReg.getRegistrationId());
            ps.executeUpdate();
        }
    }

    public void delete(int registrationId) throws SQLException {
        String sql = "DELETE FROM activity_registration WHERE registration_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, registrationId);
            ps.executeUpdate();
        }
    }
}
