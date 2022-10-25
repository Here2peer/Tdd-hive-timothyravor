package classes;

import interfaces.Hive;

import java.util.ArrayList;
import java.util.Objects;

public class Player {

    private boolean isMyTurn;
    private ArrayList<Piece> pieces;

    private final Enum colour;

    public Player(Enum colour) {

        this.colour = colour;
        this.pieces = new ArrayList<>();

        isMyTurn = Objects.equals(colour.toString(), "WHITE");

        addPiece(Hive.Tile.QUEEN_BEE, 1);
        addPiece(Hive.Tile.SPIDER, 2);
        addPiece(Hive.Tile.SOLDIER_ANT, 2);
        addPiece(Hive.Tile.BEETLE, 2);
        addPiece(Hive.Tile.GRASSHOPPER, 3);
    }

    private void addPiece(Enum type, int amount) {
        for (int i = 0; i <= amount; i++) {
            this.pieces.add(new Piece(type));
        }
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public Piece getPlacablePiece(Enum type) {
        for (Piece p : pieces) {
            if (p.getType() == type){
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

}
