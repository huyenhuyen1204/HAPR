package jdi.test;

public class Rook extends Piece {
    public Rook(int x, int y) {
        super(x, y);
    }

    public Rook(int x, int y, String color) {
        super(x, y, color);
    }

    public String getSymbol() {
        return "R";
    }

    /**canMove.*/
    public boolean canMove(Board board, int x, int y) {
        int count;
        if (!board.validate(x, y) || (this.getCoordinatesX() == x && this.getCoordinatesY() == y)) {
            return false;
        }
        if (this.getCoordinatesX() == x) {
            if (this.getCoordinatesY() < y) {
                count = 0;
                for (int i = this.getCoordinatesY(); i < y - 1; i++) {
                    if (board.getAt(x, i + 1) != null) {
                        count++;
                    }
                }
                if (count > 0) {
                    return false;
                } else {
                    if (board.getAt(x, y) != null && board.getAt(x, y).getColor().equals(this.getColor())) {
                        return false;
                    }
                    return true;
                }
            } else {
                count = 0;
                for (int i = this.getCoordinatesY(); i > y + 1; i--) {
                    if (board.getAt(x, i - 1) != null) {
                        count++;
                    }
                }
                if (count > 0) {
                    return false;
                } else {
                    if (board.getAt(x, y) != null && board.getAt(x, y).getColor().equals(this.getColor())) {
                        return false;
                    }
                    return true;
                }
            }
        }
        if (this.getCoordinatesY() == y) {
            if (this.getCoordinatesX() < x) {
                count = 0;
                for (int i = this.getCoordinatesX(); i < x - 1; i++) {
                    if (board.getAt(i + 1, y) != null) {
                        count++;
                    }
                }
                if (count > 0) {
                    return false;
                } else {
                    if (board.getAt(x, y) != null && board.getAt(x, y).getColor().equals(this.getColor())) {
                        return false;
                    }
                    return true;
                }
            } else {
                count = 0;
                for (int i = this.getCoordinatesX(); i > x + 1; i--) {
                    if (board.getAt(i - 1, y) != null) {
                        count++;
                    }
                }
                if (count > 0) {
                    return false;
                } else {
                    if (board.getAt(x, y) != null && board.getAt(x, y).getColor().equals(this.getColor())) {
                        return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean checkPosition(Piece piece) {
        return false;
    }
}
