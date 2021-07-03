import junit.framework.TestCase;
import net.bqc.oasis.junit.JavaReflection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class MyTest extends TestCase {
    static Board board;
    static Game game;


    // @Before
    public MyTest() {
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
    }


    @Test
    public void test_addPiece_method_in_board() {
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
    }

    @Test
    public void test_validate_method_in_board() {
        Method boardValidateMethod = JavaReflection.getMethod(Board.class, "validate", boolean.class, "", "", int.class, int.class);
        if (boardValidateMethod == null) {
            Assert.fail("Can't not get method 'validate(int,int):boolean'");
        }

        try {
            boolean valid = (boolean) boardValidateMethod.invoke(board, 1, 2);
            Assert.assertEquals("Test 'validate(int,int):boolean' method with input = (1,2): ", true, valid);

            valid = (boolean) boardValidateMethod.invoke(board, -1, 5);
            Assert.assertEquals("Test 'validate(int,int):boolean' method with input = (-1,5): ", false, valid);

            valid = (boolean) boardValidateMethod.invoke(board, 4, 9);
            Assert.assertEquals("Test 'validate(int,int):boolean' method with input = (4,9): ", false, valid);

        } catch (IllegalAccessException e) {
            Assert.fail(e.getMessage());
        } catch (InvocationTargetException e) {
            String methodThrowExeption = e.getCause().getStackTrace()[0].getMethodName();
            Assert.fail(String.format("Catch exception '%s' while invoking method '%s'!", e.getCause().getMessage(), methodThrowExeption));
        }
    }

    @Test
    public void test_removeAt_method_in_board() {
        Method boardRemoveAtMethod = JavaReflection.getMethod(Board.class, "removeAt", void.class, "", "", int.class, int.class);
        if (boardRemoveAtMethod == null) {
            Assert.fail("Can't not get method 'removeAt(int,int):void'");
        }
        try {
            boardRemoveAtMethod.invoke(board, 2, 3);
            Assert.assertEquals("Remove existed Piece at (2,3), check 'pieces' size:", 3, board.getPieces().size());
            boardRemoveAtMethod.invoke(board, 8, 8);
            Assert.assertEquals("Remove not existed Piece at (8,8), check 'pieces' size:", 3, board.getPieces().size());
        } catch (IllegalAccessException e) {
            Assert.fail(e.getMessage());
        } catch (InvocationTargetException e) {
            String methodThrowExeption = e.getCause().getStackTrace()[0].getMethodName();
            Assert.fail(String.format("Catch exception '%s' while invoking method '%s'!", e.getCause().getMessage(), methodThrowExeption));
        }
    }

    @Test
    public void test_getAt_method_in_board() {

        Method boardGetAtMethod = JavaReflection.getMethod(Board.class, "getAt", Piece.class, "", "", int.class, int.class);
        if (boardGetAtMethod == null) {
            Assert.fail("Can't not get method 'getAt(int,int):Piece'");
        }
        try {
            Piece piece = (Piece) boardGetAtMethod.invoke(board, 2, 3);
            Assert.assertEquals("Get Rook at (2,3), check return not null: ", false, piece == null);
            Assert.assertEquals("Check type of Piece object at (2,3)", true, piece instanceof Rook);
            Assert.assertEquals("Check x of Piece object at (2,3)", 2, piece.getCoordinatesX());
            Assert.assertEquals("Check y of Piece object at (2,3)", 3, piece.getCoordinatesY());
            Assert.assertEquals("Check color of Piece object at (2,3)", "white", piece.getColor());

            piece = (Piece) boardGetAtMethod.invoke(board, 5, 5);
            Assert.assertEquals("Get Piece at (5,5), check return null: ", true, piece == null);

            piece = (Piece) boardGetAtMethod.invoke(board, -8, 9);
            Assert.assertEquals("Get Piece at (-8,9), check return null: ", true, piece == null);
        } catch (IllegalAccessException e) {
            Assert.fail(e.getMessage());
        } catch (InvocationTargetException e) {
            String methodThrowExeption = e.getCause().getStackTrace()[0].getMethodName();
            Assert.fail(String.format("Catch exception '%s' while invoking method '%s'!", e.getCause().getMessage(), methodThrowExeption));
        }
    }

    @Test
    public void test_toString_method_in_move() {
        try {
            Constructor moveConstructor5ParamMethod = Move.class.getConstructor(int.class, int.class, int.class, int.class, Piece.class);
            try {
                Constructor moveConstructor6ParamMethod = Move.class.getConstructor(int.class, int.class, int.class, int.class, Piece.class, Piece.class);

                try {
                    Move move = (Move) moveConstructor5ParamMethod.newInstance(2, 6, 3, 3, new Rook(2, 3, "white"));
                    String s = move.toString();
                    Assert.assertEquals("Check 'toString():String' of Move(2,6,3,3,Rook(2,3,'white'))", "white-Rf3", s);

                    move = (Move) moveConstructor6ParamMethod.newInstance(2, 7, 3, 3, new Rook(2, 3, "white"), new Rook(7, 3, "black"));
                    s = move.toString();
                    Assert.assertEquals("Check 'toString():String' of Move(2,7,3,3,Rook(2,3,'white'),Rook(7,3,'black'))", "white-Rg3", s);
                } catch (InstantiationException e) {
                    Assert.fail("Call constructor 'Move(int,int,int,int,Piece)' failed!!");
                } catch (IllegalAccessException e) {
                    Assert.fail("Access constructor 'Move(int,int,int,int,Piece)' failed!!");
                } catch (InvocationTargetException e) {
                    String methodThrowExeption = e.getCause().getStackTrace()[0].getMethodName();
                    Assert.fail(String.format("Catch exception '%s' while invoking method '%s'!", e.getCause().getMessage(), methodThrowExeption));
                }
            } catch (NoSuchMethodException e) {
                Assert.fail("Can't not get constructor Move(int,int,int,int,Piece,Piece)");
            }
        } catch (NoSuchMethodException e) {
            Assert.fail("Can't not get constructor Move(int,int,int,int,Piece)");
        }
    }

    @Test
    public void test_getSymbol_method_in_rook() {
        Method rookGetSymbolMethod = JavaReflection.getMethod(Rook.class, "getSymbol", String.class, "", "");
        if (rookGetSymbolMethod == null) {
            Assert.fail("Can't not access method 'getSymbol():string'");
        }
        try {
            Rook rook = new Rook(1, 1, "white");
            String symbol = (String) rookGetSymbolMethod.invoke(rook);
            Assert.assertEquals("Check getSymbol() of Rook return 'R'", "R", symbol);
        } catch (IllegalAccessException e) {
            Assert.fail(e.getMessage());
        } catch (InvocationTargetException e) {
            String methodThrowExeption = e.getCause().getStackTrace()[0].getMethodName();
            Assert.fail(String.format("Catch exception '%s' while invoking method '%s'!", e.getCause().getMessage(), methodThrowExeption));
        }
    }

    @Test
    public void test_canMove_method_in_rook() {
        Method rookCanMoveMethod;
        rookCanMoveMethod = JavaReflection.getMethod(Rook.class, "canMove", boolean.class, "", "", Board.class, int.class, int.class);
        if (rookCanMoveMethod == null) {
            Assert.fail("Can't not access method 'canMove(Board,int,int):string'");
        }
        try {
            Rook rook = (Rook) board.getPieces().get(0);
            boolean canMove = (boolean) rookCanMoveMethod.invoke(rook, board, 2, 5);
            Assert.assertEquals("Check canMove() method, must return true with input=(2,5)", true, canMove);

            canMove = (boolean) rookCanMoveMethod.invoke(rook, board, 2, 6);
            Assert.assertEquals("Check canMove() method, must return true with input=(2,6)", true, canMove);

            canMove = (boolean) rookCanMoveMethod.invoke(rook, board, 7, 3);
            Assert.assertEquals("Check canMove() method, must return true with input=(7,3)", false, canMove);

            canMove = (boolean) rookCanMoveMethod.invoke(rook, board, 8, 3);
            Assert.assertEquals("Check canMove() method, must return false with input=(8,3)", false, canMove);

            canMove = (boolean) rookCanMoveMethod.invoke(rook, board, 3, 4);
            Assert.assertEquals("Check canMove() method, must return false with input=(3,4)", false, canMove);
        } catch (IllegalAccessException e) {
            Assert.fail(e.getMessage());
        } catch (InvocationTargetException e) {
            String methodThrowExeption = e.getCause().getStackTrace()[0].getMethodName();
            Assert.fail(String.format("Catch exception '%s' while invoking method '%s'!", e.getCause().getMessage(), methodThrowExeption));
        }
    }

    @Test
    public void test_movePiece_method_in_game() {
        Method gameMovePieceMethod;
        gameMovePieceMethod = JavaReflection.getMethod(Game.class, "movePiece", void.class, "", "", Piece.class, int.class, int.class);
        if (gameMovePieceMethod == null) {
            Assert.fail("Can't not access method 'movePiece(Piece,int,int):void'");
        }
        try {
            Rook rook = (Rook) board.getPieces().get(0);
            gameMovePieceMethod.invoke(game, rook, 2, 6);
            Assert.assertEquals("Check moveHistory not null after moving one Piece", false, game.getMoveHistory() == null);
            Assert.assertEquals("Move White Rook to (2,6), defeat black Rook at (2,6), check moveHistory size", 1, game.getMoveHistory().size());
            Assert.assertEquals("Move White Rook to (2,6), defeat black Rook at (2,6), check new X position of movedPiece", 2, rook.getCoordinatesX());
            Assert.assertEquals("Move White Rook to (2,6), defeat black Rook at (2,6), check new Y position of movedPiece", 6, rook.getCoordinatesY());

            Rook rook1 = (Rook) board.getAt(7, 6);
            Assert.assertEquals("Get Piece at (7,6), must return black rook", false, rook1 == null);

            gameMovePieceMethod.invoke(game, rook1, 2, 6);
            Assert.assertEquals("Check moveHistory not null after 2 movements", false, game.getMoveHistory() == null);
            Assert.assertEquals("Move Black Rook to (2,6), defeat white Rook at (2,6), check moveHistory size", 2, game.getMoveHistory().size());
            Assert.assertEquals("Move Black Rook to (2,6), defeat white Rook at (2,6), check new X position of movedPiece", 2, rook1.getCoordinatesX());
            Assert.assertEquals("Move Black Rook to (2,6), defeat white Rook at (2,6), check new Y position of movedPiece", 6, rook1.getCoordinatesY());

            Rook rook1Empty = (Rook) board.getAt(7, 6);
            Assert.assertEquals("Move Black Rook from (7,6) to (2,6), check getPiece(7,6) must return null", true, rook1Empty == null);
            Move firstMove = game.getMoveHistory().get(0);
            Assert.assertEquals("Check killedPiece at first move, must not null", true, !(firstMove.getKilledPiece() == null));
            Move secondMove = game.getMoveHistory().get(1);
            Assert.assertEquals("Check killedPiece at second move, must not null", true, !(secondMove.getKilledPiece() == null));
        } catch (IllegalAccessException e) {
            Assert.fail(e.getMessage());
        } catch (InvocationTargetException e) {
            String methodThrowExeption = e.getCause().getStackTrace()[0].getMethodName();
            Assert.fail(String.format("Catch exception '%s' while invoking method '%s'!", e.getCause().getMessage(), methodThrowExeption));
        }
    }
}
