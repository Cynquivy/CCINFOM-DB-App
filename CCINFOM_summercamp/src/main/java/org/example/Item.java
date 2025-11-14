package org.example;

public class Item {
    private int itemId;
    private String itemType; // product or utility
    private String name;
    private String description;
    private double price;
    private int quantityInStock;

    public Item() {}
    public Item(int id, String type, String name, String desc, double price, int qty) {
        this.itemId = id; this.itemType = type; this.name = name; this.description = desc; this.price = price; this.quantityInStock = qty;
    }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getQuantityInStock() { return quantityInStock; }
    public void setQuantityInStock(int quantityInStock) { this.quantityInStock = quantityInStock; }

    @Override
    public String toString() { return itemId + " - " + name; }
}

