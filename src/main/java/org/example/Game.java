package org.example;

import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {
    private Board board;

    public Game(Board board) {
        setTitle("Checkers");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.board = board;
        add(this.board);
        setVisible(true);
    }
}
