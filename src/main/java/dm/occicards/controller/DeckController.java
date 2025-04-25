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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeckController {
    private Stage dialogStage;
    private MainController mainController;
    private Deck deck;
    private final ObservableList<Card> temporaryCards = FXCollections.observableArrayList();
    private Card selectedCard; // To keep track of the selected card for editing

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
        submitButton.setText("Create Deck");

        questionColumn.setCellValueFactory(new PropertyValueFactory<>("question"));
        answerColumn.setCellValueFactory(new PropertyValueFactory<>("answer"));

        editColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("\uD83D\uDD8A");

            {
                editButton.setOnAction(event -> {
                    selectedCard = getTableView().getItems().get(getIndex());
                    handleEditCard(selectedCard); // Open the card view for editing
                });
                editButton.getStyleClass().add("btn-warning");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : editButton);
            }
        });

        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button(" \uD83D\uDDD1 ");

            {
                deleteButton.setOnAction(event -> {
                    Card card = getTableView().getItems().get(getIndex());
                    if (deck != null) {
                        if (new AlertManager("Supprimer une carte",
                                "", "Etes-vous sur de vouloir supprimer la carte ?",
                                Alert.AlertType.CONFIRMATION).confirm()) {
                            getTableView().getItems().remove(card);
                            deck.getCards().remove(card);
                            updateDeckFile();
                        }
                    } else {
                        temporaryCards.remove(card);
                    }
                });

                deleteButton.getStyleClass().add("btn-danger");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });

        tableView.setItems(temporaryCards);
    }

    @FXML
    private void handleSubmit() {
        String deckName = name.getText();
        String deckDescription = description.getText();

        boolean alertShown = false;

        if (deck == null) {
            if (deckName.isEmpty()) {
                new AlertManager("Erreur", null, "Entrer le nom du deck", Alert.AlertType.ERROR).alert();
                alertShown = true;
            } else if (deckName.length() > 50) {
                new AlertManager("Erreur", null, "Le nom du deck est trop long", Alert.AlertType.ERROR).alert();
                alertShown = true;
            } else if (!deckName.matches("[a-zA-Z0-9 ]+")) {
                new AlertManager("Erreur", null, "Le nom du deck ne doit pas contenir de caractères spéciaux", Alert.AlertType.ERROR).alert();
                alertShown = true;
            }
        }

        if (deckDescription.isEmpty()) {
            new AlertManager("Erreur", null, "Entrer une description du deck", Alert.AlertType.ERROR).alert();
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
            new AlertManager("Deck Creation", null, "Deck créé avec succès", Alert.AlertType.INFORMATION).alert();
        } else {
            deck.setDescription(deckDescription);
            new AlertManager("Deck Modifier", null, "Deck modifié avec succès", Alert.AlertType.INFORMATION).alert();
        }

        updateDeckFile();

        if (mainController != null) {
            mainController.populateTable();
        }
        handleCancel();
    }

    private void updateDeckFile() {
        if (deck == null) return;

        File fileDeck = new File("user_dir", deck.getName().replace(" ", "_").toLowerCase() + ".json");
        FileManager fileManager = new FileManager(fileDeck);

        JSONObject json = new JSONObject();
        json.put("name", deck.getName());
        json.put("description", deck.getDescription());

        JSONObject deckJson = new JSONObject();
        for (int i = 0; i < deck.getCards().size(); i++) {
            Card card = deck.getCards().get(i);
            JSONObject cardObject = new JSONObject();
            cardObject.put("question", card.getQuestion());
            cardObject.put("answer", card.getAnswer());
            deckJson.put(String.valueOf(i + 1), cardObject);
        }
        json.put("deck", deckJson);

        fileManager.writeFileContent(json.toString(4));
        System.out.println("Deck file overwritten successfully.");
    }

    @FXML
    private void handleAddCard() {
        openCardView(null);
    }

    private void handleEditCard(Card card) {
        openCardView(card);
    }

    private void openCardView(Card card) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/card-view.fxml"));
            VBox page = loader.load();

            Stage cardStage = new Stage();
            cardStage.setTitle(card == null ? "Ajouter une carte" : "Modifier une carte");
            cardStage.initModality(Modality.APPLICATION_MODAL);

            Scene cardScene = new Scene(page);
            String stylesheet = getClass().getResource("/style.css").toExternalForm();
            cardScene.getStylesheets().add(stylesheet);
            cardStage.setScene(cardScene);

            CardController cardController = loader.getController();
            cardController.setCardStage(cardStage);
            cardController.setDeckController(this);

            if (card != null) {
                cardController.setCardData(card);
            }

            cardStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }

    public void addCardToDeck(Card card) {
        if (card.getQuestion() == null || card.getAnswer() == null) {
            System.out.println("Erreur: La carte n'a pas de question ou de réponse");
            return;
        }

        if (deck != null) {
            deck.getCards().add(card);
            updateDeckFile();
        } else {
            temporaryCards.add(card);
        }
        populateTable();
        System.out.println("Card ajoutée : " + card.getQuestion());
    }

    public void updateCardInDeck(Card card, Card newCard) {
        if (deck != null) {
            int index = deck.getCards().indexOf(card);
            if (index >= 0) {
                deck.getCards().set(index, newCard);
                updateDeckFile();
            }
        } else {
            int index = temporaryCards.indexOf(card);
            if (index >= 0) {
                temporaryCards.set(index, newCard);
            }
        }
        populateTable();
        System.out.println("Card modifiée : " + newCard.getQuestion());
    }

    @FXML
    public void handleCancel() {
        if (dialogStage != null) {
            dialogStage.close();
        }
        temporaryCards.clear();
        if (deck != null) {
            temporaryCards.addAll(deck.getCards());
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
        if (deck == null) return;

        ObservableList<Card> cards = FXCollections.observableArrayList(deck.getCards());
        tableView.setItems(cards);
        System.out.println("Table populated with " + cards.size() + " cards.");
    }

    public void setTitleValue(String value) {
        title.setText(value);
    }

    public TextArea getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description.setText(description);
    }

    public TextField getName() {
        return name;
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public Button getSubmitButton() {
        return submitButton;
    }

    public void setSubmitButton(String buttonText) {
        this.submitButton.setText(buttonText);
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
        if (deck != null) {
            temporaryCards.setAll(deck.getCards());
            populateTable();
            name.setEditable(false);
        } else {
            name.setEditable(true);
        }
    }

    public void loadDeckFromFile(File file) {
        if (file == null || !file.exists()) {
            System.out.println("Deck file not found. Starting with an empty deck.");
            deck = new Deck("Nouveau Deck", "Description par défaut", new ArrayList<>());
            return;
        }

        FileManager fileManager = new FileManager(file);
        String fileContent = fileManager.getFileContent();

        JsonManager jsonManager = new JsonManager(fileContent);
        JSONArray deckElements = jsonManager.getDeckElements();

        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < deckElements.length(); i++) {
            JSONObject cardJson = deckElements.getJSONObject(i);
            Card card = new Card(cardJson.getString("question"), cardJson.getString("answer"));
            cards.add(card);
        }

        deck = new Deck(jsonManager.getValue("name"), jsonManager.getValue("description"), cards);
        temporaryCards.setAll(cards);
        populateTable();

        System.out.println("Deck loaded from file with " + cards.size() + " cards.");
    }
}
