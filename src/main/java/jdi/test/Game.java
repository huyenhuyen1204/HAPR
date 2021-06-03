package jdi.test;

import java.util.ArrayList;

public class Game {
    private Board board;
    private ArrayList<Move> moveHistory;

    public Game(Board board) {
        this.board = board;
    }

    /**movePiece.*/
    public void movePiece(Piece piece, int x, int y) {
        Move move;
        if (piece.canMove(board, x, y)) {
            if (board.getAt(x, y) == null) {
                board.removeAt(piece.getCoordinatesX(), piece.getCoordinatesY());
                board.addPiece(piece);
                piece.setCoordinatesX(x);
                piece.setCoordinatesY(y);
                move = new Move(piece.getCoordinatesX(), x, piece.getCoordinatesY(), y, piece);
                moveHistory.add(move);
            } else {
                move = new Move(piece.getCoordinatesX(), x, piece.getCoordinatesY(), y, piece, board.getAt(x, y));
                board.removeAt(piece.getCoordinatesX(), piece.getCoordinatesY());
                board.removeAt(x, y);
                board.addPiece(piece);
                piece.setCoordinatesX(x);
                piece.setCoordinatesY(y);
                moveHistory.add(move);
            }
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
}
