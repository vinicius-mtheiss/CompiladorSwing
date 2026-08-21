package br.edu.compilador.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

public class EditorPanel extends JPanel {

    private static final Font EDITOR_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 14);

    private final JTextArea editorArea;
    private final JTextArea lineNumberArea;
    private final JScrollPane scrollPane;

    public EditorPanel() {
        editorArea = new JTextArea();
        editorArea.setFont(EDITOR_FONT);
        editorArea.setTabSize(4);
        editorArea.setLineWrap(false);

        lineNumberArea = new JTextArea("1");
        lineNumberArea.setFont(EDITOR_FONT);
        lineNumberArea.setBackground(new Color(240, 240, 240));
        lineNumberArea.setForeground(Color.DARK_GRAY);
        lineNumberArea.setEditable(false);
        lineNumberArea.setFocusable(false);
        lineNumberArea.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

        scrollPane = new JScrollPane(editorArea);
        scrollPane.setRowHeaderView(lineNumberArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        editorArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                updateLineNumbers();
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                updateLineNumbers();
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                updateLineNumbers();
            }
        });

        scrollPane.getViewport().addChangeListener(event -> syncRowHeaderScroll());

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        updateLineNumbers();
    }

    public JTextArea getEditorArea() {
        return editorArea;
    }

    public void setText(String text) {
        editorArea.setText(text);
        editorArea.setCaretPosition(0);
        updateLineNumbers();
    }

    public String getText() {
        return editorArea.getText();
    }

    public void clear() {
        setText("");
    }

    private void updateLineNumbers() {
        try {
            int lineCount = editorArea.getLineWrap()
                    ? countWrappedLines()
                    : editorArea.getLineCount();
            StringBuilder builder = new StringBuilder();
            for (int line = 1; line <= lineCount; line++) {
                builder.append(line);
                if (line < lineCount) {
                    builder.append('\n');
                }
            }
            lineNumberArea.setText(builder.toString());
            syncRowHeaderScroll();
        } catch (BadLocationException exception) {
            lineNumberArea.setText("1");
        }
    }

    private int countWrappedLines() throws BadLocationException {
        int lines = 0;
        int offset = 0;
        while (offset <= editorArea.getDocument().getLength()) {
            lines++;
            offset = editorArea.getLineEndOffset(lines - 1) + 1;
        }
        return Math.max(lines, 1);
    }

    private void syncRowHeaderScroll() {
        JViewport editorViewport = scrollPane.getViewport();
        JViewport rowHeader = scrollPane.getRowHeader();
        if (rowHeader != null) {
            rowHeader.setViewPosition(editorViewport.getViewPosition());
        }
    }
}
