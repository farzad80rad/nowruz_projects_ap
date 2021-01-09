/**
 * disc class is defining discs that are placed on board.
 * @author farzad
 * @version 0.0 (1.April.2020)
 */
public class Disc {
    private char team; // this team's character.

    /**
     * constructor: simple one.
     * @param team this team's character.
     */
    public Disc(char team) {
        this.team = team;
    }

    /**
     * get this team's character.
     * @return char: this team's character.
     */
    public char getTeam() {
        return team;
    }

    /**
     * set this team's character.
     * @param team this team's character.
     */
    public void setTeam(char team) {
        this.team = team;
    }
}
