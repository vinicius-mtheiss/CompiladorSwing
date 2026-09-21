package br.edu.compilador.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;

import br.edu.compilador.ArquivosGals.LexicalError;
import br.edu.compilador.ArquivosGals.Lexico;
import br.edu.compilador.ArquivosGals.Token;
import br.edu.compilador.file.FileManager;
import br.edu.compilador.model.DocumentState;
import br.edu.compilador.ui.EditorPanel;
import br.edu.compilador.ui.MessagePanel;
import br.edu.compilador.ui.StatusBarPanel;
import br.edu.compilador.ui.ToolbarPanel;

public class EditorController {

    private static final String COMPILE_MESSAGE = "compilação de programas ainda não foi implementada";

    private static final String TEAM_MESSAGE = """
            Equipe de desenvolvimento:

            Vinícius Martins Theiss
            Pedro França de Carvalho
            Bernardo Henrique Rech
            """;

    private final Component parent;
    private final EditorPanel editorPanel;
    private final MessagePanel messagePanel;
    private final StatusBarPanel statusBarPanel;
    private final DocumentState documentState;
    private final FileManager fileManager;

    public EditorController(
            Component parent,
            EditorPanel editorPanel,
            MessagePanel messagePanel,
            StatusBarPanel statusBarPanel) {
        this.parent = parent;
        this.editorPanel = editorPanel;
        this.messagePanel = messagePanel;
        this.statusBarPanel = statusBarPanel;
        this.documentState = new DocumentState();
        this.fileManager = new FileManager();
    }

    public void handleToolbarAction(String actionName) {
        switch (actionName) {
            case ToolbarPanel.ACTION_NOVO -> novo();
            case ToolbarPanel.ACTION_ABRIR -> abrir();
            case ToolbarPanel.ACTION_SALVAR -> salvar();
            case ToolbarPanel.ACTION_COPIAR -> copiar();
            case ToolbarPanel.ACTION_COLAR -> colar();
            case ToolbarPanel.ACTION_RECORTAR -> recortar();
            case ToolbarPanel.ACTION_COMPILAR -> compilar();
            case ToolbarPanel.ACTION_EQUIPE -> equipe();
            default -> {
            }
        }
    }

    public void registerShortcuts(javax.swing.JComponent rootPane) {
        registerAction(rootPane, ToolbarPanel.ACTION_NOVO, KeyStroke.getKeyStroke("control N"), this::novo);
        registerAction(rootPane, ToolbarPanel.ACTION_ABRIR, KeyStroke.getKeyStroke("control O"), this::abrir);
        registerAction(rootPane, ToolbarPanel.ACTION_SALVAR, KeyStroke.getKeyStroke("control S"), this::salvar);
        registerAction(rootPane, ToolbarPanel.ACTION_COPIAR, KeyStroke.getKeyStroke("control C"), this::copiar);
        registerAction(rootPane, ToolbarPanel.ACTION_COLAR, KeyStroke.getKeyStroke("control V"), this::colar);
        registerAction(rootPane, ToolbarPanel.ACTION_RECORTAR, KeyStroke.getKeyStroke("control X"), this::recortar);
        registerAction(rootPane, ToolbarPanel.ACTION_COMPILAR, KeyStroke.getKeyStroke("F7"), this::compilar);
        registerAction(rootPane, ToolbarPanel.ACTION_EQUIPE, KeyStroke.getKeyStroke("F1"), this::equipe);
    }

    private void registerAction(
            javax.swing.JComponent rootPane,
            String actionName,
            KeyStroke keyStroke,
            Runnable action) {
        rootPane.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, actionName);
        rootPane.getActionMap().put(actionName, new AbstractAction(actionName) {
            @Override
            public void actionPerformed(ActionEvent event) {
                action.run();
            }
        });
    }

    private void novo() {
        editorPanel.clear();
        messagePanel.clearMessages();
        statusBarPanel.clearStatus();
        documentState.clear();
    }

    private void abrir() {
        Optional<FileManager.OpenFileResult> result = fileManager.openFile(parent);
        if (result.isEmpty()) {
            return;
        }

        FileManager.OpenFileResult openFileResult = result.get();
        editorPanel.setText(openFileResult.content());
        messagePanel.clearMessages();
        documentState.setCurrentFile(openFileResult.file());
        statusBarPanel.setStatusText(documentState.getStatusText());
    }

    private void salvar() {
        String content = editorPanel.getText();

        if (!documentState.hasAssociatedFile()) {
            Optional<File> savedFile = fileManager.saveAs(parent, content);
            if (savedFile.isEmpty()) {
                return;
            }

            documentState.setCurrentFile(savedFile.get());
            statusBarPanel.setStatusText(documentState.getStatusText());
            messagePanel.clearMessages();
            return;
        }

        try {
            fileManager.saveExisting(documentState.getCurrentFile(), content);
            messagePanel.clearMessages();
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(
                    parent,
                    "Nao foi possivel salvar o arquivo.",
                    "Erro ao salvar",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void copiar() {
        invokeEditorAction(DefaultEditorKit.copyAction);
    }

    private void colar() {
        invokeEditorAction(DefaultEditorKit.pasteAction);
    }

    private void recortar() {
        invokeEditorAction(DefaultEditorKit.cutAction);
    }

    private void compilar() {
        Lexico lexico = new Lexico();
        String texto = editorPanel.getText();
        lexico.setInput(texto);
        StringBuilder saida = new StringBuilder();
        saida.append(String.format("%-7s%-20s%s\n", "linha", "classe", "lexema"));

        try {
            Token t = null;

            while ((t = lexico.nextToken()) != null) {
                int linha = calcularLinha(texto, t.getPosition());

                if (t.getId() == 2) {
                    exibirErro("linha " + linha + ": " + t.getLexeme() + " palavra reservada inválida");
                    return;
                }

                saida.append(String.format("%-7s%-20s%s\n", linha, classeDoToken(t.getId()), t.getLexeme()));

            }
            messagePanel.clearMessages();
            messagePanel.setMessage(saida + "\nprograma compilado com sucesso");
        } catch (LexicalError e) { // tratamento de erros
            int linha = calcularLinha(texto, e.getPosition());
            String msg = e.getMessage();

            if (msg.equals("símbolo inválido") && e.getPosition() < texto.length())
            {
                exibirErro("linha " + linha + ": " + texto.charAt(e.getPosition()) + " " + msg);
            }
            else if (msg.equals("palavra reservada inválida"))
            {
                exibirErro("linha " + linha + ": " + extrairPalavra(texto, e.getPosition()) + " " + msg);
            }
            else
            {
                exibirErro("linha " + linha + ": " + msg);
            }
        }
    }

    private String extrairPalavra(String texto, int inicio) {
        int fim = inicio;
        while (fim < texto.length() && (Character.isLetterOrDigit(texto.charAt(fim)) || texto.charAt(fim) == '_')) {
            fim++;
        }
        return texto.substring(inicio, fim);
    }

    private String classeDoToken(int id) {

        if(id >= 10 && id <= 21)
        {
            return "palavra reservada";
        }

        if(id >= 3 && id <= 6)
        {
            return "identificador";
        }

        if(id >= 22 && id <= 39)
        {
            return "símbolo especial";
        }

        if(id == 7)
        {
            return "constante_int";
        }

        if(id == 8)
        {
            return "constante_float";
        }

        if(id == 9)
        {
            return "constante_string";
        }
        return "";
    }

    private void exibirErro(String mensagem) {
        messagePanel.clearMessages();
        messagePanel.setMessage(mensagem);
    }

    private int calcularLinha(String texto, int posicao) {
        int linha = 1;
        for (int i = 0; i < posicao && i < texto.length(); i++) {
            if (texto.charAt(i) == '\n')
            {
                linha++;
            }
        }
        return linha;
    }

    private void equipe() {
        messagePanel.clearMessages();
        messagePanel.setMessage(TEAM_MESSAGE.trim());
    }

    private void invokeEditorAction(String actionName) {
        Action action = editorPanel.getEditorArea().getActionMap().get(actionName);
        if (action != null) {
            action.actionPerformed(new ActionEvent(editorPanel.getEditorArea(), ActionEvent.ACTION_PERFORMED, actionName));
        }
    }
}
