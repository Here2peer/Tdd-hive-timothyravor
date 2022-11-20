package classes;

import classes.services.MoveService;
import interfaces.Hive;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Game implements Hive {
    private final Board gameBoard;
    private final classes.Player player1;
    private final classes.Player player2;
    private final MoveService moveService;

    private boolean connected;

    private classes.Player currentPlayer;

    public Game() {
        this.gameBoard = new Board();
        this.player2 = new classes.Player(Player.BLACK);
        this.player1 = new classes.Player(Player.WHITE);
        this.moveService = new MoveService(gameBoard, player1, player2);
        this.currentPlayer = player1;
        this.connected = false;
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
        Piece placablePiece = moveService.checkPlay(tile, q, r, currentPlayer);
        this.gameBoard.setTile(placablePiece, q, r);
        placablePiece.setX(q);
        placablePiece.setY(r);
        moveService.checkHive(q, r, placablePiece);
        switchPlayer();
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

        ArrayList<Piece> pieces = gameBoard.getTile(fromQ, fromR);
        List<Piece> hive = new ArrayList<>();
        Piece piece = pieces.get(pieces.size() - 1);
        connected = moveService.checkHive(fromQ, fromR, piece);

        if(pieces.get(pieces.size() -1).getPlayer() != currentPlayer.getColour()) {
            throw new IllegalMove("Can only move your own pieces.");
        }

        if(Boolean.FALSE.equals(currentPlayer.isQueenPlayed())) {
            throw new IllegalMove("Queen must be played before moving a piece.");
        }

        if(moveService.isAllowedToMove((Tile) piece.getType(), fromQ, fromR, toQ, toR, gameBoard.getTile(fromQ, fromR).get(0))) {
            gameBoard.moveTile(fromQ, fromR, toQ, toR);
        } else {
            throw new IllegalMove("Move cannot be done.");
        }

        ArrayList<Piece> pieces2 = gameBoard.getTile(toQ, toR);
        hive.addAll(pieces2);
        if ((player1.getHandSize() < 10 && player2.getHandSize() < 10) && connected) {
            if (!gameBoard.recursionRenameFunction(toQ, toR, hive, 20 - (player1.getHandSize() + player2.getHandSize()))) {
                resetMove( fromQ,fromR,toQ,toR);
                throw new IllegalMove("Pieces are not connected.");
            }
        }

        if (gameBoard.getNeighbours(toQ, toR).isEmpty()) {
            resetMove( fromQ,fromR,toQ,toR);
            throw new IllegalMove();
        }
        connected = moveService.checkHive(toQ, toR, piece);
        switchPlayer();
    }


    private void resetMove(int fromQ, int fromR, int toQ, int toR) throws IllegalMove {
        gameBoard.moveTile(toQ, toR, fromQ, fromR);
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

    public boolean isHiveConnected() {
        return connected;
    }

    public classes.Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Piece findPiece(Tile tile, int x, int y, Player player) {
        return gameBoard.findPiece(tile, x, y, player);
    }

    private void switchPlayer() {
        if (currentPlayer == player1) {
            currentPlayer = player2;
        } else {
            currentPlayer = player1;
        }
    }
}
