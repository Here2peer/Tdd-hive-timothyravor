package classes;


import java.util.HashMap;
import java.util.Map;

public class Board {
    private Map<Integer, Map<Integer, Piece>> gameBoard;


    public Board() {
        gameBoard = new HashMap<>();
    }

    public boolean setTile(String type, int x, int y) {

        return false;
    }

    public Map<Integer, Map<Integer, Piece>> getGameBoard() {
        return gameBoard;
    }

}
