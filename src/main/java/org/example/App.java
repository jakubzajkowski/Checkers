package org.example;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {
    public App() {
        setTitle("Checkers");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    @Override
    public void paint(Graphics g){
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(i%2==0) g.setColor(j%2==0 ? Color.WHITE : Color.RED);
                else g.setColor(j%2==0 ? Color.RED : Color.WHITE);
                g.fillRect(i*70+10, j*70+30, 70, 70);
            }
        }
    }
}
