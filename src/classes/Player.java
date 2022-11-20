package classes;

import interfaces.Hive;

import java.util.ArrayList;
import java.util.Objects;

public class Player {

    private boolean isMyTurn;
    private ArrayList<Piece> pieces;

    private final Hive.Player colour;

    public Player(Hive.Player colour) {

        this.colour = colour;
        this.pieces = new ArrayList<>();

        isMyTurn = Objects.equals(colour, Hive.Player.WHITE);

        addPiece(Hive.Tile.QUEEN_BEE, 1);
        addPiece(Hive.Tile.SPIDER, 2);
        addPiece(Hive.Tile.SOLDIER_ANT, 2);
        addPiece(Hive.Tile.BEETLE, 2);
        addPiece(Hive.Tile.GRASSHOPPER, 3);
    }

    private void addPiece(Hive.Tile type, int amount) {
        for (int i = 0; i < amount; i++) {
            this.pieces.add(new Piece(type, colour));
        }
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public Piece getPlacablePiece(Hive.Tile type) {
        for (Piece p : pieces) {
            if (p.getType() == type){
                pieces.remove(p);
                return p;
            }
        }
        return null;
    }

    public boolean passTurn() {
        if (isMyTurn) {
            isMyTurn = false;
        }
        return isMyTurn;
    }

    public Hive.Player getColour() {
        return colour;
    }

    public int getHandSize() {
        return pieces.size();
    }

    public Boolean isQueenPlayed() {
        for (Piece piece: pieces) {
            if (piece.getType() == Hive.Tile.QUEEN_BEE) {
                return false;
            }
        }
        return true;
    }
}
