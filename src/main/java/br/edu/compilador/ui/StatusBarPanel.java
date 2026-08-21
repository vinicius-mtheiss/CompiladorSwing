package br.edu.compilador.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusBarPanel extends JPanel {

    private static final int HEIGHT = 25;

    private final JLabel statusLabel;

    public StatusBarPanel() {
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

        setLayout(new BorderLayout());
        add(statusLabel, BorderLayout.CENTER);
        setPreferredSize(new Dimension(0, HEIGHT));
        setMinimumSize(new Dimension(0, HEIGHT));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, HEIGHT));
    }

    public void clearStatus() {
        statusLabel.setText("");
    }

    public void setStatusText(String text) {
        statusLabel.setText(text == null || text.isEmpty() ? " " : text);
    }
}
