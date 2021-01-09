import java.util.*;

/**
 * manage hole things about the game and relates players with board. this is like a real manager who shuffle the cards,
 * distribute it among players , enforce the rules and give turn to players.
 */
public class GameManager {
    private Board board ;  // board of this game
    private Player[] players;  // all players of game
    private int rotationDirection;  // -1 or 1;
    private int turn;  // shows the turn.
    private int countOfDraws;  // how many cards should be taken by player due to DrawCards on board.


    /**
     * make all cards, shuffle and distribute it among players and finally puts a card as card on board.
     * @param countAiPlayer count of AI players.
     * @param countHumanPlayer  count of human players.
     */
    public GameManager(int countAiPlayer, int countHumanPlayer)
    {
        countOfDraws = 0;
        Random random = new Random();
        rotationDirection = 1;
        turn =  Math.abs(random.nextInt())+321;

        LinkedList<Card> allCards = makeCards(countAiPlayer + countHumanPlayer);
        players = new Player[countAiPlayer + countHumanPlayer];
        for(int i=0 ; i<countAiPlayer ; i++)
            players[i] = new AI_Player();
        for(int i= countAiPlayer ; i< countHumanPlayer + countAiPlayer ; i++)
            players[i] = new Player();


        for(int indexPlayer=0 ; indexPlayer<countHumanPlayer + countAiPlayer ; indexPlayer++) {
            for (int j = 0; j < 7; j++) {
                players[indexPlayer].grabThisCard(allCards.get(1));
                allCards.remove(1);
            }
        }
        ArrayList<Player> playersA = new ArrayList<>(Arrays.asList(players));
        Collections.shuffle(playersA);
        int i=0 ;
        for(Player player :playersA) {
            players[i] = player;
            i++;
        }

        board= new Board(allCards);
        if(board.getCardOnBoard() instanceof  ActionCard)
            ((ActionCard)board.getCardOnBoard()).action(this);

        if(board.getCardOnBoard() instanceof DrawCard || board.getCardOnBoard() instanceof WildDrawCard)
            nextTurn();


    }

    /**
     * make all needed cards.
     * @param contPlayer count of this game player
     * @return all cards
     */
    private LinkedList<Card> makeCards(int contPlayer )
    {
        LinkedList<Card> cards = new LinkedList<>();
        char[] colors = new char[]{'B','R','G','Y'};

        for(int k =0 ; k<contPlayer/7 + 1 ; k++) {
            for (char color : colors) {// make colored card.
                for (int number = 0; number < 10; number++) // make number cards
                    for (int i = 0; i < (number == 0 ? 1 : 2); i++)
                        cards.add(new NumberCard(number, color, number));

                for (int i = 0; i < 2; i++) { // make Action cards
                    cards.add(new SkipCard(20, color));
                    cards.add(new ReversCard(20, color));
                    cards.add(new DrawCard(20, color));
                }
            }
            for (int i = 0; i < 4; i++) // make wild cards
            {
                cards.add(new WildCard(50));
                cards.add(new WildDrawCard(50));
            }
        }

        Collections.shuffle(cards);

        return cards;
    }


    /**
     * make a view for given cards .
     * @param cards all cards.
     * @return view of this cards to be printed in console.
     */
    public static String[] viewCards(LinkedList<Card> cards)
    {
        String[] viewCards = new String[6];
        for(int i=0 ; i<6 ; i++)
            viewCards[i] = "";

        for(int i=0 ; i<cards.size(); i++)
        {
            String[] viewThisCard = cards.get(i).cardView(i == cards.size() -1);
            for(int j=0 ; j<6 ; j++)
                viewCards[j] += viewThisCard[j];
        }
        return viewCards;
    }


    /**
     * make a view for top , right and left player depend on input number.
     * @param cards cards of this player.
     * @param playerNumber 1==left  2== top   3==right
     * @return view for this player in board.
     */
    private String[] viewHiddenCards(LinkedList<Card> cards, int playerNumber)
    {
        String[] hiddenView ;
        String[] emptyView ;
        if(playerNumber == 1 || playerNumber == 3)
        {
            hiddenView = new String[]{
                    "┍━━━━━━━━┑",
                    "┍━━━━━━━━┑",
                    "┍━━━━━━━━┑",
                    "┍━━━━━━━━┑",
                    "┍━━━━━━━━┑",
                    "┍━━━━━━━━┑",
                    "┍━━━━━━━━┑",
                    "┍━━━━━━━━┑",
                    "┍━━━━━━━━┑",
                    "┍━━━━━━━━┑",
                    "┍━━━━━━━━┑",
                    "┍━━━━━━━━┑",
            };
        }
        else
        {

            hiddenView = new String[]
                    {
                            "━━┑━━┑━━┑━━┑━━┑━━┑━━┑━━┑━━┑━━┑━━┑━━┑",
                            "  │  │  │  │  │  │  │  │  │  │  │  │",
                            "  │  │  │  │  │  │  │  │  │  │  │  │",
                            "  │  │  │  │  │  │  │  │  │  │  │  │",
                            "  │  │  │  │  │  │  │  │  │  │  │  │",
                            "━━┙━━┙━━┙━━┙━━┙━━┙━━┙━━┙━━┙━━┙━━┙━━┙"
                    };
            try {
                for (int i = 0; i < 6; i++)
                    hiddenView[i] = (hiddenView[i].substring(0, 3 * cards.size() - 3) + makeSpace(12 * 3 - 3 * (cards.size() - 1)));
            }catch (StringIndexOutOfBoundsException e)
            {
                if(cards.size() > 13)
                    return hiddenView;
                else
                    return emptyView = new String[]{ // player has no card.
                            "                                    ",
                            "                                    ",
                            "                                    ",
                            "                                    ",
                            "                                    ",
                            "                                    ",
                    };
            }
        }
        return hiddenView;
    }

    /**
     * make a string with space character.
     * @param i amount of spaces
     * @return string just with space.
     */
    private String makeSpace (int i)
    {
        StringBuilder space = new StringBuilder();
        for(int j=0 ; j<i ; j++)
            space.append(" ");
        return space.toString();
    }

    /**
     * calculate amount of space needed to show current player on board
     * @param countCard count of current player cards.
     * @return amount of space character needed.
     */
    private int calculateSize(int countCard)
    {
        return 85 - countCard*6 + 4;
    }


    /**
     * make a hidden form for a player that  just shows its number and count of cards.
     * @param count count of cards of this player
     * @param player number of this player in game
     * @return hidden form for this player.
     */
    private String[] makeViewCardHidden(int count,int player)
    {
        final String WHITE = "\u001B[37m";

        String[] view = new String[]{
                "┍━━━━━━━━┑ "+WHITE+"# " + Card.ANSI_RESET,
                "│ count: │ "+WHITE+"# "+ Card.ANSI_RESET,
                "│   ii   │ "+WHITE+"# "+ Card.ANSI_RESET,
                "│ Player │ "+WHITE+"# "+ Card.ANSI_RESET,
                "│   jj   │ "+WHITE+"# "+ Card.ANSI_RESET,
                "┕━━━━━━━━┙ "+WHITE+"# "+ Card.ANSI_RESET,
                "   turn    "+WHITE+"# "+ Card.ANSI_RESET
        };
        for(int i=0 ; i<7 ; i++)
            view[i] = view[i].replace('#', rotationDirection == 1? '\u21E8':'\u21E6');
        view[2] = view[2].replaceAll("ii","" +count + (count < 10?" ":""));
        view[4] = view[4].replaceAll("jj","" +(player )+ ((player )< 10?" ":""));
        view[6] = view[6].replaceAll("turn",turn%players.length == player - 1 ?"turn":"    " );
        return view;
    }


    /**
     * make all hidden cards of all players that contains their numbers and count of cards.
     * @return String[7] that contains all hidden cards of all players that contains their numbers and count of cards.
     */
    private String[] makeAllViewCardsHidden()
    {
        StringBuilder[] stringBuilders = new StringBuilder[7];
                for(int i=0 ; i<7 ; i++)
                    stringBuilders[i] = new StringBuilder();
        int i=1;
        for(Player player :players)
        {
            String[] cardHiddenView = makeViewCardHidden(player.getCardsInHand().size(),i);
            for(int j=0; j<7 ; j++)
                stringBuilders[j].append(cardHiddenView[j]);
            i++;
        }
        String[] result = new String[7];
        for(i=0 ; i<7 ; i++)
            result[i] = stringBuilders[i].toString();

        return result;
    }

    /**
     * for more than 4 player this program must use this kind of printing method.
     */
    private void BoardPrintAbove5()
    {

        System.out.println();
        char colorChar = board.getColorOnBoard();
        String color = colorChar == 'R'? Card.ANSI_RED:colorChar == 'G'?Card.ANSI_GREEN: colorChar == 'Y' ? Card.ANSI_YELLOW:Card.ANSI_BLUE;

        String[] cardOnBoard = board.getCardOnBoard().cardView(true);

        String[] allCards = makeAllViewCardsHidden();
        for(int i=0 ; i<6 ; i++)
            System.out.println(cardOnBoard[i] + "    " + allCards[i]);
        System.out.println(color+"color board"+ Card.ANSI_RESET + "   " + allCards[6]);

        if(!(players[turn%players.length] instanceof AI_Player)) {
            String[] currentPlayerView = viewCards(players[turn % players.length].getCardsInHand());
            for (String string : currentPlayerView)
                System.out.println(string);
            System.out.print("   ");
            for(int i=1 ; i<=players[turn%players.length].getCardsInHand().size() ; i++)
                System.out.print("" + i +"     " );
        }
        else
            System.out.println("AI turn. you cant see their cards");

        System.out.println();
    }




    /**
     * make the visual board to be printed for at last 4 player.
     * @return visual board.
     */
    private String[] makeBoardPrintLess5()
    {

        final String RESET = "\u001B[0m";
        final String WHITE = "\u001B[37m";
        final String YELLOW = "\u001B[33m";

        String[] strings =  new String[]{
                YELLOW+"╔══════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗]]",
                "║ "+RESET+"         AAAAAAAAAA               ┍━━━━━━━━┑BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB               CCCCCCCCCC       "+YELLOW+" ║]]",//1
                "║  "+RESET+"        AAAAAAAAAA               │ count: │BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB               CCCCCCCCCC     "+YELLOW+"   ║]]",//2
                "║  "+RESET+"        AAAAAAAAAA               │   11   │BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB               CCCCCCCCCC     "+YELLOW+"   ║]]",//3
                "║  "+RESET+"        AAAAAAAAAA               │ player:│BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB               CCCCCCCCCC     "+YELLOW+"   ║]]",//4
                "║  "+RESET+"        AAAAAAAAAA               │  _2_   │BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB               CCCCCCCCCC     "+YELLOW+"   ║]]",//5
                "║  "+RESET+"        AAAAAAAAAA               ┕━━━━━━━━┙BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB               CCCCCCCCCC     "+YELLOW+"   ║]]",//6
                "║  "+RESET+"        AAAAAAAAAA                   !                                                        CCCCCCCCCC     "+YELLOW+"   ║]]",//7
                "║  "+RESET+"        AAAAAAAAAA                                                                            CCCCCCCCCC     "+YELLOW+"   ║]]",//8
                "║  "+RESET+"        AAAAAAAAAA                        "+WHITE+" ┍━━━━━━━━┑]]&&&&&&&&&&                             CCCCCCCCCC     "+YELLOW+"   ║]]",//9
                "║  "+RESET+"        AAAAAAAAAA                        "+WHITE+" │ cards  │]]&&&&&&&&&&                             CCCCCCCCCC     "+YELLOW+"   ║]]",//10
                "║  "+RESET+"        AAAAAAAAAA                        "+WHITE+" │   on   │]]&&&&&&&&&&                             CCCCCCCCCC     "+YELLOW+"   ║]]",//11
                "║  "+RESET+"        AAAAAAAAAA                        "+WHITE+" │ board  │]]&&&&&&&&&&                             CCCCCCCCCC     "+YELLOW+"   ║]]",//12
                "║  "+RESET+"        ┍━━━━━━━━┑                        "+WHITE+" │        │]]&&&&&&&&&&                             ┍━━━━━━━━┑     "+YELLOW+"   ║]]",//13
                "║  "+RESET+"        │ count: │                        "+WHITE+" ┕━━━━━━━━┙]]&&&&&&&&&&                             │  count:│      "+YELLOW+"  ║]]",//14
                "║  "+RESET+"        │   33   │ @                         rotation direction:                           #  │   55   │      " +YELLOW+ "  ║]]",//15
                "║  "+RESET+"        │ player:│                             anti  clockwise                                │ player:│      "+YELLOW+"  ║]]",//16
                "║  "+RESET+"        │  _4_   │                                                                            │   _6_  │      "+YELLOW+"  ║]]",//17
                "║  "+RESET+"        ┕━━━━━━━━┙      $                                                                     ┕━━━━━━━━┙      "+YELLOW+"  ║]]",//18
                "║  "+RESET+"                 RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR"+YELLOW+"  ║]]",//19
                "║  "+RESET+"                 RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR"+YELLOW+"  ║]]",//20
                "║  "+RESET+"                 RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR"+YELLOW+"  ║]]",//21
                "║  "+RESET+"                 RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR"+YELLOW+"  ║]]",//22
                "║  "+RESET+"                 RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR"+YELLOW+"  ║]]",//23
                "║  "+RESET+"                 RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR"+YELLOW+"  ║]]",//24
                "║  "+RESET+"                    1     2     3     4     5     6     7     8     9    10    11    12    13    14   15   16  "+YELLOW+" ║]]",//25
                "╚══════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝"+RESET
        };

        for(int i=0 ; i<27 ; i++) // this ]] are for an error that can't remember.
            strings[i] = strings[i].replaceAll("]]","  ");

        strings[16] = strings[16].replaceAll("anti",rotationDirection==1?"    ":"anti");// shows the rotation direction.

        int turn = getTurn();
        while (players[turn%players.length] instanceof AI_Player) // find main player hows location is at down of board.
            turn -= rotationDirection;

        int indexTopPlayer = -1, indexRightPlayer = -1,indexLeftPlayer = -1;

        // make each player location on board depend on main player found above.
        if(players.length == 4)
        {
            indexLeftPlayer = (turn-3)%players.length;
            indexRightPlayer = (turn-1)%players.length;
            indexTopPlayer = (turn-2)%players.length;
        }
        if(players.length == 3)
        {
            indexLeftPlayer = (turn- 2)%players.length;
            indexRightPlayer = (turn-1)%players.length;
        }
        if(players.length == 2)
            indexTopPlayer = (turn-1)%players.length;



        String countCards;
        try { // top player.
            countCards = String.valueOf(players[indexTopPlayer].getCardsInHand().size());
            strings[3] = strings[3].replaceAll("11",countCards.length()<2?" " + countCards:countCards);//count of cards
            strings[5] = strings[5].replace("2",String.valueOf(indexTopPlayer+1));// number of player

            String[] topPlayerCards = viewHiddenCards(players[indexTopPlayer].getCardsInHand(),2);
            for(int i=1 ; i<7 ; i++)
                strings[i] = strings[i].replaceAll("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB",topPlayerCards[i-1]);// visual hidden cards
            strings[7] = strings[7].replace('!',indexTopPlayer == this.turn%players.length ?'\u2611':' ');// turn
        }
        catch (IndexOutOfBoundsException e) { // this user is absent. so make it disappear
            String[] cardBeDelete = new String[6];
            for(int i=0 ;i<6 ; i++) {
                cardBeDelete[i] = strings[i + 1].substring(33, 86);
                strings[i + 1] = strings[i +1].replaceAll(cardBeDelete[i],"                                                     ");
            }
            strings[7] = strings[7].replace('!',' ');
        }

        try{ // right and left player.
            countCards = String.valueOf(players[indexLeftPlayer].getCardsInHand().size());
            strings[15] = strings[15].replaceAll(" 33 "," " + (countCards.length()<2?" " + countCards :countCards )+ " "); // count of cards
            strings[17] = strings[17].replace("4",String.valueOf(indexLeftPlayer+1)); // number of player

            countCards = String.valueOf(players[indexRightPlayer].getCardsInHand().size());
            strings[15] = strings[15].replaceAll(" 55 "," " + (countCards.length()<2?" " + countCards :countCards )+ " " );// count of cards
            strings[17] = strings[17].replaceAll("6",String.valueOf(indexRightPlayer+1)); // number of player

            String[] rightPlayerCards = viewHiddenCards(players[indexRightPlayer].getCardsInHand(),1);
            String[] leftPlayerCards = viewHiddenCards(players[indexLeftPlayer].getCardsInHand(),3);
            int counter = 1;
            for(int i=12 ; i>0 ; i--) // make the hidden cards.
            {
                if(players[indexRightPlayer].getCardsInHand().size() > counter)
                    strings[i] = strings[i].replaceAll("CCCCCCCCCC",rightPlayerCards[i -1 ]);
                else
                    strings[i] = strings[i].replaceAll("CCCCCCCCCC","          ");
                if(players[indexLeftPlayer].getCardsInHand().size() > counter)
                    strings[i] = strings[i].replaceAll("AAAAAAAAAA",leftPlayerCards[i-1]);
                else
                    strings[i] = strings[i].replaceAll("AAAAAAAAAA","          ");
                counter++;
            }
            strings[15] = strings[15].replace('@',indexLeftPlayer == this.turn%players.length ?'\u2611':' '); // turn
            strings[15] = strings[15].replace('#',indexRightPlayer == this.turn%players.length ?'\u2611':' '); // turn
        }
        catch (IndexOutOfBoundsException e) { // both players are absent. so must be disappeared.
            for(int i=12 ; i>0 ; i--) {
                    strings[i] = strings[i].replaceAll("AAAAAAAAAA","          ");
                    strings[i] = strings[i].replaceAll("CCCCCCCCCC","          ");
            }
            for(int i= 13 ; i <19 ; i++)
            {
                String leftStringDelete  = strings[i].substring(10,25);
                String rightStringDelete = strings[i].substring(strings[i].indexOf(YELLOW)-20,strings[i].indexOf(YELLOW));
                strings[i] = strings[i].replaceAll(rightStringDelete,"                    ");
                strings[i] = strings[i].replaceAll(leftStringDelete,"               ") ;
            }

            strings[15] = strings[15].replace('@',' ');
            strings[15] = strings[15].replace('#',' ');
        }


        String[] cardOnBoardView = board.getCardOnBoard().cardView(true);
        char color = board.getColorOnBoard();
        String colorCode = (color == 'R'? Card.ANSI_RED : color == 'B'? Card.ANSI_BLUE: color == 'G'? Card.ANSI_GREEN:Card.ANSI_YELLOW );
        char charReplace = (color == 'R'? '\u2665': color == 'B'? '\u2660': color == 'G'? '\u2663':'\u2666' );

        for(int i=9 ; i<15 ; i++) { // make the card on board visible
            strings[i] = strings[i].replaceAll("&&&&&&&&&&", cardOnBoardView[i - 9]);
            if(strings[i].contains(Card.ANSI_PURPLE)) { // if the card in wild card, change the purple color to color of board.
                strings[i] = strings[i].substring(0,strings[i].indexOf(Card.ANSI_PURPLE)) + colorCode + strings[i].substring(strings[i].indexOf(Card.ANSI_PURPLE)+5) ;
                strings[i] = strings[i].replaceAll("✎",charReplace + " ");
            }
        }

        //bottom player
        strings[18] = strings[18].replace('$',turn == this.turn ?'\u2611':' ');
        String[] playerCardView = viewCards(players[turn%players.length].getCardsInHand());
        for(int i=19 ; i<25 ; i++) // make the cards visible.
            strings[i] = strings[i].replaceAll("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR"
                    ,(players[turn%players.length].getCardsInHand().size() == 0?"    ":"")+
                    playerCardView[i-19] + makeSpace( calculateSize(players[turn%players.length].getCardsInHand().size())));


        for(int i=16 ; i>=0 ; i--) // shows number for each card.
            if( i > players[turn%players.length].getCardsInHand().size()) {
                String number = " " + i + " ";
                strings[25] = strings[25].replaceAll(number,  (number.length() == 3?"   ":"    ") );
            }
        return strings;
    }


    /**
     * prints the board
     */
    public void print()
    {
        if(players.length <= 4) {
            String[] board = makeBoardPrintLess5();
            for (String temp : board)
                System.out.println(temp);
        }
         else
             BoardPrintAbove5();
    }


    /**
     * put a card on board by the player hows turn is.
     */
    public void putCard()
    {
        Card card;
        int indexPlayer = turn % (players.length);
        card = players[indexPlayer].putCard(this,false);
        if(card != null)
            if(card instanceof ActionCard)
                 ((ActionCard) card).action(this);
        else
            countOfDraws = 0;
    }

    /**
     * checks if the game is over.
     * @return true if the game is over
     */
    public boolean endGame()
    {
        for(Player player : players)
            if(player.getCardsInHand().size() == 0)
                return true;
        return false;
    }

    /**
     * shows the scores of players and their cards.
     */
    public void scores() {


        Integer[] scoors = new  Integer[players.length];
        for (int i = 0; i < players.length; i++) {
            int sum = 0;
            for (Card cards : players[i].getCardsInHand())
                sum += cards.getScore();
            scoors[i] = sum;
        }

        for(int i=0 ; i<players.length ; i++){
            ArrayList<Integer> scores = new  ArrayList<>(Arrays.asList(scoors)) ;
            System.out.println("_____________________________");
            Integer minKey = Collections.min(scores);
            int indexPlayer = scores.indexOf(minKey);
            System.out.println("player_" + (indexPlayer + 1) + "_");
            players[indexPlayer].print();
            scoors[indexPlayer] = 3000;
            System.out.println("_____________________________");
        }
    }


    /**
     * get turn of game
     * @return turn.
     */
    public int getTurn() {
        return turn;
    }

    /**
     * get rotation direction
     * @return return rotation direction
     */
    public int getRotationDirection() {
        return rotationDirection;
    }

    /**
     * set rotation direction
     * @param rotationDirection rotation direction
     */
    public void setRotationDirection(int rotationDirection) {
        this.rotationDirection = rotationDirection;
    }

    /**
     * get players of game
     * @return players
     */
    public Player[] getPlayers() {
        return players;
    }

    /**
     * pass this turn.
     */
    public void nextTurn ()
    {
        turn += rotationDirection;
    }

    /**
     * get board of the game
     * @return board of the game
     */
    public Board getBoard() {
        return board;
    }

    /**
     * get count of cards should be taken by player due to DrawCards on board.
     * @return count of cards should be taken by player due to DrawCards on board.
     */
    public int getCountOfDraws() {
        return countOfDraws;
    }

    /**
     * set count of cards should be taken by player due to DrawCards on board.
     * @param countOfDraws count of cards should be taken by player due to DrawCards on board.
     */
    public void setCountOfDraws(int countOfDraws) {
        this.countOfDraws = countOfDraws;
    }

}
