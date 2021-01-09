/**
 * this class is an AI to play this game . the algorithm is simple : do a valid action.
 * @author farzad
 * @version 1.0 (4.April.2020)
 */
public class AI_Player extends  Player {

    /**
     * simple constructor;
     */
    public AI_Player() { super();}


    /**
     * put a card on board and removes it from cards of player .if there is no valid Card for AI_player to drop,
     * the method would be ran for second time and grab a card from board. if this new card is a valid card to be dropped,
     * the AI_player would drop it on the board. if the new card is not valid to be doped, the method returns null
     * by meaning that this AI_player did not managed to put a card on board and lost his turn.
     * @param gameManager gameManager of this game.
     * @param secondChance if the user has taken a new card in his turn due to not valid choices.
     * @return the card that is choose. null if no card is valid to be choose.
     */
    public Card putCard(GameManager gameManager,boolean secondChance)
    {
        int drawDanger = gameManager.getCountOfDraws();
        Board board = gameManager.getBoard();
        Card cardOnBoard = board.getCardOnBoard();
        char color = board.getColorOnBoard();

        try {
            if (!secondChance)
            Thread.sleep(2525);
        }catch (InterruptedException ignored){};

        if(drawDanger != 0)
            for(Card card : getCardsInHand()) // use a draw
                if((cardOnBoard instanceof WildDrawCard && card instanceof WildDrawCard) ||
                        ( cardOnBoard instanceof DrawCard && card instanceof DrawCard)) {
                    board.putCardOnBoard(card);
                    getCardsInHand().remove(card);
                    return card;
                }


        for(Card card : getCardsInHand()) // check same color
            if(card instanceof ColorCard)
                if(((ColorCard) card).getColor() == color)
                {
                    board.putCardOnBoard(card);
                    getCardsInHand().remove(card);
                    return card;
                }

        if(cardOnBoard instanceof NumberCard)
            for(Card card : getCardsInHand()) // check same number.
                if(card instanceof NumberCard)
                    if(((NumberCard) card).getNumber() == ((NumberCard) cardOnBoard).getNumber())
                    {
                        board.putCardOnBoard(card);
                        getCardsInHand().remove(card);
                        return card;
                    }

        if(cardOnBoard instanceof ActionCard && !(cardOnBoard instanceof WildCard)) // check same action card
            for(Card card : getCardsInHand())
                if(card.getClass().equals(cardOnBoard.getClass()))
                    {
                        board.putCardOnBoard(card);
                        getCardsInHand().remove(card);
                        return card;
                    }

        for(Card card : getCardsInHand()) // use wild card
            if(card instanceof WildCard)
            {
                board.putCardOnBoard(card);
                getCardsInHand().remove(card);
                return card;
            }

        if(secondChance)
            return null;
        grabCard(board);
        return putCard(gameManager,true); // the player has another chance after grabbing a card
    }


}
