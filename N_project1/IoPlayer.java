import java.util.HashMap;

/**
 * computer as a player. this algorithm is made to gain as much discs as possible in each turn. is an extend of Player.
 */
public class IoPlayer extends Player {

    /**
     * exactly the Player constructor.
     * @param team team's character
     * @param board board which is playing on.
     */
    public IoPlayer(char team, Board board) {
        super(team, board);
    }

    /**
     * automatically puts a disc on board to gain as much discs as possible. prints the coordinate that is choose.
     */
    public void putDisc() {
        HashMap<Integer, String> changes = new HashMap<>();
        int max = 0;
        for (int i = 0; i < board.getSIZE(); i++)
            for (int j = 0; j < board.getSIZE(); j++)
                if (board.nearOpponent(j,i,team)) {
                    if (board.validPlace(coordinateToString(j,i), team)) {
                        int countChanged = board.countConvertsDiscs(j,i,team);
                        changes.put(countChanged, coordinateToString(j,i));
                        max = Integer.max(max, countChanged);
                    }
                }

        System.out.println(changes.get(max));
        board.putDisc(changes.get(max), team);
    }
}
