package dm.occicards.controller;

import dm.occicards.model.Card;
import dm.occicards.model.Deck;
import dm.occicards.utils.AlertManager;
import dm.occicards.utils.FileManager;
import dm.occicards.utils.JsonManager;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main controller class for managing the deck operations in the application.
 */
public class MainController {
    @FXML
    private TableView<Deck> tableView;

    /**
     * TableColumn for displaying the deck name.
     */
    @FXML
    private TableColumn<Deck, String> nameColumn;

    /**
     * TableColumn for displaying the deck description.
     */
    @FXML
    private TableColumn<Deck, String> descriptionColumn;

    /**
     * TableColumn for revising a deck.
     */
    @FXML
    private TableColumn<Deck, Void> reviseColumn;

    /**
     * TableColumn for editing a deck.
     */
    @FXML
    private TableColumn<Deck, Void> editColumn;

    /**
     * TableColumn for deleting a deck.
     */
    @FXML
    private TableColumn<Deck, Void> deleteColumn;

    /**
     * TableColumn for downloading a deck.
     */
    @FXML
    private TableColumn<Deck, Void> downloadColumn;

    /**
     * Initializes the controller, sets up the table columns and buttons.
     */
    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        // Set column widths
        nameColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));
        descriptionColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));
        reviseColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.125));
        editColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.125));
        deleteColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.125));
        downloadColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.125));

        setupEditButton();
        setupDeleteButton();
        setupDownloadButton();
        setupReviseButton();

        this.populateTable();
    }

    /**
     * Sets up the revise button in the table.
     */
    private void setupReviseButton() {
        reviseColumn.setCellFactory(param -> new TableCell<>() {
            final Button reviseButton = new Button("⏵");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(reviseButton);
                    reviseButton.getStyleClass().addAll("btn-success", "crud");
                    reviseButton.setOnAction(event -> {
                        Deck deck = getTableView().getItems().get(getIndex());
                        handleRevise(deck);
                    });
                }
            }
        });
    }

    /**
     * Handles the revise action for a deck.
     *
     * @param deck The deck to revise.
     */
    private void handleRevise(Deck deck) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/revise-view.fxml"));

            Stage reviseStage = new Stage();
            reviseStage.setTitle("Revise");
            reviseStage.initModality(Modality.APPLICATION_MODAL);

            Scene reviseScene = new Scene(loader.load());
            String stylesheet = getClass().getResource("/style.css").toExternalForm();
            reviseScene.getStylesheets().add(stylesheet);
            reviseStage.setScene(reviseScene);

            ReviseController reviseController = loader.getController();
            reviseController.setDialogStage(reviseStage);
            reviseController.setDeck(deck);
            reviseStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }

    /**
     * Sets up the edit button in the table.
     */
    private void setupEditButton() {
        editColumn.setCellFactory(param -> new TableCell<>() {
            final Button editButton = new Button("\uD83D\uDD8A");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                    editButton.getStyleClass().addAll("btn-warning", "crud");
                    editButton.setTextAlignment(TextAlignment.CENTER);
                    editButton.setOnAction(event -> {
                        Deck deck = getTableView().getItems().get(getIndex());
                        handleEdit(deck);
                    });
                }
            }
        });
    }

    /**
     * Handles the edit action for a deck.
     *
     * @param deck The deck to edit.
     */
    private void handleEdit(Deck deck) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/deck-view.fxml"));
            VBox page = loader.load();

            Stage deckStage = new Stage();
            deckStage.setTitle("Modifier");
            deckStage.initModality(Modality.APPLICATION_MODAL);

            Scene deckScene = new Scene(page);
            String stylesheet = getClass().getResource("/style.css").toExternalForm();
            deckScene.getStylesheets().add(stylesheet);
            deckStage.setScene(deckScene);

            DeckController deckController = loader.getController();
            deckController.setDialogStage(deckStage);
            deckController.setMainController(this);

            deckController.setDeck(deck);
            deckController.setTitleValue("Modifier un deck");
            deckController.setName(deck.getName());
            deckController.setDescription(deck.getDescription());
            deckController.setSubmitButton("Modifier");

            deckStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }

    /**
     * Sets up the delete button in the table.
     */
    private void setupDeleteButton() {
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            final Button deleteButton = new Button(" \uD83D\uDDD1 ");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                    deleteButton.getStyleClass().addAll("btn-danger", "crud");
                    deleteButton.setTextAlignment(TextAlignment.CENTER);
                    deleteButton.setOnAction(event -> {
                        Deck deck = getTableView().getItems().get(getIndex());
                        handleDelete(deck.getName());
                    });
                }
            }
        });
    }

    /**
     * Handles the delete action for a deck.
     *
     * @param name The name of the deck to delete.
     */
    private void handleDelete(String name) {
        File jsonFile = new File("user_dir" + File.separator + name.replace(" ", "_").toLowerCase() + ".json");
        AlertManager alert = new AlertManager("Delete Deck",
                "Are you sure you want to delete this deck?",
                "Click OK to confirm",
                Alert.AlertType.CONFIRMATION);
        if (alert.confirm()) {
            new FileManager(jsonFile).delete();
            new AlertManager("Deck Deletion",
                    null,
                    "Deck deleted successfully", Alert.AlertType.INFORMATION).alert();
            populateTable();
        }
    }

    /**
     * Sets up the download button in the table.
     */
    private void setupDownloadButton() {
        downloadColumn.setCellFactory(param -> new TableCell<>() {
            final Button downloadButton = new Button("↓");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(downloadButton);
                    downloadButton.getStyleClass().addAll("btn-primary", "crud");
                    downloadButton.setTextAlignment(TextAlignment.CENTER);
                    downloadButton.setOnAction(event -> {
                        Deck deck = getTableView().getItems().get(getIndex());
                        handleDownload(deck.getName());
                    });
                }
            }
        });
    }

    /**
     * Handles the download action for a deck.
     *
     * @param name The name of the deck to download.
     */
    private void handleDownload(String name) {
        File jsonFile = new File("user_dir" + File.separator + name + ".json");
        FileManager fileManager = new FileManager(new File("user_dir"));
        File selectedDirectory = fileManager.openDir(this.tableView.getScene().getWindow());

        if (selectedDirectory != null) {
            File destinationFile = new File(selectedDirectory, jsonFile.getName());
            new FileManager(jsonFile).copyFileToAnotherFile(destinationFile);
            new AlertManager("Deck Download",
                    null,
                    "File downloaded successfully", Alert.AlertType.INFORMATION).alert();
        } else {
            System.out.println("No directory selected.");
        }
    }

    /**
     * Populates the table view with decks from the user directory.
     */
    public void populateTable() {
        FileManager fileManager = new FileManager(new File("user_dir"));
        List<File> files = fileManager.fetchFiles();
        ObservableList<Deck> decks = FXCollections.observableArrayList();

        for (File file : files) {
            try {
                String content = JsonManager.readFileContent(file);
                JsonManager jsonManager = new JsonManager(content);

                String name = jsonManager.getValue("name");
                String description = jsonManager.getValue("description");

                List<Card> cards = new ArrayList<>();
                JSONArray deckElements = jsonManager.getDeckElements();
                for (int i = 0; i < deckElements.length(); i++) {
                    JSONObject cardJson = deckElements.getJSONObject(i);
                    String question = cardJson.getString("question");
                    String answer = cardJson.getString("answer");
                    cards.add(new Card(question, answer));
                }

                Deck deck = new Deck(name, description, cards);
                decks.add(deck);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        tableView.setItems(decks);
    }

    /**
     * Handles the creation of a new deck.
     */
    @FXML
    private void handleNewDeck() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/deck-view.fxml"));
            VBox page = loader.load();

            Stage deckStage = new Stage();
            deckStage.setTitle("New Deck");
            deckStage.initModality(Modality.APPLICATION_MODAL);

            Scene deckScene = new Scene(page);
            String stylesheet = getClass().getResource("/style.css").toExternalForm();
            deckScene.getStylesheets().add(stylesheet);
            deckStage.setScene(deckScene);

            DeckController deckController = loader.getController();
            deckController.setDialogStage(deckStage);
            deckController.setMainController(this);
            deckController.setTitleValue("Créer un deck");
            deckController.setSubmitButton("Créer");

            deckStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }

    /**
     * Handles the opening of an existing deck.
     */
    @FXML
    private void handleOpenDeck() {
        File selectedFile = new JsonManager().openJson();
        if (selectedFile != null) {
            new FileManager(selectedFile).copy();
        }
    }

    /**
     * Handles the refresh action to repopulate the table.
     */
    @FXML
    private void handleRefresh() {
        this.populateTable();
    }

    /**
     * Handles the exit action to close the application.
     */
    @FXML
    private void handleExit() {
        Platform.exit();
    }

    /**
     * Displays the about dialog with application information.
     */
    @FXML
    private void handleAbout() {
        new AlertManager("About", "Occicards",
                "Flashcard application developed by the Occitanie region",
                Alert.AlertType.INFORMATION).alert();
    }
}
