package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Board extends JPanel {
    private final Color primaryColor = Color.GRAY;
    private final Color secondaryColor = Color.RED;
    private final int fieldSize = 70;
    private final int pieceSize = 60;
    private final int boardSize = 8 * fieldSize;
    private PieceImpl selectedPiece=null;

    private PieceImpl[][] board = new PieceImpl[8][8];

    public Board() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int marginX = (getWidth() - boardSize) / 2;
                int marginY = (getHeight() - boardSize) / 2;

                int x = (e.getX() - marginX) / fieldSize;
                int y = (e.getY() - marginY) / fieldSize;

                if(board[x][y] != null) {
                    selectedPiece = board[x][y];
                }else if (selectedPiece != null) {
                    movePiece(selectedPiece, x, y);
                    selectedPiece = null;
                }

                repaint();

            }
        });
        initializeBoard();
    }

    @Override
    public void paintComponent(Graphics g){
        drawBoard(g);
        drawPieces(g);
    }

    public void drawBoard(Graphics g){
        int windowWidth = getWidth();
        int windowHeight = getHeight();
        int marginX = (windowWidth - boardSize) / 2;
        int marginY = (windowHeight - boardSize) / 2;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i % 2 == 0) {
                    g.setColor(j % 2 == 0 ? primaryColor : secondaryColor);
                } else {
                    g.setColor(j % 2 == 0 ? secondaryColor : primaryColor);
                }
                g.fillRect(i * fieldSize + marginX, j * fieldSize + marginY, fieldSize, fieldSize);
            }
        }
    }

    public void initializeBoard(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((j == 0 || j == 2) && i % 2 != 0) {
                    board[i][j] = new PieceImpl(i, j, ColorEnum.WHITE);
                } else if (j == 1 && i % 2 == 0) {
                    board[i][j] = new PieceImpl(i, j, ColorEnum.WHITE);
                }
                else if ((j == 5 || j == 7) && i % 2 == 0) {
                    board[i][j] = new PieceImpl(i, j, ColorEnum.BLACK);
                } else if (j == 6 && i % 2 != 0) {
                    board[i][j] = new PieceImpl(i, j, ColorEnum.BLACK);
                }
            }
        }
    }
    public void drawPieces(Graphics g) {
        int windowWidth = getWidth();
        int windowHeight = getHeight();
        int marginX = (windowWidth - boardSize) / 2 + (fieldSize-pieceSize)/2;
        int marginY = (windowHeight - boardSize) / 2 + (fieldSize-pieceSize)/2;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(board[i][j] != null) {
                    g.setColor(board[i][j].getColor()==ColorEnum.WHITE ? Color.white : Color.black);
                    g.fillOval(i * fieldSize + marginX, j * fieldSize + marginY, pieceSize, pieceSize);
                }
            }
        }

        if(selectedPiece != null) {
            selectPiece(g, selectedPiece);
        }

    }
    public void movePiece(PieceImpl piece, int newX, int newY) {
        int oldX = piece.getX();
        int oldY = piece.getY();

        if (Math.abs(newY - oldY) == 1 && Math.abs(newX - oldX) == 1) {
            if ((piece.getColor() == ColorEnum.WHITE && oldY < newY) ||
                    (piece.getColor() == ColorEnum.BLACK && oldY > newY) ||
                    piece.isKing()) {

                board[oldX][oldY] = null;
                board[newX][newY] = piece;
                piece.move(newX, newY);

                // Sprawdzenie awansu na damkę
                if ((piece.getColor() == ColorEnum.WHITE && newY == 7) ||
                        (piece.getColor() == ColorEnum.BLACK && newY == 0)) {
                    piece.setKing(true);
                }

                repaint();
            }
        }

        // Ruch o dwa pola (bicie przeciwnika)
        if (Math.abs(newY - oldY) == 2 && Math.abs(newX - oldX) == 2) {
            int middleX = (newX + oldX) / 2;
            int middleY = (newY + oldY) / 2;

            if (board[middleX][middleY] != null && board[middleX][middleY].getColor() != piece.getColor()) {
                board[oldX][oldY] = null;
                board[middleX][middleY] = null;  // Usunięcie zbitego pionka
                board[newX][newY] = piece;
                piece.move(newX, newY);

                // Sprawdzenie awansu na damkę
                if ((piece.getColor() == ColorEnum.WHITE && newY == 7) ||
                        (piece.getColor() == ColorEnum.BLACK && newY == 0)) {
                    piece.setKing(true);
                }

                repaint();
            }
        }

        // Ruch damki (dowolna liczba pól po przekątnej)
        if (piece.isKing() && Math.abs(newX - oldX) == Math.abs(newY - oldY)) {
            int deltaX = (newX > oldX) ? 1 : -1;
            int deltaY = (newY > oldY) ? 1 : -1;
            int x = oldX + deltaX;
            int y = oldY + deltaY;
            boolean isCapture = false;
            int capturedX = -1, capturedY = -1;

            // Sprawdzenie, czy damka ma czystą drogę
            while (x != newX && y != newY) {
                if (board[x][y] != null) {
                    if (!isCapture && board[x][y].getColor() != piece.getColor()) {
                        isCapture = true;
                        capturedX = x;
                        capturedY = y;
                    } else {
                        return; // Nielegalny ruch - droga zablokowana
                    }
                }
                x += deltaX;
                y += deltaY;
            }

            // Przesunięcie damki
            board[oldX][oldY] = null;
            board[newX][newY] = piece;
            piece.move(newX, newY);

            // Jeśli było bicie, usuń pionek przeciwnika
            if (isCapture) {
                board[capturedX][capturedY] = null;
            }

            repaint();
        }
    }
    public void selectPiece(Graphics g, PieceImpl piece) {
        int windowWidth = getWidth();
        int windowHeight = getHeight();
        int marginX = (windowWidth - boardSize) / 2 + (fieldSize-pieceSize-8)/2;
        int marginY = (windowHeight - boardSize) / 2 + (fieldSize-pieceSize-8)/2;
        g.setColor(piece.getColor()==ColorEnum.WHITE ? Color.white : Color.black);
        g.fillOval(piece.getX() * fieldSize + marginX, piece.getY() * fieldSize + marginY, pieceSize+8, pieceSize+8);
    }
}
