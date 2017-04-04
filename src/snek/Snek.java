package snek;

import java.awt.EventQueue;
import javax.swing.JFrame;


public class Snek extends JFrame {

    public Snek() {

        add(new Board());
        
        setResizable(false);
        pack();
        
        setTitle("Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    

    public static void main(String[] args) {
        
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {                
                JFrame ex = new Snek();
                ex.setVisible(true);                
            }
        });
    }
}