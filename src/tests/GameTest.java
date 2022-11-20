package tests;

import classes.Game;
import classes.Piece;
import interfaces.Hive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameTest {
    Game game;

    @BeforeEach
    public void CreateNewGame() {
        game = new Game();
    }

    @Test
    public void PlayerCanPlaceATileInTheirTurn() throws Hive.IllegalMove {
        game.play(Hive.Tile.BEETLE, 1, 1);
        Piece piece = game.findPiece(Hive.Tile.BEETLE, 1,1, Hive.Player.WHITE);
        Assertions.assertTrue(piece.getType() == Hive.Tile.BEETLE && piece.getPlayer() == Hive.Player.WHITE);
    }

    @Test
    public void NextPlayerTurnAfterAPlayIsDone() throws Hive.IllegalMove {
        game.play(Hive.Tile.BEETLE, 1, 1);
        Assertions.assertEquals(Hive.Player.BLACK, game.getCurrentPlayer().getColour());
    }

    @Test
    public void AtGameStartCheckCurrentPlayerIsWhite() {
        Assertions.assertEquals(Hive.Player.WHITE, game.getCurrentPlayer().getColour());
    }

    @Test
    public void ThrowIlligalMoveWhenPlayerHasTilesInHandAndTriesToPass() {
        Assertions.assertThrows(Hive.IllegalMove.class, () -> game.pass());
    }

    @Test
    public void CheckIfPlayerWhiteHasWon() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 1, 1); //wit

        game.play(Hive.Tile.BEETLE, 0, 4); //zwart

        game.play(Hive.Tile.BEETLE, 1, 0); //wit

        game.play(Hive.Tile.BEETLE, 0, 5); //zwart

        game.play(Hive.Tile.BEETLE, 1, 2);// wit

        game.play(Hive.Tile.GRASSHOPPER, 0, 6); //zwart

        game.play(Hive.Tile.GRASSHOPPER, 2, 0); //wit

        game.play(Hive.Tile.QUEEN_BEE, 0, 7); //zwart

        game.play(Hive.Tile.GRASSHOPPER, 2, 1); //wit

        game.play(Hive.Tile.GRASSHOPPER, 0, 8); //zwart

        game.play(Hive.Tile.GRASSHOPPER, 0, 1); //wit

        game.play(Hive.Tile.SOLDIER_ANT, 0, 9); //zwart

        game.play(Hive.Tile.SOLDIER_ANT, 0, 2); //wit

        Assertions.assertTrue(game.isWinner(Hive.Player.WHITE));
    }
    @Test
    public void CheckIfPlayerWhiteHasNotWon() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 1, 1);

        Assertions.assertFalse(game.isWinner(Hive.Player.WHITE));
    }
    @Test
    public void CheckIfDraw() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 1, 1);
        game.play(Hive.Tile.QUEEN_BEE, 0, 1);

        game.play(Hive.Tile.BEETLE, 1, 2);
        game.play(Hive.Tile.BEETLE, -1, 2);

        game.play(Hive.Tile.BEETLE, 2, 1);
        game.play(Hive.Tile.BEETLE, -1, 1);

        game.play(Hive.Tile.SOLDIER_ANT, 2, 0);
        game.play(Hive.Tile.SOLDIER_ANT, 0, 0);

        game.play(Hive.Tile.SOLDIER_ANT, 0, 3);
        game.play(Hive.Tile.SOLDIER_ANT, 1, -1);

        game.move(0,3, 0, 2);
        game.move(1,-1, 1, 0);

        Assertions.assertTrue(game.isDraw());
    }
    @Test
    public void CheckIfNotDraw() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 1, 1);
        game.play(Hive.Tile.QUEEN_BEE, 0, 1);

        game.play(Hive.Tile.BEETLE, 1, 2);
        game.play(Hive.Tile.BEETLE, -1, 2);

        game.play(Hive.Tile.BEETLE, 2, 1);
        game.play(Hive.Tile.BEETLE, -1, 1);

        game.play(Hive.Tile.SOLDIER_ANT, 2, 0);
        game.play(Hive.Tile.SOLDIER_ANT, 0, 0);

        game.play(Hive.Tile.SOLDIER_ANT, 0, 3);
        game.play(Hive.Tile.SOLDIER_ANT, 1, -1);

        Assertions.assertFalse(game.isDraw());
    }

    @Test
    public void PlayerCanOnlyPlayTwoBeetles() throws Hive.IllegalMove {
        game.play(Hive.Tile.BEETLE, 0, 2);
        game.play(Hive.Tile.BEETLE, 2, 0);
        game.play(Hive.Tile.BEETLE, 0, 1);
        Assertions.assertThrows(Hive.IllegalMove.class, () -> game.play(Hive.Tile.BEETLE, 0, 3));
    }

    @Test
    public void PlayerCanNotPlayTileOnAnotherTile() throws Hive.IllegalMove {
        game.play(Hive.Tile.BEETLE, 0, 2);
        Assertions.assertThrows(Hive.IllegalMove.class, () -> game.play(Hive.Tile.BEETLE, 0, 2));

    }

    @Test
    public void TileCanNotBeWithoutNeighbours() throws Hive.IllegalMove {
        game.play(Hive.Tile.BEETLE, 0, 2);
        game.play(Hive.Tile.BEETLE, 2, 0);

        Assertions.assertThrows(Hive.IllegalMove.class, () -> game.play(Hive.Tile.BEETLE, 0, 4));
    }
    @Test
    public void TileCanNotBePlayedNextToOppositeColour() throws Hive.IllegalMove {
        game.play(Hive.Tile.BEETLE, 0, 2);
        game.play(Hive.Tile.BEETLE, 0, 1);
        Assertions.assertThrows(Hive.IllegalMove.class, () -> game.play(Hive.Tile.BEETLE, 0, 0));
    }

    @Test
    public void QueenBeeMustBePlayedWhenThreeTilesHaveAlreadyBeenPlayed() throws Hive.IllegalMove {
        game.play(Hive.Tile.BEETLE, 0, 2);
        game.play(Hive.Tile.BEETLE, 2, 0);

        game.play(Hive.Tile.BEETLE, 0, 3);
        game.play(Hive.Tile.BEETLE, 3, 0);

        game.play(Hive.Tile.GRASSHOPPER, 0, 4);
        game.play(Hive.Tile.GRASSHOPPER, 4, 0);

        Assertions.assertThrows(Hive.IllegalMove.class, () -> game.play(Hive.Tile.GRASSHOPPER, 0, 5));
    }

    @Test
    public void CheckIfTileIsMovedByAnotherPlayer() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, -1, 0); //wit
        game.play(Hive.Tile.BEETLE, 0, 0); //zwart
        Assertions.assertThrows(Hive.IllegalMove.class, () -> game.move(0,0, -1, 1));
    }
    @Test
    public void MoveWhiteTile() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, -1, 0); //wit
        game.play(Hive.Tile.BEETLE, 0, 0); //zwart

        game.move(-1,0, 0, -1);
        Assertions.assertEquals(Hive.Tile.QUEEN_BEE, game.findPiece(Hive.Tile.QUEEN_BEE ,0,-1, Hive.Player.WHITE).getType());
    }

    @Test
    public void MoveTileIfBeeQueenOnBoard() throws Hive.IllegalMove {

        game.play(Hive.Tile.QUEEN_BEE, 0, 0); // wit
        game.play(Hive.Tile.BEETLE, -1, 0);

        game.play(Hive.Tile.BEETLE, 1, -1); // wit
        game.play(Hive.Tile.BEETLE, -2, 0);
        game.move(1,-1, 0, -1);
        Assertions.assertEquals(Hive.Tile.BEETLE, game.findPiece(Hive.Tile.BEETLE ,0,-1, Hive.Player.WHITE).getType());
    }
    @Test
    public void MoveTileIfQueenBeeIsNotOnBoard() throws Hive.IllegalMove {
        game.play(Hive.Tile.BEETLE, 0, 0); // wit
        game.play(Hive.Tile.BEETLE, -1, 0);

        game.play(Hive.Tile.BEETLE, 1, -1); // wit
        game.play(Hive.Tile.BEETLE, -2, 0);
        Assertions.assertThrows(Hive.IllegalMove.class, () -> game.move(0,0, -1, 0));
    }

    @Test
    public void MoveTileToLocationWithoutNeighbour() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, 0); // wit
        game.play(Hive.Tile.BEETLE, -1, 0);
        Assertions.assertThrows(Hive.IllegalMove.class, () -> game.move(0,0, 1, 0));
    }

    @Test
    public void MoveTileToLocationWitNeighbour() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, 0); // wit
        game.play(Hive.Tile.BEETLE, -1, 0);
        game.move(0,0, 0, -1);
        Assertions.assertEquals(Hive.Tile.QUEEN_BEE, game.findPiece(Hive.Tile.QUEEN_BEE ,0,-1, Hive.Player.WHITE).getType());
    }

    @Test
    public void MoveTileToLocationAndBreakHive() throws Hive.IllegalMove {

        game.play(Hive.Tile.QUEEN_BEE, 0, 0);
        game.play(Hive.Tile.QUEEN_BEE, -1, 0);

        game.play(Hive.Tile.BEETLE, 1, 0);
        game.play(Hive.Tile.BEETLE, -2, 0);

        game.play(Hive.Tile.SOLDIER_ANT, 2, 0);
        game.play(Hive.Tile.SOLDIER_ANT, -1, -1);

        game.play(Hive.Tile.SOLDIER_ANT, 1, 1);
        game.play(Hive.Tile.SOLDIER_ANT, -3, 0);

        Assertions.assertThrows(Hive.IllegalMove.class, () -> game.move(0,0, 1, -1));
    }
    @Test
    public void MoveTileToLocationWithoutBreakingHive() throws Hive.IllegalMove {

        game.play(Hive.Tile.QUEEN_BEE, 0, 0);
        game.play(Hive.Tile.QUEEN_BEE, -1, 0);

        game.play(Hive.Tile.BEETLE, 1, 0);
        game.play(Hive.Tile.BEETLE, -2, 0);

        game.play(Hive.Tile.SOLDIER_ANT, 2, 0);
        game.play(Hive.Tile.SOLDIER_ANT, -1, -1);

        game.play(Hive.Tile.SOLDIER_ANT, 1, 1);
        game.play(Hive.Tile.SOLDIER_ANT, -3, 0);

        game.move(2,0 , 2, -1);

        Assertions.assertEquals(Hive.Tile.SOLDIER_ANT, game.findPiece(Hive.Tile.SOLDIER_ANT, 2, -1, Hive.Player.WHITE).getType());

    }

    @Test
    public void ShiftTileBetweenLocation1() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, 0);
        game.play(Hive.Tile.QUEEN_BEE, -1, 0);

        game.play(Hive.Tile.BEETLE, 0, 1);
        game.play(Hive.Tile.BEETLE, -2, 1);

        game.move(0,1, -1, 1);

        Assertions.assertEquals(Hive.Tile.BEETLE,game.findPiece(Hive.Tile.BEETLE, -1, 1, Hive.Player.WHITE).getType());
    }
    @Test
    public void ShiftTileBetweenLocation2() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 1, 2);
        game.play(Hive.Tile.QUEEN_BEE, 1, 1);

        game.move(1,2, 2, 1);

        Assertions.assertEquals(Hive.Tile.QUEEN_BEE ,game.findPiece(Hive.Tile.QUEEN_BEE, 2, 1, Hive.Player.WHITE).getType());
    }
    @Test
    public void ShiftTileBetweenTwoPiecesWithNoRoom1() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, 0);
        game.play(Hive.Tile.QUEEN_BEE, -1, 0);

        game.play(Hive.Tile.BEETLE, 0, 1);
        game.play(Hive.Tile.BEETLE, -2, 1);


        Assertions.assertThrows(Hive.IllegalMove.class, () -> game.move(0,0, -1, 1));
    }
    @Test
    public void ShiftTileBetweenTwoPiecesWithNoRoom2() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 1, -2);
        game.play(Hive.Tile.QUEEN_BEE, 1, -1);

        game.play(Hive.Tile.BEETLE, 0, -2);
        game.play(Hive.Tile.BEETLE, 2, -1);


        Assertions.assertThrows(Hive.IllegalMove.class, () -> game.move(1,-2, 0, -1));
    }
    @Test
    public void ShiftTileBetweenTwoPiecesWithNoRoom3() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, -1);
        game.play(Hive.Tile.QUEEN_BEE, 1, -1);

        game.play(Hive.Tile.BEETLE, 0, -2);
        game.play(Hive.Tile.BEETLE, 2, -1);


        Assertions.assertThrows(Hive.IllegalMove.class, () -> game.move(0,-1, 1, -2));
    }
    @Test
    public void ShiftTileBetweenTwoPiecesWithNoRoom4() throws Hive.IllegalMove {

        game.play(Hive.Tile.QUEEN_BEE, 1, -2);
        game.play(Hive.Tile.QUEEN_BEE, 0, -1);

        game.play(Hive.Tile.BEETLE, 2, -2);
        game.play(Hive.Tile.BEETLE, -1, -1);

        Assertions.assertThrows(Hive.IllegalMove.class, () -> game.move(1,-2, 1, -1));
    }
    @Test
    public void ShiftTileBetweenTwoPiecesWithNoRoom5() throws Hive.IllegalMove {

        game.play(Hive.Tile.QUEEN_BEE, 1, -1);
        game.play(Hive.Tile.QUEEN_BEE, 0, -1);

        game.play(Hive.Tile.BEETLE, 2, -2);
        game.play(Hive.Tile.BEETLE, -1, -1);


        Assertions.assertThrows(Hive.IllegalMove.class, () -> game.move(1,-1, 1, -2));
    }
    @Test
    public void ShiftTileBetweenTwoPiecesWithNoRoom6() throws Hive.IllegalMove {

        game.play(Hive.Tile.QUEEN_BEE, 0, 0);
        game.play(Hive.Tile.QUEEN_BEE, -1, 1);

        game.play(Hive.Tile.BEETLE, 0, -1);
        game.play(Hive.Tile.BEETLE, -1, 2);


        Assertions.assertThrows(Hive.IllegalMove.class, () -> game.move(0,0, -1, 0));
    }
    @Test
    public void ShiftTileBetweenTwoPiecesWithNoRoom7() throws Hive.IllegalMove {

        game.play(Hive.Tile.QUEEN_BEE, -1, 0);
        game.play(Hive.Tile.QUEEN_BEE, -1, 1);

        game.play(Hive.Tile.BEETLE, 0, -1);
        game.play(Hive.Tile.BEETLE, -1, 2);


        Assertions.assertThrows(Hive.IllegalMove.class, () -> game.move(-1,0, 0, 0));
    }
    @Test
    public void ShiftTileBetweenTwoPiecesWithNoRoom8() throws Hive.IllegalMove {

        game.play(Hive.Tile.QUEEN_BEE, 0, -1);
        game.play(Hive.Tile.QUEEN_BEE, -1, -1);

        game.play(Hive.Tile.BEETLE, 1, -2);
        game.play(Hive.Tile.BEETLE, -2, -1);


        Assertions.assertThrows(Hive.IllegalMove.class, () -> game.move(0,-1, 0, -2));
    }

    @Test
    public void MoveWhileThereAreTwoHives1() throws Hive.IllegalMove {

        game.play(Hive.Tile.QUEEN_BEE, -1, 0);
        game.play(Hive.Tile.QUEEN_BEE, 1, 0);

        game.play(Hive.Tile.BEETLE, -2, 0);
        game.play(Hive.Tile.BEETLE, 2, 0);

        game.move(-2,0, -1, -1);

        Assertions.assertEquals(Hive.Tile.BEETLE ,game.findPiece(Hive.Tile.BEETLE, -1, -1, Hive.Player.WHITE).getType());
    }
    @Test
    public void MoveWhileThereAreTwoHives2() throws Hive.IllegalMove {

        game.play(Hive.Tile.QUEEN_BEE, -1, 0);
        game.play(Hive.Tile.QUEEN_BEE, 1, 0);

        game.play(Hive.Tile.BEETLE, 0, -1);
        game.play(Hive.Tile.BEETLE, 2, 0);

        game.move(-1,0, 0, 0);

        Assertions.assertTrue(game.isHiveConnected());
    }
    @Test
    public void MoveWhileThereAreTwoHives3() throws Hive.IllegalMove {

        game.play(Hive.Tile.QUEEN_BEE, -1, 0);
        game.play(Hive.Tile.QUEEN_BEE, 1, 0);

        game.play(Hive.Tile.BEETLE, 0, -1);
        game.play(Hive.Tile.BEETLE, 2, 0);

        game.move(-1,0, 0, 0);


        Assertions.assertThrows(Hive.IllegalMove.class, () -> game.move(1,0, 1, -1));
    }

    @Test
    public void ShiftWithNoBorderingPiece() throws Hive.IllegalMove {

        game.play(Hive.Tile.QUEEN_BEE, 0, -1);
        game.play(Hive.Tile.QUEEN_BEE, 1, -1);

        game.play(Hive.Tile.BEETLE, -1, 0);
        game.play(Hive.Tile.BEETLE, 1, 0);

        game.play(Hive.Tile.BEETLE, -1, -1);
        game.play(Hive.Tile.BEETLE, 0, 1);

        Assertions.assertThrows(Hive.IllegalMove.class, () -> game.move(-1,0, -1, 1));
    }
    @Test
    public void ShiftWithBorderingPiece() throws Hive.IllegalMove {

        game.play(Hive.Tile.QUEEN_BEE, 0, -1);
        game.play(Hive.Tile.QUEEN_BEE, 1, -1);

        game.play(Hive.Tile.BEETLE, -1, 0);
        game.play(Hive.Tile.BEETLE, 1, 0);

        game.play(Hive.Tile.BEETLE, -1, -1);
        game.play(Hive.Tile.BEETLE, 0, 1);

        game.move(-1,0, 0, 0);
        game.play(Hive.Tile.SPIDER, 2, 0);

        game.move(-1,-1, -1, 0);
        game.play(Hive.Tile.SPIDER, 3, 0);

        game.move(-1,0, -1, 1);

        Assertions.assertEquals(Hive.Tile.BEETLE ,game.findPiece(Hive.Tile.BEETLE, -1, 1, Hive.Player.WHITE).getType());

    }

    @Test
    public void BeetleCannotMoveTwoPieces() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, -1);
        game.play(Hive.Tile.QUEEN_BEE, 1, -1);

        game.play(Hive.Tile.BEETLE, -1, 0);
        game.play(Hive.Tile.BEETLE, 1, 0);
        Assertions.assertThrows(Hive.IllegalMove.class, () -> game.move(-1,0, 0, -2));
    }
    @Test
    public void BeetleCanMoveOneSpace() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, -1);
        game.play(Hive.Tile.QUEEN_BEE, 1, -1);

        game.play(Hive.Tile.BEETLE, -1, 0);
        game.play(Hive.Tile.BEETLE, 1, 0);

        game.move(-1,0, -1, -1);

        Assertions.assertEquals(Hive.Tile.BEETLE ,game.findPiece(Hive.Tile.BEETLE, -1, -1, Hive.Player.WHITE).getType());
    }
    @Test
    public void BeetleCanMoveOneSpaceOnTopOfOtherPiece() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, -1);
        game.play(Hive.Tile.QUEEN_BEE, 1, -1);

        game.play(Hive.Tile.BEETLE, -1, 0);
        game.play(Hive.Tile.BEETLE, 1, 0);

        game.move(-1, 0, 0, -1);

        Assertions.assertEquals(Hive.Tile.BEETLE ,game.findPiece(Hive.Tile.BEETLE, 0, -1, Hive.Player.WHITE).getType());
    }

    @Test
    public void QueenBeeCanMoveOneSpace() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, -1);
        game.play(Hive.Tile.QUEEN_BEE, 1, -1);

        game.move(0,-1, 1, -2);
        Assertions.assertEquals(Hive.Tile.QUEEN_BEE ,game.findPiece(Hive.Tile.QUEEN_BEE, 1, -2, Hive.Player.WHITE).getType());
    }
    @Test
    public void QueenBeeCannotMoveOnTop() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, -1);
        game.play(Hive.Tile.QUEEN_BEE, 1, -1);

        Assertions.assertThrows(Hive.IllegalMove.class, () -> game.move(0,-1, 1, -1));
    }

    @Test
    public void SoldierAntCanMoveUnlimitedTiles() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, 0);
        game.play(Hive.Tile.QUEEN_BEE, -1, 0);

        game.play(Hive.Tile.SOLDIER_ANT, 1, 0);
        game.play(Hive.Tile.BEETLE, -2, 0);

        game.move(1,0, -3, 0);
        Assertions.assertEquals(Hive.Tile.SOLDIER_ANT ,game.findPiece(Hive.Tile.SOLDIER_ANT, -3, 0, Hive.Player.WHITE).getType());
    }
    @Test
    public void SoldierAntCanMoveUnlimitedTilesNotAllowed() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 1, 0);
        game.play(Hive.Tile.QUEEN_BEE, 0, 2);

        game.play(Hive.Tile.SOLDIER_ANT, 2, 0);
        game.play(Hive.Tile.BEETLE, -1, 2);

        game.play(Hive.Tile.SOLDIER_ANT, 2, 1);
        game.play(Hive.Tile.BEETLE, -1, 1);

        game.move(2,1, 1, 1);
        game.play(Hive.Tile.GRASSHOPPER, -1, 0);

        game.play(Hive.Tile.GRASSHOPPER, 1, -1);
        game.play(Hive.Tile.GRASSHOPPER, -2, 0);

        Assertions.assertThrows(Hive.IllegalMove.class, () -> game.move(1,1, 0, 0));
    }
    @Test
    public void GrasshopperCanJumpOverPieces1() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, -1, 0);
        game.play(Hive.Tile.QUEEN_BEE, 0, 0);

        game.play(Hive.Tile.SOLDIER_ANT, -2, 0);
        game.play(Hive.Tile.BEETLE, 1, 0);

        game.play(Hive.Tile.GRASSHOPPER, -3, 0);
        game.play(Hive.Tile.BEETLE, 0, 1);

        game.move(-3,0, 2, 0);

        Assertions.assertEquals(Hive.Tile.GRASSHOPPER ,game.findPiece(Hive.Tile.GRASSHOPPER, 2, 0, Hive.Player.WHITE).getType());
    }
    @Test
    public void GrasshopperCanJumpOverPieces2() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, -1, 0);
        game.play(Hive.Tile.QUEEN_BEE, 0, 0);

        game.play(Hive.Tile.SOLDIER_ANT, -2, 0);
        game.play(Hive.Tile.GRASSHOPPER, 1, 0);

        game.play(Hive.Tile.GRASSHOPPER, -3, 0);
        game.play(Hive.Tile.GRASSHOPPER, 0, 1);

        game.play(Hive.Tile.BEETLE, -2, 1);
        game.move(1,0, -4, 0);

        Assertions.assertEquals(Hive.Tile.GRASSHOPPER ,game.findPiece(Hive.Tile.GRASSHOPPER, -4, 0, Hive.Player.BLACK).getType());
    }
    @Test
    public void GrasshopperCanJumpOverPieces3() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, -1, 0);
        game.play(Hive.Tile.QUEEN_BEE, 0, -1);

        game.play(Hive.Tile.SOLDIER_ANT, -2, 1);
        game.play(Hive.Tile.BEETLE, 1, -2);

        game.play(Hive.Tile.GRASSHOPPER, -3, 2);
        game.play(Hive.Tile.BEETLE, 2, -3);
        game.move(-3,2, 3, -4);

        Assertions.assertEquals(Hive.Tile.GRASSHOPPER ,game.findPiece(Hive.Tile.GRASSHOPPER, 3, -4, Hive.Player.WHITE).getType());
   }
   @Test
    public void GrasshopperCanJumpOverPieces4() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, -1, 0);
        game.play(Hive.Tile.QUEEN_BEE, 0, -1);

        game.play(Hive.Tile.SOLDIER_ANT, -2, 1);
        game.play(Hive.Tile.BEETLE, 1, -2);

        game.play(Hive.Tile.GRASSHOPPER, -3, 2);
        game.play(Hive.Tile.GRASSHOPPER, 2, -3);


       game.play(Hive.Tile.GRASSHOPPER, -1, 1);
       game.move(2,-3, -4, 3);

        Assertions.assertEquals(Hive.Tile.GRASSHOPPER ,game.findPiece(Hive.Tile.GRASSHOPPER, -4, 3, Hive.Player.BLACK).getType());
   }
   @Test
    public void GrasshopperCanJumpOverPieces5() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, -1);
        game.play(Hive.Tile.QUEEN_BEE, 0, 0);

        game.play(Hive.Tile.SOLDIER_ANT, 0, -2);
        game.play(Hive.Tile.BEETLE, 0, 1);

        game.play(Hive.Tile.GRASSHOPPER, 0, -3);
        game.play(Hive.Tile.GRASSHOPPER, 0, 2);

        game.move(0,-3, 0, 3);

        Assertions.assertEquals(Hive.Tile.GRASSHOPPER ,game.findPiece(Hive.Tile.GRASSHOPPER, 0, 3, Hive.Player.WHITE).getType());
   }
   @Test
    public void GrasshopperCanJumpOverPieces6() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, 0, -1);
        game.play(Hive.Tile.QUEEN_BEE, 0, 0);

        game.play(Hive.Tile.SOLDIER_ANT, 0, -2);
        game.play(Hive.Tile.BEETLE, 0, 1);

        game.play(Hive.Tile.GRASSHOPPER, 0, -3);
        game.play(Hive.Tile.GRASSHOPPER, 0, 2);


        game.play(Hive.Tile.BEETLE, 1, -2);
        game.move(0,2, 0, -4);

        Assertions.assertEquals(Hive.Tile.GRASSHOPPER ,game.findPiece(Hive.Tile.GRASSHOPPER, 0, -4, Hive.Player.BLACK).getType());
   }

   @Test
    public void GrasshopperCanJumpOverPiecesNotAllowed() throws Hive.IllegalMove {
        game.play(Hive.Tile.QUEEN_BEE, -1, 0);
        game.play(Hive.Tile.QUEEN_BEE, 0, -1);

        game.play(Hive.Tile.SOLDIER_ANT, -2, 1);
        game.play(Hive.Tile.BEETLE, 1, -2);

        game.play(Hive.Tile.GRASSHOPPER, -3, 2);
        game.play(Hive.Tile.BEETLE, 2, -3);

        Assertions.assertThrows(Hive.IllegalMove.class, () -> game.move(-3,2, 2, -2));
   }

   @Test
    public void SpiderCanMoveOnlyThreeSpaces() throws Hive.IllegalMove {
       game.play(Hive.Tile.QUEEN_BEE, -1, 0);
       game.play(Hive.Tile.QUEEN_BEE, 0, -1);

       game.play(Hive.Tile.SPIDER, -2, 1);
       game.play(Hive.Tile.BEETLE, 1, -2);

       game.play(Hive.Tile.SPIDER, -1, 1);
       game.play(Hive.Tile.BEETLE, 1, -1);

       game.move(-2 , 1, 0, 1);

       Assertions.assertEquals(Hive.Tile.SPIDER ,game.findPiece(Hive.Tile.SPIDER, 0, 1, Hive.Player.WHITE).getType());

   }
//   @Test
//    public void WhiteCannotMoveOrPlaySoPasses() throws Hive.IllegalMove {
//       game.play(Hive.Tile.QUEEN_BEE, 0, -10);
//       game.play(Hive.Tile.QUEEN_BEE, 0, -9);
//       game.play(Hive.Tile.SPIDER, 0, -8);
//       game.play(Hive.Tile.SPIDER, 0, -7);
//       game.play(Hive.Tile.SPIDER, 0, -6);
//       game.play(Hive.Tile.SPIDER, 0, -5);
//       game.play(Hive.Tile.SOLDIER_ANT, 0, -4);
//       game.play(Hive.Tile.SOLDIER_ANT, 0, -3);
//       game.play(Hive.Tile.SOLDIER_ANT, 0, -2);
//       game.play(Hive.Tile.SOLDIER_ANT, 0, -1);
//
//       game.play(Hive.Tile.BEETLE, 0, 0);
//       game.play(Hive.Tile.BEETLE, 0, 1);
//       game.play(Hive.Tile.BEETLE, 0, 2);
//       game.play(Hive.Tile.BEETLE, 0, 3);
//       game.play(Hive.Tile.GRASSHOPPER, 0, 4);
//       game.play(Hive.Tile.GRASSHOPPER, 0, 5);
//       game.play(Hive.Tile.GRASSHOPPER, 0, 6);
//       game.play(Hive.Tile.GRASSHOPPER, 0, 7);
//       game.play(Hive.Tile.GRASSHOPPER, 0, 8);
//       game.play(Hive.Tile.GRASSHOPPER, 0, 9);
//
//       Assertions.assertThrows(Hive.IllegalMove.class, () -> game.move(0,8, 0, 10));
//   }

}
