package dm.occicards.controller;

import dm.occicards.model.Card;
import dm.occicards.utils.AlertManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class CardController {
    private Stage cardStage;
    private DeckController deckController;

    @FXML
    private TextArea question;

    @FXML
    private TextArea answer;

    @FXML
    public void handleCreate() {
        String questionText = question.getText();
        String answerText = answer.getText();

        // Vérifiez que les champs ne sont pas vides
        if (questionText.isEmpty() || answerText.isEmpty()) {
            new AlertManager("Erreur", null, "Les champs question et réponse ne peuvent pas être vides.", Alert.AlertType.ERROR).alert();
            return;
        }

        // Créez une nouvelle carte
        Card card = new Card(questionText, answerText);

        // Ajoutez la carte au deck via DeckController
        if (deckController != null) {
            deckController.addCardToDeck(card);
            new AlertManager("Ajout d'une carte", null,
                    "Carte ajoutée avec succès",
                    Alert.AlertType.INFORMATION).alert();
        }

        // Fermer la fenêtre après l'ajout
        handleCancel();
    }

    @FXML
    public void handleCancel() {
        if (cardStage != null) {
            cardStage.close();
        }
    }

    public Stage getCardStage() {
        return cardStage;
    }

    public void setCardStage(Stage cardStage) {
        this.cardStage = cardStage;
    }

    public void setDeckController(DeckController deckController) {
        this.deckController = deckController;
    }
}
