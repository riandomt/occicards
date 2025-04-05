package dm.occicards.controller;

import dm.occicards.model.Card;
import dm.occicards.model.Deck;
import dm.occicards.utils.AlertManager;
import dm.occicards.utils.FileManager;
import dm.occicards.utils.JsonManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DeckController {
    private Stage dialogStage;
    private MainController mainController;
    private Deck deck;
    private ObservableList<Card> temporaryCards = FXCollections.observableArrayList();

    @FXML
    private Text title;

    @FXML
    private TextField name;

    @FXML
    private TextArea description;

    @FXML
    private Button submitButton;

    @FXML
    private TableView<Card> tableView;

    @FXML
    private TableColumn<Card, String> questionColumn;

    @FXML
    private TableColumn<Card, String> answerColumn;

    @FXML
    private TableColumn<Card, Void> editColumn;

    @FXML
    private TableColumn<Card, Void> deleteColumn;

    @FXML
    public void initialize() {
        submitButton.setText("Créer le deck");

        // Configuration des colonnes pour afficher les questions et réponses
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("question"));
        answerColumn.setCellValueFactory(new PropertyValueFactory<>("answer"));

        // Configuration des colonnes "Modifier" et "Supprimer" avec des boutons
        editColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");

            {
                editButton.setOnAction(event -> {
                    Card card = getTableView().getItems().get(getIndex());
                    // Logique pour modifier la carte
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });

        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    Card card = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(card);
                    if (deck != null) {
                        deck.getCards().remove(card);
                        updateDeckFile(deck, deck);
                    } else {
                        temporaryCards.remove(card);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        // Lier le TableView à l'ObservableList
        tableView.setItems(temporaryCards);
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
            deck = new Deck(deckName, deckDescription, new ArrayList<>(temporaryCards));
            String content = "{\n" +
                    "  \"id\": \"" + deck.getIdAsString() + "\",\n" +
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

            updateDeckFile(deck, deck);
        } else {
            title = new Text("Modification du deck");
            Deck newDeck = new Deck(deckName, deckDescription, new ArrayList<>(temporaryCards));
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

        String content = fileManager.getFileContent();
        JsonManager jsonManager = new JsonManager(content);

        jsonManager.update("name", newDeck.getName());
        jsonManager.update("description", newDeck.getDescription());

        JSONObject deckJson = jsonManager.getJson().getJSONObject("deck");
        for (int i = 0; i < newDeck.getCards().size(); i++) {
            Card card = newDeck.getCards().get(i);
            JSONObject cardObject = new JSONObject();
            cardObject.put("question", card.getQuestion());
            cardObject.put("answer", card.getAnswer());
            deckJson.put(String.valueOf(i + 1), cardObject);
        }

        fileManager.writeFileContent(jsonManager.getJsonAsString());
        fileManager.rename(newDeck.getName());
    }

    public void addCardToDeck(Card card) {
        if (card.getQuestion() == null || card.getAnswer() == null) {
            System.out.println("Erreur : La carte n'a pas de question ou de réponse.");
            return;
        }

        if (deck != null) {
            deck.getCards().add(card);
            updateDeckFile(deck, deck);
        } else {
            temporaryCards.add(card);
        }
        populateTable();
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

    public void populateTable() {
        ObservableList<Card> cards = FXCollections.observableArrayList();
        if (deck != null) {
            cards.addAll(deck.getCards());
        } else {
            cards.addAll(temporaryCards);
        }
        tableView.setItems(cards);
    }

    public Text getTitle() {
        return title;
    }

    public void setTitleValue(String value) {
        if (title != null) {
            title.setText(value);
        }
    }

}
