package org.example;

import org.example.DBConnection;
import org.example.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {
    public List<Item> listAll() throws SQLException {
        String sql = "SELECT item_id, item_type, name, description, price, quantity_in_stock FROM items ORDER BY item_id";
        List<Item> out = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) out.add(new Item(
                    rs.getInt("item_id"),
                    rs.getString("item_type"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("quantity_in_stock")
            ));
        }
        return out;
    }

    public int insert(Item item) throws SQLException {
        String sql = "INSERT INTO items (item_type, name, description, price, quantity_in_stock) VALUES (?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, item.getItemType());
            ps.setString(2, item.getName());
            ps.setString(3, item.getDescription());
            ps.setDouble(4, item.getPrice());
            ps.setInt(5, item.getQuantityInStock());
            ps.executeUpdate();
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) return gk.getInt(1);
            }
        }
        return -1;
    }

    public void update(Item item) throws SQLException {
        String sql = "UPDATE items SET item_type=?, name=?, description=?, price=?, quantity_in_stock=? WHERE item_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, item.getItemType());
            ps.setString(2, item.getName());
            ps.setString(3, item.getDescription());
            ps.setDouble(4, item.getPrice());
            ps.setInt(5, item.getQuantityInStock());
            ps.setInt(6, item.getItemId());
            ps.executeUpdate();
        }
    }

    public void delete(int itemId) throws SQLException {
        String sql = "DELETE FROM items WHERE item_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            ps.executeUpdate();
        }
    }

    public Item findById(int id) throws SQLException {
        String sql = "SELECT item_id, item_type, name, description, price, quantity_in_stock FROM items WHERE item_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Item(
                        rs.getInt("item_id"),
                        rs.getString("item_type"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("quantity_in_stock")
                );
            }
        }
        return null;
    }
}
