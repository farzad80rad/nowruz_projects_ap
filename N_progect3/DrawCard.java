/**
 * Action: Draw card makes the next player to pick 2 card from the board and loose his turn.
 * this type of card has a color and score.
 * @author farzad
 * @version 1.0 (4.April.2020)
 */
public class DrawCard extends ColorCard implements ActionCard {

    /**
     * simple constructor.
     * @param score score of this card
     * @param color color of this card
     */
    public DrawCard(int score, char color) {
        super(score,color);
    }


    /**
     * Action: Draw card makes the next player to pick 2 card from the board and loose his turn.if next player
     * has a same card _ another draw card _ and use it , the next player of nex would take 4 cards and loose turn.
     * @param gameManager gameManger of this game.
     */
    @Override
    public void action(GameManager gameManager) {
        int indexNextPlayer =  (gameManager.getTurn() + gameManager.getRotationDirection()) % gameManager.getPlayers().length;
        Player nextPlayer = gameManager.getPlayers()[indexNextPlayer];

        boolean flag = true;
        for(Card card : nextPlayer.getCardsInHand())
            if( card instanceof DrawCard )   { flag = false;  break; }
        if( flag) {
            for(int i=0 ; i<gameManager.getCountOfDraws() + 2; i ++)
                nextPlayer.grabCard(gameManager.getBoard());
            gameManager.nextTurn();
            gameManager.setCountOfDraws(0);
        }
        else
            gameManager.setCountOfDraws( gameManager.getCountOfDraws() + 2);
    }


    /**
     * make a colorful visible String[] for this DrawCard .
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
                    colorCode+"│  +2 ",
                    colorCode+"│  DR ",
                    colorCode+"│  AW ",
                    colorCode+"┕━━━━━"
            };
        else
            view = new String[]{
                    colorCode+"┍━━━━━━━━┑"+ ANSI_RESET,
                    colorCode+"│ $ +2   │"+ANSI_RESET,
                    colorCode+"│        │"+ ANSI_RESET,
                    colorCode+"│  DRAW  │"+ ANSI_RESET,
                    colorCode+"│      $ │"+ ANSI_RESET,
                    colorCode+"┕━━━━━━━━┙"+ ANSI_RESET
            };

        char charReplace$ = (color == 'R'? '\u2665': color == 'B'? '\u2660': color == 'G'? '\u2663':'\u2666' );
        view[1] = view[1].replace('$',charReplace$);
        view[4] = view[4].replace('$',charReplace$);
        return view;
    }
}
