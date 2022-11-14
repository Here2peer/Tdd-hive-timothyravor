package classes;

import interfaces.Hive;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Game implements Hive {
    private final Board gameBoard;
    private final classes.Player player1;
    private final classes.Player player2;

    private boolean connected;

    private classes.Player currentPlayer;

    public Game() {
        this.gameBoard = new Board();
        this.player2 = new classes.Player(Player.BLACK);
        this.player1 = new classes.Player(Player.WHITE);

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
        Piece placablePiece = checkPlay(tile, q, r);
        this.gameBoard.setTile(placablePiece, q, r);
        this.updatePiece(q, r, placablePiece);
        checkHive(q, r, placablePiece);
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
        //System.out.println(Math.abs(t1+t2) - Math.abs(t3+t4));

        ArrayList<Piece> pieces = gameBoard.getTile(fromQ, fromR);
        List<Piece> hive = new ArrayList<>();
        Piece piece = pieces.get(pieces.size() - 1);

        if(pieces.get(pieces.size() -1).getPlayer() != currentPlayer.getColour()) {
            throw new IllegalMove("Can only move your own pieces.");
        }

        if(Boolean.FALSE.equals(currentPlayer.isQueenPlayed())) {
            throw new IllegalMove("Queen must be played before moving a piece.");
        }

        if(isAllowedToMove((Tile) piece.getType(), fromQ, fromR, toQ, toR)) {
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
        checkHive(toQ, toR, piece);
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

    private void checkHive(int toQ, int toR, Piece piece) {
        List<Piece> hive = new ArrayList<>();
        hive.add(piece);
        this.connected = gameBoard.recursionRenameFunction(toQ, toR, hive, 20 - (player1.getHandSize() + player2.getHandSize()));
    }

    private boolean shift(int fromX, int fromY, int toX, int toY) {
        if(checkContinuity(fromX, fromY, toX, toY)) {
            if (fromY == toY) {
                //Horizontaal
                if (toX - fromX > 0) {
                    //Rechts
                    if (gameBoard.getTile(fromX + 1, fromY - 1) != null && gameBoard.getTile(fromX, fromY + 1) != null) {
                        return false;
                    }
                } else {
                    //Links
                    if (gameBoard.getTile(fromX, fromY - 1) != null && gameBoard.getTile(fromX - 1, fromY + 1) != null) {
                        return false;
                    }
                }
            } else {
                //Diagonaal
                if (fromX == toX) {
                    if (Math.abs(fromY) + toY > 0) {
                        if (gameBoard.getTile(fromX + toX, fromY) != null && gameBoard.getTile(fromX + toY, fromY + Math.abs(toY)) != null) {
                            return false;
                        }
                    } else {
                        if (gameBoard.getTile(fromX - 1, fromY) != null && gameBoard.getTile(fromX + toX, fromY - 1) != null) {
                            return false;
                        }
                    }
                } else {
                    if (gameBoard.getTile(toX, fromY) != null && gameBoard.getTile(fromX, toY) != null) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private boolean checkContinuity(int fromX, int fromY, int toX, int toY) {
        ArrayList<Piece> pieces1 = gameBoard.getNeighbours(fromX, fromY);
        ArrayList<Piece> pieces2 = gameBoard.getNeighbours(toX, toY);

        for(Piece piece1: pieces1) {
            for(Piece piece2: pieces2) {
                if(piece1.equals(piece2)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isAllowedToMove(Tile type, int fromQ, int fromR, int toQ, int toR) {
        final boolean oneStep = Math.abs(fromQ - toQ) <= 1 && Math.abs(fromR - toR) <= 1;
        switch (type) {
            case BEETLE -> {
                return oneStep;
            }
            case QUEEN_BEE -> {
                return oneStep && shift(fromQ, fromR, toQ, toR) ;
            }
            case SOLDIER_ANT -> {
                //ToDo implement soldier-ant
                return true;
            }
            case GRASSHOPPER -> {
                //ToDo implement Grasshopper
                return true;
            }
            case SPIDER -> {
                //ToDo implement Spider
                return true;
            }
            default -> {
                return false;
            }
        }
    }

}
