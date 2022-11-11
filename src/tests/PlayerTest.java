package tests;

import classes.Player;
import interfaces.Hive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTest {

    Player player1;
    Player player2;


    @BeforeEach
    public void CreateNewPlayer() {
        player1 = new Player(Hive.Player.WHITE);
        player2 = new Player(Hive.Player.BLACK);
    }

    @Test
    public void ItIsNotPlayer1Turn() {
        Assertions.assertFalse(player2.isMyTurn());
    }

    @Test
    public void Player1PassesTurn() {
        Assertions.assertFalse(player1.passTurn());
    }

    @Test
    public void PlayerGetPlacablePiece() {
        Assertions.assertEquals( Hive.Tile.QUEEN_BEE, player1.getPlacablePiece(Hive.Tile.QUEEN_BEE).getType());
    }

    @Test
    public void PlayerWhiteStarts() {
        Assertions.assertTrue(player1.isMyTurn());
    }

}
