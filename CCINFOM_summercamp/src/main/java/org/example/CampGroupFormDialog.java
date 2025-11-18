package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CampGroupFormDialog extends JDialog {

    private JTextField txtGroupCode;
    private JComboBox<String> cbCounselor;
    private JComboBox<String> cbArea;
    private JSpinner spCapacity;

    private final CampGroupDAO dao = new CampGroupDAO();
    private CampGroup campGroup;
    private boolean saved = false;

    public CampGroupFormDialog(Frame parent, CampGroup campGroup) {
        super(parent, true);
        this.campGroup = campGroup;

        setTitle(campGroup == null ? "Add Camp Group" : "Edit Camp Group");
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        add(createFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        loadDropdowns();
        loadData();
    }

    private JPanel createFormPanel() {
        JPanel p = new JPanel(new GridLayout(4, 2, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtGroupCode = new JTextField();
        cbCounselor = new JComboBox<>();
        cbArea = new JComboBox<>();
        spCapacity = new JSpinner(new SpinnerNumberModel(10, 1, 500, 1));

        p.add(new JLabel("Group Code:"));
        p.add(txtGroupCode);

        p.add(new JLabel("Counselor:"));
        p.add(cbCounselor);

        p.add(new JLabel("Camp Area:"));
        p.add(cbArea);

        p.add(new JLabel("Capacity:"));
        p.add(spCapacity);

        return p;
    }

    private JPanel createButtonPanel() {
        JPanel p = new JPanel();
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(e -> onSave());
        btnCancel.addActionListener(e -> dispose());

        p.add(btnSave);
        p.add(btnCancel);
        return p;
    }

    private void loadDropdowns() {
        try {
            cbCounselor.removeAllItems();
            cbArea.removeAllItems();

            List<String[]> counselors = dao.getCounselorList();
            List<String[]> areas = dao.getAreaList();

            for (String[] c : counselors)
                cbCounselor.addItem(c[0] + " - " + c[1]);

            for (String[] a : areas)
                cbArea.addItem(a[0] + " - " + a[1]);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading dropdowns: " + ex.getMessage());
        }
    }

    private void loadData() {
        if (campGroup != null) {
            txtGroupCode.setText(campGroup.getGroupCode());
            
            for (int i = 0; i < cbCounselor.getItemCount(); i++) {
                if (cbCounselor.getItemAt(i).startsWith(campGroup.getCounselorId() + " -")) {
                    cbCounselor.setSelectedIndex(i);
                    break;
                }
            }
            
            for (int i = 0; i < cbArea.getItemCount(); i++) {
                if (cbArea.getItemAt(i).startsWith(campGroup.getAreaId() + " -")) {
                    cbArea.setSelectedIndex(i);
                    break;
                }
            }

            spCapacity.setValue(campGroup.getCapacity() != null ? campGroup.getCapacity() : 10);
        }
    }

    private void onSave() {
        try {
            if (campGroup == null)
                campGroup = new CampGroup();

            campGroup.setGroupCode(txtGroupCode.getText().trim());
            campGroup.setCounselorId(parseId((String) cbCounselor.getSelectedItem()));
            campGroup.setAreaId(parseId((String) cbArea.getSelectedItem()));
            campGroup.setCapacity((Integer) spCapacity.getValue());

            if (campGroup.getGroupId() == null || campGroup.getGroupId() == 0) {
                dao.insert(campGroup);
            } else {
                dao.update(campGroup);
            }

            saved = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Save error: " + ex.getMessage());
        }
    }

    private Integer parseId(String comboValue) {
        if (comboValue == null) return null;
        return Integer.parseInt(comboValue.split(" - ")[0]);
    }

    public boolean saved() {
        return saved;
    }

    public CampGroup getCampGroup() {
        return campGroup;
    }
}
