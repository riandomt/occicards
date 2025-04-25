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

    public File getUserDir() {
        return USER_DIR;
    }

    public FileManager(File file) {
        this.setFile(file);
    }

    public String create(String extension) {
        try {
            String formattedName = this.format(this.getFile().getName());
            if (!formattedName.endsWith("." + extension)) {
                formattedName += "." + extension;
            }

            if (!USER_DIR.exists() && !USER_DIR.mkdirs()) {
                throw new IOException("Failed to create directory: " + USER_DIR.getAbsolutePath());
            }

            File newFile = new File(USER_DIR, formattedName);
            if (newFile.createNewFile()) {
                this.setFile(newFile);
                return newFile.getAbsolutePath();
            }
        } catch (IOException e) {
            System.err.println("Error while creating the file: " + e.getMessage());
        }
        return null;
    }

    public void copy() {
        if (!USER_DIR.exists() && !USER_DIR.mkdirs()) {
            System.err.println("Error: Could not create user directory.");
            return;
        }

        File destinationFile = new File(USER_DIR, this.format(this.getFile().getName()));

        try {
            Files.copy(this.getFile().toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
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
        } catch (IOException e) {
            System.err.println("Error copying file: " + e.getMessage());
        }
    }

    public void writeFileContent(String content) {
        try (FileWriter writer = new FileWriter(this.getFile(), false)) {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("Writing error: " + e.getMessage());
        }
    }

    public String getFileContent() {
        StringBuilder content = new StringBuilder();
        try {
            for (String line : Files.readAllLines(this.getFile().toPath())) {
                content.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
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
        return name.replace(" ", "_").toLowerCase();
    }

    public void delete() {
        if (this.getFile().exists()) {
            try {
                Files.delete(this.getFile().toPath());
            } catch (IOException e) {
                System.err.println("Error deleting file: " + e.getMessage());
            }
        }
    }

    public File openDir(Window ownerWindow) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory");
        return directoryChooser.showDialog(ownerWindow);
    }

    public void rename(String newName) {
        if (this.getFile().exists()) {
            String formattedNewName = this.format(newName) + ".json";
            File newFile = new File(this.getFile().getParent(), formattedNewName);

            if (this.getFile().renameTo(newFile)) {
                this.setFile(newFile);
            }
        }
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
