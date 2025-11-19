package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class ActivitySchedDialog extends JDialog {

    private final JTextField txtActivityId = new JTextField(10);
    private final JTextField txtScheduledDate = new JTextField(10); //yyyy-MM-dd
    private final JTextField txtStartTime = new JTextField(8);      //hour:min
    private final JTextField txtEndTime = new JTextField(8);

    private boolean saved = false;
    private ActivitySched sched;

    public ActivitySchedDialog(Frame owner, ActivitySched sched) {
        super(owner, true);
        setTitle(sched == null ? "Add Activity Schedule" : "Edit Activity Schedule");
        this.sched = (sched == null ? new ActivitySched() : sched);
        init();
        pack();
        setLocationRelativeTo(owner);
    }

    private void init() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;

        int row = 0;

        //activity ID
        c.gridx = 0; c.gridy = row; form.add(new JLabel("Activity ID:"), c);
        c.gridx = 1; form.add(txtActivityId, c);
        row++;

        //scheduled date
        c.gridx = 0; c.gridy = row; form.add(new JLabel("Scheduled Date (yyyy-MM-dd):"), c);
        c.gridx = 1; form.add(txtScheduledDate, c);
        row++;

        //start time
        c.gridx = 0; c.gridy = row; form.add(new JLabel("Start Time (HH:mm):"), c);
        c.gridx = 1; form.add(txtStartTime, c);
        row++;

        //end time
        c.gridx = 0; c.gridy = row; form.add(new JLabel("End Time (HH:mm):"), c);
        c.gridx = 1; form.add(txtEndTime, c);
        row++;

        // Prefill when editing
        if (sched.getSchedule_id() != null && sched.getSchedule_id() != 0) {
            txtActivityId.setText(sched.getActivity_id() != null ? sched.getActivity_id().toString() : "");
            txtScheduledDate.setText(sched.getScheduled_date() != null ? sched.getScheduled_date().toString() : "");
            txtStartTime.setText(sched.getStart_time() != null ? sched.getStart_time().toString() : "");
            txtEndTime.setText(sched.getEnd_time() != null ? sched.getEnd_time().toString() : "");
        }

        //buttons
        JPanel buttons = new JPanel();
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        buttons.add(ok);
        buttons.add(cancel);

        ok.addActionListener(this::onOk);
        cancel.addActionListener(ev -> { saved = false; dispose(); });

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(form, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);
    }

    private void onOk(ActionEvent ev) {

        //validate activity_id
        if (txtActivityId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Activity ID is required", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            sched.setActivity_id(Integer.parseInt(txtActivityId.getText().trim()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Activity ID must be a number", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //validate scheduled_date
        try {
            sched.setScheduled_date(LocalDate.parse(txtScheduledDate.getText().trim()));
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Scheduled Date must be in yyyy-MM-dd format", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean emptyStart = true;
        boolean emptyEnd = true;
        //vlidate start time
        String startText = txtStartTime.getText().trim();
        if (!startText.isEmpty()) {
            emptyStart = false;
            try {
                sched.setStart_time(LocalTime.parse(startText));
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Start Time must be in HH:MM format", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else {
            sched.setStart_time(null);
        }

        //validate end time
        String endText = txtEndTime.getText().trim();
        if (!endText.isEmpty()) {
            emptyEnd = false;
            try {
                sched.setEnd_time(LocalTime.parse(endText));
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "End Time must be in HH:MM format", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else {
            sched.setEnd_time(null);
        }

        if (emptyEnd || emptyStart) {
            JOptionPane.showMessageDialog(this, "Please fill up missing cells", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        saved = true;
        dispose();
    }

    public boolean saved() {
        return saved;
    }

    public ActivitySched getActivitySched() {
        return sched;
    }
}