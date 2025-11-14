package org.example;

import org.example.Person;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PersonFormDialog extends JDialog {
    private final JTextField txtFirst = new JTextField(20);
    private final JTextField txtLast = new JTextField(20);
    private final JTextField txtEmail = new JTextField(20);
    private final JTextField txtPhone = new JTextField(15);
    private final JComboBox<String> cbType = new JComboBox<>(new String[]{"camper","employee"});
    private boolean saved = false;
    private Person person;

    public PersonFormDialog(Frame owner, Person person) {
        super(owner, true);
        setTitle(person == null ? "Add Person" : "Edit Person");
        this.person = person == null ? new Person() : person;
        init();
        pack();
        setLocationRelativeTo(owner);
    }

    private void init() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0; form.add(new JLabel("Type:"), c);
        c.gridx = 1; form.add(cbType, c);

        c.gridx = 0; c.gridy++; form.add(new JLabel("First name:"), c);
        c.gridx = 1; form.add(txtFirst, c);

        c.gridx = 0; c.gridy++; form.add(new JLabel("Last name:"), c);
        c.gridx = 1; form.add(txtLast, c);

        c.gridx = 0; c.gridy++; form.add(new JLabel("Email:"), c);
        c.gridx = 1; form.add(txtEmail, c);

        c.gridx = 0; c.gridy++; form.add(new JLabel("Phone:"), c);
        c.gridx = 1; form.add(txtPhone, c);

        if (person.getPersonId() != 0) {
            cbType.setSelectedItem(person.getPersonType());
            txtFirst.setText(person.getFirstName());
            txtLast.setText(person.getLastName());
            txtEmail.setText(person.getEmail());
            txtPhone.setText(person.getPhone());
        }

        JPanel buttons = new JPanel();
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        buttons.add(ok); buttons.add(cancel);

        ok.addActionListener(this::onOk);
        cancel.addActionListener(e -> { saved = false; dispose(); });

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(form, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);
    }

    private void onOk(ActionEvent e) {
        // Basic validation
        if (txtFirst.getText().trim().isEmpty() || txtLast.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "First and last name are required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        person.setPersonType((String) cbType.getSelectedItem());
        person.setFirstName(txtFirst.getText().trim());
        person.setLastName(txtLast.getText().trim());
        person.setEmail(txtEmail.getText().trim());
        person.setPhone(txtPhone.getText().trim());
        saved = true;
        dispose();
    }

    public boolean saved() { return saved; }
    public Person getPerson() { return person; }
}

