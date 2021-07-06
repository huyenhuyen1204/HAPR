import java.util.ArrayList;

public class Board {

    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    private ArrayList<Piece> pieces = new ArrayList<>();

    /**
     * kiem tra toa do x, y co thuoc ban co khong.
     */
    public boolean validate(int x, int y) {
        if (x > WIDTH || y > HEIGHT || x < 0 || y < 0) {
            return false;
        }
        return true;
    }

    /**
     * add.
     */
    public void addPiece(Piece piece) {
       int l = pieces.size();
       for (int i = 0; i < l; i++) {
           if (piece.getCoordinatesX() == pieces.get(i).getCoordinatesX()
                   && piece.getCoordinatesY() == pieces.get(i).getCoordinatesY()) {
               return;
           }
       }
       pieces.add(piece);
    }

    /**
     * tra ve quan co dang o vi tri x, y.
     */
    public Piece getAt(int x, int y) {
        int l = pieces.size();
        for (int i = 0; i < l; i++) {
            if (pieces.get(i).getCoordinatesX() == x && pieces.get(i).getCoordinatesY() == y) {
                return pieces.get(i);
            }
        }
        return null;
    }

    /**
     * xoa phan tu o vi tri x, y.
     */
    public void removeAt(int x, int y) {
        int l = pieces.size();
        for (int i = 0; i < l; i++) {
            if (pieces.get(i).getCoordinatesX() == x && pieces.get(i).getCoordinatesY() == y) {
                pieces.remove(i);
                break;
            }
        }
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public void setPieces(ArrayList<Piece> pieces) {
        this.pieces = pieces;
    }
}
