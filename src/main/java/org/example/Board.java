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
    private ColorEnum currentPlayer = ColorEnum.WHITE; // Białe zaczynają


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

    private void switchPlayer() {
        currentPlayer = (currentPlayer == ColorEnum.WHITE) ? ColorEnum.BLACK : ColorEnum.WHITE;
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
        int marginXKing = (windowWidth - boardSize) / 2 + (fieldSize-10)/2;
        int marginYKing = (windowHeight - boardSize) / 2 + (fieldSize-10)/2;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(board[i][j] != null) {
                    g.setColor(board[i][j].getColor()==ColorEnum.WHITE ? Color.white : Color.black);
                    g.fillOval(i * fieldSize + marginX, j * fieldSize + marginY, pieceSize, pieceSize);
                    if(board[i][j].isKing()){
                        g.setColor(Color.ORANGE);
                        g.fillOval(i * fieldSize + marginXKing, j * fieldSize + marginYKing, 10, 10);
                    }
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

        // Sprawdzenie, czy pionek należy do aktualnego gracza
        if (piece.getColor() != currentPlayer) {
            System.out.println("Teraz ruch ma " + currentPlayer);
            return;
        }

        boolean validMove = false;
        boolean wasCapture = false;

        // Zwykły ruch o jedno pole
        if (Math.abs(newY - oldY) == 1 && Math.abs(newX - oldX) == 1) {
            if ((piece.getColor() == ColorEnum.WHITE && oldY < newY) ||
                    (piece.getColor() == ColorEnum.BLACK && oldY > newY) ||
                    piece.isKing()) {

                board[oldX][oldY] = null;
                board[newX][newY] = piece;
                piece.move(newX, newY);
                validMove = true;
            }
        }

        // Bicie skokiem o dwa pola
        if (Math.abs(newY - oldY) == 2 && Math.abs(newX - oldX) == 2) {
            int middleX = (newX + oldX) / 2;
            int middleY = (newY + oldY) / 2;

            if (board[middleX][middleY] != null && board[middleX][middleY].getColor() != piece.getColor()) {
                board[oldX][oldY] = null;
                board[middleX][middleY] = null;  // Usunięcie zbitego pionka
                board[newX][newY] = piece;
                piece.move(newX, newY);
                wasCapture = true;
                validMove = true;
            }
        }

        // Ruch damki
        if (piece.isKing() && Math.abs(newX - oldX) == Math.abs(newY - oldY)) {
            int deltaX = (newX > oldX) ? 1 : -1;
            int deltaY = (newY > oldY) ? 1 : -1;
            int x = oldX + deltaX;
            int y = oldY + deltaY;
            boolean isCapture = false;
            int capturedX = -1, capturedY = -1;

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

            board[oldX][oldY] = null;
            board[newX][newY] = piece;
            piece.move(newX, newY);

            if (isCapture) {
                board[capturedX][capturedY] = null;
                wasCapture = true;
            }

            validMove = true;
        }

        // Sprawdzenie awansu na damkę
        if (validMove) {
            if ((piece.getColor() == ColorEnum.WHITE && newY == 7) ||
                    (piece.getColor() == ColorEnum.BLACK && newY == 0)) {
                piece.setKing(true);
            }

            repaint();

            // Jeśli było bicie i są kolejne możliwe bicia, ten sam gracz gra dalej
            if (wasCapture && canJumpAgain(piece)) {
                return;
            }

            // Przełącz gracza, jeśli ruch był poprawny i nie ma kolejnych bić
            switchPlayer();
        }
    }
    private boolean canJumpAgain(PieceImpl piece) {
        int x = piece.getX();
        int y = piece.getY();
        ColorEnum color = piece.getColor();

        // Możliwe kierunki ruchu (dla zwykłych pionków)
        int[][] directions = {
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // Lewo-góra, prawo-góra, lewo-dół, prawo-dół
        };

        for (int[] dir : directions) {
            int midX = x + dir[0];
            int midY = y + dir[1];
            int newX = x + 2 * dir[0];
            int newY = y + 2 * dir[1];

            // Sprawdzenie, czy pola są w granicach planszy
            if (isInsideBoard(midX, midY) && isInsideBoard(newX, newY)) {
                // Czy na środkowym polu jest pionek przeciwnika?
                if (board[midX][midY] != null && board[midX][midY].getColor() != color) {
                    // Czy docelowe pole jest puste?
                    if (board[newX][newY] == null) {
                        return true; // Możliwe bicie
                    }
                }
            }
        }
        return false;
    }
    private boolean isInsideBoard(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    public void selectPiece(Graphics g, PieceImpl piece) {
        int windowWidth = getWidth();
        int windowHeight = getHeight();
        int marginX = (windowWidth - boardSize) / 2 + (fieldSize-pieceSize-8)/2;
        int marginY = (windowHeight - boardSize) / 2 + (fieldSize-pieceSize-8)/2;
        int marginXKing = (windowWidth - boardSize) / 2 + (fieldSize-10)/2;
        int marginYKing = (windowHeight - boardSize) / 2 + (fieldSize-10)/2;
        g.setColor(piece.getColor()==ColorEnum.WHITE ? Color.white : Color.black);
        g.fillOval(piece.getX() * fieldSize + marginX, piece.getY() * fieldSize + marginY, pieceSize+8, pieceSize+8);
        if(piece.isKing()){
            g.setColor(Color.ORANGE);
            g.fillOval(piece.getX() * fieldSize + marginXKing, piece.getY() * fieldSize + marginYKing, 10, 10);
        }
    }
}
