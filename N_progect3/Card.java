import java.util.Objects;

/**
 * this class is the main card class that other cards are inherited from , so the methods are depend on subclasses.
 * @author farzad
 * @version 1.0 (4.April.2020)
 */
public abstract class Card {

    private int score;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    /**
     * simple constructor .
     * @param score score of this card.
     */
    public Card(int score) {
        this.score = score;
    }

    /**
     * get score of this card
     * @return score of this card.
     */
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    /**
     * make a colorFull String[] of this kind of card_ depends on subclass.
     * @param endCard if this card is the last card among cards in hand _ last card prints complete but others are half.
     * @return colorFull view of this card. depends on subclass.
     */
    public abstract String[] cardView (boolean endCard);

    /**
     * checks the equality by score and class type.
     * @param o object to be checked
     * @return true if are same else false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return getScore() == card.getScore();
    }

    /**
     * this make a hashCode depends on score of card.
     * @return hashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(getScore());
    }
}
