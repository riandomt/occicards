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
        if (getCard() != null) {
            getQuestion().setText(getCard().getQuestion());
            getAnswer().setText(getCard().getAnswer());
        }
    }

    /**
     * Handles the creation or updating of a card.
     * Validates the input fields and adds or updates the card in the deck if valid.
     */
    @FXML
    public void handleCreate() {
        String questionText = getQuestion().getText();
        String answerText = getAnswer().getText();

        if (questionText.isEmpty() || answerText.isEmpty()) {
            new AlertManager("Erreur", null, "Les champs question et réponse ne peuvent pas être vides.", Alert.AlertType.ERROR).alert();
            return;
        }

        Card newCard = new Card(questionText, answerText);

        if (getDeckController() != null) {
            if (getCard() == null) {
                getDeckController().addCardToDeck(newCard);
                new AlertManager("Ajout d'une carte", null,
                        "Carte ajoutée avec succès.",
                        Alert.AlertType.INFORMATION).alert();
            } else {
                getDeckController().updateCardInDeck(getCard(), newCard);
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
        if (getCardStage() != null) {
            getCardStage().close();
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
     * Gets the deck controller associated with this card controller.
     *
     * @return The deck controller.
     */
    public DeckController getDeckController() {
        return deckController;
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
     * Gets the card data for editing.
     *
     * @return The card to edit.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Sets the card data for editing.
     *
     * @param card The card to edit.
     */
    public void setCardData(Card card) {
        this.card = card;
        if (getQuestion() != null && getAnswer() != null) {
            getQuestion().setText(card.getQuestion());
            getAnswer().setText(card.getAnswer());
        }
    }

    /**
     * Gets the TextArea for entering the question.
     *
     * @return The TextArea for the question.
     */
    public TextArea getQuestion() {
        return question;
    }

    /**
     * Gets the TextArea for entering the answer.
     *
     * @return The TextArea for the answer.
     */
    public TextArea getAnswer() {
        return answer;
    }
}
