/**
 * main playing board which is an 6*6 board made by four 3*3 board.
 * each smaller board ia able to be routed . all possible actions are done throw this class_ like putting a disc,
 * route a board check the end game and ... .
 * @author farzad
 * @version 0.0 (1.April.2020)
 */
public class MainPlayingBoard {
    private  final  static int SIZE = 6;   // board size.
    private Board[] boards ;   // 4 smaller boards.
    private Disc[][] mainPlayingBoard ;   //main 6*6 board.
    private char teamB;   // teamB character use for print method.
    private static final char emptyPlace = '\u2742';   //empty place character.


    /**
     * constructor: simple one for this class.
     * @param teamB black team character.
     * @param teamW white team character.
     */
    public MainPlayingBoard (char teamB , char teamW) // yes yes. teamW is not used. it's just a fake input.
    {
        this.teamB = teamB;

        boards = new Board[4];
        for (int i=0 ; i<4 ; i++)
            boards[i] = new Board();

        mainPlayingBoard = new Disc[SIZE][SIZE];
        for(int i=0 ;i<SIZE ; i++)
            for(int j=0 ; j<SIZE ; j++)
                mainPlayingBoard[i][j] = new Disc( emptyPlace );
    }


    /**
     * sync a smaller board with the main board. everything changes in one of 4 boards so after that
     * it must be synced with the main board .
     * @param index index of board in boards array.
     */
    private void montageBoards (int index)
    {
        switch (index)
        {
            case 0:
                for(int i=0; i< SIZE/2 ; i++)
                    for(int j=0 ; j< SIZE/2 ; j++)
                        mainPlayingBoard[i][j] = boards[index].getBoardPiece()[i%3][j%3];
            break;
            case 1:
                for(int i= 0; i< SIZE/2 ; i++)
                    for(int j= SIZE/2 ; j< SIZE ; j++)
                        mainPlayingBoard[i][j] = boards[index].getBoardPiece()[i%3][j%3];
                break;
            case 2:
                for(int i= SIZE/2; i< SIZE ; i++)
                    for(int j=0 ; j< SIZE/2 ; j++)
                        mainPlayingBoard[i][j] = boards[index].getBoardPiece()[i%3][j%3];
                break;
            case 3:
                for(int i=SIZE/2; i< SIZE ; i++)
                    for(int j= SIZE/2 ; j< SIZE ; j++)
                        mainPlayingBoard[i][j] = boards[index].getBoardPiece()[i%3][j%3];
                break;
        }
    }


    /**
     * its exactly 3 of replace method in String .
     * @param newString string to be converted.
     * @param oldCh1 old character 1
     * @param newCh1 new character to be replace with oldCh1
     * @param oldCh2 old character 2
     * @param newCh2 new character to be replace with oldCh2
     * @param oldCh3 old character 3
     * @param newCh3 new character to be replace with oldCh3
     * @return String: converted String.
     */
    private String replace (String newString,char oldCh1, char newCh1,char oldCh2, char newCh2,char oldCh3, char newCh3)
    {
        newString = newString.replace(oldCh1,newCh1);
        newString = newString.replace(oldCh2,newCh2);
         return newString.replace(oldCh3,newCh3);
    }

    /**
     * print the board.
     */
    public void print()
    {
        final String ANSI_YELLOW_BL = "\u001B[33m" , ANSI_RED_WI = "\u001B[31m";
        final String ANSI_WHITE_BO  = "\u001B[37m" , ANSI_RESET  = "\u001B[0m";

        String form = "#*************&*************@";
        String topLine = form.replace('*','\u2550');
        topLine = replace(topLine,'#','\u2554','&','\u2566','@','\u2557');
        String bottomLine = replace(topLine,'\u2554','\u255A','\u2566','\u2569','\u2557','\u255D');
        String middleLine = replace(topLine,'\u2554','\u2560','\u2566','\u256C','\u2557','\u2563');

        System.out.print("  ");
        for(int i= 'A' ; i < 'A' + SIZE ; i++)
            System.out.print("   " + (i=='D' ?"  ":"") +  (char) i);
        System.out.println();
        System.out.println("  " + topLine);

        for(int i=0 ; i<SIZE ; i++)
        {
            System.out.print( (i!=3 ?  i+1 + " " : "  " + middleLine + "\n4 ") + '\u2551');
            for (int j = 0; j < SIZE; j++)
            {
                char teamChar = mainPlayingBoard[i][j].getTeam();
                System.out.print("" + (j==3 ? "  " + '\u2551' + "  " :"  ") +
                        (teamChar == emptyPlace ? ANSI_WHITE_BO : teamChar == teamB ? ANSI_YELLOW_BL : ANSI_RED_WI) +
                        teamChar + ANSI_RESET);
            }
            System.out.println("  " + '\u2551');
        }
        System.out.println("  " + bottomLine);
    }

    /**
     * put a disc for a team on main board.
     * @param coordinate coordinate of the place to put the disc _ like "2E"
     * @param team the team's character.
     * @return boolean: return true if the disc is placed else false.
     */
    public boolean putDisc (String coordinate, char team)
    {
        int x = xCoordinate(coordinate);
        int y = yCoordinate(coordinate);
        int index = findIndexBoard(x,y);

        if (x == -1 || y == -1)
            return false;

        boolean temp = boards[index].putDisc(x,y,team);
        montageBoards(index);
        return temp;
    }

    /**
     * find the index of board in boards array.
     * @param x x coordinate of place.
     * @param y y coordinate of place.
     * @return int index of board in boards array.
     */
    private int findIndexBoard(int x, int y)
    {
        if (x/(SIZE/2) == 0  && y/(SIZE/2) == 0)
            return 0;

        else if (x/(SIZE/2) == 1  && y/(SIZE/2) == 0)
            return 1;

        else if (x/(SIZE/2) == 0  && y/(SIZE/2) == 1)
            return 2;

        else
            return 3;

    }


    /**
     * find the x coordinate of this coordinateString.
     * @param coordinate coordinate of place like "2E"
     * @return int : x coordinate.
     */
    int xCoordinate (String coordinate)
    {
        try {
            int x = (int)coordinate.charAt(1) - 65;
            if(x >=0 && x< SIZE)
                return x;
            return -1;
        }catch (StringIndexOutOfBoundsException e)
        {
            System.out.println("not valid coordinate_ must be like \"2E\"");
            return -1;
        }
    }

    /**
     * find the y coordinate of this coordinateString.
     * @param coordinate coordinate of place like "2E"
     * @return int : y coordinate.
     */
    int yCoordinate (String coordinate)
    {
        try {
            int y = Integer.parseInt(coordinate.substring(0, 1)) - 1;
            if (y >= 0 && y < SIZE)
                return y;
            return -1;
        }catch (NumberFormatException e)
        {
            System.out.println("first input should be an integer_ y coordinate");
            return -1;
        }
        catch (StringIndexOutOfBoundsException e)
        {
            System.out.println("the format must be like _ \"2E\"");
            return -1;
        }
    }


    /**
     * rote one of 4 boards in clockwise or anti clockwise.
     * @param index index of bord _ starts from 0 to 3;
     * @param direction  1 == clockwise , 2 == anti clockwise
     * @return boolean: return true if the board is routed else false.
     */
    public boolean roteBoard(int index, int direction)
    {
        try {
            boards[index].roteBoard(direction);
            montageBoards(index);
            return true;
        }catch (IndexOutOfBoundsException e)
        {
            System.out.println("not valid number for board.");
            return false;
        }
    }

    /**
     * checks if the player wins by the game's rules.
     * @param team team's character to be checked.
     * @return boolean: return true if the player wins the game else false.
     */
    public boolean winGame (char team) {
        for (int i = 0; i < SIZE; i++)
            if (checkLine(4, i, 0, team))
                return true;
        for (int j = 0; j < SIZE; j++)
            if (checkLine(2, 0, j, team))
                return true;
        if (checkLine(3, 0, 0, team))
            return true;
        if (checkLine(3, 0, 1, team))
            return true;
        if (checkLine(3, 1, 0, team))
            return true;
        if (checkLine(1, 0, SIZE - 1 , team))
            return true;
        if (checkLine(1, 0, SIZE - 2, team))
            return true;
        return checkLine(1, 1, SIZE - 1, team);
    }


    /**
     * check each line to find 5 disc of a team in row.
     * @param direction direction of this line to be checked _ a number of 0_7 , clockwise , starts from north.
     * @param x x coordinate of starting this line.
     * @param y y coordinate to start this line.
     * @param team team's character to be checked.
     * @return boolean return true if there are 5 disc of this team in line else false.
     */
    private boolean checkLine (int direction , int x , int y,char team)
    {
        int xGrowth = xDirection( direction );
        int yGrowth = yDirection( direction );
        String tempString = "";
        int sizeThisDirection = sizeOfThisLine(x,y,direction);
        String resultToBeFound = "*****";
        resultToBeFound = resultToBeFound.replace('*',team);

        for(int i=0 ; i<sizeThisDirection ; i++)
        {
            tempString = tempString.concat("" + mainPlayingBoard[y][x].getTeam());
            x += xGrowth;
            y += yGrowth;
        }

        return tempString.contains(resultToBeFound);
    }


    /**
     * find the size of this line with this direction in board
     * @param x x coordinate of line beginning
     * @param y y coordinate of line beginning
     * @param direction direction of this line _ a number of 0_7 , clockwise , starts from north.
     * @return int: size ( 5 or 6 )
     */
    int sizeOfThisLine ( int x , int y, int direction)
    {
        if(direction == 3 &&  ((x==0 && y == 1) || (x==1 && y==0) ) )
            return 5;
        if(direction == 1 &&  ((x==0 && y == 4) || (x==1 && y==5) ) )
            return 5;
        return 6;
    }


    /**
     * x growing of this direction.
     * @param direction direction of this line_ a number of 0_7 , clockwise , starts from north.
     * @return int: 1,0 or -1;
     */
    int xDirection (int direction)
    {
        if(direction > 0 && direction < 4)
            return 1;
        if(direction > 4 && direction < 8)
            return -1;

        return 0;
    }

    /**
     * y growing of this direction.
     * @param direction direction of this line_ a number of 0_7 , clockwise , starts from north.
     * @return int: 1,0 or -1;
     */
    int yDirection (int direction)
    {
        if( direction < 2 || direction > 6)
            return -1;
        if(direction > 2 && direction < 6)
            return 1;

        return 0;
    }

    /**
     * get main playing board of game
     * @return Disc[][] main playing board.
     */
    public Disc[][] getMainPlayingBoard() {
        return mainPlayingBoard;
    }

    /**
     * get 4 boards of main playing board.
     * @return Board[] boards of main playing board.
     */
    public Board[] getBoards() {
        return boards;
    }
}
