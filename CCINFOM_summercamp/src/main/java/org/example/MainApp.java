package org.example;

import javax.swing.SwingUtilities;
import org.example.MainFrame;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mf = new MainFrame();
            mf.setVisible(true);
        });
    }
}

