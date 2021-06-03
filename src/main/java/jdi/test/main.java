package jdi.test;

import net.bqc.oasis.junit.JavaReflection;
import org.junit.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class main {
    public static void main(String[] args) {
         Board board;
         Game game;
        board = new Board();
        ArrayList<Piece> pieces = new ArrayList<>();
        Rook rookWhite1 = new Rook(2, 3, "white");
        Rook rookWhite2 = new Rook(7, 3, "white");
        Rook rookWhite3 = new Rook(2, 6, "black");
        Rook rookWhite4 = new Rook(7, 6, "black");
        pieces.add(rookWhite1);
        pieces.add(rookWhite2);
        pieces.add(rookWhite3);
        pieces.add(rookWhite4);
        board.setPieces(pieces);
        game = new Game(board);
        Assert.assertEquals(4, board.getPieces().size());
        Rook newRook = new Rook(5, 3, "white");
        try {
            Method boardAddPieceMethod = JavaReflection.getMethod(Board.class, "addPiece", void.class, "", "", Piece.class);
            if (boardAddPieceMethod == null) {
                Assert.fail("Can't not get method 'addPiece(Piece)'");
            }
            boardAddPieceMethod.invoke(board, newRook);
            Assert.assertEquals("Add valid Rook object to Board, check 'pieces' size:", 5, board.getPieces().size());
            boardAddPieceMethod.invoke(board, newRook);
            Assert.assertEquals("Add duplicate Rook object to Board, check 'pieces' size:", 5, board.getPieces().size());
        } catch (IllegalAccessException e) {
            Assert.fail(e.getMessage());
        } catch (InvocationTargetException e) {
            String methodThrowExeption = e.getCause().getStackTrace()[0].getMethodName();
            Assert.fail(String.format("Catch exception '%s' while invoking method '%s'!", e.getCause().getMessage(), methodThrowExeption));
        }
        int a = 100;
        String b = "ẠKDHKDJS";
        System.out.println("BYEEEEEEE");
    }
}
