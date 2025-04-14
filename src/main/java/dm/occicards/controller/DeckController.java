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

/**
 * Controller class for managing deck-related operations in the application.
 */
public class DeckController {
    private Stage dialogStage;
    private MainController mainController;
    private Deck deck;
    private final ObservableList<Card> temporaryCards = FXCollections.observableArrayList();
    private Card selectedCard; // To keep track of the selected card for editing

    /**
     * Text element for displaying the title.
     */
    @FXML
    private Text title;

    /**
     * TextField for entering the deck name.
     */
    @FXML
    private TextField name;

    /**
     * TextArea for entering the deck description.
     */
    @FXML
    private TextArea description;

    /**
     * Button for submitting the deck creation or modification.
     */
    @FXML
    private Button submitButton;

    /**
     * TableView for displaying the list of cards in the deck.
     */
    @FXML
    private TableView<Card> tableView;

    /**
     * TableColumn for displaying the question of each card.
     */
    @FXML
    private TableColumn<Card, String> questionColumn;

    /**
     * TableColumn for displaying the answer of each card.
     */
    @FXML
    private TableColumn<Card, String> answerColumn;

    /**
     * TableColumn for editing a card.
     */
    @FXML
    private TableColumn<Card, Void> editColumn;

    /**
     * TableColumn for deleting a card.
     */
    @FXML
    private TableColumn<Card, Void> deleteColumn;

    /**
     * Initializes the controller, sets up the table columns and buttons.
     */
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
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
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
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        tableView.setItems(temporaryCards);
    }

    /**
     * Handles the submission of the deck creation or modification form.
     * Validates the input fields and saves the deck to a file.
     */
    @FXML
    private void handleSubmit() {
        String deckName = name.getText();
        String deckDescription = description.getText();

        boolean alertShown = false;

        if (deck == null) {
            // Only validate and set the deck name if creating a new deck
            if (deckName.isEmpty()) {
                new AlertManager("Error", null, "Entrer le nom du deck", Alert.AlertType.ERROR).alert();
                alertShown = true;
            } else if (deckName.length() > 50) {
                new AlertManager("Error", null, "Le nom du deck est trop long", Alert.AlertType.ERROR).alert();
                alertShown = true;
            } else if (!deckName.matches("[a-zA-Z0-9 ]+")) {
                new AlertManager("Error", null, "Le nom du deck ne dois pas contenir " +
                        "de caractères spéciaux", Alert.AlertType.ERROR).alert();
                alertShown = true;
            }
        }

        if (deckDescription.isEmpty()) {
            new AlertManager("Error", null, "Entrer une description du deck", Alert.AlertType.ERROR).alert();
            alertShown = true;
        } else if (deckDescription.length() > 300) {
            new AlertManager("Error", null, "La description du deck est trop longue", Alert.AlertType.ERROR).alert();
            alertShown = true;
        }

        if (alertShown) {
            return;
        }

        if (deck == null) {
            deck = new Deck(deckName, deckDescription, new ArrayList<>(temporaryCards));
            String content = "{\n" +
                    "  \"name\": \"" + deck.getName() + "\",\n" +
                    "  \"description\": \"" + deck.getDescription() + "\",\n" +
                    "  \"deck\": {}\n" +
                    "}";

            File file = new File("user_dir", deck.getName().replace(" ", "_").toLowerCase() + ".json");
            FileManager fileManager = new FileManager(file);
            fileManager.writeFileContent(content);
            new AlertManager("Deck Creation", null,
                    "Deck created successfully",
                    Alert.AlertType.INFORMATION).alert();

            updateDeckFile(deck, deck);
        } else {
            // Do not update the deck name when modifying
            deck.setDescription(deckDescription);
            deck.setCards(new ArrayList<>(temporaryCards));
            updateDeckFile(deck, deck);
            new AlertManager("Deck Modification", null,
                    "Deck modified successfully",
                    Alert.AlertType.INFORMATION).alert();
        }

        if (mainController != null) {
            mainController.populateTable();
        }
        this.handleCancel();
    }

    /**
     * Updates the deck file with the new deck information.
     *
     * @param deck   The original deck.
     * @param newDeck The updated deck.
     */
    private void updateDeckFile(Deck deck, Deck newDeck) {
        File fileDeck = new File("user_dir", deck.getName().replace(" ", "_").toLowerCase() + ".json");
        FileManager fileManager = new FileManager(fileDeck);

        String content = fileManager.getFileContent();
        JsonManager jsonManager = new JsonManager(content);

        // Do not update the deck name
        jsonManager.update("description", newDeck.getDescription());

        JSONObject deckJson = jsonManager.getJson().optJSONObject("deck");
        if (deckJson == null) {
            deckJson = new JSONObject();
            jsonManager.getJson().put("deck", deckJson);
        }

        for (int i = 0; i < newDeck.getCards().size(); i++) {
            Card card = newDeck.getCards().get(i);
            JSONObject cardObject = new JSONObject();
            cardObject.put("question", card.getQuestion());
            cardObject.put("answer", card.getAnswer());
            deckJson.put(String.valueOf(i + 1), cardObject);
        }

        fileManager.writeFileContent(jsonManager.getJsonAsString());
        // Do not rename the file
    }

    /**
     * Handles the addition of a new card to the deck.
     */
    @FXML
    private void handleAddCard() {
        openCardView(null); // Open the card view for adding a new card
    }

    /**
     * Handles the editing of an existing card.
     *
     * @param card The card to be edited.
     */
    private void handleEditCard(Card card) {
        openCardView(card); // Open the card view for editing the selected card
    }

    /**
     * Opens the card view for adding or editing a card.
     *
     * @param card The card to be edited, or null if adding a new card.
     */
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
                cardController.setCardData(card); // Set the card data for editing
            }

            cardStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }

    /**
     * Adds a card to the deck.
     *
     * @param card The card to add.
     */
    public void addCardToDeck(Card card) {
        if (card.getQuestion() == null || card.getAnswer() == null) {
            System.out.println("Error: The card does not have a question or answer.");
            return;
        }

        if (deck != null) {
            deck.getCards().add(card);
            updateDeckFile(deck, deck);
        } else {
            temporaryCards.add(card);
        }
        populateTable(); // Ensure the table is updated
        System.out.println("Card added: " + card.getQuestion()); // Debugging line
    }

    /**
     * Updates a card in the deck.
     *
     * @param card The card to update.
     * @param newCard The updated card data.
     */
    public void updateCardInDeck(Card card, Card newCard) {
        if (deck != null) {
            int index = deck.getCards().indexOf(card);
            if (index >= 0) {
                deck.getCards().set(index, newCard);
                updateDeckFile(deck, deck);
            }
        } else {
            int index = temporaryCards.indexOf(card);
            if (index >= 0) {
                temporaryCards.set(index, newCard);
            }
        }
        populateTable(); // Ensure the table is updated
        System.out.println("Card updated: " + newCard.getQuestion()); // Debugging line
    }

    /**
     * Handles the cancellation of the current operation.
     * Closes the dialog stage and clears temporary cards.
     */
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

    /**
     * Gets the current dialog stage.
     *
     * @return The current dialog stage.
     */
    public Stage getDialogStage() {
        return dialogStage;
    }

    /**
     * Sets the current dialog stage.
     *
     * @param dialogStage The stage to set.
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the main controller associated with this deck controller.
     *
     * @param mainController The main controller to set.
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Populates the table view with cards from the deck or temporary cards.
     */
    public void populateTable() {
        ObservableList<Card> cards = FXCollections.observableArrayList();
        if (deck != null) {
            cards.addAll(deck.getCards());
        } else {
            cards.addAll(temporaryCards);
        }
        for (Card card : cards) {
            System.out.println("Card: " + card.getQuestion() + ", " + card.getAnswer()); // Debugging line
        }
        tableView.setItems(cards);
        System.out.println("Table populated with " + cards.size() + " cards."); // Debugging line
    }
    /**
     * Sets the value of the title text.
     *
     * @param value The value to set.
     */
    public void setTitleValue(String value) {
        title.setText(value);
    }

    /**
     * Gets the description text area.
     *
     * @return The description text area.
     */
    public TextArea getDescription() {
        return description;
    }

    /**
     * Sets the description text.
     *
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description.setText(description);
    }

    /**
     * Gets the name text field.
     *
     * @return The name text field.
     */
    public TextField getName() {
        return name;
    }

    /**
     * Sets the name text.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name.setText(name);
    }

    /**
     * Gets the submit button.
     *
     * @return The submit button.
     */
    public Button getSubmitButton() {
        return submitButton;
    }

    /**
     * Sets the text of the submit button.
     *
     * @param buttonText The text to set.
     */
    public void setSubmitButton(String buttonText) {
        this.submitButton.setText(buttonText);
    }

    /**
     * Sets the deck associated with this controller.
     *
     * @param deck The deck to set.
     */
    public void setDeck(Deck deck) {
        this.deck = deck;
        if (deck != null) {
            temporaryCards.setAll(deck.getCards());
            populateTable();
            // Disable the name field when modifying an existing deck
            name.setEditable(false);
        } else {
            // Enable the name field when creating a new deck
            name.setEditable(true);
        }
    }
}
