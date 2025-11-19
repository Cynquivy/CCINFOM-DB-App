package org.example;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ActivitySched {
        private Integer schedule_id;
    private Integer activity_id;
    private LocalDate scheduled_date;
    private LocalTime start_time;
    private LocalTime end_time;

    public ActivitySched() {}

    public ActivitySched(Integer schedule_id, Integer activity_id, LocalDate scheduled_date, LocalTime start_time, LocalTime end_time) {
        this.schedule_id = schedule_id;
        this.activity_id = activity_id;
        this.scheduled_date = scheduled_date;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public Integer getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(Integer schedule_id) {
        this.schedule_id = schedule_id;
    }

    public Integer getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(Integer activity_id) {
        this.activity_id = activity_id;
    }

    public LocalDate getScheduled_date() {
        return scheduled_date;
    }

    public void setScheduled_date(LocalDate scheduled_date) {
        this.scheduled_date = scheduled_date;
    }

    public LocalTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalTime start_time) {
        this.start_time = start_time;
    }

    public LocalTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalTime end_time) {
        this.end_time = end_time;
    }
}
