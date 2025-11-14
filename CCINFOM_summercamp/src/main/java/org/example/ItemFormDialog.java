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
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name required", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        item.setItemType((String) cbType.getSelectedItem());
        item.setName(txtName.getText().trim());
        item.setDescription(txtDesc.getText().trim());
        try {
            item.setPrice(Double.parseDouble(txtPrice.getText().trim().isEmpty() ? "0" : txtPrice.getText().trim()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid price", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        item.setQuantityInStock((Integer) spQty.getValue());
        saved = true;
        dispose();
    }

    public boolean saved() { return saved; }
    public Item getItem() { return item; }
}
