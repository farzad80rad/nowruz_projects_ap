import java.util.HashMap;
import java.util.Random;

/**
 * AI is an artefactual intelligence callable of playing this game.
 * this AI has 3 kind of playing : Offensive , Defensive and Moderate.
 * @author farzad
 * @version 0.0 (1.April.2020)
 */
public class AI_Player extends Player{

    HashMap<String,String> main5PossibleMove;   // 4 main decision that this AI has.
    private final static int SIZE = 6;   //size of playing board.
    private static final char emptyPlace = '\u2742';   // character for empty places
    private int levelOfPlay;   // kind of playing : Moderate == 1 , Defensive == 2  , Offensive == 3;
    private char enemy ;  // enemy character

    /* 4 main kind of decision : "4w" , "3W" , "2W, "4L" , "3L"
     "W" stands for "win" and means this decision lead to win the game.
     "L" stands for "lose" and means this decision is for preventing enemy to win the game.
     the int coming first is stands for the count of enemy or allied discs present in line at this moment.
     the combination of these 5 keys makes the AI level.
     */
    private String[] keys;

    // 4_discs in line formations.
    private  final String[] RESULTS_TO_BE_FOUND_4 = new String[]{ "****#", "***#*", "**#**", "*#***", "#****"};

    // 3_discs in line formations.
    private final String[] RESULTS_TO_BE_FOUND_3 = new String[]{
            "***##", "#***#", "##***", "#*#**", "#**#*",
            "*##**", "**##*", "*#*#*", "**#*#", "*#**#"
    };

    // 2_discs in line formations.
    private final String[] RESULTS_TO_BE_FOUND_2 = new String[]{
            "###**", "*###*", "**###", "*#*##", "*##*#",
            "#**##", "##**#", "#*#*#", "##*#*", "#*##*"
    };


    /**
     * constructor: simple constructor for this class.
     * @param team team's character of AI
     * @param playingBoard the board which this AI is playing on.
     * @param enemy the opponent character.
     * @param levelOfPlay the kind of playing of this AI : Moderate == 1 , Defensive == 2  , Offensive == 3;
     */
    public AI_Player(char team, MainPlayingBoard playingBoard, char enemy, int levelOfPlay)
    {
        super(team,playingBoard);

        final int  MODERATE = 1 , DEFENSIVE = 2 , OFFENSIVE = 3;

        main5PossibleMove = new HashMap<>();
        this.enemy = enemy;
        this.levelOfPlay = levelOfPlay;

        if(levelOfPlay == MODERATE)
            keys = new String[]{"4W","4L","3W","3L","2W"};
        if(levelOfPlay == DEFENSIVE)
            keys = new String[]{"4W","4L","3L","3W","2W"};
        if(levelOfPlay == OFFENSIVE)
            keys = new String[]{"4W","4L","3W","2W","3L"};
    }

    /**
     * put a disc on board by the algorithm given.
     * @return String: index of board to be routed and its direction as form of "index_direction_"
     */
    public String putDiscIo()
    {
        main5PossibleMove.clear();

        String currentMove = "";
        for(int index=0 ; index<4 ; index++) // find the main 5 possible moves.
            for(int rotationDirection=1; rotationDirection<=2; rotationDirection++)
            {
                currentMove = "" + index + "_" + rotationDirection + "_";
                roteBoard(index,rotationDirection);
                checkAllPossibleMove(currentMove);
                roteBoard(index,rotationDirection==1?2:1);
            }

        for(String key : main5PossibleMove.keySet())  // print all possible moves
            System.out.println(key + "   " + main5PossibleMove.get(key));


            for(String key : keys) {  // do possible moves by priority.
                if (main5PossibleMove.containsKey(key)) {
                    int direction = getDirectionOfCoordinateCode(main5PossibleMove.get(key));
                    int index = getIndexOfCoordinateCode(main5PossibleMove.get(key));
                    int rotationDirection = getRotationDirectionOfCoordinateCode(main5PossibleMove.get(key));
                    String coordinateCode = getCoordinateOfCoordinateCode(main5PossibleMove.get(key));
                    int symbol = getSymbolOfCoordinateCode(main5PossibleMove.get(key));

                    roteBoard(index, rotationDirection );
                    putDiscPlan(direction, coordinateCode, symbol, Integer.parseInt( key.substring(0,1)));
                    roteBoard(index, rotationDirection == 1? 2:1 );
                    return main5PossibleMove.get(key);
                }
            }

        for(int i=0; i<4 ; i++) // capture middle of boards.
            if(playingBoard.getBoards()[i].getBoardPiece()[1][1].getTeam() == emptyPlace) {
                playingBoard.getBoards()[i].putDisc(1, 1, team);
                return currentMove;
            }

            putDiscRandom(); // random
            return currentMove;
    }


    /**
     * rote a board clockwise or anti clockwise.
     * @param rotationCode  the code explain index of array and direction of rotation as form of "index_direction"
     */
    public void roteBoard (String rotationCode)
    {
        System.out.println(rotationCode);
        int index = getIndexOfCoordinateCode(rotationCode);
        int rotationDirection = getRotationDirectionOfCoordinateCode(rotationCode);
        roteBoard(index,rotationDirection);
        System.out.println("board_" + (index+1) + " roted in " + (rotationDirection==1?"clockwise":"counter clockwise") );
    }

    /**
     * randomly put a disc on a valid place on board.
     */
    private void putDiscRandom()
    {
        Random random = new Random();
        while (true)
        {
            int x = random.nextInt(6);
            int y = random.nextInt(6);
            if(playingBoard.getMainPlayingBoard()[y][x].getTeam() == emptyPlace)
            {
                String coordinate = coordinateToString(x,y,0,0,0);
                putDisc(coordinate);
                return;
            }
        }
    }


    /**
     * put a disc according to the form of discs on board.
     * @param direction direction of this line_ a number of 0_7 , clockwise , starts from north.
     * @param coordinate  the coordinate of starting point of line. as form of "3A"
     * @param symbol index  in RESULTS_TO_BE_FOUND arrays. look to the fields to see these arrays.
     * @param kindKey 4,3,2 stands for amount of discs on line.
     */
    private void putDiscPlan(int direction, String coordinate, int symbol, int kindKey)
    {
        int x = playingBoard.xCoordinate(coordinate);
        int y = playingBoard.yCoordinate(coordinate);
        int xGrowth = playingBoard.xDirection(direction);
        int yGrowth = playingBoard.yDirection(direction);
        String newCoordinate = "";
        int operator = 10*kindKey + symbol;

        switch (operator)
        {
            case 40:
            case 31:
                newCoordinate = coordinateToString(x,y,xGrowth,yGrowth,4);
                break;
            case 41:
            case 30:
            case 34:
            case 37:
            case 28:
            case 29:
                newCoordinate = coordinateToString(x,y,xGrowth,yGrowth,3);
                break;
            case 42:
            case 33:
            case 35:
            case 36:
            case 38:
            case 20:
            case 27:
                newCoordinate = coordinateToString(x,y,xGrowth,yGrowth,2);
                break;
            case 43:
            case 32:
            case 39:
            case 21:
            case 23:
            case 24:
            case 26:
                newCoordinate = coordinateToString(x,y,xGrowth,yGrowth,1);
                break;
            case 44:
            case 25:
                newCoordinate = coordinateToString(x,y,xGrowth,yGrowth,0);
                break;
        }

        System.out.println("coordinate of new disc: " + newCoordinate);
        putDisc(newCoordinate);
    }

    /**
     * get index of board from code String.
     * @param coordinateCode  code String in format of "index_rotationDirection_direction_coordinate_symbol"
     * @return int: index of board
     */
    private int getIndexOfCoordinateCode(String coordinateCode)
    {
        String[] splits = coordinateCode.split("_");
        return Integer.parseInt( splits[0]);
    }

    /**
     * get rotation direction of board from code String.
     * @param coordinateCode  code String in format of "index_rotationDirection_direction_coordinate_symbol"
     * @return int: 1== clockwise   2== anti clockwise
     */
    private int getRotationDirectionOfCoordinateCode(String coordinateCode)
    {
        String[] splits = coordinateCode.split("_");
        return Integer.parseInt( splits[1]);
    }

    /**
     * get direction of line  from code String.
     * @param coordinateCode  code String in format of "index_rotationDirection_direction_coordinate_symbol"
     * @return  int: direction of this line_ a number of 0_7 , clockwise , starts from north.     */
    private int getDirectionOfCoordinateCode(String coordinateCode)
    {
        String[] splits = coordinateCode.split("_");
        return Integer.parseInt( splits[2]);
    }

    /**
     * get coordinate of line's starting point from code String.
     * @param coordinateCode  code String in format of "index_rotationDirection_direction_coordinate_symbol"
     * @return String: coordinate of line's starting point.
     */
    private String getCoordinateOfCoordinateCode(String coordinateCode)
    {
        String[] splits = coordinateCode.split("_");
        return  splits[3];
    }

    /**
     * get index of symbol from code String.
     * @param coordinateCode  code String in format of "index_rotationDirection_direction_coordinate_symbol"
     * @return int: symbol index in RESULTS_TO_BE_FOUND arrays. look to the fields to see these arrays.
     */
    private int getSymbolOfCoordinateCode(String coordinateCode)
    {
        String[] splits = coordinateCode.split("_");
        return Integer.parseInt( splits[4]);
    }


    /**
     * checks all directions to find all possible moves.
     * @param currentMove String which contains index of board and rotation direction.
     */
    private void checkAllPossibleMove(String currentMove)
    {
        for (int i = 0; i < SIZE; i++)
            checkLine(4, i, 0, team, currentMove + 4 + "_");

        for (int j = 0; j < SIZE; j++)
            checkLine(2, 0, j, team, currentMove + 2 + "_");


        checkLine(3, 0, 0, team, currentMove+ 3 + "_");
        checkLine(3, 0, 1, team, currentMove+ 3 + "_");
        checkLine(3, 1, 0, team, currentMove+ 3 + "_");
        checkLine(1, 0, SIZE - 1 , team, currentMove+ 1 + "_");
        checkLine(1, 0, SIZE - 2, team, currentMove+ 1 + "_");
        checkLine(1, 1, SIZE - 1, team, currentMove+ 1 + "_");
    }


    /**
     * checks an specific line to find the form given in RESULTS_TO_BE_FOUND arrays.
     * @param direction direction of this line.
     * @param x x coordinate of line's starting point.
     * @param y y coordinate of line's starting point.
     * @param team team to be checked .
     * @param currentMove contains index of board , rotation direction and direction of line.
     * @return boolean: return true if there is a form in this line else false.
     */
    private boolean checkLine (int direction , int x , int y,char team,String currentMove)
    {
        int X = x;
        int Y = y;
        int xGrowth = playingBoard.xDirection( direction );
        int yGrowth = playingBoard.yDirection( direction );
        String formatInLine = "";
        int sizeThisDirection = playingBoard.sizeOfThisLine(x,y,direction);

        String[] resultToBeFound4Win = convert(RESULTS_TO_BE_FOUND_4, team);
        String[] resultToBeFound3Win = convert(RESULTS_TO_BE_FOUND_3, team);
        String[] resultToBeFound4Lose = convert(RESULTS_TO_BE_FOUND_4, enemy);
        String[] resultToBeFound3Lose = convert(RESULTS_TO_BE_FOUND_3, enemy);
        String[] resultToBeFound2Win = convert(RESULTS_TO_BE_FOUND_2, team);
        String[][] allResults = new String[1][1];

        if(levelOfPlay == 1)
            allResults= new String[][]{
                resultToBeFound4Win, resultToBeFound4Lose,resultToBeFound3Win,resultToBeFound3Lose,resultToBeFound2Win};
        if(levelOfPlay == 2)
            allResults= new String[][]{
                    resultToBeFound4Win, resultToBeFound4Lose,resultToBeFound3Lose,resultToBeFound3Win,resultToBeFound2Win};
        if(levelOfPlay == 3)
            allResults= new String[][]{
                    resultToBeFound4Win  , resultToBeFound4Lose, resultToBeFound3Win , resultToBeFound2Win , resultToBeFound3Lose };

        for(int i=0 ; i<sizeThisDirection ; i++)
        {
            formatInLine = formatInLine.concat("" + playingBoard.getMainPlayingBoard()[y][x].getTeam());
            x += xGrowth;
            y += yGrowth;
        }

        for(int i=0 ; i<5 ; i++) {
            String[] kindResult = allResults[i];
            int symbolCount =0;
            for (String tempString : kindResult) {
                if (formatInLine.contains(tempString)) {
                    int indexOfString = formatInLine.indexOf(tempString);
                    String coordinate = coordinateToString(X, Y, xGrowth, yGrowth, indexOfString);
                    main5PossibleMove.put(keys[i], currentMove + coordinate + "_" + symbolCount);
                    return true;
                }
                symbolCount++;
            }
        }
        return false;
    }


    /**
     * convert a raw String[] to a String[] customize for this team.
     * @param sToBeConverted the String[] to be converted.
     * @param team team's char to be replaced by '*' char.
     * @return String[]: converted String[].
     */
    private String[] convert (String[] sToBeConverted, char team)
    {
        String[] convertedString = new String[sToBeConverted.length];
        int i=0 ;
        for(String tempString : sToBeConverted)
        {
            tempString = tempString.replace('#',emptyPlace);
            convertedString[i] = tempString.replace('*',team);
            i++;
        }

        return convertedString;

    }

    /**
     * make a coordinate String by this coordinate given. its used normally for getting the coordinate of
     * specific place in a line.
     * @param x x coordinate of line's starting point.
     * @param y x coordinate of line's starting point.
     * @param xGrowth growing of x element in this direction. 1 , 0 or -1.
     * @param yGrowth growing of y element in this direction. 1 , 0 or -1.
     * @param indexInArray index of wanted place in this line .
     * @return String: coordinate String .
     */
    private String coordinateToString (int x, int y, int xGrowth, int yGrowth, int indexInArray)
    {
        x += (xGrowth * (indexInArray));
        y += (yGrowth *(indexInArray));
        return "" + (y+1) + (char)(x + 65);
    }
}
