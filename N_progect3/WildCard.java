import java.util.Scanner;

/**
 * defining of wild card.
 */
public class WildCard extends Card implements ActionCard{

    /**
     * simple constructor.
     * @param score score of this card
     */
    public WildCard(int score) {
        super(score);
    }


    /**
     * chane the color of the board. getting inputs are among the method.
     * @param gameManager game manager of the game.
     */
    @Override
    public void action(GameManager gameManager) {

        Player currentPlayer = gameManager.getPlayers()[gameManager.getTurn()%gameManager.getPlayers().length];
        Scanner scanner = new Scanner(System.in);
        char[] colors = new char[]{'B','R','G','Y'};

        if(!(currentPlayer instanceof AI_Player)) {
            while (true) {
                System.out.println("chose a color."+Card.ANSI_BLUE+" 1-blue " + Card.ANSI_RED +" 2-red " +
                        Card.ANSI_GREEN +" 3-green " + Card.ANSI_YELLOW + " 4-yellow" + Card.ANSI_RESET);
                try {
                    gameManager.getBoard().setColorOnBoard(colors[scanner.nextInt() - 1]);
                    return;
                } catch (Exception e) {
                    System.out.println("not valid");
                }
            }
        }
        else
            for(Card card : currentPlayer.getCardsInHand()) {
                if(card instanceof ColorCard) {
                    gameManager.getBoard().setColorOnBoard(((ColorCard) card).getColor());
                    return;
                }
            }
    }

    /**
     * make a colorful visible String[] for this Wild card .
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
                    colorCode+"│  WI ",
                    colorCode+"│  LD ",
                    colorCode+"│     ",
                    colorCode+"┕━━━━━"
            };
        else
            view = new String[]{
                    colorCode+"┍━━━━━━━━┑"+ ANSI_RESET,
                    colorCode+"│ ✎     │"+ANSI_RESET,
                    colorCode+"│        │"+ ANSI_RESET,
                    colorCode+"│  WILD  │"+ ANSI_RESET,
                    colorCode+"│     ✎ │"+ ANSI_RESET,
                    colorCode+"┕━━━━━━━━┙"+ ANSI_RESET
            };

        return view;
    }
}
