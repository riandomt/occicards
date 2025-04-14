package dm.occicards.controller;

import dm.occicards.model.Card;
import dm.occicards.utils.AlertManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * Controller class for managing card-related operations in the application.
 */
public class CardController {
    private Stage cardStage;
    private DeckController deckController;
    private Card card; // To hold the card data for editing

    /**
     * TextArea for entering the question.
     */
    @FXML
    private TextArea question;

    /**
     * TextArea for entering the answer.
     */
    @FXML
    private TextArea answer;

    /**
     * Initializes the controller with existing card data if editing.
     */
    public void initialize() {
        if (card != null) {
            question.setText(card.getQuestion());
            answer.setText(card.getAnswer());
        }
    }

    /**
     * Handles the creation or updating of a card.
     * Validates the input fields and adds or updates the card in the deck if valid.
     */
    @FXML
    public void handleCreate() {
        String questionText = question.getText();
        String answerText = answer.getText();

        if (questionText.isEmpty() || answerText.isEmpty()) {
            new AlertManager("Erreur", null, "Les champs question et réponse ne peuvent pas être vides.", Alert.AlertType.ERROR).alert();
            return;
        }

        Card newCard = new Card(questionText, answerText);

        if (deckController != null) {
            if (card == null) {
                deckController.addCardToDeck(newCard);
                new AlertManager("Ajout d'une carte", null,
                        "Carte ajoutée avec succès.",
                        Alert.AlertType.INFORMATION).alert();
            } else {
                deckController.updateCardInDeck(card, newCard);
                new AlertManager("Carte mise à jour", null,
                        "Carte mise à jour avec succès.",
                        Alert.AlertType.INFORMATION).alert();
            }
        }
        handleCancel();
    }

    /**
     * Handles the cancellation of the current operation.
     * Closes the card stage if it is open.
     */
    @FXML
    public void handleCancel() {
        if (cardStage != null) {
            cardStage.close();
        }
    }

    /**
     * Gets the current card stage.
     *
     * @return The current card stage.
     */
    public Stage getCardStage() {
        return cardStage;
    }

    /**
     * Sets the current card stage.
     *
     * @param cardStage The stage to set.
     */
    public void setCardStage(Stage cardStage) {
        this.cardStage = cardStage;
    }

    /**
     * Sets the deck controller associated with this card controller.
     *
     * @param deckController The deck controller to set.
     */
    public void setDeckController(DeckController deckController) {
        this.deckController = deckController;
    }

    /**
     * Sets the card data for editing.
     *
     * @param card The card to edit.
     */
    public void setCardData(Card card) {
        this.card = card;
        if (question != null && answer != null) {
            question.setText(card.getQuestion());
            answer.setText(card.getAnswer());
        }
    }
}
