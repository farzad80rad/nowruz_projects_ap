/**
 * a 3*3 board which is a part of main playing board
 * @author farzad
 * @version 0.0 (1.April.2020)
 */
public class Board {

    private static final int SIZE = 3;
    private static final char emptyPlace = '\u2742';
    Disc[][] boardPiece = new Disc[SIZE][SIZE];

    /**
     * simple constructor.
     */
    public Board ()
    {
        for(int i=0 ; i<SIZE ; i++)
            for(int j=0 ; j<SIZE ; j++)
                boardPiece[i][j] = new Disc(emptyPlace);
    }

    /**
     * put a disc on this coordinate
     * @param x x coordinate of place
     * @param y y coordinate of place
     * @param team team's character
     * @return boolean: true if is placed else false
     */
    public boolean putDisc (int x, int y,char team)
    {
        try {
            if(boardPiece[y%SIZE][x%SIZE].getTeam() == emptyPlace) {
                boardPiece[y%SIZE][x%SIZE].setTeam(team);
                return true;
            }
            else
            {
                System.out.println("there is another disc here.");
                return false;
            }
        }catch (IndexOutOfBoundsException e)
        {
            System.out.println("not valid coordinate");
            return false;
        }
    }


    /**
     * rote a board clockwise or anti clockwise
     * @param rotationDirection direction  1 == clockwise  2 == anti clockwise
     */
    public void roteBoard(int rotationDirection)
    {
        // this code is copied from:
        // https://www.geeksforgeeks.org/rotate-a-matrix-by-90-degree-in-clockwise-direction-without-using-any-extra-space/
        // https://www.geeksforgeeks.org/inplace-rotate-square-matrix-by-90-degrees/


        // Traverse each cycle
        if( rotationDirection == 1)
            for (int i = 0; i < SIZE / 2; i++)
            {
                for (int j = i; j < SIZE - i - 1; j++)
                {
                    // Swap elements of each cycle
                    // in clockwise direction
                    Disc temp = boardPiece[i][j];
                    boardPiece[i][j] = boardPiece[SIZE - 1 - j][i];
                    boardPiece[SIZE - 1 - j][i] = boardPiece[SIZE - 1 - i][SIZE - 1 - j];
                    boardPiece[SIZE - 1 - i][SIZE - 1 - j] = boardPiece[j][SIZE - 1 - i];
                    boardPiece[j][SIZE - 1 - i] = temp;
                }
            }

        // Consider all squares one by one
        if(rotationDirection == 2)
        for (int x = 0; x < SIZE / 2; x++)
        {
            // Consider elements in group of 4 in
            // current square
            for (int y = x; y < SIZE-x-1; y++)
            {
                // store current cell in temp variable
                Disc temp = boardPiece[x][y];

                // move values from right to top
                boardPiece[x][y] = boardPiece[y][SIZE-1-x];

                // move values from bottom to right
                boardPiece[y][SIZE-1-x] = boardPiece[SIZE-1-x][SIZE-1-y];

                // move values from left to bottom
                boardPiece[SIZE-1-x][SIZE-1-y] = boardPiece[SIZE-1-y][x];

                // assign temp to left
                boardPiece[SIZE-1-y][x] = temp;
            }
        }
    }

    /**
     * get the board
     * @return Dosc[][]: board.
     */
    public Disc[][] getBoardPiece() {
        return boardPiece;
    }
}
