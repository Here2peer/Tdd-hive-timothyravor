package classes.services;

import classes.Board;
import classes.Piece;
import classes.Player;
import interfaces.Hive;

import java.util.ArrayList;
import java.util.List;

public class MoveService {
    private final Board gameboard;
    private final Player player1;
    private final Player player2;

    public MoveService(Board gameboard, Player player1, Player player2) {
        this.gameboard = gameboard;

        this.player1 = player1;
        this.player2 = player2;
    }

    public Piece checkPlay(Hive.Tile tile, int q, int r, Player currentPlayer) throws Hive.IllegalMove {
        ArrayList<Piece> neighbours = gameboard.getNeighbours(q, r);
        if (gameboard.getTile(q, r) != null) {
            throw new Hive.IllegalMove();
        }


        if (Boolean.TRUE.equals(!currentPlayer.isQueenPlayed()) && currentPlayer.getHandSize() == 7 && tile != Hive.Tile.QUEEN_BEE) {
            throw new Hive.IllegalMove("Queen must be played.");
        }

        if (!neighbours.isEmpty()) {
            for (Piece piece : neighbours) {
                if (piece.getPlayer() != currentPlayer.getColour() && currentPlayer.getHandSize() < 10) {
                    throw new Hive.IllegalMove("Cannot play tile next to opposite colour.");
                }
            }


        } else if (currentPlayer.getHandSize() < 10) {
            throw new Hive.IllegalMove("Must play tile next to own tile.");
        }


        Piece placablePiece = currentPlayer.getPlacablePiece(tile);
        if (placablePiece == null) {
            throw new Hive.IllegalMove("No piece of given type in your hand.");
        }

        return placablePiece;
    }

    public boolean checkHive(int toQ, int toR, Piece piece) {
        List<Piece> hive = new ArrayList<>();
        hive.add(piece);
        return gameboard.recursionRenameFunction(toQ, toR, hive, 20 - (player1.getHandSize() + player2.getHandSize()));
    }

    boolean shift(int fromX, int fromY, int toX, int toY, Piece piece) {
        if (checkContinuity(fromX, fromY, toX, toY, piece)) {
            if (fromY == toY) {
                //Horizontaal
                if (toX - fromX > 0) {
                    //Rechts
                    if (gameboard.getTile(fromX + 1, fromY - 1) != null && gameboard.getTile(fromX, fromY + 1) != null) {
                        return false;
                    }
                } else {
                    //Links
                    if (gameboard.getTile(fromX, fromY - 1) != null && gameboard.getTile(fromX - 1, fromY + 1) != null) {
                        return false;
                    }
                }
            } else {
                //Diagonaal
                if (fromX == toX) {
                    if (Math.abs(fromY) + toY > 0) {
                        if (gameboard.getTile(fromX + toX, fromY) != null && gameboard.getTile(fromX + toY, fromY + Math.abs(toY)) != null) {
                            return false;
                        }
                    } else {
                        if (gameboard.getTile(fromX - 1, fromY) != null && gameboard.getTile(fromX + 1, fromY - 1) != null) {
                            return false;
                        }
                    }
                } else {
                    if (gameboard.getTile(toX, fromY) != null && gameboard.getTile(fromX, toY) != null) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    boolean checkContinuity(int fromX, int fromY, int toX, int toY, Piece piece) {
        ArrayList<Piece> pieces1 = gameboard.getNeighbours(fromX, fromY);
        ArrayList<Piece> pieces2 = gameboard.getNeighbours(toX, toY);

        if (piece.getType() == Hive.Tile.BEETLE && gameboard.getTile(toX, toY) != null) {
            return true;
        }

        if (piece.getType() == Hive.Tile.BEETLE || piece.getType() == Hive.Tile.QUEEN_BEE) {
            for (Piece piece1 : pieces1) {
                for (Piece piece2 : pieces2) {
                    if (piece1.equals(piece2)) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            return true;
        }

    }

    boolean isSameLocation(int fromQ, int fromR, int toQ, int toR) {
        return !(fromQ == fromR && toQ == toR);
    }

    public boolean isAllowedToMove(Hive.Tile type, int fromQ, int fromR, int toQ, int toR, Piece piece) {
        final boolean oneStep = Math.abs(fromQ - toQ) <= 1 && Math.abs(fromR - toR) <= 1;
        switch (type) {
            case BEETLE, QUEEN_BEE -> {
                return oneStep && shift(fromQ, fromR, toQ, toR, piece);
            }
            case SOLDIER_ANT -> {

                return isSameLocation(fromQ, fromR, toQ, toR) && shift(fromQ, fromR, toQ, toR, piece);
            }
            case GRASSHOPPER -> {
                return grasshopperCheck(fromQ, fromR, toQ, toR);
            }
            case SPIDER -> {
                ArrayList<Piece> pieces = new ArrayList<>();
                pieces.add(piece);
                return isSameLocation(fromQ, fromR, toQ, toR) && checkSpider(fromQ, fromR, toQ, toR, pieces);
            }
            default -> {
                return false;
            }
        }
    }

    boolean grasshopperCheck(int fromQ, int fromR, int toQ, int toR) {
        int offset = Math.abs(fromQ + fromR);
        int moveCount;

        if (fromQ == toQ && fromR != toR) {
            moveCount = calculateMoveCount(fromR, toR);
        } else {
            moveCount = calculateMoveCount(fromQ, toQ);
        }

        for (int x = 0; x < moveCount; ++x) {
            if (fromQ != toQ && fromR == toR) {
                if (gameboard.getTile(fromQ + x, fromR) == null && fromQ < toQ) {
                    return false;
                }
                if (gameboard.getTile(fromQ - x, fromR) == null && fromQ > toQ) {
                    return false;
                }
            }

            if (fromQ == toQ && fromR != toR) {
                if (gameboard.getTile(fromQ, fromR - x) == null && fromR > toR) {
                    return false;
                }
                if (gameboard.getTile(fromQ, fromR + x) == null && fromR < toR) {
                    return false;
                }
            }

            if ((fromQ != toQ && fromR != toR)) {
                if (toQ + toR + offset == 0) {
                    if (gameboard.getTile(fromQ + x, fromR - x) == null && fromQ < toQ) {
                        return false;
                    }
                    if (gameboard.getTile(fromQ - x, fromR + x) == null && fromQ > toQ) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    int calculateMoveCount(int moveX, int moveY) {
        return Math.abs(moveX - moveY);
    }

    boolean checkSpider(int fromQ, int fromR, int toQ, int toR, ArrayList<Piece> arrayList){
        if (arrayList.size() == 4){
            if (arrayList.get(arrayList.size() -1).getX() == toQ && arrayList.get(arrayList.size() -1).getY() == toR) {
                return true;
            }
        } else if (arrayList.size() > 3) {
            return false;
        }

        if (gameboard.getNeighbours(fromQ, fromR).size() == 1 && arrayList.size() == 2) {
            return false;
        }
        int[] neib = {-1, 0, 1,};
        for (int indexY = 0; indexY < neib.length; indexY++) {
            for (int indexX = 0; indexX < neib.length; indexX++) {
                if(neib[indexY] != neib[indexX] && gameboard.getTile(neib[indexX] + fromQ, neib[indexY] + fromR) == null) {
                    Piece piece = new Piece(neib[indexX] + fromQ , neib[indexY] + fromR);
                    if((piece.getX() != arrayList.get(0).getX() || piece.getY() != arrayList.get(0).getY()) && arrayList.size() <= 3) {
                        if (!gameboard.getNeighbours(piece.getX(), piece.getY()).isEmpty()) {
                            if (shift(fromQ, fromR, neib[indexX] + fromQ, neib[indexY] + fromR, piece)) {

                                arrayList.add(piece);
                                if (!checkSpider(piece.getX(), piece.getY(), toQ, toR, arrayList)) {
                                    arrayList.remove(arrayList.get(arrayList.size() - 1));
                                } else {
                                    return true;
                                }
                            }
                    }

                    }
                }
            }
        }
        return false;
    }
}