package org.example;

public interface Piece {
    void move(int x, int y);
    void setKing(boolean king);
    boolean isKing();
}
