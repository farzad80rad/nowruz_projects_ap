import java.net.URI;
import java.util.Scanner;

/**
 * this is the Othello game. this program let you play the game with an IO or another player in the same
 * computer. the game is played in 8*8 board.
 * @author farzad
 * @version 0.0 (28.3.2020)
 */
public class Run {

    /**
     * the condition of  the game to be continued.
     * @param p1 player 1
     * @param p2 player 2
     * @return true if the game can be continued.
     */
    private static boolean continueGame(Player p1, Player p2)
    {
        return (p1.canPlay() || p2.canPlay());
    }


    private static void menu()
    {
        System.out.println( "1- single player game\n" +
                            "2- multi player game\n" +
                            "3- tutorial\n" +
                            "4- exit");
    }


    /**
     * multi player game mood. playing with another human .
     */
    private static void multiPlayerGame()
    {
        Scanner scanner = new Scanner(System.in);
        Board board = new Board();
        board.print();
        Player player_W = new Player('W',board);
        Player player_B = new Player('B',board);

        Player player ;
        int turn =0;


        while ( continueGame(player_B,player_W) )
        {
            if(turn%2 == 0) {
                player = player_B;
                System.out.println("turn of player  " +  "black("+'\u2740'+")");
            }
            else
            {
                player = player_W;
                System.out.println("turn of player  " + "white("+'\u273f'+")");
            }

            if(player.canPlay()) {
                System.out.println("enter coordinate  to place the disc");
                String coordinate = scanner.next();
                if( coordinate.length() != 2)
                {
                    System.out.println("not valid . must be like \"2B\"");
                    continue;
                }
                turn += player.putDisc(coordinate);
                board.print();
            }
            else
                System.out.println("there is no place to put a disk. your turn is skipped.");

            turn++;
        }

        if(board.countDiscsW() > board.countDiscsB())
            System.out.println("player White ("+'\u273f'+") wins");
        else
            System.out.println("player Black ("+'\u2740'+") wins");


    }


    /**
     * single game mode. playing whit AI.
     */
    private  static void singleGame()
    {
        Scanner scanner = new Scanner(System.in);
        Board board = new Board();
        board.print();
        Player player_W = new Player('W',board);
        Player player_B = new IoPlayer('B',board);
        Player player ;

        int turn =0;


        while ( continueGame(player_B,player_W) )
        {
            if(turn%2 == 1) {
                player = player_B;
                System.out.println("turn of player  " +  "black("+'\u2740'+")");
            }
            else
            {
                player = player_W;
                System.out.println("turn of player  " + "white("+'\u273f'+")");
            }

            if(player.canPlay()) {
                if(!(player instanceof  IoPlayer)) {
                    System.out.println("enter coordinate  to place the disc");
                    String coordinate = scanner.next();
                    if( coordinate.length() != 2)
                    {
                        System.out.println("not valid . must be like \"2B\"");
                        continue;
                    }
                    turn += player.putDisc(coordinate);
                }
                else {
                    try {
                        Thread.sleep(1300);
                    } catch (InterruptedException ignored){}

                    ((IoPlayer) player).putDisc();
                }


                board.print();
            }
            else
                System.out.println("there is no place to put a disk. your turn is skipped.");

            turn++;
        }

        if(board.countDiscsW() > board.countDiscsB())
            System.out.println("player White ("+'\u273f'+") wins");
        else
            System.out.println("player Black ("+'\u2740'+") wins");

    }

    /**
     * open a website for more things to learn about this game
     */
    private static void tutorial()
    {
        System.out.println("the format of coordinate is like \"1A\" for tutorial see the page");
        try {
            URI uri = new URI("https://www.eothello.com");
            java.awt.Desktop.getDesktop().browse(uri);
            System.out.println("a page got opened in browser.");
        }catch (Exception e)
        {
            System.out.println("\nsomething went wrong.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int oper = 1;
        while (oper != 4) {
            menu();
            oper = scanner.nextInt();
            scanner.nextLine();

            switch (oper)
            {
                case 1:
                    singleGame();
                    break;
                case 2:
                    multiPlayerGame();
                    break;
                case 3:
                    tutorial();
            }
        }
    }
}
