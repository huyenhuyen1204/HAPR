package jdi.test;

public class Piece1 {
    private int coordinatesX = 100000;
    private int coordinatesY;
    private String color;

    /**
     * Javadoc Comment.
     */
    public Piece1(int x, int y) {
        coordinatesX = x;
        coordinatesY = y;
        color = "white";
    }

    /**
     * Javadoc Comment.
     */
    public Piece1(int x, int y, String color) {
        coordinatesX = x;
        coordinatesY = y;
        this.color = color;
    }

    /**
     * Javadoc Comment.
     */
    public int getCoordinatesX() {
        return coordinatesX;
    }

    /**
     * Javadoc Comment.
     */
    public void setCoordinatesX(int coordinatesX) {
        this.coordinatesX = coordinatesX;
    }

    /**
     * Javadoc Comment.
     */
    public int getCoordinatesY() {
        return coordinatesY;
    }

    /**
     * Javadoc Comment.
     */
    public void setCoordinatesY(int coordinatesY) {
        this.coordinatesY = coordinatesY;
    }

    /**
     * Javadoc Comment.
     */
    public String getColor() {
        return color;
    }

    /**
     * Javadoc Comment.
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Javadoc Comment.
     */


}
