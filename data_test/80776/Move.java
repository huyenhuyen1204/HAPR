public class Move {

    private  Piece movedPiece;
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private Piece killedPiece;

    /**
     * phuong thuc khoi tao.
     */
    public Move(int startX, int endX, int startY, int endY, Piece movedPiece) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.movedPiece = movedPiece;
        killedPiece = null;
    }

    /**
     * phuong thuc khoi tao.
     */
    public Move(int startX, int endX, int startY, int endY, Piece movedPiece, Piece killedPiece) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.movedPiece = movedPiece;
        this.killedPiece = killedPiece;
    }

    public Piece getMovedPiece() {
        return movedPiece;
    }

    public void setMovedPiece(Piece movedPiece) {
        this.movedPiece = movedPiece;
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

    public Piece getKilledPiece() {
        return killedPiece;
    }

    public void setKilledPiece(Piece killedPiece) {
        this.killedPiece = killedPiece;
    }

    @Override
    public String toString() {
        String indexX = null;
        switch (movedPiece.getCoordinatesX()) {
            case 1 :
                indexX = "a";
                break;
            case 2 :
                indexX = "b";
                break;
            case 3 :
                indexX = "c";
                break;
            case 4 :
                indexX = "d";
                break;
            case 5 :
                indexX = "e";
                break;
            case 6 :
                indexX = "f";
                break;
            case 7 :
                indexX = "g";
                break;
            case 8 :
                indexX = "h";
                break;
        }
        return "`" + movedPiece.getColor() + "-" + movedPiece.getSymbol() + indexX
                + String.valueOf(movedPiece.getCoordinatesY()) + "`";
    }
}
