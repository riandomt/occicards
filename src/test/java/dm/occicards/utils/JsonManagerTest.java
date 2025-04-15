package dm.occicards.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for JsonManager.
 * This class uses JUnit 5 to test various functionalities of JsonManager.
 */
class JsonManagerTest {

    private static final String EXAMPLE_JSON = "{\"key1\": \"value1\", \"key2\": \"value2\"}";
    private JsonManager jsonManager;

    @TempDir
    Path tempDir;

    /**
     * Initializes a new JsonManager object before each test with a sample JSON.
     */
    @BeforeEach
    void setUp() {
        this.setJsonManager(EXAMPLE_JSON);
    }

    /**
     * Tests the getValue method to ensure it returns the correct value for a given key.
     */
    @Test
    void testGetValue() {
        assertEquals("value1", this.getJsonManager().getValue("key1"));
    }

    /**
     * Tests the update method to ensure it correctly updates the value of a key.
     */
    @Test
    void testUpdate() {
        this.getJsonManager().update("key1", "newValue");
        assertEquals("newValue", this.getJsonManager().getValue("key1"));
    }

    /**
     * Tests the openJson method by creating a temporary JSON file and verifying that its content is correctly loaded.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    void testOpenJson() throws IOException {
        File jsonFile = tempDir.resolve("test.json").toFile();
        Files.write(jsonFile.toPath(), EXAMPLE_JSON.getBytes());

        // Use the content read from the file to initialize JsonManager
        String fileContent = JsonManager.readFileContent(jsonFile);
        this.setJsonManager(fileContent);

        assertEquals("value1", jsonManager.getValue("key1"));
        assertEquals("value2", jsonManager.getValue("key2"));
    }

    /**
     * Tests the getJsonAsString method to ensure it returns the correct string representation of the JSON.
     */
    @Test
    void testGetJsonAsString() {
        JSONObject expectedJson = new JSONObject(EXAMPLE_JSON);
        JSONObject actualJson = new JSONObject(jsonManager.getJsonAsString());
        assertEquals(expectedJson.toString(), actualJson.toString());
    }

    /**
     * Tests the getDeckElements method to ensure it correctly extracts deck elements from the JSON.
     */
    @Test
    void testGetDeckElements() {
        String deckJson = "{\"deck\": {\"card1\": {\"name\": \"Card1\"}, \"card2\": {\"name\": \"Card2\"}}}";
        JsonManager deckManager = new JsonManager(deckJson);

        JSONArray deckElements = deckManager.getDeckElements();

        assertEquals(2, deckElements.length());
        assertEquals("Card1", deckElements.getJSONObject(0).getString("name"));
        assertEquals("Card2", deckElements.getJSONObject(1).getString("name"));
    }

    /**
     * Tests the readFileContent method to ensure it correctly reads the content of a file.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    void testReadFileContent() throws IOException {
        File tempFile = tempDir.resolve("temp.txt").toFile();
        String content = "Test content";
        Files.write(tempFile.toPath(), content.getBytes());

        assertEquals(content, JsonManager.readFileContent(tempFile));
    }

    /**
     * Returns the current instance of JsonManager used for testing.
     *
     * @return the JsonManager instance.
     */
    public JsonManager getJsonManager() {
        return jsonManager;
    }

    /**
     * Initializes a new JsonManager object with a given JSON string.
     *
     * @param jsonString the JSON string to initialize JsonManager with.
     */
    public void setJsonManager(String jsonString) {
        this.jsonManager = new JsonManager(jsonString);
    }

    /**
     * Tests the remove method to ensure it correctly removes a key from the JSON.
     */
    @Test
    void testRemove() {
        this.getJsonManager().remove("key1");
        assertNull(this.getJsonManager().getValue("key1"));
    }
}
