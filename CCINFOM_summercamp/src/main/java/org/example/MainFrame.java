package org.example;

import org.example.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class MainFrame extends JFrame {

    private final PersonDAO personDAO = new PersonDAO();
    private final ItemDAO itemDAO = new ItemDAO();
    private final ActivityDAO activityDAO = new ActivityDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    public MainFrame() {
        setTitle("Summer Camp DB App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Persons", createPersonPanel());
        tabs.addTab("Items", createItemPanel());
        tabs.addTab("Activities", createActivityPanel());
        tabs.addTab("Transactions", createTransactionPanel());
        add(tabs);
    }

    private JPanel createPersonPanel() {
        JPanel p = new JPanel(new BorderLayout());
        String[] cols = {"ID","Type","First","Last","Email","Phone"};
        DefaultTableModel model = new DefaultTableModel(cols,0) { public boolean isCellEditable(int r,int c){return false;}};
        JTable table = new JTable(model);
        JButton btnRefresh = new JButton("Refresh");
        JButton btnAdd = new JButton("Add");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");
        JPanel top = new JPanel();
        top.add(btnRefresh); top.add(btnAdd); top.add(btnEdit); top.add(btnDelete);
        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> {
            try {
                model.setRowCount(0);
                List<Person> all = personDAO.listAll();
                for (Person per: all) model.addRow(new Object[]{per.getPersonId(), per.getPersonType(), per.getFirstName(), per.getLastName(), per.getEmail(), per.getPhone()});
            } catch (SQLException ex) { showError(ex); }
        });

        btnAdd.addActionListener(e -> {
            PersonFormDialog dlg = new PersonFormDialog(this, null);
            dlg.setVisible(true);
            if (dlg.saved()) {
                try {
                    personDAO.insert(dlg.getPerson());
                    btnRefresh.doClick();
                } catch (SQLException ex) { showError(ex); }
            }
        });

        btnEdit.addActionListener(e -> {
            int id = getSelectedIdFromTable(table, model);
            if (id < 0) return;
            try {
                Person pObj = personDAO.findById(id);
                PersonFormDialog dlg = new PersonFormDialog(this, pObj);
                dlg.setVisible(true);
                if (dlg.saved()) {
                    personDAO.update(dlg.getPerson());
                    btnRefresh.doClick();
                }
            } catch (SQLException ex) { showError(ex); }
        });

        btnDelete.addActionListener(e -> {
            int id = getSelectedIdFromTable(table, model);
            if (id < 0) return;
            if (JOptionPane.showConfirmDialog(this,"Delete selected person?","Confirm",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION) return;
            try { personDAO.delete(id); btnRefresh.doClick(); } catch (SQLException ex) { showError(ex); }
        });

        // initial load
        btnRefresh.doClick();
        return p;
    }

    private JPanel createItemPanel() {
        JPanel p = new JPanel(new BorderLayout());
        String[] cols = {"ID","Type","Name","Price","Qty"};
        DefaultTableModel model = new DefaultTableModel(cols,0) { public boolean isCellEditable(int r,int c){return false;}};
        JTable table = new JTable(model);
        JButton btnRefresh = new JButton("Refresh");
        JButton btnAdd = new JButton("Add");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");
        JPanel top = new JPanel();
        top.add(btnRefresh); top.add(btnAdd); top.add(btnEdit); top.add(btnDelete);
        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> {
            try {
                model.setRowCount(0);
                List<Item> all = itemDAO.listAll();
                for (Item it: all) model.addRow(new Object[]{it.getItemId(), it.getItemType(), it.getName(), it.getPrice(), it.getQuantityInStock()});
            } catch (SQLException ex) { showError(ex); }
        });

        btnAdd.addActionListener(e -> {
            ItemFormDialog dlg = new ItemFormDialog(this, null);
            dlg.setVisible(true);
            if (dlg.saved()) {
                try {
                    itemDAO.insert(dlg.getItem());
                    btnRefresh.doClick();
                } catch (SQLException ex) { showError(ex); }
            }
        });

        btnEdit.addActionListener(e -> {
            int id = getSelectedIdFromTable(table, model);
            if (id < 0) return;
            try {
                Item it = itemDAO.findById(id);
                ItemFormDialog dlg = new ItemFormDialog(this, it);
                dlg.setVisible(true);
                if (dlg.saved()) {
                    itemDAO.update(dlg.getItem());
                    btnRefresh.doClick();
                }
            } catch (SQLException ex) { showError(ex); }
        });

        btnDelete.addActionListener(e -> {
            int id = getSelectedIdFromTable(table, model);
            if (id < 0) return;
            if (JOptionPane.showConfirmDialog(this,"Delete selected item?","Confirm",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION) return;
            try { itemDAO.delete(id); btnRefresh.doClick(); } catch (SQLException ex) { showError(ex); }
        });

        btnRefresh.doClick();
        return p;
    }

    private JPanel createActivityPanel() {
        JPanel p = new JPanel(new BorderLayout());
        String[] cols = {"ID","Name","Area ID","Max"};
        DefaultTableModel model = new DefaultTableModel(cols,0) { public boolean isCellEditable(int r,int c){return false;}};
        JTable table = new JTable(model);
        JButton btnRefresh = new JButton("Refresh");
        JButton btnAdd = new JButton("Add");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");
        JPanel top = new JPanel();
        top.add(btnRefresh); top.add(btnAdd); top.add(btnEdit); top.add(btnDelete);
        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> {
            try {
                model.setRowCount(0);
                List<Activity> all = activityDAO.listAll();
                for (Activity a: all) model.addRow(new Object[]{a.getActivityId(), a.getName(), a.getAreaId(), a.getMaxParticipants()});
            } catch (SQLException ex) { showError(ex); }
        });

        btnAdd.addActionListener(e -> {
            ActivityFormDialog dlg = new ActivityFormDialog(this, null);
            dlg.setVisible(true);
            if (dlg.saved()) {
                try { activityDAO.insert(dlg.getActivity()); btnRefresh.doClick(); } catch (SQLException ex) { showError(ex); }
            }
        });

        btnEdit.addActionListener(e -> {
            int id = getSelectedIdFromTable(table, model);
            if (id < 0) return;
            try {
                Activity a = activityDAO.listAll().stream().filter(x->x.getActivityId()==id).findFirst().orElse(null);
                ActivityFormDialog dlg = new ActivityFormDialog(this, a);
                dlg.setVisible(true);
                if (dlg.saved()) { activityDAO.update(dlg.getActivity()); btnRefresh.doClick(); }
            } catch (SQLException ex) { showError(ex); }
        });

        btnDelete.addActionListener(e -> {
            int id = getSelectedIdFromTable(table, model);
            if (id < 0) return;
            if (JOptionPane.showConfirmDialog(this,"Delete selected activity?","Confirm",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION) return;
            try { activityDAO.delete(id); btnRefresh.doClick(); } catch (SQLException ex) { showError(ex); }
        });

        btnRefresh.doClick();
        return p;
    }

    private JPanel createTransactionPanel() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<String> cbType = new JComboBox<>(new String[]{"sale","order","payment","refund"});
        JTextField txtNotes = new JTextField(30);
        JButton btnNew = new JButton("New Transaction");
        JButton btnRefresh = new JButton("Refresh");
        JButton btnDelete = new JButton("Delete");
        top.add(new JLabel("Type:")); top.add(cbType); top.add(new JLabel("Notes:")); top.add(txtNotes);
        top.add(btnNew); top.add(btnRefresh); top.add(btnDelete);

        String[] cols = {"ID","Type","CreatedBy","RelatedCamper","Notes"};
        DefaultTableModel model = new DefaultTableModel(cols,0) { public boolean isCellEditable(int r,int c){return false;}};
        JTable table = new JTable(model);
        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> {
            try {
                model.setRowCount(0);
                List<org.example.Transaction> all = transactionDAO.listAll();
                for (org.example.Transaction t : all) {
                    model.addRow(new Object[]{t.getTransactionId(), t.getTransactionType(), t.getCreatedBy(), t.getRelatedCamperId(), t.getNotes()});
                }
            } catch (SQLException ex) { showError(ex); }
        });

        btnNew.addActionListener(e -> {
            try {
                NewTransactionDialog dlg = new NewTransactionDialog(this, personDAO, itemDAO);
                dlg.setTransactionType((String) cbType.getSelectedItem());
                dlg.setVisible(true);
                if (dlg.saved()) {
                    org.example.Transaction tx = dlg.getTransaction();
                    // ensure tx.transactionType matches cbType (user selected)
                    tx.setTransactionType((String) cbType.getSelectedItem());
                    transactionDAO.insertTransaction(tx);
                    btnRefresh.doClick();
                }
            } catch (Exception ex) { showError(ex); }
        });

        btnDelete.addActionListener(e -> {
            int id = getSelectedIdFromTable(table, model);
            if (id < 0) return;
            if (JOptionPane.showConfirmDialog(this,"Delete selected transaction?","Confirm",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION) return;
            try { transactionDAO.deleteTransaction(id); btnRefresh.doClick(); } catch (SQLException ex) { showError(ex); }
        });

        btnRefresh.doClick();
        return p;
    }

    private void showError(Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    private int getSelectedIdFromTable(JTable table, DefaultTableModel model) {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) return -1;
        int modelRow = table.convertRowIndexToModel(viewRow);
        Object idObj = model.getValueAt(modelRow, 0);
        if (idObj == null) return -1;
        if (idObj instanceof Number) return ((Number) idObj).intValue();
        try { return Integer.parseInt(idObj.toString()); }
        catch (NumberFormatException ex) { return -1; }
    }
}

