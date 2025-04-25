package dm.occicards.controller;

import dm.occicards.model.Card;
import dm.occicards.model.Deck;
import dm.occicards.utils.AlertManager;
import dm.occicards.utils.JsonManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for managing the revision of a deck of cards.
 */
public class ReviseController {
    private Deck deck;
    private Stage dialogStage;
    private List<Card> cards;
    private int currentCardIndex = 0;

    /**
     * Text element for displaying the number of remaining cards.
     */
    @FXML
    private Text remainingCard;

    /**
     * Text element for displaying the question of the current card.
     */
    @FXML
    private Text questionText;

    /**
     * Text element for displaying the answer of the current card.
     */
    @FXML
    private Text answerText;

    /**
     * Label for the question text.
     */
    @FXML
    private Label questionLabel;

    /**
     * Label for the answer text.
     */
    @FXML
    private Label answerLabel;

    /**
     * Button for returning the card to reveal the answer.
     */
    @FXML
    private Button returnCardBtn;

    /**
     * Button for evaluating the card as excellent.
     */
    @FXML
    private Button excellentBtn;

    /**
     * Button for evaluating the card as good.
     */
    @FXML
    private Button goodBtn;

    /**
     * Button for evaluating the card as average.
     */
    @FXML
    private Button averageBtn;

    /**
     * Button for evaluating the card as poor.
     */
    @FXML
    private Button poorBtn;

    /**
     * Initializes the controller and sets up the list of cards.
     */
    @FXML
    public void initialize() {
        cards = new ArrayList<>();
    }

    /**
     * Handles the action of returning the card to reveal the answer.
     */
    @FXML
    public void handleReturnCard() {
        // Hide question elements
        returnCardBtn.setVisible(false);
        questionText.setVisible(false);
        questionLabel.setVisible(false);

        // Show answer elements and evaluation buttons
        answerText.setVisible(true);
        answerLabel.setVisible(true);
        excellentBtn.setVisible(true);
        goodBtn.setVisible(true);
        averageBtn.setVisible(true);
        poorBtn.setVisible(true);
    }

    /**
     * Handles the evaluation of the current card based on the given score.
     *
     * @param score The evaluation score for the card.
     */
    private void handleEvaluate(int score) {
        Card currentCard = cards.get(currentCardIndex);

        switch (score) {
            case 1:
                if (currentCardIndex > 0) {
                    cards.remove(currentCardIndex);
                    cards.add(currentCardIndex - 1, currentCard);
                    currentCardIndex--;
                }
                break;
            case 2:
                if (currentCardIndex > 1) {
                    cards.remove(currentCardIndex);
                    cards.add(currentCardIndex - 2, currentCard);
                    currentCardIndex -= 2;
                } else if (currentCardIndex == 1) {
                    cards.remove(currentCardIndex);
                    cards.add(0, currentCard);
                    currentCardIndex = 0;
                }
                break;
            case 3:
                if (currentCardIndex > 2) {
                    cards.remove(currentCardIndex);
                    cards.add(currentCardIndex - 3, currentCard);
                    currentCardIndex -= 3;
                } else {
                    cards.remove(currentCardIndex);
                    cards.add(0, currentCard);
                    currentCardIndex = 0;
                }
                break;
            case 4:
                cards.remove(currentCardIndex);
                if (currentCardIndex > 0) {
                    currentCardIndex--;
                }
                break;
            default:
                // Handle invalid scores if necessary
                break;
        }

        // Check if all cards have been reviewed
        if (cards.isEmpty()) {
            if (dialogStage != null) {
                dialogStage.close();
            }
            new AlertManager("Révisions terminées", "",
                    "Vous avez terminé votre révision.",
                    Alert.AlertType.INFORMATION).alert();
            return;
        }

        // Update the index to display the next card
        if (currentCardIndex < cards.size() - 1) {
            currentCardIndex++;
        } else {
            currentCardIndex = 0; // Loop back to the start if it's the last card
        }

        displayCard(currentCardIndex);
    }

    /**
     * Handles the action of evaluating the card as excellent.
     */
    @FXML
    public void handleExcellent() {
        this.handleEvaluate(4);
    }

    /**
     * Handles the action of evaluating the card as good.
     */
    @FXML
    public void handleGood() {
        this.handleEvaluate(3);
    }

    /**
     * Handles the action of evaluating the card as average.
     */
    @FXML
    public void handleAverage() {
        this.handleEvaluate(2);
    }

    /**
     * Handles the action of evaluating the card as poor.
     */
    @FXML
    public void handlePoor() {
        this.handleEvaluate(1);
    }

    /**
     * Sets the deck to be revised.
     *
     * @param deck The deck to set.
     */
    public void setDeck(Deck deck) {
        this.deck = deck;
        loadCardsFromDeck();
        if (!cards.isEmpty()) {
            displayCard(currentCardIndex);
        }
    }

    /**
     * Loads the cards from the deck into the list of cards.
     */
    private void loadCardsFromDeck() {
        JsonManager jsonManager = new JsonManager(deck.getJsonContent());
        JSONArray deckElements = jsonManager.getDeckElements();

        cards.clear();
        for (int i = 0; i < deckElements.length(); i++) {
            JSONObject cardJson = deckElements.getJSONObject(i);
            String question = cardJson.getString("question");
            String answer = cardJson.getString("answer");
            cards.add(new Card(question, answer));
        }
    }

    /**
     * Displays the card at the specified index.
     *
     * @param index The index of the card to display.
     */
    private void displayCard(int index) {
        if (cards.isEmpty()) {
            remainingCard.setText("0 carte restante");
            return;
        }

        Card card = cards.get(index);
        remainingCard.setText(String.valueOf(cards.size() - index) + " cartes restante");
        questionText.setText(card.getQuestion());
        answerText.setText(card.getAnswer());

        // Hide answer elements and evaluation buttons
        answerText.setVisible(false);
        answerLabel.setVisible(false);
        excellentBtn.setVisible(false);
        goodBtn.setVisible(false);
        averageBtn.setVisible(false);
        poorBtn.setVisible(false);

        // Show question elements and return button
        returnCardBtn.setVisible(true);
        questionText.setVisible(true);
        questionLabel.setVisible(true);
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
}
