package org.example;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ActivitySchedDAO {

    public List<ActivitySched> listAll() throws SQLException {
        String sql = """
            SELECT schedule_id, activity_id, scheduled_date, start_time, end_time
            FROM activity_schedule
            ORDER BY schedule_id
        """;

        List<ActivitySched> out = new ArrayList<>();

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ActivitySched as = new ActivitySched();

                as.setSchedule_id(rs.getInt("schedule_id"));
                as.setActivity_id(rs.getInt("activity_id"));

                Date date = rs.getDate("scheduled_date");
                as.setScheduled_date(date != null ? date.toLocalDate() : null);

                Time st = rs.getTime("start_time");
                as.setStart_time(st != null ? st.toLocalTime() : null);

                Time et = rs.getTime("end_time");
                as.setEnd_time(et != null ? et.toLocalTime() : null);

                out.add(as);
            }
        }

        return out;
    }

    public int insert(ActivitySched as) throws SQLException {
        String sql = """
            INSERT INTO activity_schedule
            (activity_id, scheduled_date, start_time, end_time)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, as.getActivity_id());

            if (as.getScheduled_date() != null)
                ps.setDate(2, Date.valueOf(as.getScheduled_date()));
            else
                ps.setNull(2, Types.DATE);

            if (as.getStart_time() != null)
                ps.setTime(3, Time.valueOf(as.getStart_time()));
            else
                ps.setNull(3, Types.TIME);

            if (as.getEnd_time() != null)
                ps.setTime(4, Time.valueOf(as.getEnd_time()));
            else
                ps.setNull(4, Types.TIME);

            ps.executeUpdate();

            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) return gk.getInt(1);
            }
        }

        return -1;
    }

    public void update(ActivitySched as) throws SQLException {
        String sql = """
            UPDATE activity_schedule
            SET activity_id=?, scheduled_date=?, start_time=?, end_time=?
            WHERE schedule_id=?
        """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, as.getActivity_id());

            if (as.getScheduled_date() != null)
                ps.setDate(2, Date.valueOf(as.getScheduled_date()));
            else
                ps.setNull(2, Types.DATE);

            if (as.getStart_time() != null)
                ps.setTime(3, Time.valueOf(as.getStart_time()));
            else
                ps.setNull(3, Types.TIME);

            if (as.getEnd_time() != null)
                ps.setTime(4, Time.valueOf(as.getEnd_time()));
            else
                ps.setNull(4, Types.TIME);

            ps.setInt(5, as.getSchedule_id());
            ps.executeUpdate();
        }
    }

    public void delete(int scheduleId) throws SQLException {
        String sql = "DELETE FROM activity_schedule WHERE schedule_id=?";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, scheduleId);
            ps.executeUpdate();
        }
    }
}