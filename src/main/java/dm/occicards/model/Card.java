package dm.occicards.model;

/**
 * Represents a flashcard with a question and an answer.
 */
public class Card {
    private String question;
    private String answer;

    /**
     * Constructs a new Card with the specified question and answer.
     *
     * @param question The question text for the card.
     * @param answer The answer text for the card.
     */
    public Card(String question, String answer) {
        this.setQuestion(question);
        this.setAnswer(answer);
    }

    /**
     * Gets the question text of the card.
     *
     * @return The question text.
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Sets the question text of the card.
     *
     * @param question The question text to set.
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Gets the answer text of the card.
     *
     * @return The answer text.
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Sets the answer text of the card.
     *
     * @param answer The answer text to set.
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
