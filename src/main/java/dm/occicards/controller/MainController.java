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
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    @FXML
    private TableView<Deck> tableView;

    @FXML
    private TableColumn<Deck, String> nameColumn;

    @FXML
    private TableColumn<Deck, String> descriptionColumn;

    @FXML
    private TableColumn<Deck, Void> reviseColumn;

    @FXML
    private TableColumn<Deck, Void> editColumn;

    @FXML
    private TableColumn<Deck, Void> deleteColumn;

    @FXML
    private TableColumn<Deck, Void> downloadColumn;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        setupEditButton();
        setupDeleteButton();
        setupDownloadButton();
        setupReviseButton(); // Configuration du bouton "Réviser"

        this.populateTable();
    }

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

    private void handleRevise(Deck deck) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/revise-view.fxml"));

            Stage reviseStage = new Stage();
            reviseStage.setTitle("Réviser");
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
                    editButton.setOnAction(event -> {
                        Deck deck = getTableView().getItems().get(getIndex());
                        handleEdit(deck);
                    });
                }
            }
        });
    }
    private void handleEdit(Deck deck) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/deck-view.fxml"));
            VBox page = loader.load();

            Stage deckStage = new Stage();
            deckStage.setTitle("Éditer le Deck");
            deckStage.initModality(Modality.APPLICATION_MODAL);

            Scene deckScene = new Scene(page);
            String stylesheet = getClass().getResource("/style.css").toExternalForm();
            deckScene.getStylesheets().add(stylesheet);
            deckStage.setScene(deckScene);

            DeckController deckController = loader.getController();
            deckController.setDialogStage(deckStage);
            deckController.setMainController(this);

            // Mettre à jour le titre
            deckController.setTitleValue("Modifier le deck");

            deckStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }


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
                    deleteButton.setOnAction(event -> {
                        Deck deck = getTableView().getItems().get(getIndex());
                        handleDelete(deck.getName());
                    });
                }
            }
        });
    }

    private void handleDelete(String name) {
        File jsonFile = new File("user_dir" + File.separator + name.replace(" ", "_").toLowerCase() + ".json");
        AlertManager alert = new AlertManager("Supprimer un deck",
                "Etes-vous sûr de vouloir supprimer ce deck ?",
                "Cliquez sur OK pour confirmer",
                Alert.AlertType.CONFIRMATION);
        if (alert.confirm()) {
            new FileManager(jsonFile).delete();
            new AlertManager("Suppression du deck",
                    null,
                    "Deck supprimé avec succès", Alert.AlertType.INFORMATION).alert();
            populateTable();
        }
    }

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
                    downloadButton.setOnAction(event -> {
                        Deck deck = getTableView().getItems().get(getIndex());
                        handleDownload(deck.getName());
                    });
                }
            }
        });
    }

    private void handleDownload(String name) {
        File jsonFile = new File("user_dir" + File.separator + name + ".json");
        FileManager fileManager = new FileManager(new File("user_dir"));
        File selectedDirectory = fileManager.openDir(this.tableView.getScene().getWindow());

        if (selectedDirectory != null) {
            File destinationFile = new File(selectedDirectory, jsonFile.getName());
            new FileManager(jsonFile).copyFileToAnotherFile(destinationFile);
            new AlertManager("Téléchargement du deck",
                    null,
                    "Fichier téléchargé avec succès", Alert.AlertType.INFORMATION).alert();
        } else {
            System.out.println("Aucun répertoire sélectionné.");
        }
    }

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

    @FXML
    public void handleNewDeck() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/deck-view.fxml"));
            VBox page = loader.load();

            Stage deckStage = new Stage();
            deckStage.setTitle("Nouveau Deck");
            deckStage.initModality(Modality.APPLICATION_MODAL);

            Scene deckScene = new Scene(page);
            String stylesheet = getClass().getResource("/style.css").toExternalForm();
            deckScene.getStylesheets().add(stylesheet);
            deckStage.setScene(deckScene);

            DeckController deckController = loader.getController();
            deckController.setDialogStage(deckStage);
            deckController.setMainController(this);

            deckStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }

    @FXML
    public void handleOpenDeck() {
        File selectedFile = new JsonManager().openJson();
        if (selectedFile != null) {
            new FileManager(selectedFile).copy();
        }
    }

    @FXML
    public void handleRefresh() {
        this.populateTable();
    }

    @FXML
    public void handleExit() {
        Platform.exit();
    }

    @FXML
    public void handleAbout() {
        new AlertManager("À propos", "Occicards",
                "Application de flashcards développée par la région Occitanie",
                Alert.AlertType.INFORMATION).alert();
    }
}
