package org.example;

import java.time.LocalDateTime;

public class ActivityReg {
    private int registrationId; 
    private int activityId;
    private int personId;  
    private LocalDateTime registeredAt; 
    private String status; 

    public ActivityReg(){}
    public ActivityReg(int registrationId, int activityId, LocalDateTime registeredAt, String status){
        this.registrationId = registrationId; this.activityId = activityId; this.registeredAt = registeredAt; this.status = status;
    }

    public int getRegistrationId(){ return registrationId; }
    public void setRegistrationId(int registrationId){ this.registrationId = registrationId; }
    public int getActivityId(){ return activityId; }
    public void setActivityId(int activityId){ this.activityId = activityId; }
    public int getPersonId(){ return personId; }
    public void setPersonId(int personId){ this.personId = personId; }
    public LocalDateTime getRegisteredAt(){ return registeredAt; }
    public void setRegisteredAt(LocalDateTime registeredAt){ this.registeredAt = registeredAt; }
    public String getStatus(){ return status; }
    public void setStatus(String status){ this.status = status; }

    @Override
    public String toString(){
        return registrationId + "-" + activityId + "-" + personId; 
    }
}
