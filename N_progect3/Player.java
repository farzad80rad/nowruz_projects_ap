import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * human player of this game.
 * @author farzad
 * @version 1.0 (4.April.2020)
 */
public class Player {
    private LinkedList<Card> cardsInHand;

    /**
     * simple constructor.
     */
    public Player() {
        cardsInHand = new LinkedList<>();
    }

    /**
     * add this card to cards of this user.
     * @param card card to be added.
     */
    public void grabThisCard(Card card)
    {
        cardsInHand.add(card);
    }

    /**
     * take a random card from the cards of board and transfer it to player cards.
     * @param board board that this player is playing on.
     */
    public void grabCard(Board board)
    {
        Collections.shuffle(board.getCards());
        cardsInHand.add( board.getCards().get(1) );
        board.getCards().remove(1);
    }

    /**
     * put a card on board and removes it from cards of player. getting inputs are among the method. the method would
     * handle invalid actions itself , like dropping a wild card while player has another valid card to drop. if there is
     * no valid Card for player to drop, the method would be ran for second time and grab a card from board. if this new
     * card is a valid card, the player has to drop it on the board. if the new card is not valid to be doped, the method
     * return null by meaning that this player did not managed to put a card on board and lost his turn.
     * @param gameManager gameManager of this game.
     * @param secondChance if the user has taken a new card in his turn due to not valid choices.
     * @return the card that is choose. null if no card is valid to be choose.
     */
    public Card putCard(GameManager gameManager,boolean secondChance)
    {
        Board board = gameManager.getBoard();
        Scanner scanner = new Scanner(System.in);
        int drawDanger = gameManager.getCountOfDraws();

        if( ! haveChoice(board.getCardOnBoard(),board.getColorOnBoard())) // checks if this player has any valid choice.
        {
            if(secondChance) // so this player has already used this chance.
                return null;
            System.out.println("there is no option . you have to grab a new card. this work is done automatically in 2 seconds.");
            grabCard(board);
            try {
                Thread.sleep(2000);
            }catch (InterruptedException ignored){};
           return putCard(gameManager,true);
        }

        if(secondChance) // for player to see its new card.
            gameManager.print();

        if(drawDanger != 0)
            System.out.println("you must put a Draw card or lose your turn and got punished.");

        while (true) {
            System.out.println("enter the number of card.");
            try
            {
                int index = scanner.nextInt() - 1;
                Card card = cardsInHand.get( index );
                //if player do not use a draw card which has to use.
                if(drawDanger != 0 && !(card instanceof DrawCard || card instanceof WildDrawCard ))
                {
                    for(int i=0 ; i<drawDanger ; i++)
                        grabCard(board);
                    System.out.println(" really? so you got what you deserve!");
                    gameManager.setCountOfDraws(0);
                    return null;
                }
                if( ! validChose(board.getCardOnBoard(),board.getColorOnBoard(),card,drawDanger>0))
                    continue;

                board.putCardOnBoard(card);
                cardsInHand.remove(index);
                return card;
            }
            catch (IndexOutOfBoundsException e) {
                System.out.println("not valid number.");
            }
        }
    }

    /**
     * checks if this card is valid to be used .
     * @param cardOnBoard card on board of the game.
     * @param colorOnBoard color of the board.
     * @return true if is valid to drop else false
     */
    public boolean haveChoice (Card cardOnBoard, char colorOnBoard)
    {
        for(Card card : cardsInHand) { // checks the color to be same
            if (card instanceof ColorCard)
                if (((ColorCard) card).getColor() == colorOnBoard)
                    return true;

            if(card.equals(cardOnBoard)) // if this card is exactly same as the card on board
                return true;

            if(card instanceof NumberCard && cardOnBoard instanceof NumberCard) // checks the number
                if(((NumberCard) card).getNumber() == ((NumberCard) cardOnBoard).getNumber())
                    return true;

            if(card instanceof ActionCard && cardOnBoard instanceof ActionCard) // checks if the actions  are same
                if(card.getClass() == cardOnBoard.getClass())
                    return true;

            if(card instanceof WildCard)
                return true;
        }
        return false;
    }

    /**
     * checks if the chosen card is valid depend on card on board and color of it.
     * @param cardOnBoard card on board
     * @param colorOnBoard color of board
     * @param card chosen card
     * @param dangerDraw if this player must use a draw Card due to the rules of game.
     * @return true if this choice is valid else false.
     */
    private boolean validChose (Card cardOnBoard, char colorOnBoard, Card card,boolean dangerDraw)
    {
        if (card instanceof ColorCard) // check to be same color
            if (((ColorCard) card).getColor() == colorOnBoard)
                return true;

        // check to be same action card or wild card
        if( card instanceof ActionCard && cardOnBoard instanceof ActionCard)
            if(card.getClass() == cardOnBoard.getClass())
                return true;

        if(card instanceof NumberCard && cardOnBoard instanceof NumberCard) // check numbers
            if( ((NumberCard) card).getNumber() == ((NumberCard) cardOnBoard).getNumber())
                return  true;


        for (Card playerCard : cardsInHand) {// check if there is any possible move depend on color , number and type.
            if (playerCard instanceof ColorCard)
                if (((ColorCard) playerCard).getColor() == colorOnBoard) {
                    System.out.println("there is another card with same color in your hand");
                    return false;
                }


            if(playerCard instanceof NumberCard && cardOnBoard instanceof NumberCard) // checks the number
                if(((NumberCard) playerCard).getNumber() == ((NumberCard) cardOnBoard).getNumber()) {
                    System.out.println("there is another card with same number.");
                    return false;
                }


            if((playerCard instanceof ActionCard && cardOnBoard instanceof ActionCard) && !(playerCard instanceof WildCard)) // checks the actions if are same
                if(playerCard.getClass() == cardOnBoard.getClass()) {
                    System.out.println("there is another action card like the card on board");
                    return false;
                }
        }

        if (card instanceof WildCard)// the last chance is to be wild card.
            return true;
        else
        {
            System.out.println("you must use a wild card.");
            return false;
        }
    }

    /**
     * get the cards of this player.
     * @return cards of this player
     */
    public LinkedList<Card> getCardsInHand() {
        return cardsInHand;
    }

    /**
     * print all cards and total score
     */
    public void print()
    {
        System.out.println("cards:");
         String[] cards =  GameManager.viewCards(cardsInHand);
         for(String line : cards)
             System.out.println(line);

         int sum = 0;
        for(Card card :cardsInHand)
            sum += card.getScore();
        System.out.println("score: " + sum);
    }
}
