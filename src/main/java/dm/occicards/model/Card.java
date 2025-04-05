package dm.occicards.model;

public class Card {
    private String question;
    private String answer;
    private int score;

    public Card(String question, String answer) {
        this.setQuestion(question);
        this.setAnswer(answer);
        this.score = 0; // Initialiser le score à 0 par défaut
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
