package br.edu.compilador;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Mantem o look and feel padrao caso o sistema nao esteja disponivel.
            }

            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
