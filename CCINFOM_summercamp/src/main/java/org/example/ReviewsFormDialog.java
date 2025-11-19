package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;

public class ReviewsFormDialog extends JDialog {
    private final JTextField txtPersonID = new JTextField(6);
    private final JSpinner spRating = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
    private final JTextField txtComments = new JTextField(35);
    private boolean saved = false;
    private Reviews reviews;

    public ReviewsFormDialog(Frame owner, Reviews reviews) {
        super(owner, true);
        setTitle(reviews == null ? "Add Review" : "Edit Review");
        this.reviews = reviews == null ? new Reviews() : reviews;
        init();
        pack();
        setLocationRelativeTo(owner);
    }

    private void init() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.anchor = GridBagConstraints.WEST;

        c.gridx=0; c.gridy=0; form.add(new JLabel("Person ID:"), c);
        c.gridx=1; form.add(txtPersonID, c);
        c.gridx=0; c.gridy++; form.add(new JLabel("Rating:"), c);
        c.gridx=1; form.add(spRating, c);
        c.gridx=0; c.gridy++; form.add(new JLabel("Comments:"), c);
        c.gridx=1; form.add(txtComments, c);

        if (reviews.getReviewID() != 0) {
            txtPersonID.setText(String.valueOf(reviews.getPersonID()));
            txtComments.setText(reviews.getComments());
            spRating.setValue(reviews.getRating());
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
        if (txtPersonID.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Person ID required", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            reviews.setPersonID(Integer.parseInt(txtPersonID.getText().trim()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Person ID must be a number", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        reviews.setRating((Integer) spRating.getValue());
        reviews.setComments(txtComments.getText());

        if (reviews.getReviewID() == 0) {
            reviews.setReviewDate(LocalDate.now());
        }

        saved = true;
        dispose();
    }

    public boolean saved() { return saved; }
    public Reviews getReviews() { return reviews; }
}
