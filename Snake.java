/**

    Snake
        A Snake for the Snake game

    @author Christopher Leap

*/

import java.util.*;

public class Snake {

    /** An enum that describes the direction the snake moves */
    public static enum Dir {
        UP, DOWN, LEFT, RIGHT;
    }

    /** Inner class to describe each body unit of the snake */
    private static class Body {
        private int x;
        private int y;

        /** Constructs a body at the given coordinate */
        public Body(int x, int y) { this.x = x; this.y = y; }
        /** Moves the body in the given direction */
        public void move(int dx, int dy) { x += dx; y += dy; }

        /** Getter for the x coordinate */
        public int getX() { return x; }
        /** Getter for the y coordinate */
        public int getY() { return y; }
    }

    // The body links for the snake
    private List<Body> body;

    // The x and y directions for the snake
    private int dx = 0;
    private int dy = 0;

    // How much the snake grows for each food bit
    private int feedFactor = 5;

    // Whether or not the snake is alive
    private boolean isAlive = true;

    /** Constructs a Snake at the given coordinate */
    public Snake(int x, int y) {
        // Initialize the body
        body = new ArrayList<>();

        // Add body parts equal to it's feedFactor
        for(int i = 0; i < feedFactor; ++i) body.add(new Body(x, y));
    }

    /** Moves the snake one unit based on its facing */
    public void step() {

        // Move the snake if it's alive
        if(isAlive) {

            // Starting at the back, move each body link towards the next
            for(int i = body.size() - 1; i > 0; --i) {
                body.get(i).move(
                    body.get(i-1).getX() - body.get(i).getX(),
                    body.get(i-1).getY() - body.get(i).getY()
                );
            }

            // Move the head in the appropriate direction
            body.get(0).move(dx, dy);
        }
    }

    /** Returns a list of the position of each body link in the form
        <x0, y0, x1, y1, ..., xn, yn> */
    public List<Integer> getPos() {
        List<Integer> pos = new ArrayList<>();
        for(Body b : body) {
            pos.add(b.getX());
            pos.add(b.getY());
        }

        return pos;
    }

    /** Turns the snake in the given direction, but it won't turn 180 degrees */
    public void turn(Dir dir) {
        switch(dir) {
            case UP:
                if(dy == 0) {
                    dy = -1;
                    dx = 0;
                }
                break;
            case DOWN:
                if(dy == 0) {
                    dy = 1;
                    dx = 0;
                }
                break;
            case LEFT:
                if(dx == 0) {
                    dy = 0;
                    dx = -1;
                }
                break;
            case RIGHT:
                if(dx == 0) {
                    dy = 0;
                    dx = 1;
                }
                break;
        }
    }

    /** Grows the snake */
    public void feed() {
        for(int i = 0; i < feedFactor; ++i) {
            body.add(new Body(getTailX(), getTailY()));
        }
    }

    /** Kills the snake :'( */
    public void die() {
        isAlive = false;
    }

    /** Getter for the x coordinate of the head */
    public int getHeadX() { return body.get(0).getX(); }
    /** Getter for the y coordinate of the head */
    public int getHeadY() { return body.get(0).getY(); }
    /** Getter for the x coordinate of the tail */
    public int getTailX() { return body.get(body.size() - 1).getX(); }
    /** Getter for the y coordinate of the tail */
    public int getTailY() { return body.get(body.size() - 1).getY(); }
}
