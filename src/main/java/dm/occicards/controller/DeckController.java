package dm.occicards.controller;

import dm.occicards.model.Deck;
import dm.occicards.utils.AlertManager;
import dm.occicards.utils.FileManager;
import dm.occicards.utils.JsonManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class DeckController {
    private Stage dialogStage;
    private MainController mainController;
    private Deck deck;

    @FXML
    private TextField name;

    @FXML
    private TextArea description;

    @FXML
    private Button submitButton;

    @FXML
    public void initialize() {
        // Initialisation par défaut pour la création
        submitButton.setText("Créer le deck");
    }

    public void initializeFields(Deck deck) {
        this.deck = deck;
        if (deck != null) {
            name.setText(deck.getName());
            description.setText(deck.getDescription());
            submitButton.setText("Modifier le deck");
        } else {
            submitButton.setText("Créer le deck");
        }
    }

    @FXML
    public void handleSubmit() {
        String deckName = name.getText();
        String deckDescription = description.getText();
        boolean alertShown = false;

        if (deckName.isEmpty()) {
            new AlertManager("Erreur", null, "Saisissez un nom de deck", Alert.AlertType.ERROR).alert();
            alertShown = true;
        } else if (deckName.length() > 50) {
            new AlertManager("Erreur", null, "Le nom du deck est trop long", Alert.AlertType.ERROR).alert();
            alertShown = true;
        } else if (!deckName.matches("[a-zA-Z0-9 ]+")) {
            new AlertManager("Erreur", null, "Le nom du deck ne doit pas contenir de caractères spéciaux", Alert.AlertType.ERROR).alert();
            alertShown = true;
        } else if (deckDescription.isEmpty()) {
            new AlertManager("Erreur", null, "Saisissez une description du deck", Alert.AlertType.ERROR).alert();
            alertShown = true;
        } else if (deckDescription.length() > 300) {
            new AlertManager("Erreur", null, "La description du deck est trop longue", Alert.AlertType.ERROR).alert();
            alertShown = true;
        }

        if (alertShown) {
            return;
        }

        if (deck == null) {
            // Création d'un nouveau deck
            deck = new Deck(deckName, deckDescription);
            String content = "{\n" +
                    "  \"name\": \"" + deck.getName() + "\",\n" +
                    "  \"description\": \"" + deck.getDescription() + "\",\n" +
                    "  \"deck\": {}\n" +
                    "}";

            File file = new File("user_dir", deck.getName().replace(" ", "_").toLowerCase() + ".json");
            FileManager fileManager = new FileManager(file);
            fileManager.writeFileContent(content);
            new AlertManager("Création du deck", null,
                    "Deck créé avec succès",
                    Alert.AlertType.INFORMATION).alert();
        } else {
            Deck newDeck = new Deck(deckName, deckDescription);
            this.updateDeckFile(deck, newDeck);
            new AlertManager("Modification du deck", null,
                    "Deck modifié avec succès",
                    Alert.AlertType.INFORMATION).alert();
        }

        if (mainController != null) {
            mainController.populateTable();
        }
        this.handleCancel();
    }

    private void updateDeckFile(Deck deck, Deck newDeck) {
        File fileDeck = new File("user_dir", deck.getName().replace(" ", "_").toLowerCase() + ".json");
        FileManager fileManager = new FileManager(fileDeck);

        // Lire le contenu du fichier JSON
        String content = fileManager.getFileContent();
        JsonManager jsonManager = new JsonManager(content);

        // Mettre à jour les valeurs dans l'objet JSON
        jsonManager.update("name", newDeck.getName());
        jsonManager.update("description", newDeck.getDescription());

        fileManager.writeFileContent(jsonManager.getJsonAsString());

        // Renommer le fichier si le nom a changé
        fileManager.rename(newDeck.getName());
    }



    @FXML
    public void handleAddCard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/card-view.fxml"));
            VBox page = loader.load();

            Stage cardStage = new Stage();
            cardStage.setTitle("Nouvelles cartes");
            cardStage.initModality(Modality.APPLICATION_MODAL);

            Scene cardScene = new Scene(page);
            String stylesheet = getClass().getResource("/style.css").toExternalForm();
            cardScene.getStylesheets().add(stylesheet);
            cardStage.setScene(cardScene);

            CardController cardController = loader.getController();
            cardController.setCardStage(cardStage);
            cardController.setDeckController(this);
            cardStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }

    @FXML
    public void handleCancel() {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
