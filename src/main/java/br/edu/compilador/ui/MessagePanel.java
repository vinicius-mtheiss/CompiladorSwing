package br.edu.compilador.ui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import br.edu.compilador.util.ScrollPaneFactory;

public class MessagePanel extends JPanel {

    private final JTextArea messageArea;

    public MessagePanel() {
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        messageArea.setLineWrap(false);

        var scrollPane = ScrollPaneFactory.createAlwaysVisibleScrollPane(messageArea);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }

    public void clearMessages() {
        messageArea.setText("");
    }

    public void setMessage(String message) {
        messageArea.setText(message);
        messageArea.setCaretPosition(0);
    }
}
