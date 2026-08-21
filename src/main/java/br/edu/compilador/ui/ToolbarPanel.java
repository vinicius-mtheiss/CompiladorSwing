package br.edu.compilador.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import br.edu.compilador.util.IconFactory;

public class ToolbarPanel extends JPanel {

    public static final String ACTION_NOVO = "novo";
    public static final String ACTION_ABRIR = "abrir";
    public static final String ACTION_SALVAR = "salvar";
    public static final String ACTION_COPIAR = "copiar";
    public static final String ACTION_COLAR = "colar";
    public static final String ACTION_RECORTAR = "recortar";
    public static final String ACTION_COMPILAR = "compilar";
    public static final String ACTION_EQUIPE = "equipe";

    private static final int TOOLBAR_WIDTH = 150;
    private static final Dimension BUTTON_SIZE = new Dimension(130, 72);

    public ToolbarPanel(Consumer<String> actionHandler) {
        setPreferredSize(new Dimension(TOOLBAR_WIDTH, 0));
        setLayout(new GridLayout(8, 1, 4, 4));

        addButton(ACTION_NOVO, "novo", "[ctrl-n]", IconFactory.novo(), actionHandler);
        addButton(ACTION_ABRIR, "abrir", "[ctrl-o]", IconFactory.abrir(), actionHandler);
        addButton(ACTION_SALVAR, "salvar", "[ctrl-s]", IconFactory.salvar(), actionHandler);
        addButton(ACTION_COPIAR, "copiar", "[ctrl-c]", IconFactory.copiar(), actionHandler);
        addButton(ACTION_COLAR, "colar", "[ctrl-v]", IconFactory.colar(), actionHandler);
        addButton(ACTION_RECORTAR, "recortar", "[ctrl-x]", IconFactory.recortar(), actionHandler);
        addButton(ACTION_COMPILAR, "compilar", "[F7]", IconFactory.compilar(), actionHandler);
        addButton(ACTION_EQUIPE, "equipe", "[F1]", IconFactory.equipe(), actionHandler);
    }

    private void addButton(
            String actionName,
            String label,
            String shortcut,
            javax.swing.Icon icon,
            Consumer<String> actionHandler) {
        JButton button = new JButton("<html><center>" + label + "<br>" + shortcut + "</center></html>", icon);
        button.setActionCommand(actionName);
        button.setPreferredSize(BUTTON_SIZE);
        button.setMinimumSize(BUTTON_SIZE);
        button.setMaximumSize(BUTTON_SIZE);
        button.setMargin(new Insets(4, 4, 4, 4));
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.addActionListener(event -> actionHandler.accept(actionName));
        add(button);
    }
}
