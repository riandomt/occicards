package dm.occicards.utils;

import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private File file;
    private final static File USER_DIR = new File("user_dir");

    // Constructor that accepts a file
    public FileManager(File file) {
        this.setFile(file);
    }

    public String create(String extension) {
        if (!this.getFile().exists()) {
            try {
                // Check file extension before creating the file
                String formattedName = this.format(this.getFile().getName());
                if (!formattedName.endsWith("." + extension)) {
                    throw new IOException("File does not have the required extension: " + extension);
                }

                // Create the directory if it doesn't exist
                if (!USER_DIR.exists() && !USER_DIR.mkdirs()) {
                    throw new IOException("Failed to create directory: " + USER_DIR.getAbsolutePath());
                }

                // Create the file in the user directory
                File newFile = new File(USER_DIR, formattedName);
                if (newFile.createNewFile()) {
                    System.out.println("File successfully created: " + newFile.getAbsolutePath());

                    // Update the file object to point to the newly created file
                    this.setFile(newFile);

                    return USER_DIR.getAbsolutePath();
                }

            } catch (IOException e) {
                System.err.println("Error while creating the file: " + e.getMessage());
            }
        }
        return null;
    }

    public void copy() {
        if (!USER_DIR.exists() && !USER_DIR.mkdirs()) {
            System.err.println("Error: Could not create user directory.");
        }

        File destinationFile = new File(USER_DIR, this.format(this.getFile().getName()));

        try {
            Files.copy(this.getFile().toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied to: " + destinationFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error copying file: " + e.getMessage());
        }
    }

    public void copyFileToAnotherFile(File destinationFile) {
        if (!this.getFile().exists()) {
            System.err.println("Source file does not exist: " + this.getFile().getAbsolutePath());
            return;
        }

        if (!destinationFile.getParentFile().exists() && !destinationFile.getParentFile().mkdirs()) {
            System.err.println("Failed to create destination directory: " + destinationFile.getParentFile().getAbsolutePath());
            return;
        }

        try {
            Files.copy(this.getFile().toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File successfully copied to: " + destinationFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error copying file: " + e.getMessage());
        }
    }

    public void writeFileContent(String content) {
        try (FileWriter writer = new FileWriter(this.getFile(), false)) {
            System.out.println("Writing to file: " + this.getFile().getAbsolutePath());

            writer.write(content);
            System.out.println("Content written successfully.");
        } catch (IOException e) {
            System.err.println("Writing error: " + e.getMessage());
        }
    }

    public String getFileContent() {
        StringBuilder content = new StringBuilder();
        try {
            for (String ligne : Files.readAllLines(this.getFile().toPath())) {
                content.append(ligne).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        }
        return content.toString();
    }

    public List<File> fetchFiles() {
        List<File> jsonFiles = new ArrayList<>();
        fetchFilesRecursively(this.getFile(), jsonFiles);
        return jsonFiles;
    }

    private void fetchFilesRecursively(File directory, List<File> jsonFiles) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        fetchFilesRecursively(file, jsonFiles);
                    } else if (file.getName().endsWith(".json")) {
                        jsonFiles.add(file);
                    }
                }
            }
        }
    }

    private String format(String name) {
        String formattedName = name.replace(" ", "_").toLowerCase();

        if (formattedName.length() > 30) {
            formattedName = formattedName.substring(0, 30);
        }

        return formattedName;
    }

    public void delete() {
        if (this.getFile().exists()) {
            try {
                Files.delete(this.getFile().toPath());
                System.out.println("File successfully deleted: " + this.getFile().getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error deleting file: " + e.getMessage());
            }
        } else {
            System.err.println("File does not exist: " + this.getFile().getAbsolutePath());
        }
    }

    public File openDir(Window ownerWindow) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Sélectionner un répertoire");

        File selectedDirectory = directoryChooser.showDialog(ownerWindow);

        if (selectedDirectory != null) {
            System.out.println("Répertoire sélectionné : " + selectedDirectory.getAbsolutePath());
        } else {
            System.out.println("Aucun répertoire sélectionné.");
        }

        return selectedDirectory;
    }

    public void rename(String newName) {
        if (this.getFile().exists()) {
            String formattedNewName = this.format(newName) + ".json";
            File newFile = new File(this.getFile().getParent(), formattedNewName);

            if (this.getFile().renameTo(newFile)) {
                System.out.println("File successfully renamed to: " + newFile.getAbsolutePath());
                this.setFile(newFile); // Update the file reference
            } else {
                System.err.println("Failed to rename file.");
            }
        } else {
            System.err.println("File does not exist: " + this.getFile().getAbsolutePath());
        }
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
