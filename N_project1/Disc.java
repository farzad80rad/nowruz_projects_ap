/**
 * this class is defining a disc or a nut which is used be player to play the game.
 * @author farzad
 * @version 0.0 (28.3.2020)
 */
public class Disc {

    private char team ; // a team's character.
    private final char TEAM_B = 'B';
    private final char TEAM_W = 'W';

    /**
     * a simple constructor which put the teamChar as '*'.
     */
    public Disc ()
    {
        team = '*';
    }



    /**
     * constructor: make a Disc with specific team's character.
     * @param team team's character
     */
    public Disc (char team)
    {
        this.team = team;
    }


    /**
     * there are 2 teams, so this method shifts between them.
     */
    public void changeTeam ()
    {
        if( team == TEAM_B)
            team = TEAM_W;
        else
            team = TEAM_B;
    }


    /**
     * change a specific char's team to this disc
     * @param team team's character
     */
    public void putTeam ( char team)
    {
        if (team == TEAM_B || team == TEAM_W) {
            this.team = team;
        }
    }


    /**
     * get the character of this disc
     * @return char: the disc's character of team.
     */
    public char getTeam() {
        return team;
    }
}
