package org.example;

import org.example.Activity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ActivityFormDialog extends JDialog {
    private final JTextField txtName = new JTextField(25);
    private final JTextField txtAreaId = new JTextField(6);
    private final JSpinner spMax = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
    private boolean saved = false;
    private Activity activity;

    public ActivityFormDialog(Frame owner, Activity activity) {
        super(owner, true);
        setTitle(activity == null ? "Add Activity" : "Edit Activity");
        this.activity = activity == null ? new Activity() : activity;
        init();
        pack();
        setLocationRelativeTo(owner);
    }

    private void init() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.anchor = GridBagConstraints.WEST;

        c.gridx=0; c.gridy=0; form.add(new JLabel("Name:"), c);
        c.gridx=1; form.add(txtName, c);
        c.gridx=0; c.gridy++; form.add(new JLabel("Area ID:"), c);
        c.gridx=1; form.add(txtAreaId, c);
        c.gridx=0; c.gridy++; form.add(new JLabel("Max participants:"), c);
        c.gridx=1; form.add(spMax, c);

        if (activity.getActivityId() != 0) {
            txtName.setText(activity.getName());
            txtAreaId.setText(String.valueOf(activity.getAreaId()));
            spMax.setValue(activity.getMaxParticipants());
        }

        JPanel buttons = new JPanel();
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        buttons.add(ok); buttons.add(cancel);
        ok.addActionListener(this::onOk);
        cancel.addActionListener(ev -> { saved = false; dispose(); });

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(form, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);
    }

    private void onOk(ActionEvent ev) {
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name required", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        activity.setName(txtName.getText().trim());
        try {
            activity.setAreaId(Integer.parseInt(txtAreaId.getText().trim().isEmpty() ? "0" : txtAreaId.getText().trim()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Area ID must be a number", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        activity.setMaxParticipants((Integer) spMax.getValue());
        saved = true;
        dispose();
    }

    public boolean saved() { return saved; }
    public Activity getActivity() { return activity; }
}
