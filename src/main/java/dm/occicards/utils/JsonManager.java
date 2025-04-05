package dm.occicards.utils;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class JsonManager {
    private JSONObject json;

    public JsonManager(String jsonString) {
        setJson(jsonString);
    }

    public JsonManager() {
        this.json = new JSONObject();
    }

    public String getValue(String key) {
        try {
            return getJson().getString(key);
        } catch (JSONException e) {
            System.err.println("Key '" + key + "' not found in JSON object.");
            return null;
        }
    }

    public void update(String key, String newValue) {
        getJson().put(key, newValue);
        System.out.println("The value of '" + key + "' has been updated to '" + newValue + "'");
    }

    public void remove(String key) {
        if (getJson().has(key)) {
            getJson().remove(key);
            System.out.println("The key '" + key + "' has been removed.");
        } else {
            System.err.println("Key '" + key + "' not found in JSON object.");
        }
    }

    public File openJson() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("user_dir"));
        fileChooser.setTitle("Select JSON File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON files", "*.json")
        );

        boolean valid = true;
        File selectedFile = null;

        Stage fileChooserStage = new Stage();
        while (valid) {
            selectedFile = fileChooser.showOpenDialog(fileChooserStage);
            if (selectedFile == null) {
                return null;
            }
            if (selectedFile.getName().length() <= 30) {
                valid = false;
            } else {
                new AlertManager("Erreur", null,
                        "Le nom du deck ne doit pas dépasser 30 caractères.",
                        Alert.AlertType.ERROR).alert();
                return null;
            }
        }

        return selectedFile;
    }

    public JSONObject getJson() {
        return json;
    }

    private void setJson(String jsonString) {
        try {
            this.json = new JSONObject(jsonString);
        } catch (JSONException e) {
            System.err.println("Invalid JSON string: " + e.getMessage());
            this.json = new JSONObject();
        }
    }

    public String getJsonAsString() {
        return getJson().toString();
    }

    public JSONArray getDeckElements() {
        JSONArray deckElements = new JSONArray();
        JSONObject deck = getJson().optJSONObject("deck");

        if (deck != null) {
            for (String key : deck.keySet()) {
                JSONObject card = deck.optJSONObject(key);
                if (card != null) {
                    deckElements.put(card);
                }
            }
        }

        return deckElements;
    }

    public static String readFileContent(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()));
    }
}
