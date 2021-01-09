import java.util.LinkedList;

/**
 * the surface of game were there are just a card on it and a bunch of hidden cards.
 * keep cation that there is no player here. its just a simple board .
 * @author farzad
 * @version 1.0 (4.April.2020)
 */
public class Board {
    private LinkedList<Card> cards; // hidden cards on board.
    private Card cardOnBoard; //card on board
    private char colorOnBoard; //color to be play . 'B'= blue  'Y' = yellow  'G' = green  'R' = red


    /**
     * make the board and pick a random  card from the hidden cards to be the card on board.
     * @param cardsOnBoard all cards that remain after division among players.
     */
    public Board(LinkedList<Card> cardsOnBoard)
    {
        int i=1;
        do {
            cardOnBoard = cardsOnBoard.get(i);// this seems to not be random but the thing is it's been shuffled before.
            i++;
        } while (cardOnBoard instanceof WildCard);
        cardsOnBoard.remove(cardOnBoard);
        this.cards = cardsOnBoard;
        colorOnBoard =  ((ColorCard)cardOnBoard).getColor();
    }

    /**
     * get all cards of board.
     * @return all cards of board
     */
    public LinkedList<Card> getCards() {
        return cards;
    }

    /**
     * get the card on board
     * @return the card on board
     */
    public Card getCardOnBoard() {
        return cardOnBoard;
    }

    /**
     * put a card as the card on board. the last card is transferred to hidden cards of bard.
     * @param card new card on board.
     */
    public void putCardOnBoard(Card card)
    {
        cards.add(cardOnBoard);
        cardOnBoard = card;
        if(card instanceof ColorCard)
            colorOnBoard = ((ColorCard)card).getColor();
    }

    /**
     * get the color of board.
     * @return 'R':red   'G':green  'Y':yellow   'B':blue
     */
    public char getColorOnBoard() {
        return colorOnBoard;
    }

    /**
     * set a color as color of board
     * @param colorOnBoard 'R':red   'G':green  'Y':yellow   'B':blue
     */
    public void setColorOnBoard(char colorOnBoard) {
        this.colorOnBoard = colorOnBoard;
    }
}
