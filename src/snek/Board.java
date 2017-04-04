package snek;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 120;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];
    
    private final int xSnake[] = new int[ALL_DOTS];
    private final int ySnake[] = new int[ALL_DOTS];

    private int dots;
    private int dotsSnake;

    private int apple_x;
    private int apple_y;
    private int r;

    private boolean leftDirectionSnake = false;
    private boolean rightDirectionSnake = false;
    private boolean upDirectionSnake = false;
    private boolean downDirectionSnake = false;
    private boolean inGame = true;
    
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;

    
    private boolean leftCol = false;
    private boolean rightCol = false;
    private boolean downCol = false;
    private boolean upCol = false;


    private Timer timer;
    private Image ball;
    private Image ball2;
    private Image apple;
    private Image head;


    public Board() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("dot.png");
        ball = iid.getImage();

        ImageIcon iid2 = new ImageIcon("dot2.png");
        ball2 = iid2.getImage();
        
        ImageIcon iia = new ImageIcon("apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("head.png");
        head = iih.getImage();
    }

    private void initGame() {

        dots = 3;
        dotsSnake = 3;
        
        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }
        for (int z = 0; z < dotsSnake; z++) {
            xSnake[z] = 50 - z * 10;
            ySnake[z] = 250;
        }
        
        locateApple();
        
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    private void doDrawing(Graphics g) {
        
        if (inGame) {

            g.drawImage(apple, apple_x, apple_y, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }
            for (int z = 0; z < dotsSnake; z++) {
                if (z == 0) {
                    g.drawImage(head, xSnake[z], ySnake[z], this);
                } else {
                    g.drawImage(ball2, xSnake[z], ySnake[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();
        } else 
        {

            gameOver(g);    
            
        }        
    }

    private void gameOver(Graphics g)  {
        
        String msg = "Game Over. Your score: " + (dots - 3) + " Snake score: " + (dotsSnake - 3);
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
        msg = "Space to restart";
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, (B_HEIGHT / 2)+16);        
        timer.stop();
        
    }

    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            dots ++;
            locateApple();           
        }
        if ((xSnake[0] == apple_x) && (ySnake[0] == apple_y)) {
            dotsSnake ++;
            locateApple(); 
                  
        }
    }
    
    private void locateApple() {

        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
        
    }

    private void move() {
        
        checkDirection();           

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }
        
        for (int z = dotsSnake; z > 0; z--) {
            xSnake[z] = xSnake[(z - 1)];
            ySnake[z] = ySnake[(z - 1)];
        }
        movePlayer();
        moveSnake();
    }
    
    private void movePlayer() {
         if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }
    
    private void moveSnake() {
        
        if(leftDirectionSnake) {
            if (!leftCol) {
            xSnake[0] -= DOT_SIZE;
            }
            else  {
                if(apple_y < ySnake[0]) {
                    if((B_HEIGHT - ySnake[0] + apple_y) < ySnake[0] - apple_y && !downCol) {
                        goDown();
                    }
                    else if(!upCol)  goUp();
                }
                else 
                {
                    if((ySnake[0] + B_HEIGHT - apple_y) < apple_y - ySnake[0] && !upCol) {
                        goUp();
                    }
                    else if(!downCol) goDown();
                }
            }          
        }
        
        if(rightDirectionSnake) {
          if (!rightCol) {
            xSnake[0] += DOT_SIZE;
          }
          else  {
                if(apple_y < ySnake[0]) {
                    if((B_HEIGHT - ySnake[0] + apple_y) < ySnake[0] - apple_y && !downCol) {
                        goDown();
                    }
                    else if(!upCol) goUp();
                }
                else 
                {
                    if((ySnake[0] + B_HEIGHT - apple_y) < apple_y - ySnake[0] && !upCol) {
                        goUp();
                    }
                    else if(!downCol) goDown();
                }
            }
        }
        
        if(upDirectionSnake) {
            if (!upCol) {
            ySnake[0] -= DOT_SIZE;
            }
           else  {
                if(apple_x < xSnake[0]) {
                    if((B_WIDTH - xSnake[0] + apple_x) < xSnake[0] - apple_x && !rightCol)  {
                        goRight();
                    }
                    else if(!leftCol) goLeft();
                }
                else 
                {
                    if((xSnake[0] + B_WIDTH - apple_x) < apple_x - xSnake[0] && !leftCol) {
                        goLeft();
                    }
                    else if(!rightCol) goRight();
                }
            }
        }

        if(downDirectionSnake) {
            if (!downCol) {          
            ySnake[0] += DOT_SIZE;
            }
            else  {
                if(apple_x < xSnake[0]) {
                    if((B_WIDTH - xSnake[0] + apple_x) < xSnake[0] - apple_x && !rightCol)  {
                        goRight();
                    }
                    else if(!leftCol) goLeft();
                }
                else 
                {
                    if((xSnake[0] + B_WIDTH - apple_x) < apple_x - xSnake[0] && !leftCol) {
                        goLeft();
                    }
                    else if(!rightCol) goRight();
                }
            }
         }        
    }
    
   

    private void checkCollision() {

        for (int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }
        
        for (int z = dotsSnake; z > 0; z--) {

            if ((z > 4) && (xSnake[0] == xSnake[z]) && (ySnake[0] == ySnake[z])) {
                dotsSnake--;
            }
        }

        for (int z =0; z< dots; z++) {
           for (int k =0; k < dotsSnake; k++) {
               if(x[z] == xSnake[k] && y[z] == ySnake[k]) {
                   inGame = false;
               }
            } 
        }
        
        if (y[0] >= B_HEIGHT) {
            y[0] = 0;
        }

        if (y[0] < 0) {
            y[0] = B_HEIGHT-10;
        }

        if (x[0] >= B_WIDTH) {
            x[0] = 0;
        }

        if (x[0] < 0) {
            x[0] = B_WIDTH-10;
        }
        
        
        if (ySnake[0] >= B_HEIGHT) {
            ySnake[0] = 0;
        }

        if (ySnake[0] < 0) {
            ySnake[0] = B_HEIGHT-10;
        }

        if (xSnake[0] >= B_WIDTH) {
            xSnake[0] = 0;
        }

        if (xSnake[0] < 0) {
            xSnake[0] = B_WIDTH-10;
        }
    }

    
    
    
    private void checkDirection() {
        checkAllCol();
        if(xDifBigger()) {
            checkXRoute();
        }
        else if(yDifBigger()) {
            checkYRoute();
        }
        else if(coordDifEqual()) {
        r = (int) Math.ceil(Math.random() * 2);
        if(r==1) {
            if(apple_x > xSnake[0]) {goRight();}
            else {goLeft();}
        }
        else {
            if(apple_y > ySnake[0]) {goDown();}
            else {goUp();}
        }
        
        }
    }
    
    private boolean xDifBigger() {
     if((Math.abs(apple_x - xSnake[0]) > Math.abs(apple_y - ySnake[0]) || apple_y == ySnake[0]))
        return true;
     else 
         return false;
    }
    
    private boolean yDifBigger() {
        if((Math.abs(apple_y - ySnake[0]) > Math.abs(apple_x - xSnake[0])) || apple_x == xSnake[0]) 
            return true;
        else
            return false;
    }
    
    private boolean coordDifEqual() {
        if((Math.abs(apple_y - ySnake[0]) > Math.abs(apple_x - xSnake[0])) || apple_x == xSnake[0]) 
            return true;
        else
            return false;
    }
    
    private void goLeft() {
        System.out.println("left");
        if(!leftCol) {
        leftDirectionSnake = true;
        rightDirectionSnake = false;
        upDirectionSnake = false;
        downDirectionSnake = false;
        }
        else
            resetCollision();
    }
    
    private void goRight() {
        System.out.println("right");
        if(!rightCol) {
        rightDirectionSnake = true;
        leftDirectionSnake = false;
        upDirectionSnake = false;
        downDirectionSnake = false;
        }
        else
            resetCollision();
        
    }
    
    private void goUp() {
        System.out.println("up");
        if(!upCol) {
        upDirectionSnake = true;
        downDirectionSnake = false;
        rightDirectionSnake = false;
        leftDirectionSnake = false;     
        }
        else
            resetCollision();
    }
    
    private void goDown() {
        System.out.println("down");
        if(!downCol) {
        downDirectionSnake = true;
        upDirectionSnake = false;  
        rightDirectionSnake = false;
        leftDirectionSnake = false;
        }
        else
            resetCollision();
    }
    
    private void resetCollision() {
        rightCol = false;
        upCol = false;
        downCol = false;
        leftCol = false;
    }
    
    private void checkXRoute() {
        if(apple_x < xSnake[0]) {
            if((B_WIDTH - xSnake[0] + apple_x) < xSnake[0] - apple_x) {
                goRight();
            }
            else 
                goLeft();
        }
        else {
            if((xSnake[0] + B_WIDTH - apple_x) < apple_x - xSnake[0]) {
                goLeft();
            }
        else
            goRight();
        }
        
    }
    
    private void checkYRoute() {
        if(apple_y < ySnake[0]) {
            if((B_HEIGHT - ySnake[0] + apple_y) < ySnake[0] - apple_y) {
                goDown();
            }
            else 
                goUp();
        }
        else {
            if((ySnake[0] + B_HEIGHT - apple_y) < apple_y - ySnake[0]) {
                goUp();
            }
        else
            goDown();
        }
    }
    
    private void checkLeft() {
       for(int i=1; i<dotsSnake; i++) {
           if(xSnake[0] - DOT_SIZE == xSnake[i] && ySnake[0] == ySnake[i]) {
               leftCol = true;              
           }
       }       
    }
    
    private void checkRight() {
        for(int i=1; i<dotsSnake; i++) {
           if(xSnake[0] + DOT_SIZE == xSnake[i] && ySnake[0] == ySnake[i]) {
               rightCol = true; 
           }
        }
    }
    
    private void checkUp() {
        for(int i=1; i<dotsSnake; i++) {
           if(ySnake[0] - DOT_SIZE == ySnake[i] && xSnake[0] == xSnake[i]) {
               upCol = true;              
           }
        }
    }
    
    private void checkDown() {
        for(int i=1; i<dotsSnake; i++) {
           if(ySnake[0] + DOT_SIZE == ySnake[i] && xSnake[0] == xSnake[i]) {
               downCol = true;              
           }
        }
    }
    
    private void checkAllCol() {
        checkLeft();
        checkRight();
        checkUp();
        checkDown();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            checkApple();
            checkCollision();
            move();
            resetCollision();

        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
            
            if(key == KeyEvent.VK_SPACE && !inGame) {
                inGame = true;
                timer.start();
                dots = 3;
                dotsSnake = 3;

                for (int z = 0; z < dots; z++) {
                    x[z] = 50 - z * 10;
                    y[z] = 50;
                }
                for (int z = 0; z < dotsSnake; z++) {
                    xSnake[z] = 50 - z * 10;
                    ySnake[z] = 250;
                }

                locateApple();
            }
    }
}
}