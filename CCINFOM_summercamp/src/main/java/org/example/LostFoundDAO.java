package org.example;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class LostFoundDAO {

    public List<LostFound> listAll() throws SQLException {
        String sql = """ 
            SELECT found_id, description, date_found, location_found, status, claimed_by_person_id, claimed_date
            FROM lost_and_found
            ORDER BY found_id
        """;

        List<LostFound> out = new ArrayList<>();

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LostFound lf = new LostFound();
                lf.setFoundId(rs.getInt("found_id"));
                lf.setDescription(rs.getString("description"));
                
                Date df = rs.getDate("date_found");
                lf.setDateFound(df != null ? df.toLocalDate() : null);

                lf.setLocationFound(rs.getString("location_found"));
                lf.setStatus(rs.getString("status"));

                int claimedBy = rs.getInt("claimed_by_person_id");
                lf.setClaimedByPersonId(rs.wasNull() ? null : claimedBy);

                Date cd = rs.getDate("claimed_date");
                lf.setClaimedDate(cd != null ? cd.toLocalDate() : null);

                out.add(lf);
            }
        }

        return out;
    }

    public int insert(LostFound lf) throws SQLException {
        String sql = """
            INSERT INTO lost_and_found
            (description, date_found, location_found, status, claimed_by_person_id, claimed_date)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, lf.getDescription());

            if (lf.getDateFound() != null)
                ps.setDate(2, Date.valueOf(lf.getDateFound()));
            else
                ps.setNull(2, Types.DATE);

            ps.setString(3, lf.getLocationFound());
            ps.setString(4, lf.getStatus());

            if (lf.getClaimedByPersonId() != null)
                ps.setInt(5, lf.getClaimedByPersonId());
            else
                ps.setNull(5, Types.INTEGER);

            if (lf.getClaimedDate() != null)
                ps.setDate(6, Date.valueOf(lf.getClaimedDate()));
            else
                ps.setNull(6, Types.DATE);

            ps.executeUpdate();

            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) return gk.getInt(1);
            }
        }

        return -1;
    }

    public void update(LostFound lf) throws SQLException {
        String sql = """
            UPDATE lost_and_found
            SET description=?, date_found=?, location_found=?, status=?,
                claimed_by_person_id=?, claimed_date=?
            WHERE found_id=?
        """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, lf.getDescription());

            if (lf.getDateFound() != null)
                ps.setDate (2, Date.valueOf(lf.getDateFound()));
            else
                ps.setNull(2, Types.DATE);

            ps.setString(3, lf.getLocationFound());
            ps.setString(4, lf.getStatus());

            if (lf.getClaimedByPersonId() != null)
                ps.setInt(5, lf.getClaimedByPersonId());
            else
                ps.setNull(5, Types.INTEGER);

            if (lf.getClaimedDate() != null)
                ps.setDate(6, Date.valueOf(lf.getClaimedDate()));
            else
                ps.setNull(6, Types.DATE);

            ps.setInt(7, lf.getFoundId());
            ps.executeUpdate();
        }
    }

    public void delete(int foundId) throws SQLException {
        String sql = "DELETE FROM lost_and_found WHERE found_id=?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, foundId);
            ps.executeUpdate();
        }
    }
}
