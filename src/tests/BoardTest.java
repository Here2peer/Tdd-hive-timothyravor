package tests;

import classes.Board;
import classes.Piece;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class BoardTest {

    Board board;

    @BeforeEach
    public void CreateNewBoardBeforeEachTest() {
        board = new Board();
    }

    @Test
    public void BoardIsEmpty() {
        Map<Integer, Map<Integer, Piece>> refBoard = new HashMap<>();
        Assertions.assertEquals(refBoard, board.getGameBoard());
    }

}
