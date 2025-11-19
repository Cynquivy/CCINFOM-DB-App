package org.example;

import org.example.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ItemFormDialog extends JDialog {
    private final JComboBox<String> cbType = new JComboBox<>(new String[]{"product","utility"});
    private final JTextField txtName = new JTextField(25);
    private final JTextArea txtDesc = new JTextArea(4, 25);
    private final JTextField txtPrice = new JTextField(10);
    private final JSpinner spQty = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
    private boolean saved = false;
    private Item item;

    private static final int MAX_QUANTITY = 10;


    public ItemFormDialog(Frame owner, Item item) {
        super(owner, true);
        setTitle(item == null ? "Add Item" : "Edit Item");
        this.item = item == null ? new Item() : item;
        init();
        pack();
        setLocationRelativeTo(owner);
    }

    private void init() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.anchor = GridBagConstraints.WEST;

        c.gridx=0; c.gridy=0; form.add(new JLabel("Type:"), c);
        c.gridx=1; form.add(cbType, c);

        c.gridx=0; c.gridy++; form.add(new JLabel("Name:"), c);
        c.gridx=1; form.add(txtName, c);

        c.gridx=0; c.gridy++; form.add(new JLabel("Description:"), c);
        c.gridx=1; form.add(new JScrollPane(txtDesc), c);

        c.gridx=0; c.gridy++; form.add(new JLabel("Price:"), c);
        c.gridx=1; form.add(txtPrice, c);

        c.gridx=0; c.gridy++; form.add(new JLabel("Quantity:"), c);
        c.gridx=1; form.add(spQty, c);

        if (item.getItemId() != 0) {
            cbType.setSelectedItem(item.getItemType());
            txtName.setText(item.getName());
            txtDesc.setText(item.getDescription());
            txtPrice.setText(String.valueOf(item.getPrice()));
            spQty.setValue(item.getQuantityInStock());
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
        String name = txtName.getText().trim();
        String priceStr = txtPrice.getText().trim();
        
        if (name.isEmpty()) {
            showValidationError("Name is required.");
            return;
        }
        
        if (priceStr.isEmpty()) {
            showValidationError("Price is required.");
            return;
        }
        
        double price;

        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException ex) {
            showValidationError("Price must be a valid number.");
            return;
        }
        
        if (price < 0) {
            showValidationError("Price cannot be negative. Please enter a value of 0 or greater.");
            return;
        }
        
        int quantity = (Integer) spQty.getValue();
        if (quantity < 0) {
            showValidationError("Quantity cannot be negative.");
            return;
        }
        if (quantity > MAX_QUANTITY) {
            showValidationError("Quantity cannot exceed " + MAX_QUANTITY + " items.");
            return;
        }
        
        item.setItemType((String) cbType.getSelectedItem());
        item.setName(name);
        item.setDescription(txtDesc.getText().trim());
        item.setPrice(price);
        item.setQuantityInStock(quantity);
        saved = true;
        dispose();
    }

     void showValidationError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }

    public boolean saved() { return saved; }
    public Item getItem() { return item; }
}
