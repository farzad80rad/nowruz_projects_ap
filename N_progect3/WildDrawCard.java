/**
 * defining WildDraw cards.
 * @author farzad
 * @version 1.0 (4.April.2020)
 */
public class WildDrawCard extends  WildCard{

    /**
     * simple constructor
     * @param score score of this card
     */
    public WildDrawCard(int score) {
        super(score);
    }

    /**
     * chane the color of the board and force next player to grab 4 cards from board. getting inputs are among the method.
     * @param gameManager game manager of the game.
     */
    @Override
    public void action(GameManager gameManager) {
        super.action(gameManager);

        int indexNextPlayer =  (gameManager.getTurn() + gameManager.getRotationDirection()) % gameManager.getPlayers().length;
        Player nextPlayer = gameManager.getPlayers()[indexNextPlayer];

        boolean flag = true;
        for(Card card : nextPlayer.getCardsInHand())
            if(card instanceof WildDrawCard)   { flag = false;  break; }
        if( flag) {
            for(int i=0 ; i<gameManager.getCountOfDraws() + 4 ; i++)
                nextPlayer.grabCard(gameManager.getBoard());
            gameManager.nextTurn();
            gameManager.setCountOfDraws(0);
        }
        else
            gameManager.setCountOfDraws( gameManager.getCountOfDraws() + 4);
    }

    /**
     * make a colorful visible String[] for this WildDraw card .
     * @param endCard if this card is the last card among cards in hand _ last card prints complete but others are half.
     * @return String[6] view of this card.
     */
    @Override
    public String[] cardView(boolean endCard) {
        String colorCode = Card.ANSI_PURPLE;
        String[] view;
        if(!endCard)
            view = new String[]{
                    colorCode+"┍━━━━━",
                    colorCode+"│ ✎  ",
                    colorCode+"│  +4 ",
                    colorCode+"│  WD ",
                    colorCode+"│     ",
                    colorCode+"┕━━━━━"
            };
        else
            view = new String[]{
                    colorCode+"┍━━━━━━━━┑"+ ANSI_RESET,
                    colorCode+"│ ✎+4   │"+ANSI_RESET,
                    colorCode+"│  WILD  │"+ ANSI_RESET,
                    colorCode+"│  DRAW  │"+ ANSI_RESET,
                    colorCode+"│   +4 ✎│"+ ANSI_RESET,
                    colorCode+"┕━━━━━━━━┙"+ ANSI_RESET
            };

        return view;
    }
}
