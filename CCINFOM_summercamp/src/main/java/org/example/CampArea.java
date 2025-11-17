package org.example;

public class CampArea {
    private int areaId;
    private String areaName;
    private Integer capacity;
    private boolean available = true;

    public CampArea() {}

    public CampArea(int areaId, String areaName, Integer capacity, boolean available) {
        this.areaId = areaId;
        this.areaName = areaName;
        this.capacity = capacity;
        this.available = available;
    }

    public CampArea(String areaName, Integer capacity, boolean available) {
        this.areaName = areaName;
        this.capacity = capacity;
        this.available = available;
    }

    public int getAreaId() { return areaId; }
    public void setAreaId(int areaId) { this.areaId = areaId; }

    public String getAreaName() { return areaName; }
    public void setAreaName(String areaName) { this.areaName = areaName; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return areaName + (capacity != null ? " (cap: " + capacity + ")" : "");
    }
}
