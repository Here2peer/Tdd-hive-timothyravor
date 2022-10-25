package classes;

import java.util.Objects;

public class Piece {

    private int x;
    private int y;

    private String type;

    public Piece() {

    }

    public Piece(Enum type) {
        this.type = type;
    }

    public Enum getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Piece)) return false;
        Piece piece = (Piece) o;
        return x == piece.x && y == piece.y && Objects.equals(type, piece.type);
    }

    @Override
    public int hashCode() {
        int hash = x;
        hash = 31 * hash + y;
        hash = 31 * hash + (type == null ? 0 : type.hashCode());
        return hash;
    }

}
