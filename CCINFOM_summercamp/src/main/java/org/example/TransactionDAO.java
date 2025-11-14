package org.example;

import org.example.DBConnection;
import org.example.Transaction;
import org.example.TransactionLine;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    /**
     * Inserts a transaction header and its lines inside a DB transaction (atomic).
     * Returns generated transaction_id.
     */
    public int insertTransaction(Transaction tx) throws SQLException {
        String insertHeader = "INSERT INTO transactions (transaction_type, created_by, related_camper_id, notes) VALUES (?,?,?,?)";
        String insertLine = "INSERT INTO transaction_lines (transaction_id, item_id, quantity, unit_price) VALUES (?,?,?,?)";

        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement ph = c.prepareStatement(insertHeader, Statement.RETURN_GENERATED_KEYS)) {
                ph.setString(1, tx.getTransactionType());
                if (tx.getCreatedBy() == null) ph.setNull(2, Types.INTEGER); else ph.setInt(2, tx.getCreatedBy());
                if (tx.getRelatedCamperId() == null) ph.setNull(3, Types.INTEGER); else ph.setInt(3, tx.getRelatedCamperId());
                ph.setString(4, tx.getNotes());
                ph.executeUpdate();

                int txId;
                try (ResultSet gk = ph.getGeneratedKeys()) {
                    if (!gk.next()) throw new SQLException("Failed to get generated transaction id");
                    txId = gk.getInt(1);
                }

                try (PreparedStatement pl = c.prepareStatement(insertLine)) {
                    for (TransactionLine line : tx.getLines()) {
                        pl.setInt(1, txId);
                        pl.setInt(2, line.getItemId());
                        pl.setInt(3, line.getQuantity());
                        pl.setDouble(4, line.getUnitPrice());
                        pl.addBatch();
                    }
                    pl.executeBatch();
                }

                c.commit();
                return txId;
            } catch (Exception ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public List<Transaction> listAll() throws SQLException {
        String sql = "SELECT transaction_id, transaction_type, created_by, related_camper_id, created_at, notes FROM transactions ORDER BY created_at DESC";
        List<Transaction> out = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Transaction t = new Transaction();
                t.setTransactionId(rs.getInt("transaction_id"));
                t.setTransactionType(rs.getString("transaction_type"));
                if (rs.getObject("created_by") != null) t.setCreatedBy(rs.getInt("created_by"));
                if (rs.getObject("related_camper_id") != null) t.setRelatedCamperId(rs.getInt("related_camper_id"));
                t.setNotes(rs.getString("notes"));
                out.add(t);
            }
        }
        return out;
    }

    public void deleteTransaction(int transactionId) throws SQLException {
        // transaction_lines has ON DELETE CASCADE in DDL; otherwise delete child rows first
        String sql = "DELETE FROM transactions WHERE transaction_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, transactionId);
            ps.executeUpdate();
        }
    }

    public List<org.example.TransactionLine> listLinesForTransaction(int transactionId) throws SQLException {
        String sql = "SELECT line_id, item_id, quantity, unit_price FROM transaction_lines WHERE transaction_id = ?";
        List<org.example.TransactionLine> out = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, transactionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    org.example.TransactionLine tl = new org.example.TransactionLine();
                    tl.setLineId(rs.getInt("line_id"));
                    tl.setItemId(rs.getInt("item_id"));
                    tl.setQuantity(rs.getInt("quantity"));
                    tl.setUnitPrice(rs.getDouble("unit_price"));
                    out.add(tl);
                }
            }
        }
        return out;
    }
}

