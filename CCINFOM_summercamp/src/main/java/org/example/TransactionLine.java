package org.example;

public class TransactionLine {
    private int lineId;
    private int itemId;
    private int quantity;
    private double unitPrice;

    public TransactionLine() {}
    public TransactionLine(int itemId, int quantity, double unitPrice) {
        this.itemId = itemId; this.quantity = quantity; this.unitPrice = unitPrice;
    }

    public int getLineId() { return lineId; }
    public void setLineId(int lineId) { this.lineId = lineId; }
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    public double getLineTotal() { return unitPrice * quantity; }
}
