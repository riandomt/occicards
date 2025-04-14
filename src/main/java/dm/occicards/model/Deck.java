package dm.occicards.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a deck of flashcards.
 */
public class Deck {
    private String name;
    private String description;
    private List<Card> cards;

    /**
     * Constructs a new Deck with the specified name, description, and list of cards.
     *
     * @param name        The name of the deck.
     * @param description The description of the deck.
     * @param cards       The list of cards in the deck.
     */
    public Deck(String name, String description, List<Card> cards) {
        this.name = name;
        this.description = description;
        this.cards = cards != null ? cards : new ArrayList<>();
    }

    /**
     * Constructs a new Deck with the specified name and description, initializing an empty list of cards.
     *
     * @param name        The name of the deck.
     * @param description The description of the deck.
     */
    public Deck(String name, String description) {
        this(name, description, new ArrayList<>());
    }

    /**
     * Gets the name of the deck.
     *
     * @return The name of the deck.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the deck.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the deck.
     *
     * @return The description of the deck.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the deck.
     *
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the list of cards in the deck.
     *
     * @return The list of cards.
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Sets the list of cards in the deck.
     *
     * @param cards The list of cards to set.
     */
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    /**
     * Returns the JSON representation of the deck.
     *
     * @return A JSON string representing the deck.
     */
    public String getJsonContent() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", getName());
        jsonObject.put("description", getDescription());

        JSONObject deckJson = new JSONObject();
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            JSONObject cardJson = new JSONObject();
            cardJson.put("question", card.getQuestion());
            cardJson.put("answer", card.getAnswer());
            deckJson.put(String.valueOf(i + 1), cardJson);
        }

        jsonObject.put("deck", deckJson);
        return jsonObject.toString();
    }
}
