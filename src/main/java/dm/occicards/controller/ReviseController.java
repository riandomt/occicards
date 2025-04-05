package dm.occicards.controller;

import dm.occicards.model.Card;
import dm.occicards.model.Deck;
import dm.occicards.utils.JsonManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReviseController {
    private Deck deck;
    private Stage dialogStage;
    private List<Card> cards;
    private int currentCardIndex = 0;

    @FXML
    private Text remainingCard;

    @FXML
    private Text questionText;

    @FXML
    private Text answerText;

    @FXML
    private Label questionLabel;

    @FXML
    private Label answerLabel;

    @FXML
    private Button returnCardBtn;

    @FXML
    private Button excellentBtn;

    @FXML
    private Button goodBtn;

    @FXML
    private Button averageBtn;

    @FXML
    private Button poorBtn;

    @FXML
    public void initialize() {
        cards = new ArrayList<>();
    }

    @FXML
    public void handleReturnCard() {
        // Masquer les éléments de la question
        returnCardBtn.setVisible(false);
        questionText.setVisible(false);
        questionLabel.setVisible(false);

        // Afficher les éléments de la réponse et les boutons d'évaluation
        answerText.setVisible(true);
        answerLabel.setVisible(true);
        excellentBtn.setVisible(true);
        goodBtn.setVisible(true);
        averageBtn.setVisible(true);
        poorBtn.setVisible(true);
    }

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
                // Gérer les scores invalides si nécessaire
                break;
        }

        // Mettre à jour l'index pour afficher la carte suivante
        if (currentCardIndex < cards.size() - 1) {
            currentCardIndex++;
        } else if (cards.isEmpty()) {
            this.getDialogStage().close();
            return;
        } else {
            currentCardIndex = 0; // Revenir au début si c'est la dernière carte
        }

        displayCard(currentCardIndex);
    }

    @FXML
    public void handleExcellent() {
        this.handleEvaluate(4);
    }

    @FXML
    public void handleGood() {
        this.handleEvaluate(3);
    }

    @FXML
    public void handleAverage() {
        this.handleEvaluate(2);
    }

    @FXML
    public void handlePoor() {
        this.handleEvaluate(1);
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
        loadCardsFromDeck();
        if (!cards.isEmpty()) {
            displayCard(currentCardIndex);
        }
    }

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

    private void displayCard(int index) {
        if (cards.isEmpty()) {
            remainingCard.setText("0 cartes restantes");
            return;
        }

        Card card = cards.get(index);
        remainingCard.setText(String.valueOf(cards.size() - index + " cartes restantes"));
        questionText.setText(card.getQuestion());
        answerText.setText(card.getAnswer());

        // Masquer les éléments de la réponse et les boutons d'évaluation
        answerText.setVisible(false);
        answerLabel.setVisible(false);
        excellentBtn.setVisible(false);
        goodBtn.setVisible(false);
        averageBtn.setVisible(false);
        poorBtn.setVisible(false);

        // Afficher les éléments de la question et le bouton retourner
        returnCardBtn.setVisible(true);
        questionText.setVisible(true);
        questionLabel.setVisible(true);
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
