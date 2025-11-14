package org.example;

import org.example.DBConnection;
import org.example.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDAO {

    public List<Person> listAll() throws SQLException {
        String sql = "SELECT person_id, person_type, first_name, last_name, email, phone FROM persons ORDER BY person_id";
        List<Person> out = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new Person(
                        rs.getInt("person_id"),
                        rs.getString("person_type"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone")
                ));
            }
        }
        return out;
    }

    public Person findById(int id) throws SQLException {
        String sql = "SELECT person_id, person_type, first_name, last_name, email, phone FROM persons WHERE person_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Person(
                            rs.getInt("person_id"),
                            rs.getString("person_type"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("phone")
                    );
                }
            }
        }
        return null;
    }

    public int insert(Person p) throws SQLException {
        String sql = "INSERT INTO persons (person_type, first_name, last_name, email, phone) VALUES (?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getPersonType());
            ps.setString(2, p.getFirstName());
            ps.setString(3, p.getLastName());
            ps.setString(4, p.getEmail());
            ps.setString(5, p.getPhone());
            ps.executeUpdate();
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) return gk.getInt(1);
            }
        }
        return -1;
    }

    public void update(Person p) throws SQLException {
        String sql = "UPDATE persons SET person_type=?, first_name=?, last_name=?, email=?, phone=? WHERE person_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getPersonType());
            ps.setString(2, p.getFirstName());
            ps.setString(3, p.getLastName());
            ps.setString(4, p.getEmail());
            ps.setString(5, p.getPhone());
            ps.setInt(6, p.getPersonId());
            ps.executeUpdate();
        }
    }

    public void delete(int personId) throws SQLException {
        String sql = "DELETE FROM persons WHERE person_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, personId);
            ps.executeUpdate();
        }
    }
}
