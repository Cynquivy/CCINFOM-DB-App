package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CampGroupDAO {

    private static final String TABLE = "camp_groups";

    private Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    public List<CampGroup> listAll() throws SQLException {
        List<CampGroup> list = new ArrayList<>();
        String sql = "SELECT group_id, group_code, counselor_id, area_id, capacity FROM " + TABLE + " ORDER BY group_code";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CampGroup g = new CampGroup();
                g.setGroupId(rs.getInt("group_id"));
                g.setGroupCode(rs.getString("group_code"));
                g.setCounselorId((Integer) rs.getObject("counselor_id"));
                g.setAreaId((Integer) rs.getObject("area_id"));
                g.setCapacity((Integer) rs.getObject("capacity"));
                list.add(g);
            }
        }
        return list;
    }

    public CampGroup findById(int id) throws SQLException {
        String sql = "SELECT group_id, group_code, counselor_id, area_id, capacity FROM " + TABLE + " WHERE group_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CampGroup g = new CampGroup();
                    g.setGroupId(rs.getInt("group_id"));
                    g.setGroupCode(rs.getString("group_code"));
                    g.setCounselorId((Integer) rs.getObject("counselor_id"));
                    g.setAreaId((Integer) rs.getObject("area_id"));
                    g.setCapacity((Integer) rs.getObject("capacity"));
                    return g;
                }
            }
        }
        return null;
    }

    public void insert(CampGroup g) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (group_code, counselor_id, area_id, capacity) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, g.getGroupCode());
            if (g.getCounselorId() == null) stmt.setNull(2, Types.INTEGER);
            else stmt.setInt(2, g.getCounselorId());
            if (g.getAreaId() == null) stmt.setNull(3, Types.INTEGER);
            else stmt.setInt(3, g.getAreaId());
            if (g.getCapacity() == null) stmt.setNull(4, Types.INTEGER);
            else stmt.setInt(4, g.getCapacity());

            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) g.setGroupId(keys.getInt(1));
            }
        }
    }

    public boolean update(CampGroup g) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET group_code = ?, counselor_id = ?, area_id = ?, capacity = ? WHERE group_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, g.getGroupCode());
            if (g.getCounselorId() == null) stmt.setNull(2, Types.INTEGER);
            else stmt.setInt(2, g.getCounselorId());
            if (g.getAreaId() == null) stmt.setNull(3, Types.INTEGER);
            else stmt.setInt(3, g.getAreaId());
            if (g.getCapacity() == null) stmt.setNull(4, Types.INTEGER);
            else stmt.setInt(4, g.getCapacity());
            stmt.setInt(5, g.getGroupId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int groupId) throws SQLException {
        String sql = "DELETE FROM " + TABLE + " WHERE group_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, groupId);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<String[]> getCounselorList() throws SQLException {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT person_id, CONCAT(first_name, ' ', last_name) AS full_name "
                + "FROM persons WHERE person_type = 'employee' ORDER BY full_name";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new String[]{ rs.getString("person_id"), rs.getString("full_name") });
            }
        }
        return list;
    }

    public List<String[]> getAreaList() throws SQLException {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT area_id, area_name FROM camp_areas ORDER BY area_name";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new String[]{ rs.getString("area_id"), rs.getString("area_name") });
            }
        }
        return list;
    }
}
