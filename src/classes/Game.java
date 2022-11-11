package classes;

import interfaces.Hive;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Game implements Hive {
    private final Board gameBoard;
    private final classes.Player player1;
    private final classes.Player player2;

    private classes.Player currentPlayer;

    public Game() {
        this.gameBoard = new Board();
        this.player2 = new classes.Player(Player.BLACK);
        this.player1 = new classes.Player(Player.WHITE);

        this.currentPlayer = player1;

    }

    /**
     * Play a new tile.
     *
     * @param tile Tile to play
     * @param q    Q coordinate of hexagon to play to
     * @param r    R coordinate of hexagon to play to
     * @throws IllegalMove If the tile could not be played
     */
    @Override
    public void play(Tile tile, int q, int r) throws IllegalMove {
        Piece placablePiece = checkPlay(tile, q, r);
        this.gameBoard.setTile(placablePiece, q, r);
        this.updatePiece(q, r, placablePiece);
        switchPlayer();
    }

    private Piece checkPlay(Tile tile, int q, int r) throws IllegalMove {
        ArrayList<Piece> neighbours = gameBoard.getNeighbours(q, r);
        if (gameBoard.getTile(q, r) != null) {
            throw new IllegalMove();
        }


        if (Boolean.TRUE.equals(!currentPlayer.isQueenPlayed()) && currentPlayer.getHandSize() == 7 && tile != Tile.QUEEN_BEE) {
            throw new IllegalMove("Queen must be played.");
        }

        if (!neighbours.isEmpty()) {
            for (Piece piece : neighbours) {
                if (piece.getPlayer() != this.currentPlayer.getColour() && currentPlayer.getHandSize() < 10) {
                    throw new IllegalMove("Cannot play tile next to opposite colour.");
                }
            }


        } else if (currentPlayer.getHandSize() < 10) {
            throw new IllegalMove("Must play tile next to own tile.");
        }


        Piece placablePiece = currentPlayer.getPlacablePiece(tile);
        if (placablePiece == null) {
            throw new IllegalMove("No piece of given type in your hand.");
        }

        return placablePiece;
    }

    /**
     * Move an existing tile.
     *
     * @param fromQ Q coordinate of the tile to move
     * @param fromR R coordinate of the tile to move
     * @param toQ   Q coordinate of the hexagon to move to
     * @param toR   R coordinare of the hexagon to move to
     * @throws IllegalMove If the tile could not be moved
     */
    @Override
    public void move(int fromQ, int fromR, int toQ, int toR) throws IllegalMove {
        throw new IllegalMove();
    }






    /**
     * Pass the turn.
     *
     * @throws IllegalMove If the turn could not be passed
     */
    @Override
    public void pass() throws IllegalMove {
        if (currentPlayer.getHandSize() == 0) {
            // TODO:
            // And if player cannot move.
            switchPlayer();
        } else {
            throw new IllegalMove();
        }
    }

    /**
     * Check whether the given player is the winner.
     *
     * @param player Player to check
     * @return Boolean
     */
    @Override
    public boolean isWinner(Player player) {

        Map<Integer, Map<Integer, ArrayList<Piece>>> gameboard = gameBoard.getGameBoard();
        for (int x : gameboard.keySet()) {
            for (int y : gameboard.get(x).keySet()) {
                ArrayList<Piece> pieces = gameboard.get(x).get(y);
                for (Piece piece : pieces) {
                    if (piece != null) {
                        if (piece.getType() == Hive.Tile.QUEEN_BEE && piece.getPlayer() == player) {
                            ArrayList<Piece> neighbours = gameBoard.getNeighbours(x, y);
                            if (neighbours.size() == 6) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


    /**
     * Check whether the game is a draw.
     *
     * @return Boolean
     */
    @Override
    public boolean isDraw() {
        return isWinner(Player.BLACK) && isWinner(Player.WHITE);
    }

    public classes.Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Piece findPiece(Tile tile, int x, int y, Enum player) {
        return gameBoard.findPiece(tile, x, y, player);
    }

    private void switchPlayer() {
        if (currentPlayer == player1) {
            currentPlayer = player2;
        } else {
            currentPlayer = player1;
        }
    }

    private void updatePiece(int x, int y, Piece piece) {
        piece.setX(x);
        piece.setY(y);
    }

}
