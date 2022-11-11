package tests;

import classes.Board;
import classes.Piece;
import interfaces.Hive;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BoardTest {

    Board board;
    Piece piece1;
    Piece piece2;

    @BeforeEach
    public void CreateNewBoardBeforeEachTest() {
        board = new Board();
        piece1 = new Piece(Hive.Tile.BEETLE, Hive.Player.WHITE);
        piece2 = new Piece(Hive.Tile.GRASSHOPPER, Hive.Player.WHITE);
    }

    @Test
    public void BoardIsEmpty() {
        Assertions.assertTrue(board.getGameBoard().isEmpty());
    }

    @Test
    public void PlaceTileOnTheboardAndRetreiveTile() throws Hive.IllegalMove {
        board.setTile(piece1, 1000, -1000);
        ArrayList<Piece> pieces = board.getTile(1000, -1000);
        Assertions.assertEquals(piece1, pieces.get(pieces.size() - 1));
    }

    @Test
    public void NoTilePlaceOnCoordsThrowsException() {
        Assertions.assertNull(board.getTile(0,0));
    }

    @Test
    public void MovePlacedTile() throws Hive.IllegalMove {
        board.setTile(piece1, 0, 0);
        board.moveTile(0, 0, -1,0);
        ArrayList<Piece> pieces = board.getTile(-1, 0);
        Assertions.assertEquals(piece1, pieces.get(pieces.size() - 1));
    }

    @Test
    public void MovePlacedTileDoesntExistThrowsError() {
        Assertions.assertThrows(Hive.IllegalMove.class, () ->  board.moveTile(0, 0, -1,0));
    }

    @Test
    public void PlaceTilesOnTopOfEachOther() throws Hive.IllegalMove {
        board.setTile(piece1, 0, 0);
        board.setTile(piece2, 0, 0);
        board.moveTile(0, 0, -1,0);
        ArrayList<Piece> pieces = board.getTile(0, 0);
        Assertions.assertEquals(piece1, pieces.get(pieces.size() - 1));
    }

    @Test
    public void CheckIfTileHasSixNeighbours() {
        board.setTile(piece1, 2,2);
        board.setTile(piece2, -1,0);
        board.setTile(piece2, 0,-1);
        board.setTile(piece2, 1,-1);
        board.setTile(piece2, 1,0);
        board.setTile(piece2, 0,1);
        board.setTile(piece2, -1,1);
        Assertions.assertEquals(6, board.getNeighbours(0,0).size());
    }



//    is goed!

}
