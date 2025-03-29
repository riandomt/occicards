package dm.occicards.model;

public class Card {
    private String question;
    private String answer;

    public Card(String question, String answer) {
        this.setQuestion(question);
        this.setAnswer(answer);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
