package br.edu.compilador.model;

import java.io.File;

public class DocumentState {

    private File currentFile;

    public boolean hasAssociatedFile() {
        return currentFile != null;
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
    }

    public void clear() {
        currentFile = null;
    }

    public String getStatusText() {
        if (currentFile == null) {
            return "";
        }
        return currentFile.getAbsolutePath();
    }
}
