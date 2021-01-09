import java.net.URI;
import java.util.Scanner;

/**
 * this is the Pentago game. a game which is played on a 6*6 board which is made by four 3*3 smaller board.
 * this program enable the game to be played singular (play with an AI) or multi player.
 * @author farzad
 * @version 0.0 (1.April.2020)
 */
public class Java {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK_WI = "\u001B[31m";
    public static final String ANSI_YELLOW_BL = "\u001B[33m";

    /**
     * multi player game.
     */
    private static void multiGame ()
    {
        Scanner scanner = new Scanner(System.in);
        MainPlayingBoard playingBoard = new MainPlayingBoard('\u273F','\u2743');
        Player player_B = new Player('\u273F',playingBoard);
        Player player_W = new Player('\u2743',playingBoard);
        int turn = 0;
        Player currentPlayer ;
        playingBoard.print();

        while (true)
        {
            boolean flag = true;
           if(turn%2 == 0)
           {
               System.out.println("turn of player_" + ANSI_YELLOW_BL + player_B.getTeam() + ANSI_RESET);
               currentPlayer = player_B;
           }
           else
           {
               System.out.println("turn of player_" + ANSI_BLACK_WI + player_W.getTeam() + ANSI_RESET);
               currentPlayer = player_W;
           }

            while (flag) // put a disc on board.
            {
                System.out.println("enter a coordinate to put the disc");
                String coordinate = scanner.next();
                flag = !currentPlayer.putDisc(coordinate);
            }

            playingBoard.print();
            if(currentPlayer.winGame())
            {
                System.out.println("player_" + currentPlayer.getTeam() + " wins.");
                break;
            }

            flag = true;
            while (flag) // route a board.
            {
                System.out.println("choose on of 4 board to rote.");
                int index = scanner.nextInt() -1;
                System.out.println("1-clockwise   2-counterclockwise");
                int operator = scanner.nextInt();
                flag = !currentPlayer.roteBoard(index, operator);
            }

            playingBoard.print();


            if(player_B.winGame() && player_W.winGame())
            {
                System.out.println("draw!");
                break;
            }

            if(currentPlayer.winGame())
            {
                System.out.println("player_" + currentPlayer.getTeam() + " wins.");
                break;
            }

            turn++;
        }
    }

    /**
     * play with AI.
     */
    private static void singleGame ()
    {
        Scanner scanner = new Scanner(System.in);
        MainPlayingBoard playingBoard = new MainPlayingBoard('\u2743','\u273F');

        System.out.println("choose the AI level: ");
        System.out.println("1- moderate");
        System.out.println("2- defensive");
        System.out.println("3- offensive");

        Player player_W = new AI_Player('\u273F',playingBoard,'\u2743',scanner.nextInt());
        Player player_B = new Player('\u2743',playingBoard);
        int turn = 0;
        Player currentPlayer ;
        playingBoard.print();

        while (true)
        {
            String rotationCoordinateIO = "" ;
            boolean flag = true;
            if(turn%2 == 0)
            {
                System.out.println("turn of player_" + ANSI_YELLOW_BL + player_B.getTeam() + ANSI_RESET);
                currentPlayer = player_B;
            }
            else
            {
                System.out.println("turn of player_" + ANSI_BLACK_WI + player_W.getTeam() + ANSI_RESET);
                currentPlayer = player_W;
            }

            if(currentPlayer == player_B) { // put a disc
                while (flag) {
                    System.out.println("enter a coordinate to put the disc");
                    String coordinate = scanner.next();
                    flag = !currentPlayer.putDisc(coordinate);
                }
            }
            else
            {
                rotationCoordinateIO = ((AI_Player)player_W).putDiscIo();
            }

            playingBoard.print();

            if(currentPlayer.winGame())
                {
                    System.out.println("player_" + currentPlayer.getTeam() + " wins.");
                    break;
                }

            flag = true;
            if(currentPlayer == player_B) { // route a board
                while (flag) {
                    System.out.println("choose on of 4 board to rote.");
                    int index = scanner.nextInt() - 1;
                    System.out.println("1-clockwise   2-counterclockwise");
                    int operator = scanner.nextInt();
                    flag = !currentPlayer.roteBoard(index, operator);
                }
            }
            else {
                if( ! rotationCoordinateIO.equals(""))
                    ((AI_Player)player_W).roteBoard(rotationCoordinateIO);
                else
                    ((AI_Player)player_W).roteBoard("1_1");
            }

            playingBoard.print();

            if(player_B.winGame() && player_W.winGame())
            {
                System.out.println("draw!");
                break;
            }

            if(currentPlayer.winGame())
            {
                System.out.println("player_" + currentPlayer.getTeam() + " wins.");
                break;
            }

            turn++;
        }
    }

    /**
     * print the menu
     */
    private static void menu ()
    {
        System.out.println( "1-single game\n" +
                            "2-multi player game\n" +
                            "3-tutorial\n" +
                            "4-exit");
    }

    /**
     * print the tutorial
     */
    private static void tutorial()
    {
        System.out.println("coordinate: the coordinates are like  \"2D\".");
        System.out.println("boards' number:  1 2\n" +
                           "                 3 4");
        System.out.println("to see hole tutorial see the web page.");
        System.out.println("1-open the page  2-exit");
        if( new Scanner(System.in).nextInt() == 1)
        {
            try {
                URI url = new URI("https://www.ultraboardgames.com/pentago/game-rules.php");
                java.awt.Desktop.getDesktop().browse(url);
            }catch (Exception e ){
                System.out.println("something went wrong.");
            }

        }
    }


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int operator = 1;

        while (operator != 4)
        {
            System.out.println("\n");
            menu();
            operator = scanner.nextInt();
            switch (operator)
            {
                case 1:
                    singleGame();
                break;

                case 2:
                    multiGame();
                    break;

                case 3:
                    tutorial();
            }
        }
    }
}
