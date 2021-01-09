/**
 * defining the skipCard.
 * @author farzad
 * @version 1.0 (4.April.2020)
 */
public class SkipCard extends ColorCard implements ActionCard {

    /**
     * simple constructor.
     * @param score score of this card.
     * @param color color of this card.
     */
    public SkipCard(int score, char color) {
        super(score,  color);
    }


    /**
     * skip this player so player would loose this turn .
     * @param gameManager game manager of this game.
     */
    @Override
    public void action(GameManager gameManager) {
        gameManager.nextTurn();
    }

    /**
     * make a colorful visible String[] for this SkipCard .
     * @param endCard if this card is the last card among cards in hand _ last card prints complete but others are half.
     * @return String[6] view of this card.
     */
    @Override
    public String[] cardView(boolean endCard) {

        char color = getColor();
        String colorCode = (color == 'R'? Card.ANSI_RED : color == 'B'? Card.ANSI_BLUE: color == 'G'? Card.ANSI_GREEN:Card.ANSI_YELLOW );
        String[] view;
        if(!endCard)
            view = new String[]{
                    colorCode+"┍━━━━━",
                    colorCode+"│ $   ",
                    colorCode+"│     ",
                    colorCode+"│  SK ",
                    colorCode+"│  IP ",
                    colorCode+"┕━━━━━"
            };
        else
            view = new String[]{
                    colorCode+"┍━━━━━━━━┑"+ ANSI_RESET,
                    colorCode+"│  $     │"+ ANSI_RESET,
                    colorCode+"│        │"+ ANSI_RESET,
                    colorCode+"│  SKIP  │"+ ANSI_RESET,
                    colorCode+"│     $  │"+ ANSI_RESET,
                    colorCode+"┕━━━━━━━━┙"+ ANSI_RESET
            };

        char charReplace$ = (color == 'R'? '\u2665': color == 'B'? '\u2660': color == 'G'? '\u2663':'\u2666' );
        view[1] = view[1].replace('$',charReplace$);
        view[4] = view[4].replace('$',charReplace$);
        return view;
    }
}
