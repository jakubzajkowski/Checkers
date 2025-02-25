package org.example;

import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    private Color primaryColor = Color.WHITE;
    private Color secondaryColor = Color.RED;
    private int fieldSize = 70;

    public Board() {
    }
    @Override
    public void paintComponent(Graphics g){
        drawBoard(g);
    }

    public void drawBoard(Graphics g){
        int boardSize = 8 * fieldSize;
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
}
