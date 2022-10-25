package tests;

import classes.Game;
import classes.Piece;
import org.junit.jupiter.api.BeforeEach;

public class GameTest {
    Game game;
    Piece piece;

    @BeforeEach
    public void CreateNewGame() {
        game = new Game();
        piece = new Piece();
    }

}
