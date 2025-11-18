package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class LostFoundDialog extends JDialog {
    private final JTextField txtDescription = new JTextField(25);
    private final JTextField txtLocation = new JTextField(15);
    private final JComboBox<String> cbStatus = new JComboBox<>(new String[]{"found", "claimed", "returned"});
    private final JTextField txtClaimedBy = new JTextField(6);
    private final JTextField txtClaimedDate = new JTextField(10); // format: yyyy-MM-dd

    private boolean saved = false;
    private LostFound lostFound;

    public LostFoundDialog(Frame owner, LostFound lostFound) {
        super(owner, true);
        setTitle(lostFound == null ? "Add Lost & Found Item" : "Edit Lost & Found Item");
        this.lostFound = lostFound == null ? new LostFound() : lostFound;
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

        //desc
        c.gridx = 0; c.gridy = row; form.add(new JLabel("Description:"), c);
        c.gridx = 1; form.add(txtDescription, c);
        row++;

        //loc found
        c.gridx = 0; c.gridy = row; form.add(new JLabel("Location Found:"), c);
        c.gridx = 1; form.add(txtLocation, c);
        row++;

        //status
        c.gridx = 0; c.gridy = row; form.add(new JLabel("Status:"), c);
        c.gridx = 1; form.add(cbStatus, c);
        row++;

        //claimed by person id
        c.gridx = 0; c.gridy = row; form.add(new JLabel("Claimed By (Person ID):"), c);
        c.gridx = 1; form.add(txtClaimedBy, c);
        row++;

        //claim date
        c.gridx = 0; c.gridy = row; form.add(new JLabel("Claimed Date (yyyy-MM-dd):"), c);
        c.gridx = 1; form.add(txtClaimedDate, c);
        row++;

        //editing existing object
        if (lostFound.getFoundId() != null && lostFound.getFoundId() != 0) {
            txtDescription.setText(lostFound.getDescription());
            txtLocation.setText(lostFound.getLocationFound());
            cbStatus.setSelectedItem(lostFound.getStatus());
            txtClaimedBy.setText(lostFound.getClaimedByPersonId() != null ? 
                String.valueOf(lostFound.getClaimedByPersonId()) : "");
            txtClaimedDate.setText(lostFound.getClaimedDate() != null ? 
                lostFound.getClaimedDate().toString() : "");
        }

        //buttons panel
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
        if (txtDescription.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Description is required", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (txtLocation.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Location found is required", "Validation", JOptionPane.WARNING_MESSAGE);
        return;
        }

        lostFound.setDescription(txtDescription.getText().trim());
        lostFound.setLocationFound(txtLocation.getText().trim());
        lostFound.setStatus((String) cbStatus.getSelectedItem());

        boolean emptyID = true;
        boolean emptyDate = true;

        String claimedByText = txtClaimedBy.getText().trim();
        if (!claimedByText.isEmpty()) {
            emptyID = false;
            try {
                lostFound.setClaimedByPersonId(Integer.parseInt(claimedByText));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Claimed By must be a number", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else {
            lostFound.setClaimedByPersonId(null);
        }

        String claimedDateText = txtClaimedDate.getText().trim();
        if (!claimedDateText.isEmpty()) {
            emptyDate = false;
            try {
                lostFound.setClaimedDate(LocalDate.parse(claimedDateText));
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Claimed Date must be in yyyy-MM-dd format", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else {
            lostFound.setClaimedDate(null);
        }

        if (cbStatus.getSelectedItem().equals("claimed") && (emptyID || emptyDate)) {
            JOptionPane.showMessageDialog(this, "Please fill up missing cells", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        saved = true;
        dispose();
    }

    public boolean saved() {
        return saved; 
    }
    public LostFound getLostFound() {
        return lostFound;
    }
}
