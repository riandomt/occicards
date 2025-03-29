package dm.occicards.controller;

import javafx.stage.Stage;

public class CardController {
    private Stage cardStage;
    private DeckController deckController;


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
