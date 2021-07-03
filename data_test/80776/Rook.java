import java.util.ArrayList;

public class Rook extends Piece {

    /**
     * phuong thuc khoi tao.
     */
    public Rook(int x, int y) {
        super(x, y);
    }

    /**
     * phuong thuc khoi tao.
     */
    public Rook(int x, int y, String color) {
        super(x, y, color);
    }

    @Override
    public String getSymbol() {
       return "R";
    }

    @Override
    public boolean canMove(Board board, int x, int y) {
        if (x != this.getCoordinatesX() && y != this.getCoordinatesY()) {
            return false;
        }
        if ( x > 8 || x < 0 || y > 8 || y < 0) {
            return false;
        }
        int maxX = 0, minX = 0;
        int maxY = 0, minY = 0;
        if (this.getCoordinatesX() > x) {
            maxX = this.getCoordinatesX();
            minX = x;
        } else {
            maxX = x;
            minX = this.getCoordinatesX();
        }
        if (this.getCoordinatesY() > y) {
            maxY = this.getCoordinatesY();
            minY = y;
        } else {
            maxY = y;
            minY = this.getCoordinatesY();
        }
        ArrayList<Piece> pieceArrayList = board.getPieces();
        int l = pieceArrayList.size();

        for (int i = 0; i < l; i++) {
            if (pieceArrayList.get(i).getCoordinatesY() == this.getCoordinatesY()
                    && pieceArrayList.get(i).getCoordinatesX() != this.getCoordinatesX()) {

                if (pieceArrayList.get(i).getCoordinatesX() >= minX
                        && pieceArrayList.get(i).getCoordinatesX() <= maxX) {

                    if (pieceArrayList.get(i).getCoordinatesX() == x) {
                        if (!pieceArrayList.get(i).getColor().equals(this.getColor())) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }

            } else if (pieceArrayList.get(i).getCoordinatesX() == this.getCoordinatesX()
                    && pieceArrayList.get(i).getCoordinatesY() != this.getCoordinatesY()) {

                if (pieceArrayList.get(i).getCoordinatesY() >= minY
                        && pieceArrayList.get(i).getCoordinatesY() <= maxY) {

                    if (pieceArrayList.get(i).getCoordinatesY() == y) {
                        if (!pieceArrayList.get(i).getColor().equals(this.getColor())) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
        }

       return true;
    }
}
