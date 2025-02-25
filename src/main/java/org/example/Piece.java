package org.example;

public interface Piece {
    void move(int x, int y);
    void capture(int x, int y);
    boolean isKing();
}
