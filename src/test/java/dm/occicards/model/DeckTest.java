package dm.occicards.model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    private Deck deck;

    @BeforeEach
    void setUp() {
        this.deck = new Deck("deck1", "Description 1", List.of(
                new Card("Question 1", "Réponse 1"),
                new Card("Question 2", "Réponse 2")
        ));
    }

    @Test
    void testGetName() {
        assertEquals("deck1", this.deck.getName());
    }

    @Test
    void testSetName() {
        this.deck.setName("deck2");
        assertEquals("deck2", this.deck.getName());
    }

    @Test
    void testGetDescription() {
        assertEquals("Description 1", this.deck.getDescription());
    }

    @Test
    void testSetDescription() {
        this.deck.setDescription("Description 2");
        assertEquals("Description 2", this.deck.getDescription());
    }

    @Test
    void testGetCards() {
        List<Card> expectedCards = List.of(
                new Card("Question 1", "Réponse 1"),
                new Card("Question 2", "Réponse 2")
        );
        assertEquals(expectedCards, this.deck.getCards());
    }

    @Test
    void testSetCards() {
        List<Card> newCards = List.of(
                new Card("Question 3", "Réponse 3")
        );
        this.deck.setCards(newCards);
        assertEquals(newCards, this.deck.getCards());
    }

    @Test
    void testGetJsonContent() {
        String expectedJson = "{" +
                "\"name\":\"deck1\"," +
                "\"description\":\"Description 1\"," +
                "\"deck\":{" +
                "\"1\":{\"question\":\"Question 1\",\"answer\":\"Réponse 1\"}," +
                "\"2\":{\"question\":\"Question 2\",\"answer\":\"Réponse 2\"}}" +
                "}";

        JSONObject expectedJsonObject = new JSONObject(expectedJson);
        JSONObject actualJsonObject = new JSONObject(this.deck.getJsonContent());

        assertEquals(expectedJsonObject.toString(), actualJsonObject.toString());
    }
}
