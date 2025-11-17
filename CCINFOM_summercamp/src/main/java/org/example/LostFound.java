package org.example;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LostFound {
    private Integer found_id;
    private String description;
    private LocalDateTime date_found;
    private String location_found;
    private String status;
    private Integer claimed_by_person_id;
    private LocalDateTime claimed_date;

    public Integer getFoundId() { return found_id; }
    public void setFoundId(int foundId) { this.found_id = foundId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDateFound() { return date_found; }
    public void setDateFound(LocalDate dateFound) { this.date_found = date_found; }

    public String getLocationFound() { return location_found; }
    public void setLocationFound(String locationFound) { this.location_found = locationFound; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getClaimedByPersonId() { return claimed_by_person_id; }
    public void setClaimedByPersonId(Integer claimedByPersonId) { this.claimed_by_person_id = claimedByPersonId; }

    public LocalDateTime getClaimedDate() { return claimed_date; }
    public void setClaimedDate(LocalDate claimedDate) { this.claimed_date = claimed_date; }

    //check if the item is claimed
    public boolean isClaimed() {
        return "claimed".equalsIgnoreCase(status);
    }
    //mark item as returned
    public void markReturned() {
        this.status = "returned";
    }
}
