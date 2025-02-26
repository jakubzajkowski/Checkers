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
                int x = (int)(e.getPoint().getX()/70);
                int y = (int)(e.getPoint().getY() / 70);

                if(board[x][y] != null) {
                    System.out.println(board[x][y]);
                    selectedPiece = board[x][y];
                }

                repaint();

            }
        });
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
    public void drawPieces(Graphics g) {
        int windowWidth = getWidth();
        int windowHeight = getHeight();
        int marginX = (windowWidth - boardSize) / 2 + (fieldSize-pieceSize)/2;
        int marginY = (windowHeight - boardSize) / 2 + (fieldSize-pieceSize)/2;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                g.setColor(j==1 || j==0 || j==2 ? Color.white : Color.black);
                if(j==0 || j==6 || j==2) {
                    if (i % 2 != 0) {
                        board[i][j] = new PieceImpl(i,j,g.getColor()==Color.white ? ColorEnum.WHITE : ColorEnum.BLACK);
                        g.fillOval(i * fieldSize + marginX, j * fieldSize + marginY, pieceSize, pieceSize);
                    }
                }if(j==1 || j==5 || j==7) {
                    if (i % 2 == 0) {
                        board[i][j] = new PieceImpl(i,j,g.getColor()==Color.white ? ColorEnum.WHITE : ColorEnum.BLACK);
                        g.fillOval(i * fieldSize + marginX, j * fieldSize + marginY, pieceSize, pieceSize);
                    }
                }
            }
        }
        if(selectedPiece != null) {
            selectPiece(g, selectedPiece);
        }
    }
    public void movePiece(Graphics g, PieceImpl piece) {

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
