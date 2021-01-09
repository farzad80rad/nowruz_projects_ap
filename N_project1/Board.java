/**
 * board is where players put their discs on it and is the core of this program.
 * this class anabel a player to put discs and also checks if the place is valid.
 * @author farzad
 * @version 0.0 (28.3.2020)
 */
public class Board {
    private final int SIZE = 8; // size of playing board.

    private Disc[][] board ; // this is the board which is made by discs.
    private final char TEAM_B = 'B';
    private final char TEAM_W = 'W';


    /**
     * constructor: simple one which put 4 discs at the middle of board_ 2 for each team.
     */
    public Board()
    {
        board = new Disc[SIZE][SIZE];
        for(int i=0; i<SIZE ; i++)
            for(int j=0; j<SIZE ; j++)
                board[i][j] = new Disc();

        putDisc("4D", TEAM_W);
        putDisc("5D", TEAM_B);
        putDisc("4E", TEAM_B);
        putDisc("5E", TEAM_W);
    }


    /**
     * put a disc on board. if the place is not valid, would print a message.
     * @param coordinate coordinate of place in format like "2A".
     * @param team team's character.
     * @return boolean: true if the disk is placed else false.
     */
    public boolean putDisc (String coordinate, char team)
    {
        int x = xCoordinate( coordinate );
        int y = yCoordinate( coordinate );

        if( x != -1 && y != -1)
        {
            board[y][x].putTeam( team );
            convertDiscs(x,y);
            return true;
        }
        else
        {
            System.out.println("not valid coordinate");
            return false;
        }
    }


    /**
     * convert a form like "2E" into two int to be used as index of array and return the x value.
     * so the form example is y=1 and x=4.
     * @param coordinate coordinate code as format of "2E"
     * @return x coordinate to be used in index.
     */
    private int xCoordinate (String coordinate)
    {
        if(coordinate.length() != 2)
        {
            System.out.println("coordinate should be as format of \"2B\"");
            return -1;
        }
        try {
            int x = (int)coordinate.charAt(1) - 65;
            if(x >=0 && x< SIZE)
                return x;
            return -1;
        }catch (StringIndexOutOfBoundsException e)
        {
            System.out.println("coordinate should be as format of \"2B\"");
            return  -1;
        }


    }

    /**
     * convert a form like "2E" into two int to be used as index of array and return the y value.
     * so the form example is y=1 and x=4.
     * @param coordinate coordinate code as format of "2E"
     * @return y coordinate to be used in index.
     */
    private int yCoordinate (String coordinate)
    {
        if(coordinate.length() != 2)
        {
            System.out.println("coordinate should be as format of \"2B\"");
            return -1;
        }
        try {
            int y = Integer.parseInt(coordinate.substring(0, 1)) - 1;
            if (y >= 0 && y < SIZE)
                return y;
            return -1;
        }catch (NumberFormatException e) { return -1;}
    }


    /**
     * prints the board and the amount of each team's discs.
     */
    public void print()
    {

        System.out.println( "\n\n Black(" + String.valueOf('\u2740') + ") : " + countDiscsB() +
                            "  |  white(" + String.valueOf('\u273f') +") : " + countDiscsW());
        System.out.print(" ");
        for(int i=65; i <73; i++)
            System.out.print(((i%2 == 1 && i!=69 )?"  " : "   ")+ (char)i);
        System.out.println();
        for(int i=0 ; i<SIZE ; i++) {
            System.out.print(i+1);
            for (int j = 0; j < SIZE; j++)
            {
                char temp = board[i][j].getTeam();
                System.out.print("  " + (temp=='*' ? '\u271B' : temp=='B' ? '\u2740' : '\u273f'));
            }
            System.out.println();
        }
    }

    /**
     count the numbers of player W discs on board.
     * @return count  W discs on board
     */
    public int countDiscsW()
    {
        int count =0;
        for(int y=0; y<SIZE ; y++)
            for(int x=0; x<SIZE ; x++)
                if (board[y][x].getTeam() == TEAM_W)
                    count ++;

        return count;
    }

    /**
     * count the numbers of player B discs on board.
     * @return count  B discs on board
     */
    public int countDiscsB()
    {
        int count =0;
        for(int y=0; y<SIZE ; y++)
            for(int x=0; x<SIZE ; x++)
                if (board[y][x].getTeam() == TEAM_B)
                    count ++;

        return count;
    }


    /**
     * change all opponent discs which are able to be changed. check all 8 possible direction and convert
     * opponent discs into allied discs if are able to be .
     * @param x x coordinate of the place in board.
     * @param y y coordinate of the place in board.
     */
    public void convertDiscs(int x, int y)
    {
        char team = board[y][x].getTeam();
        for (int i=0; i<8 ; i++)
            if( validDirection(i,y,x,team))
                convertDiscsInLine(i,x,y);

    }

    /**
     * converts all opponent discs in a specific line.
     * @param direction direction of this line _ direction starts from 0 to 7 and turn clockwise from the north _ north is 0 , south is 4.
     * @param x x coordinate of line's starting point.
     * @param y y coordinate of line's starting point.
     */
    private void   convertDiscsInLine (int direction, int x, int y)
    {
        int xGrowth = xDirection(direction); // this is 0, 1 or -1;
        int yGrowth = yDirection(direction); // this is 0, 1 or -1;
        char team = board[y][x].getTeam();
        while (board[ y + yGrowth][x + xGrowth].getTeam() != team)
        {
            x += xGrowth;
            y += yGrowth;
            board[y][x].changeTeam();
        }
    }



    // this two methods coming below are exactly the same as methods came above but only dont convert the discs.
    /**
     * just count that how many discs would be changed if a disc is place at this coordinate.
     * @param x  x coordinate of the place on board.
     * @param y  y coordinate of the place on board.
     * @param team  team's character.
     * @return int: count of discs that would be changed.
     */
    public int countConvertsDiscs(int x, int y ,char team)
    {
        int countChanged = 0;
        for (int i=0; i<8 ; i++)
            if( validDirection(i,y,x,team)) {
                countChanged += countConvertsDiscsInLine(i,x,y,team);
            }
        return countChanged;
    }
    /**
     * just count that how many discs would be changed at this line if a disc is place at this coordinate.
     * @param direction direction of this line _ direction starts from 0 to 7 and turn clockwise from the north _ north is 0 , south is 4.
     * @param x x coordinate of line's starting point.
     * @param y y coordinate of line's starting point.
     */
    private int  countConvertsDiscsInLine (int direction, int x, int y,char team)
    {
        int xGrowth = xDirection(direction);
        int yGrowth = yDirection(direction);
        int countChanged =0;
        while (board[ y + yGrowth][x + xGrowth].getTeam() != team)
        {
            x += xGrowth;
            y += yGrowth;
            countChanged ++;
        }
        return countChanged;
    }


    /**
     * calculate x growth of this direction.
     * @param direction direction of this line _ direction starts from 0 to 7 and turn clockwise from the north _ north is 0 , south is 4.
     * @return x growth _ 1,0,-1
     */
    private int xDirection (int direction)
    {
        if(direction > 0 && direction < 4)
            return 1;
        if(direction > 4 && direction < 8)
            return -1;

        return 0;
    }

    /**
     * calculate y growth of this direction.
     * @param direction direction of this line _ direction starts from 0 to 7 and turn clockwise from the north _ north is 0 , south is 4.
     * @return y growth _ 1,0,-1
     */
    private int yDirection (int direction)
    {
        if( direction < 2 || direction > 6)
            return -1;
        if(direction > 2 && direction < 6)
            return 1;

        return 0;
    }


    /**
     * checks if the place is a valid place to put a disc or not.
     * @param coordinate  coordinate of the place in format like "2E"
     * @param team team's character.
     * @return boolean:  true if the place is valid else false.
     */
    public boolean validPlace (String coordinate , char team)
    {
        boolean flag = false;
        for(int i=0; i<8; i++)
            if(validDirection(i,yCoordinate(coordinate),xCoordinate(coordinate), team))
            {
                flag = true;
                break;
            }
        return flag;
    }


    /**
     * checks if this direction is a valid direction or not.the validate means that this direction would cause any disc to be changed.
     * @param direction direction of this line _ direction starts from 0 to 7 and turn clockwise from the north _ north is 0 , south is 4.
     * @param x x coordinate of line's starting point.
     * @param y y coordinate of line's starting point.
     * @param team team character to be checked
     * @return true if is valid else false
     */
    private boolean validDirection (int direction, int y, int x , char team)
    {
        // direction is start from 0 to 7 and is clockwise from the north.
        int xGrowth = xDirection(direction); // 0 , 1 or -1
        int yGrowth = yDirection(direction); // 0 , 1 or -1
        int countOfDiscsToChange = 0;
        boolean flag = true;
        try
        {
            while (board[y + yGrowth][x + xGrowth].getTeam() != '*' && board[y + yGrowth][x + xGrowth].getTeam() != team)
            {
                countOfDiscsToChange ++;
                y += yGrowth;
                x += xGrowth;
            }
            if(board[y + yGrowth][x + xGrowth].getTeam() == '*') // due to game rules.
                flag = false;

        }catch (IndexOutOfBoundsException e) // the place is out of board.
        {
            return false;
        }
        if( countOfDiscsToChange > 0 && flag )
            return true;
        return false;
    }


    /**
     * get the size of this board.
     * @return int: size of board.
     */
    public int getSIZE() {
        return SIZE;
    }


    /**
     * check if the place is near an opponent disc or not_ all sides places are checked.
     * @param x x coordinate of place on board.
     * @param y y coordinate of place on board.
     * @param team team's character
     * @return boolean: true if is near an opponent else false.
     */
    public boolean nearOpponent (int x, int y, char team)
    {
        for(int i=-1; i<=1 ; i++)
            for(int j=-1; j<=1 ; j++) {
                try
                {
                    if(board[y][x].getTeam() == '*')
                        if (board[y + i][x + j].getTeam() != team && board[y + i][x + j].getTeam() != '*') {
                            return true;
                        }

                }catch (IndexOutOfBoundsException ignored){ }; // out of board.
            }

        return  false;
    }

}
