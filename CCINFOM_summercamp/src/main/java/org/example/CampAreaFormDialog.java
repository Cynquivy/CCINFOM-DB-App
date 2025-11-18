package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class CampAreaFormDialog extends JDialog {
    private JTextField tfName;
    private JSpinner spCapacity;
    private JCheckBox cbAvailable;
    private JButton btnSave;
    private JButton btnCancel;

    private CampArea area;
    private final CampAreaDAO dao = new CampAreaDAO();
    private boolean saved = false;

    public CampAreaFormDialog(Frame owner, CampArea area) {
        super(owner, true);
        this.area = (area == null ? new CampArea() : area);
        initComponents();
        loadDataToForm();
        setTitle((this.area.getAreaId() <= 0) ? "New Camp Area" : "Edit Camp Area");
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        tfName = new JTextField(25);
        spCapacity = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        cbAvailable = new JCheckBox("Available", true);

        btnSave = new JButton("Save");
        btnCancel = new JButton("Cancel");

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);

        c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.EAST;
        p.add(new JLabel("Name:"), c);
        c.gridx = 1; c.anchor = GridBagConstraints.WEST;
        p.add(tfName, c);

        c.gridx = 0; c.gridy = 1; c.anchor = GridBagConstraints.EAST;
        p.add(new JLabel("Capacity:"), c);
        c.gridx = 1; c.anchor = GridBagConstraints.WEST;
        p.add(spCapacity, c);

        c.gridx = 0; c.gridy = 2; c.gridwidth = 2; c.anchor = GridBagConstraints.CENTER;
        p.add(cbAvailable, c);

        JPanel btns = new JPanel();
        btns.add(btnSave);
        btns.add(btnCancel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(p, BorderLayout.CENTER);
        getContentPane().add(btns, BorderLayout.SOUTH);

        btnSave.addActionListener(this::onSave);
        btnCancel.addActionListener(e -> onCancel());
    }

    private void loadDataToForm() {
        if (area.getAreaId() <= 0) {
            tfName.setText("");
            spCapacity.setValue(0);
            cbAvailable.setSelected(true);
        } else {
            tfName.setText(area.getAreaName());
            spCapacity.setValue(area.getCapacity() == null ? 0 : area.getCapacity());
            cbAvailable.setSelected(area.isAvailable());
        }
    }

    private void onSave(ActionEvent e) {
        String name = tfName.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer capacity = (Integer) spCapacity.getValue();
        boolean available = cbAvailable.isSelected();

        try {
            area.setAreaName(name);
            area.setCapacity(capacity);
            area.setAvailable(available);

            if (area.getAreaId() <= 0) {
                dao.insert(area);
            } else {
                dao.update(area);
            }

            saved = true;
            dispose();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error saving camp area: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        saved = false;
        dispose();
    }

    public boolean saved() { return saved; }
    public CampArea getCampArea() { return area; }
}
