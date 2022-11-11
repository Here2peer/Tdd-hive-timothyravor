package classes;


public class Piece {


    private String type;

    public Piece() {

    }

    public Piece(Enum type, Enum player) {
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
