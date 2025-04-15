package dm.occicards.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    private Card card;

    @BeforeEach
    void setUp() {
        this.card = new Card("Question 1", "Réponse 1");
    }

    @Test
    void getQuestion() {
        assertEquals("Question 1", this.card.getQuestion());
    }

    @Test
    void setQuestion() {
        this.card.setQuestion("Question 2");
        assertEquals("Question 2", this.card.getQuestion());
    }

    @Test
    void getAnswer() {
        assertEquals("Réponse 1", this.card.getAnswer());
    }

    @Test
    void setAnswer() {
        this.card.setAnswer("Réponse 2");
        assertEquals("Réponse 2", this.card.getAnswer());
    }
}