package jdi.test;

public class Move {
    private int startX;
    private int endX;
    private int startY;
    private int endY;
    private Piece movedPiece;
    private Piece killedPiece;

    /**Move.*/
    public Move(int startX, int endX, int startY, int endY, Piece movedPiece) {
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
        this.movedPiece = movedPiece;
    }

    /**Move.*/
    public Move(int startX, int endX, int startY, int endY, Piece movedPiece, Piece killedPiece) {
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
        this.movedPiece = movedPiece;
        this.killedPiece = killedPiece;
    }

    /**String.*/
    public String toString() {
        if (endX == 1) {
            return movedPiece.getColor() + "-" + movedPiece.getSymbol() + "a" + endY;
        } else if (endX == 2) {
            return movedPiece.getColor() + "-" + movedPiece.getSymbol() + "b" + endY;
        } else if (endX == 3) {
            return movedPiece.getColor() + "-" + movedPiece.getSymbol() + "c" + endY;
        } else if (endX == 4) {
            return movedPiece.getColor() + "-" + movedPiece.getSymbol() + "d" + endY;
        } else if (endX == 5) {
            return movedPiece.getColor() + "-" + movedPiece.getSymbol() + "e" + endY;
        } else if (endX == 6) {
            return movedPiece.getColor() + "-" + movedPiece.getSymbol() + "f" + endY;
        } else if (endX == 7) {
            return movedPiece.getColor() + "-" + movedPiece.getSymbol() + "g" + endY;
        } else {
            return movedPiece.getColor() + "-" + movedPiece.getSymbol() + "h" + endY;
        }
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getEndX() {
        return endX;
    }

    public void setEndX(int endX) {
        this.endX = endX;
    }

    public int getEndY() {
        return endY;
    }

    public void setEndY(int endY) {
        this.endY = endY;
    }

    public Piece getMovedPiece() {
        return movedPiece;
    }

    public void setMovedPiece(Piece movedPiece) {
        this.movedPiece = movedPiece;
    }

    public Piece getKilledPiece() {
        return killedPiece;
    }

    public void setKilledPiece(Piece killedPiece) {
        this.killedPiece = killedPiece;
    }
}
