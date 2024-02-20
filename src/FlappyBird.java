
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class FlappyBird extends JPanel implements  ActionListener , KeyListener{
    int boardWidth = 360;
    int boardHeight = 640;

    int birdx= boardWidth/8;
    int birdy = boardHeight/2;
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird{
        int x= birdx;
        int y = birdy;
        int w = birdWidth;
        int h= birdHeight;
        Image img;

        Bird (Image img){
            this.img=img;
        }
    }

    int pipex = boardWidth;
    int pipey = birdHeight;
    int pipewidth = 64;
    int pipeheught = 512;

    class Pipe{
        int x = pipex;
        int y = pipey;
        int width = pipewidth;
        int height= pipeheught;
        Image img ;
        boolean passed = false;

        Pipe(Image img){
            this.img = img;
        }
    }



    Image bottompipe;
    Image flappybird;
    Image flappybirdbg;
    Image toppipe;

    Bird bird;
    int velocityx= -4;
    int velocitiy = 0;
    int gracity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameloop;
    Timer placePipestimer;

    boolean gameOver = false;
    double score =0;

    FlappyBird(){
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        setFocusable(true);
        addKeyListener(this);

        flappybirdbg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();

        flappybird = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();

        bottompipe = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        toppipe = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();



        bird = new Bird(flappybird);
        pipes = new ArrayList<Pipe>();

        gameloop = new Timer(1000/60 , this);
        placePipestimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placesPipe();
            }
        });
        placePipestimer.start();
        gameloop.start();
    }

    public void placesPipe(){
        int randomy   = (int) (pipey - pipeheught/4 - Math.random()*(pipeheught/2));
        int openingSpace = boardHeight/4;

        Pipe topPipe = new Pipe(toppipe);
        topPipe.y = randomy;
        pipes.add(topPipe);


        Pipe bottomPipe = new Pipe(bottompipe);
        bottomPipe.y = topPipe.y+ pipeheught+openingSpace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        g.drawImage(flappybirdbg , 0 , 0 , boardWidth,boardHeight,null);
        g.drawImage(flappybird , bird.x , bird.y , bird.w, bird.h,null);

        for(int i = 0 ; i<pipes.size() ; i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img , pipe.x ,pipe.y,pipe.width,pipe.height,null);
        }

        //score
        g.setColor(Color.white);

        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
        }
        else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public  void move (){
        velocitiy += gracity;
         bird.y += velocitiy;
         bird.y = Math.max(bird.y, 0);

         for(int i =0 ; i< pipes.size() ; i++ ){
             Pipe pipe = pipes.get(i);
             pipe.x += velocityx;

             if(!pipe.passed && bird.x> pipe.x + pipe.width){
                 pipe.passed = true;
                 score += 0.5;
             }

             if (collision(bird, pipe)) {
                 gameOver = true;
             }
         }
         if(bird.y > boardHeight){
             gameOver = true;
         }

    }

    boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width &&   //a's top left corner doesn't reach b's top right corner
                a.x + a.w > b.x &&   //a's top right corner passes b's top left corner
                a.y < b.y + b.height &&  //a's top left corner doesn't reach b's bottom left corner
                a.y + a.h > b.y;    //a's bottom left corner passes b's top left corner
    }

    public void actionPerformed(ActionEvent ae){
        move();
        repaint();
        if(gameOver){
            placePipestimer.stop();
            gameloop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            velocitiy = -9;
        }
        if (gameOver) {
            //restart game by resetting conditions
            bird.y = birdy;
            velocitiy = 0;
            pipes.clear();
            gameOver = false;
            score = 0;
            gameloop.start();
            placePipestimer.start();
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
