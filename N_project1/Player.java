/**
 * define the player who play the game and put the discs on board.
 * @author farzad
 * @version 0.0 (28.3.2020)
 */
public class Player {
    protected char team ; // team's character
    protected Board board ; // board which is playing on.

    /**
     * constructor: simple .
     * @param team team's character
     * @param board board which is playing on.
     */
    public Player (char team, Board board)
    {
        this.board = board;
        this.team = team;
    }

    /**
     * put a disc on the board if is possible else prints a message.
     * @param coordinate coordinate of this place on board
     * @return boolean: return true if the disc is placed else false.
     */
    public int putDisc ( String coordinate)
    {
        boolean flag = board.validPlace( coordinate ,team);
       // boolean flag = true;
        if(flag )
            board.putDisc( coordinate , team);
        if ( !flag )
        {
            System.out.println("not valid place");
            return -1;
        }
        return 0;
    }


    /**
     * check if the player can move in this turn.
     * @return boolean: true if is possible to move else false
     */
    public boolean canPlay ()
    {
        for(int i=0; i < board.getSIZE() ; i++)
            for( int j=0; j<board.getSIZE() ; j++)
                if(board.nearOpponent(j,i,team))
                    if(board.validPlace( coordinateToString(j,i),team))
                        return true;

        return  false;
    }


    /**
     * make a coordinate String as form os "2E" from given x and y .
     * @param x x coordinate of point
     * @param y y coordinate of point
     * @return String coordinate of point in board.
     */
    protected String coordinateToString (int x, int y)
    {
        return "" + (y+1) + (char)(x + 65);
    }

}
