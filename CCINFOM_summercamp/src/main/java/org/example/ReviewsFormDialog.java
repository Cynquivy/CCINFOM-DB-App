package org.example;

import org.example.Reviews;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;


public class ReviewsFormDialog extends JDialog {
    private final JTextField txtReviewID = new JTextField(6);
    private final JTextField txtCamperID = new JTextField(6); 
    private final JTextField txtCounselorID = new JTextField(6); 
    private final JSpinner spMax = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
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

        c.gridx=0; c.gridy=0; form.add(new JLabel("Review ID:"), c);
        c.gridx=1; form.add(txtReviewID, c);
        c.gridx=0; c.gridy++; form.add(new JLabel("Camper ID"), c); 
        c.gridx=1; form.add(txtCamperID, c); 
        c.gridx=0; c.gridy++; form.add(new JLabel("Counselor ID:"), c);
        c.gridx=1; form.add(txtCounselorID, c);
        c.gridx=0; c.gridy++; form.add(new JLabel("Rating:"), c);
        c.gridx=1; form.add(spMax, c);
        c.gridx=0; c.gridy++; form.add(new JLabel("Comments:"), c); 
        c.gridx=1; form.add(txtComments, c); 
        

        if (reviews.getReviewID() != 0) {
            txtCounselorID.setText(String.valueOf(reviews.getCounselorID()));
            txtComments.setText(String.valueOf(reviews.getComments()));
            spMax.setValue(reviews.getRating());
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
        if (txtReviewID.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Review ID required", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
      
        try {
            reviews.setReviewID(Integer.parseInt(txtReviewID.getText().trim().isEmpty() ? "0" : txtReviewID.getText().trim()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Review ID must be a number", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (txtCamperID.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Camper ID required", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
      
        try {
            reviews.setCamperID(Integer.parseInt(txtCamperID.getText().trim().isEmpty() ? "0" : txtCamperID.getText().trim()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Camper ID must be a number", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }


        if (txtCounselorID.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Counselor ID required", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            reviews.setCounselorID(Integer.parseInt(txtCounselorID.getText().trim().isEmpty() ? "0" : txtCounselorID.getText().trim()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Counselor ID must be a number", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }


        reviews.setRating((Integer) spMax.getValue());
        reviews.setComments(txtComments.getText());
        saved = true;
        dispose();
    }

    public boolean saved() { return saved; }
    public Reviews getReviews() { return reviews; }
}
