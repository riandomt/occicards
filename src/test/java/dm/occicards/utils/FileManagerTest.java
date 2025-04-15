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
        // Use a temporary directory for testing
        testFile = tempDir.resolve("test_file.txt").toFile();
        Files.createFile(testFile.toPath());
        fileManager = new FileManager(testFile);
    }

    @AfterEach
    void tearDown() {
        // Clean up any created files after each test
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    void testCreate() {
        File newFile = tempDir.resolve("new_file.txt").toFile();
        fileManager = new FileManager(newFile);

        String result = fileManager.create("txt");
        assertNotNull(result);
        assertTrue(newFile.exists());
    }

    @Test
    void testCopy() {
        fileManager.copy();
        File copiedFile = tempDir.resolve("user_dir/test_file.txt").toFile();
        assertTrue(copiedFile.exists());
    }

    @Test
    void testCopyFileToAnotherFile() {
        File destinationFile = tempDir.resolve("destination_file.txt").toFile();
        fileManager.copyFileToAnotherFile(destinationFile);
        assertTrue(destinationFile.exists());
    }

    @Test
    void testWriteFileContent() {
        String content = "Test content";
        fileManager.writeFileContent(content);

        String fileContent = fileManager.getFileContent();
        assertEquals(content + System.lineSeparator(), fileContent);
    }

    @Test
    void testGetFileContent() {
        String content = "Test content";
        fileManager.writeFileContent(content);

        String fileContent = fileManager.getFileContent();
        assertEquals(content + System.lineSeparator(), fileContent);
    }

    @Test
    void testFetchFiles() {
        File jsonFile = tempDir.resolve("test.json").toFile();
        try {
            Files.createFile(jsonFile.toPath());
        } catch (IOException e) {
            fail("Failed to create test file: " + e.getMessage());
        }

        List<File> files = fileManager.fetchFiles();
        assertEquals(1, files.size());
        assertEquals("test.json", files.get(0).getName());
    }

    @Test
    void testDelete() {
        fileManager.delete();
        assertFalse(testFile.exists());
    }

    @Test
    void testRename() {
        fileManager.rename("new_name");
        File renamedFile = tempDir.resolve("new_name.json").toFile();
        assertTrue(renamedFile.exists());
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
