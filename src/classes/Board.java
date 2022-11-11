package classes;


import interfaces.Hive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Board {
    public Map<Integer, Map<Integer, ArrayList<Piece>>> gameBoard;

    public Board() {
        this.gameBoard = new HashMap<>();
    }

    //TODO
    // Rewrite try catch block
    public void setTile(Piece piece, int x, int y) {
        ArrayList<Piece> pieces = new ArrayList<>();
        pieces.add(piece);

        if (this.gameBoard.containsKey(x)) {
            if (this.gameBoard.get(x).containsKey(y)) {
                this.gameBoard.get(x).get(y).add(piece);
            } else {
                this.gameBoard.get(x).put(y, pieces);
            }
        } else {
            Map<Integer, ArrayList<Piece>> innerMap = new HashMap<>();
            innerMap.put(y, pieces);
            this.gameBoard.put(x, innerMap);
        }
    }


    public void moveTile(int fromX, int fromY, int toX, int toY) throws Hive.IllegalMove {
        Piece piece = removeTile(fromX, fromY);
        this.setTile(piece, toX, toY);
        piece.setX(toX);
        piece.setY(toY);
    }

    public ArrayList<Piece> getTile(int x, int y) {
        try {
            ArrayList<Piece> pieces = this.gameBoard.get(x).get(y);
            return pieces;
        } catch (Exception e) {
            return null;
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
            if (pieces.isEmpty()) {
                this.gameBoard.get(x).remove(y);
            }
            return piece;
        } catch (Exception e) {
            throw new Hive.IllegalMove("No tile on " + x + " " + y + "!");
        }
    }

    public Piece findPiece(Hive.Tile tile, int x, int y, Enum player) {
        ArrayList<Piece> pieces = gameBoard.get(x).get(y);
        if (pieces != null) {
            for (Piece piece : pieces) {
                if (piece.getType().equals(tile) && piece.getPlayer().equals(player)) {
                    return piece;
                }
            }
        }
        return null;
    }



    public ArrayList<Piece> getNeighbours(int x, int y) {
        ArrayList<Piece> pieces = new ArrayList<>();
        int[] neib = {-1, 0, 1,};
        for (int indexY = 0; indexY < neib.length; indexY++) {
            for (int indexX = 0; indexX < neib.length; indexX++) {
                if(neib[indexY] != neib[indexX]) {
                    if (gameBoard.get(x +neib[indexX]) != null && gameBoard.get(x +neib[indexX]).get(y + neib[indexY]) !=null ) {
                        ArrayList<Piece> tempPieces2 = gameBoard.get(x + neib[indexX]).get(y + neib[indexY]);
                        pieces.add(tempPieces2.get(tempPieces2.size() - 1));
                    }
                }
            }
        }
        return pieces;
    }

    public boolean recursionRenameFunction (int x, int y, List<Piece> hive, int hiveSize) {
        boolean hiveConnected = false;
        ArrayList<Piece> neighbours = getNeighbours(x, y);
        for (Piece neighbour : neighbours) {
            if(!hive.contains(neighbour) && neighbour != null) {
                hive.add(neighbour);
                hiveConnected = recursionRenameFunction(neighbour.getX(), neighbour.getY(), hive, hiveSize);
            }
        }
        if (hive.size() == hiveSize) {
            hiveConnected = true;
        }
        return hiveConnected;
    }



}
