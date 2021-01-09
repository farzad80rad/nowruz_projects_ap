/**
 *defining a Player who is responsible to put a disc or etc.
 * @author farzad
 * @version 0.0 (1.April.2020)
 */
public class Player {
    protected char team ; // team's char.
    protected MainPlayingBoard playingBoard ;   // the board which is playing on.

    /**
     * simple constructor.
     * @param team team's character
     * @param playingBoard the board which is playing on.
     */
    public Player (char team,MainPlayingBoard playingBoard)
    {
        this.team = team;
        this.playingBoard = playingBoard;
    }


    /**
     * put a disc on board.
     * @param coordinate coordinate of this place.
     * @return boolean: return true if is placed else false.
     */
    public boolean putDisc (String coordinate)
    {
        return playingBoard.putDisc(coordinate,team);
    }

    /**
     * rote one of 4 boards.
     * @param index index of the board.
     * @param direction rotation direction . 1 == clockwise    2 == anti clockwise
     * @return boolean: return true if is routed else false.
     */
    public boolean roteBoard (int index, int direction)
    {
        if(direction == 2 || direction == 1)
        {
               return playingBoard.roteBoard(index,direction);
        }
        else
        {
            System.out.println("not valid direction.");
            return false;
        }
    }

    /**
     * checks if player win the game.
     * @return boolean: true if wins else false
     */
    public boolean winGame ()
    {
        return  playingBoard.winGame(team);
    }

    /**
     * get the player's character.
     * @return character: player's character
     */
    public char getTeam() {
        return team;
    }
}
