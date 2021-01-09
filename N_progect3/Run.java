import java.util.Scanner;

/**
 * game of Unio .this program lets you play this game in single mode or multi player mode both with no player's amount limit.
 * but printing for 2,3 or 4 players game is differ from +4 players game.
 * @author farzad
 * @version 1.0 (4.April.2020)
 */
public class Run {
    public static void main(String[] args) {
        System.out.println( "1-single player Game\n"
        +                   "2-multi player Game");
        Scanner scanner = new Scanner(System.in);
        int oper = 10;
        while (oper != 1 && oper != 2)
            oper = scanner.nextInt();
        if(oper == 1)
            singleGame();
        else
            multiGame();
    }

    private static void singleGame()
    {
        System.out.println("enter count of players _ 2,3 or 4 (you can play even mor than 4 player but the quality would become lower.");
        Scanner scanner = new Scanner(System.in);
        int count = -1;
        while (count < 2 )
            count = scanner.nextInt();
        GameManager gameManager = new GameManager(count-1 , 1);
        gameManager.print();
        while ( ! gameManager.endGame())
        {
            gameManager.putCard();
            gameManager.nextTurn();
            gameManager.print();
        }
        gameManager.scores();
    }

    private static void multiGame()
    {
        Scanner scanner = new Scanner(System.in);
        int count = -10;
        while ((count < 2)) {
            System.out.println("enter count of players _ 2,3 or 4 (you can play even mor than 4 player but the quality would become lower.");
            count = scanner.nextInt();
        }

        int countHuman = 10;
        while (!(countHuman <=count && countHuman > 0)) {
            System.out.println("enter count of human players. the rest players would be AI");
            countHuman = scanner.nextInt();
        }

        GameManager gameManager = new GameManager(count - countHuman , countHuman);
        gameManager.print();
        while ( ! gameManager.endGame())
        {
            gameManager.putCard();
            gameManager.nextTurn();
            gameManager.print();
        }
        gameManager.scores();
    }
}
