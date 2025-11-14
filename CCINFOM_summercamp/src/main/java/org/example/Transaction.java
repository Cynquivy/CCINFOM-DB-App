package org.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private int transactionId;
    private String transactionType; // sale, order, payment, refund
    private Integer createdBy; // person id (nullable)
    private Integer relatedCamperId;
    private LocalDateTime createdAt;
    private String notes;
    private final List<TransactionLine> lines = new ArrayList<>();

    public Transaction() {}

    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
    public Integer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy; }
    public Integer getRelatedCamperId() { return relatedCamperId; }
    public void setRelatedCamperId(Integer relatedCamperId) { this.relatedCamperId = relatedCamperId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<TransactionLine> getLines() { return lines; }

    public double computeTotal() {
        return lines.stream().mapToDouble(TransactionLine::getLineTotal).sum();
    }
}
