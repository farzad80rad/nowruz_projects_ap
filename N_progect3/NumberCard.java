/**
 * a kind of color card that has a number as addition of color.
 * @author farzad
 * @version 1.0 (4.April.2020)
 */
public class NumberCard extends ColorCard
{
    private int number;

    /**
     * simple constructor
     * @param score score of this card
     * @param color color of this card
     * @param number number of this card
     */
    public NumberCard(int score, char color, int number) {
        super(score, color);
        this.number = number;
    }

    /**
     * get number of this card
     * @return number of this card
     */
    public int getNumber() {
        return number;
    }

    /**
     * make a colorful visible String[] for this cord number.
     * @param endCard if this card is the last card among cards in hand _ last card prints complete but others are half.
     * @return String[6] visible view for card.
     */
    @Override
    public String[] cardView(boolean endCard) {

        char color = getColor();
        String colorCode = (color == 'R'? Card.ANSI_RED : color == 'B'? Card.ANSI_BLUE: color == 'G'? Card.ANSI_GREEN:Card.ANSI_YELLOW );
        String[] view;
        if(!endCard)
            view = new String[]{
                    colorCode+"┍━━━━━",
                    colorCode+"│ $#  ",
                    colorCode+"│     ",
                    colorCode+"│     ",
                    colorCode+"│     ",
                    colorCode+"┕━━━━━"
            };
        else
            view = new String[]{
                    colorCode+"┍━━━━━━━━┑"+ ANSI_RESET,
                    colorCode+"│ $#     │"+ANSI_RESET,
                    colorCode+"│        │"+ ANSI_RESET,
                    colorCode+"│        │"+ ANSI_RESET,
                    colorCode+"│     #$ │"+ ANSI_RESET,
                    colorCode+"┕━━━━━━━━┙"+ ANSI_RESET
            };


        char charReplace$ = (color == 'R'? '\u2665': color == 'B'? '\u2660': color == 'G'? '\u2663':'\u2666' );
        char number = (char)(getNumber() + '0');
        view[1] = view[1].replace('$',charReplace$);
        view[1] = view[1].replace('#',number);
        view[4] = view[4].replace('$',charReplace$);
        view[4] = view[4].replace('#',number);
        return view;
    }
}
