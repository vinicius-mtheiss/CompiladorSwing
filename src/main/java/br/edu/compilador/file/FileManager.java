package br.edu.compilador.file;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileManager {

    public record OpenFileResult(String content, File file) {
    }

    public Optional<OpenFileResult> openFile(Component parent) {
        JFileChooser chooser = createTextFileChooser();
        if (chooser.showOpenDialog(parent) != JFileChooser.APPROVE_OPTION) {
            return Optional.empty();
        }

        File selectedFile = chooser.getSelectedFile();
        if (!isTextFile(selectedFile)) {
            return Optional.empty();
        }

        try {
            String content = Files.readString(selectedFile.toPath(), StandardCharsets.UTF_8);
            return Optional.of(new OpenFileResult(content, selectedFile));
        } catch (IOException exception) {
            return Optional.empty();
        }
    }

    public Optional<File> saveAs(Component parent, String content) {
        JFileChooser chooser = createTextFileChooser();
        if (chooser.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) {
            return Optional.empty();
        }

        File selectedFile = ensureTxtExtension(chooser.getSelectedFile());
        try {
            Files.writeString(selectedFile.toPath(), content, StandardCharsets.UTF_8);
            return Optional.of(selectedFile);
        } catch (IOException exception) {
            return Optional.empty();
        }
    }

    public void saveExisting(File file, String content) throws IOException {
        Files.writeString(file.toPath(), content, StandardCharsets.UTF_8);
    }

    private JFileChooser createTextFileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Arquivos de texto (*.txt)", "txt"));
        chooser.setAcceptAllFileFilterUsed(false);
        return chooser;
    }

    private File ensureTxtExtension(File file) {
        if (file.getName().toLowerCase().endsWith(".txt")) {
            return file;
        }
        return new File(file.getParentFile(), file.getName() + ".txt");
    }

    private boolean isTextFile(File file) {
        return file.getName().toLowerCase().endsWith(".txt");
    }
}
