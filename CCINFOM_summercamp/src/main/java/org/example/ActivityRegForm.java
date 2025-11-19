package org.example;

import org.example.Activity;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import java.awt.*;
import java.awt.event.ActionEvent;

public class ActivityRegForm extends JDialog {
    private final JTextField txtRegistrationId = new JTextField(6);
    private final JTextField txtActivityId = new JTextField(6); 
    private final JTextField txtPersonId = new JTextField(6);
    private final JTextField txtStatus = new JTextField(15); 
    private boolean saved = false;
    private ActivityReg activityReg;

    public ActivityRegForm(Frame owner, ActivityReg activityReg) {
        super(owner, true);
        setTitle(activityReg == null ? "Register Activity" : "Edit Registration");
        this.activityReg = activityReg == null ? new ActivityReg() : activityReg;
        init();
        pack();
        setLocationRelativeTo(owner);
    }

    private void init() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.anchor = GridBagConstraints.WEST;

        c.gridx=0; c.gridy++; form.add(new JLabel("Activity ID:"), c);
        c.gridx=1; form.add(txtActivityId, c);
        c.gridx=0; c.gridy++; form.add(new JLabel("Person ID:"), c); 
        c.gridx=1; form.add(txtPersonId, c); 
        c.gridx=0; c.gridy++; form.add(new JLabel("Status:"), c);
        c.gridx=1; form.add(txtStatus, c);

        if (activityReg.getRegistrationId() != 0) {
            txtActivityId.setText(String.valueOf(activityReg.getActivityId()));
            txtPersonId.setText(String.valueOf(activityReg.getPersonId()));
            txtStatus.setText(activityReg.getStatus() != null ? activityReg.getStatus() : "registered");  
        } else{
            txtStatus.setText("registered");
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
        if (txtActivityId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Activity ID required", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }   
        
        if (txtPersonId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Person ID required", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            activityReg.setPersonId(Integer.parseInt(txtPersonId.getText().trim().isEmpty() ? "0" : txtPersonId.getText().trim()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Person ID must be a number", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        activityReg.setStatus(txtStatus.getText().trim().isEmpty() ? "registered" : txtStatus.getText().trim());
    
        saved = true;
        dispose();
    }

    public boolean saved() { return saved; }
    public ActivityReg getActivityReg() { return activityReg; }
}
