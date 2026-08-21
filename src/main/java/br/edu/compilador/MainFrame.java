package br.edu.compilador;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import br.edu.compilador.controller.EditorController;
import br.edu.compilador.ui.EditorPanel;
import br.edu.compilador.ui.MessagePanel;
import br.edu.compilador.ui.StatusBarPanel;
import br.edu.compilador.ui.ToolbarPanel;

public class MainFrame extends JFrame {

    private static final int WINDOW_WIDTH = 1500;
    private static final int WINDOW_HEIGHT = 800;

    public MainFrame() {
        super("Compilador");

        EditorPanel editorPanel = new EditorPanel();
        MessagePanel messagePanel = new MessagePanel();
        StatusBarPanel statusBarPanel = new StatusBarPanel();
        EditorController controller = new EditorController(this, editorPanel, messagePanel, statusBarPanel);

        ToolbarPanel toolbarPanel = new ToolbarPanel(controller::handleToolbarAction);
        controller.registerShortcuts(getRootPane());

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                editorPanel,
                messagePanel);
        splitPane.setResizeWeight(0.7);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);

        setLayout(new BorderLayout());
        add(toolbarPanel, BorderLayout.WEST);
        add(splitPane, BorderLayout.CENTER);
        add(statusBarPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setMaximumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setResizable(false);
        setLocationRelativeTo(null);
    }
}
