import java.util.ArrayList;

public class Game {
    private Board board;
    private ArrayList<Move> moveHistory;

    public Game(Board board) {
        this.board = board;
        moveHistory = new ArrayList<>();
    }

    /**
     * di chuyen.
     */
    public void movePiece(Piece piece, int x, int y) {
        int l =  board.getPieces().size();
        if (piece.canMove(board, x, y)) {
            for (int i = 0; i < l; i++) {
                if (board.getPieces().get(i).getCoordinatesX() == x
                        && board.getPieces().get(i).getCoordinatesY() == y) {
                    Move move = new Move(piece.getCoordinatesX(), x, piece.getCoordinatesY(),
                            y, piece, board.getPieces().get(i));
                    moveHistory.add(move);
                    board.removeAt(x, y);
                    return;
                }
            }
            Move move = new Move(piece.getCoordinatesX(), x, piece.getCoordinatesY(), y, piece);
            moveHistory.add(move);
        }
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public ArrayList<Move> getMoveHistory() {
        return moveHistory;
    }

    public void setMoveHistory(ArrayList<Move> moveHistory) {
        this.moveHistory = moveHistory;
    }
}
