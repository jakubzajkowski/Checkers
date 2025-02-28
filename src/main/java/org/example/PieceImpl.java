package org.example;

public class PieceImpl implements Piece {
    private int x;
    private int y;
    private ColorEnum color;
    private boolean king;

    public PieceImpl(int x, int y, ColorEnum color) {
        this.x = x;
        this.y = y;
        this.color = color;
        king = false;
    }

    @Override
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void setKing(boolean king) {
        this.king = king;
    }

    @Override
    public boolean isKing() {
        return king;
    }

    public ColorEnum getColor() {
        return color;
    }

    public void setColor(ColorEnum color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
