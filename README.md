# Compilador Swing

Interface grafica de um compilador desenvolvida em Java Swing para trabalho academico.

## Requisitos

- JDK 17 ou superior
- Maven 3.9+ (opcional, para build automatizado)

## Estrutura

```text
CompiladorSwing/
├── pom.xml
├── README.md
└── src/main/java/br/edu/compilador/
    ├── Main.java
    ├── MainFrame.java
    ├── controller/EditorController.java
    ├── file/FileManager.java
    ├── model/DocumentState.java
    ├── ui/
    │   ├── EditorPanel.java
    │   ├── MessagePanel.java
    │   ├── StatusBarPanel.java
    │   └── ToolbarPanel.java
    └── util/IconFactory.java
```

## Execucao com Maven

```bash
cd CompiladorSwing
mvn compile exec:java
```

## Execucao manual

```bash
cd CompiladorSwing
javac -d target/classes -encoding UTF-8 src/main/java/br/edu/compilador/Main.java src/main/java/br/edu/compilador/MainFrame.java src/main/java/br/edu/compilador/controller/EditorController.java src/main/java/br/edu/compilador/file/FileManager.java src/main/java/br/edu/compilador/model/DocumentState.java src/main/java/br/edu/compilador/ui/EditorPanel.java src/main/java/br/edu/compilador/ui/MessagePanel.java src/main/java/br/edu/compilador/ui/StatusBarPanel.java src/main/java/br/edu/compilador/ui/ToolbarPanel.java src/main/java/br/edu/compilador/util/IconFactory.java
java -cp target/classes br.edu.compilador.Main
```

## Personalizar equipe

Edite a constante `TEAM_MESSAGE` em `EditorController.java` para substituir os nomes placeholder.
