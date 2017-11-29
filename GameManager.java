/**

    GameManager
        Manages the snake game

    @author Christopher Leap

*/

import java.util.*;
import java.io.*;

public class GameManager {

    /** Describes a tile on the game board */
    public static enum TileState {
        PLAYER, WALL, FOOD, EMPTY;

        /** Makes toString() friendly with grids */
        @Override
        public String toString() {
            switch(this) {
                case PLAYER: return "S";
                case WALL: return "X";
                case FOOD: return "f";
                case EMPTY: return ".";
                default: return "?";
            }
        }
    }

    // A two dimensional array describing the game board
    private List<List<TileState>> board;
    private Snake snake; // The player

    // Dimensions of the board
    private int width = 0;
    private int height = 0;

    private Random rand;

    // Whether or not debug text should be printed
    private boolean debugMode = false;
    // Whether the game was successfully initialized
    public final boolean initialized;

    // The default level
    public static final String DEF_LEVEL = "maze-zigzag.txt";

    /** Creates GameManager from the given file using the given Random */
    public GameManager(String fileName, Random rand) {

        // Read in from the file
        try (BufferedReader in =
                new BufferedReader(new FileReader(fileName))) {

            String line;
            if((line = in.readLine()) != null) {

                // Get the width and height of the board
                String[] vals = line.split("\\s");
                if(vals.length >= 2) {
                    width = Integer.parseInt(vals[0]);
                    height = Integer.parseInt(vals[1]);
                }

                board = new ArrayList<>();

                // Fill the board to the appropriate size
                for(int h = 0; h < height; ++h) {
                    board.add(new ArrayList<>());
                    for(int w = 0; w < width; ++w) {
                        board.get(h).add(TileState.EMPTY);
                    }
                }
            }

            // If the board has been initialized
            if(width != 0 && height != 0) {

                // Create Walls
                while((line = in.readLine()) != null) {

                    String[] vals = line.split("\\s");
                    if(vals.length <= 4) {
                        for(int x = Integer.parseInt(vals[0]);
                            x <= Integer.parseInt(vals[2]); ++x) {

                            for(int y = Integer.parseInt(vals[1]);
                                y <= Integer.parseInt(vals[3]); ++y) {

                                board.get(y).set(x, TileState.WALL);
                            }
                        }
                    }
                }

                // Once the level has been created, the random can be set
                this.rand = rand;

                // Spawn Snake in an unoccupied area
                int snakeX, snakeY;
                do {
                    snakeX = rand.nextInt(width);
                    snakeY = rand.nextInt(height);
                } while(board.get(snakeY).get(snakeX) != TileState.EMPTY);

                snake = new Snake(snakeX, snakeY);
                board.get(snakeY).set(snakeX, TileState.PLAYER);

                // Spawn the food
                spawnFood();
            }

        } catch(IOException e) {
            e.printStackTrace();
        }

        initialized = width != 0 && height != 0;
    }

    /** Creates GameManager with debug text available */
    public GameManager(String level, Random rand, boolean debug) {
        this(level, rand);
        debugMode = debug;
    }

    /** Creates GameManager from the default level using the given Random */
    public GameManager(Random rand) {
        this(DEF_LEVEL, rand);
    }

    /** Creates GameManager from the default level, given random,
        with debug text */
    public GameManager(Random rand, boolean debug) {
        this(rand);
        debugMode = debug;
    }

    /** Places a food piece in an unoccupied space */
    public void spawnFood() {
        List<Integer> coord = openCoord();
        board.get(coord.get(1)).set(coord.get(0), TileState.FOOD);
/*        int x, y;

        do {
            x = rand.nextInt(width);
            y = rand.nextInt(height);
        } while(board.get(y).get(x) != TileState.EMPTY);

        board.get(y).set(x, TileState.FOOD);*/
    }

    public List<Integer> openCoord() {
        int x, y;

        do {
            x = rand.nextInt(width);
            y = rand.nextInt(height);
        } while(board.get(y).get(x) != TileState.EMPTY);

        List<Integer> list = new ArrayList<>();
        list.add(x);
        list.add(y);

        return list;
    }

    /** Returns a string representation of the game board */
    @Override
    public String toString() {

        StringBuilder str = new StringBuilder((width + 1) * height);

        for(List<TileState> l : board) {
            for(TileState t : l) {
                str.append(t);
            }
            str.append("\n");
        }

        return str.toString();
    }

    /* Getter for the width */
    public int getWidth() { return width; }
    /* Getter for the height */
    public int getHeight() { return height; }

    public TileState getState(int row, int col) {
        return board.get(col).get(row);
    }

    /**
        Updates the game state
        @return Whether the snake is still alive
    */
    public boolean update() {

        // Clear the spot where the tail was
        board.get(snake.getTailY()).set(snake.getTailX(), TileState.EMPTY);

        // Move the snake
        snake.step();

        // Check if the snake has moved out of bounds
        if(snake.getHeadX() < 0 || snake.getHeadX() >= getWidth() ||
            snake.getHeadY() < 0 || snake.getHeadY() >= getHeight()) {

            debug("snake has gone out of bounds");
            return false;
        }

        TileState collision = board.get(snake.getHeadY()).get(snake.getHeadX());

        // Check if the snake has collided with any harmful obstacles
        if(collision == TileState.WALL) {
            debug("snake has run into a wall");
            snake.die();
            return false;
        }
        else if(collision == TileState.PLAYER) {
            debug("snake has run into itself");
            snake.die();
            return false;
        }

        // Check if the snake has found food
        else if(collision == TileState.FOOD) {
            snake.feed();
            debug("Yum!");
            spawnFood();
        }

        // Add the new head and new tail to the game board
        board.get(snake.getHeadY()).set(snake.getHeadX(), TileState.PLAYER);
        board.get(snake.getTailY()).set(snake.getTailX(), TileState.PLAYER);

        return true;
    }

    /** Turns the snake the given direction */
    public void turn(Snake.Dir dir) {
        snake.turn(dir);
        debug("snake has turned " + dir);
    }

    /** Prints out debug messages if debugMode is enabled */
    public void debug(String message) {
        if(debugMode) {
            System.out.println(message);
        }
    }
}
