package dm.occicards.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

    private FileManager fileManager;
    private File testFile;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        testFile = tempDir.resolve("test_file.txt").toFile();
        Files.createFile(testFile.toPath());
        fileManager = new FileManager(testFile);
    }

    @AfterEach
    void tearDown() {
        // Supprimez le fichier temporaire s'il existe
        if (testFile.exists()) {
            testFile.delete();
        }

        // Nettoyage du répertoire USER_DIR
        File userDir = fileManager.getUserDir();
        if (userDir.exists()) {
            for (File file : userDir.listFiles()) {
                file.delete();
            }
        }

        // Supprimez tous les fichiers créés dans le répertoire temporaire
        try {
            Files.walk(tempDir)
                    .sorted((a, b) -> b.compareTo(a)) // Delete directories after files
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            System.err.println("Error deleting file: " + path);
                        }
                    });
        } catch (IOException e) {
            System.err.println("Error cleaning up temporary directory: " + e.getMessage());
        }
    }

    @Test
    void testCreate() {
        fileManager = new FileManager(new File(tempDir.toFile(), "new_file"));
        String result = fileManager.create("txt");
        assertNotNull(result);
        assertTrue(new File(fileManager.getUserDir(), "new_file.txt").exists());
    }

    @Test
    void testCopy() {
        fileManager.copy();
        assertTrue(new File(fileManager.getUserDir(), "test_file.txt").exists());
    }

    @Test
    void testCopyFileToAnotherFile() {
        File destinationFile = tempDir.resolve("destination_file.txt").toFile();
        fileManager.copyFileToAnotherFile(destinationFile);
        assertTrue(destinationFile.exists());
    }

    @Test
    void testWriteFileContent() {
        fileManager.writeFileContent("Test content");
        assertEquals("Test content" + System.lineSeparator(), fileManager.getFileContent());
    }

    @Test
    void testFetchFiles() throws IOException {
        Files.createFile(tempDir.resolve("file1.json"));
        Files.createFile(tempDir.resolve("file2.txt"));

        fileManager = new FileManager(tempDir.toFile());
        List<File> jsonFiles = fileManager.fetchFiles();

        assertEquals(1, jsonFiles.size());
        assertEquals("file1.json", jsonFiles.get(0).getName());
    }

    @Test
    void testDelete() {
        fileManager.delete();
        assertFalse(testFile.exists());
    }

    @Test
    void testRename() {
        fileManager.rename("new_name");
        assertTrue(tempDir.resolve("new_name.json").toFile().exists());
    }


    @Test
    void testGetFile() {
        assertEquals(testFile, fileManager.getFile());
    }

    @Test
    void testSetFile() {
        File newFile = tempDir.resolve("new_file.txt").toFile();
        fileManager.setFile(newFile);
        assertEquals(newFile, fileManager.getFile());
    }
}
