import java.util.Objects;

/**
 * colorFull cards which as addition of score have a color.
 * @author farzad
 * @version 1.0 (4.April.2020)
 */
public abstract class ColorCard extends Card{

    private char color ;

    /**
     * simple constructor.
     * @param score score of this card
     * @param color color of this card
     */
    public ColorCard(int score, char color) {
        super(score);
        this.color = color;
    }

    /**
     * get color of this card
     * @return 'R':red   'G':green  'Y':yellow   'B':blue
     */
    public char getColor() {
        return color;
    }


    /**
     * checks color , score and class type to be same.
     * @param o object to be checked
     * @return true if are same else false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ColorCard)) return false;
        if (!super.equals(o)) return false;
        ColorCard colorCard = (ColorCard) o;
        return getColor() == colorCard.getColor();
    }

    /**
     * make a hashCode by score and color
     * @return hashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getColor());
    }
}
