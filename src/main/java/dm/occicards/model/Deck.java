package dm.occicards.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Deck {
    private final UUID id;
    private String name;
    private String description;
    private List<Card> cards;

    public Deck(String name, String description, List<Card> cards) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.cards = cards != null ? cards : new ArrayList<>();
    }

    public Deck(String name, String description) {
        this(name, description, new ArrayList<>());
    }

    public String getIdAsString() {
        return id.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public String getJsonContent() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", getIdAsString());
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
