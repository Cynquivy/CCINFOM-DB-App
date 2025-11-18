package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CampAreaDAO {
    private static final String TABLE = "camp_areas";

    public List<CampArea> listAll() throws SQLException {
        return getAll();
    }

    public List<CampArea> getAll() throws SQLException {
        List<CampArea> list = new ArrayList<>();
        String sql = "SELECT area_id, area_name, capacity, available FROM " + TABLE + " ORDER BY area_name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public CampArea findById(int id) throws SQLException {
        String sql = "SELECT area_id, area_name, capacity, available FROM " + TABLE + " WHERE area_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
                return null;
            }
        }
    }

    public boolean existsByName(String name, Integer excludeId) throws SQLException {
        String sql = "SELECT area_id FROM " + TABLE + " WHERE area_name = ?";

        if (excludeId != null)
            sql += " AND area_id <> ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);

            if (excludeId != null)
                ps.setInt(2, excludeId);

            ResultSet rs = ps.executeQuery();
            return rs.next(); // true = duplicate exists
        }
    }

    public CampArea insert(CampArea area) throws SQLException {

        if (existsByName(area.getAreaName(), null)) {
            throw new SQLException("Camp area name already exists: " + area.getAreaName());
        }

        String sql = "INSERT INTO " + TABLE + " (area_name, capacity, available) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, area.getAreaName());

            if (area.getCapacity() == null)
                ps.setNull(2, Types.INTEGER);
            else
                ps.setInt(2, area.getCapacity());

            ps.setBoolean(3, area.isAvailable());
            ps.executeUpdate();

            try (ResultSet g = ps.getGeneratedKeys()) {
                if (g.next())
                    area.setAreaId(g.getInt(1));
            }
        }
        return area;
    }

    public boolean update(CampArea area) throws SQLException {

        if (existsByName(area.getAreaName(), area.getAreaId())) {
            throw new SQLException("Another camp area already uses the name: " + area.getAreaName());
        }

        String sql = "UPDATE " + TABLE + " SET area_name = ?, capacity = ?, available = ? WHERE area_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, area.getAreaName());

            if (area.getCapacity() == null)
                ps.setNull(2, Types.INTEGER);
            else
                ps.setInt(2, area.getCapacity());

            ps.setBoolean(3, area.isAvailable());
            ps.setInt(4, area.getAreaId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM " + TABLE + " WHERE area_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private CampArea mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("area_id");
        String name = rs.getString("area_name");

        int cap = rs.getInt("capacity");
        Integer capacity = rs.wasNull() ? null : cap;

        boolean available = rs.getBoolean("available");

        return new CampArea(id, name, capacity, available);
    }
}
