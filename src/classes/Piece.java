package classes;


import interfaces.Hive;

public class Piece {


    private Enum type;
    private Hive.Player player;

    private int x;
    private int y;

    public Piece() {

    }
    public Piece(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Piece(Enum type, Hive.Player player) {
        this.type = type;
        this.player = player;
    }

    public Enum getType() {
        return type;
    }

    public Enum getPlayer() {
        return player;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
