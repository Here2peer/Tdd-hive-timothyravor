package classes;


import interfaces.Hive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board {
    private Map<Integer, Map<Integer, ArrayList<Piece>>> gameBoard;
    private Map<Integer, ArrayList<Piece>> innerMap;

    public Board() {
        this.gameBoard = new HashMap<>();
        this.innerMap = new HashMap<>();
    }

    //TODO
    // Rewrite try catch block
    public void setTile(Piece piece, int x, int y) {
        try{
            this.gameBoard.get(x).get(y).add(piece);
        } catch (Exception e) {
            ArrayList<Piece> pieces = new ArrayList<>();
            pieces.add(piece);
            this.innerMap.put(y, pieces);
            this.gameBoard.put(x, this.innerMap);
        }
    }

    public void moveTile(int fromX, int fromY, int toX, int toY) throws Hive.IllegalMove {
        Piece piece = removeTile(fromX, fromY);
        this.setTile(piece, toX, toY);
    }

    public ArrayList<Piece> getTile(int x, int y) throws Hive.IllegalMove {
        try {
            ArrayList<Piece> pieces = this.gameBoard.get(x).get(y);
            return pieces;
        } catch (Exception e) {
            throw new Hive.IllegalMove();
        }
    }

    public Map<Integer, Map<Integer, ArrayList<Piece>>> getGameBoard() {
        return this.gameBoard;
    }

    private Piece removeTile(int x, int y) throws Hive.IllegalMove {
        try {
            ArrayList<Piece> pieces = getTile(x, y);
            Piece piece = pieces.get(pieces.size() - 1);
            pieces.remove(pieces.size() - 1);
            if (pieces.size() == 0) {
                this.innerMap.remove(y);
                this.gameBoard.remove(x);
            }
            return piece;
        } catch (Exception e) {
            throw new Hive.IllegalMove("No tile on " + x + " " + y + "!");
        }
    }

}
