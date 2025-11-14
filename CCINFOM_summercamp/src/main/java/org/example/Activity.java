package org.example;

public class Activity {
    private int activityId;
    private String name;
    private int areaId;
    private int maxParticipants;

    public Activity() {}
    public Activity(int id, String name, int areaId, int max) {
        this.activityId = id; this.name = name; this.areaId = areaId; this.maxParticipants = max;
    }

    public int getActivityId() { return activityId; }
    public void setActivityId(int activityId) { this.activityId = activityId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAreaId() { return areaId; }
    public void setAreaId(int areaId) { this.areaId = areaId; }
    public int getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(int maxParticipants) { this.maxParticipants = maxParticipants; }

    @Override
    public String toString() { return activityId + " - " + name; }
}

