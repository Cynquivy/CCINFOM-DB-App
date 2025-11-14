package org.example;

import org.example.ItemDAO;
import org.example.PersonDAO;
import org.example.Item;
import org.example.Transaction;
import org.example.TransactionLine;
import org.example.Person;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

public class NewTransactionDialog extends JDialog {
    private final PersonDAO personDAO;
    private final ItemDAO itemDAO;

    private final JComboBox<Person> cbCreatedBy = new JComboBox<>();
    private final JComboBox<Person> cbRelatedCamper = new JComboBox<>();
    private final JComboBox<Item> cbItems = new JComboBox<>();
    private final JSpinner spQty = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
    private final JTextField txtUnitPrice = new JTextField(8);
    private final DefaultTableModel linesModel = new DefaultTableModel(new String[]{"ItemId","Item","Qty","UnitPrice","LineTotal"},0) {
        public boolean isCellEditable(int r,int c){ return false; }
    };
    private final JTable tblLines = new JTable(linesModel);
    private final JTextArea txtNotes = new JTextArea(3, 30);

    private boolean saved = false;
    private Transaction transaction = new Transaction();
    private String transactionType = "sale";

    public NewTransactionDialog(Frame owner, PersonDAO personDAO, ItemDAO itemDAO) {
        super(owner, true);
        this.personDAO = personDAO;
        this.itemDAO = itemDAO;
        setTitle("New Transaction");
        init();
        pack();
        setLocationRelativeTo(owner);
        loadLookups();
    }

    public void setTransactionType(String type) {
        this.transactionType = type;
    }

    private void init() {
        JPanel top = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.anchor = GridBagConstraints.WEST;

        c.gridx=0; c.gridy=0; top.add(new JLabel("Created By (employee):"), c);
        c.gridx=1; top.add(cbCreatedBy, c);
        c.gridx=0; c.gridy++; top.add(new JLabel("Related Camper:"), c);
        c.gridx=1; top.add(cbRelatedCamper, c);

        // line entry row
        JPanel lineEntry = new JPanel();
        lineEntry.add(new JLabel("Item:"));
        lineEntry.add(cbItems);
        lineEntry.add(new JLabel("Qty:"));
        lineEntry.add(spQty);
        lineEntry.add(new JLabel("Unit Price:"));
        txtUnitPrice.setColumns(6);
        lineEntry.add(txtUnitPrice);
        JButton btnAddLine = new JButton("Add Line");
        JButton btnRemoveLine = new JButton("Remove Line");
        lineEntry.add(btnAddLine);
        lineEntry.add(btnRemoveLine);

        btnAddLine.addActionListener(this::onAddLine);
        btnRemoveLine.addActionListener(ev -> {
            int r = tblLines.getSelectedRow();
            if (r >= 0) linesModel.removeRow(r);
        });

        JPanel center = new JPanel(new BorderLayout());
        center.add(lineEntry, BorderLayout.NORTH);
        center.add(new JScrollPane(tblLines), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(new JScrollPane(txtNotes), BorderLayout.CENTER);

        JPanel actions = new JPanel();
        JButton ok = new JButton("Save");
        JButton cancel = new JButton("Cancel");
        actions.add(ok); actions.add(cancel);

        ok.addActionListener(this::onSave);
        cancel.addActionListener(ev -> { saved = false; dispose(); });

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(top, BorderLayout.NORTH);
        getContentPane().add(center, BorderLayout.CENTER);
        getContentPane().add(bottom, BorderLayout.SOUTH);
        getContentPane().add(actions, BorderLayout.PAGE_END);
    }

    private void loadLookups() {
        try {
            // populate persons
            cbCreatedBy.removeAllItems();
            cbRelatedCamper.removeAllItems();
            List<Person> all = personDAO.listAll();
            for (Person p : all) {
                // place employees in createdBy, campers in related camper (based on personType)
                if ("employee".equalsIgnoreCase(p.getPersonType())) cbCreatedBy.addItem(p);
                if ("camper".equalsIgnoreCase(p.getPersonType())) cbRelatedCamper.addItem(p);
            }

            // populate items
            cbItems.removeAllItems();
            List<Item> items = itemDAO.listAll();
            for (Item it: items) {
                cbItems.addItem(it);
            }

            // set default unit price when item changes
            cbItems.addActionListener(e -> {
                Item it = (Item) cbItems.getSelectedItem();
                if (it != null) txtUnitPrice.setText(String.valueOf(it.getPrice()));
            });
            if (cbItems.getItemCount() > 0) {
                cbItems.setSelectedIndex(0);
                txtUnitPrice.setText(String.valueOf(((Item)cbItems.getSelectedItem()).getPrice()));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load lookups: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onAddLine(ActionEvent ev) {
        Item it = (Item) cbItems.getSelectedItem();
        if (it == null) return;
        int qty = (Integer) spQty.getValue();
        double unit;
        try { unit = Double.parseDouble(txtUnitPrice.getText().trim()); } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Invalid unit price"); return; }
        double lineTotal = unit * qty;
        linesModel.addRow(new Object[]{it.getItemId(), it.getName(), qty, unit, lineTotal});
    }

    private void onSave(ActionEvent ev) {
        // basic validation: at least one line for sale/order
        if ((transactionType.equals("sale") || transactionType.equals("order")) && linesModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Add at least one line for sales/orders.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        transaction = new Transaction();
        transaction.setTransactionType(transactionType);
        Person createdBy = (Person) cbCreatedBy.getSelectedItem();
        Person camper = (Person) cbRelatedCamper.getSelectedItem();
        if (createdBy != null) transaction.setCreatedBy(createdBy.getPersonId());
        if (camper != null) transaction.setRelatedCamperId(camper.getPersonId());
        transaction.setNotes(txtNotes.getText().trim());

        // read lines
        for (int i = 0; i < linesModel.getRowCount(); i++) {
            int itemId = (int) linesModel.getValueAt(i, 0);
            int qty = (int) linesModel.getValueAt(i, 2);
            double unit = Double.parseDouble(String.valueOf(linesModel.getValueAt(i, 3)));
            TransactionLine tl = new TransactionLine(itemId, qty, unit);
            transaction.getLines().add(tl);
        }

        saved = true;
        dispose();
    }

    public boolean saved() { return saved; }
    public Transaction getTransaction() { return transaction; }
}
