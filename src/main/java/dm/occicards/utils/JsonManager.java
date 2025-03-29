package dm.occicards.utils;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class JsonManager {
    private JSONObject json;

    public JsonManager(String jsonString) {
        setJson(jsonString);
    }

    public JsonManager() {

    }

    public String getValue(String key) {
        try {
            String value = getJson().getString(key);
            return value;
        } catch (JSONException e) {
            System.err.println("Key '" + key + "' not found in JSON object.");
            return null;
        }
    }

    public void update(String key, String newValue) {
        getJson().put(key, newValue);
        System.out.println("The value of '" + key + "' has been updated to '" + newValue + "'");
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
}
