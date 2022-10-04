package classes;

import interfaces.Hive;


public class Game implements Hive {
    private final Board gameBoard;

    public Game() {
        this.gameBoard = new Board();
    }

    public void nextPlayer(){
    }

    /**
     * Play a new tileType.
     *
     * @param tile Tile to play
     * @param q    Q coordinate of hexagon to play to
     * @param r    R coordinate of hexagon to play to
     * @throws IllegalMove If the tileType could not be played
     */
    public void play(Tile tile, int q, int r) throws IllegalMove {

    }
    /**
     * Move an existing tileType.
     *
     * @param fromQ Q coordinate of the tileType to move
     * @param fromR R coordinate of the tileType to move
     * @param toQ   Q coordinate of the hexagon to move to
     * @param toR   R coordinare of the hexagon to move to
     * @throws IllegalMove If the tileType could not be moved
     */
    public void move(int fromQ, int fromR, int toQ, int toR) throws IllegalMove {

    }

    /**
     * Pass the turn.
     *
     * @throws IllegalMove If the turn could not be passed
     */
    public void pass() throws IllegalMove {
    }

    /**
     * Check whether the given player is the winner.
     *
     * @param player Player to check
     * @return Boolean
     */
    public boolean isWinner(Player player) {
        return true;
    }

    /**
     * Check whether the game is a draw.
     *
     * @return Boolean
     */
    public boolean isDraw() {
        return isWinner(Player.WHITE) && isWinner(Player.BLACK);
    }



}
