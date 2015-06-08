import com.game.reusable.graphics.Animation;

import javax.swing.*;
import java.awt.*;

/**
 * Created by chenhao on 5/12/2015.
 */
public class AnimationTest1 {

    public static void main(String[] args) {

        DisplayMode displayMode;

        if (args.length == 3){

            displayMode = new DisplayMode(Integer.parseInt(args[0]),
                    Integer.parseInt(args[1]),
                    Integer.parseInt(args[2]),
                    DisplayMode.REFRESH_RATE_UNKNOWN);
        }else {
            displayMode = new DisplayMode(800,600,16,DisplayMode.REFRESH_RATE_UNKNOWN);
        }

        AnimationTest1 imageTest = new AnimationTest1();
        imageTest.run(displayMode);
    }

    private static final long DEMO_TIME = 10000;

    private SimpleScreenManger screenManger;
    private Image bgImage;
    private Animation animation;

    public void run(DisplayMode displayMode){

        screenManger = new SimpleScreenManger();

        try{
            screenManger.setFullScreen(displayMode,new JFrame());
            LoadImages();
            animationLoop();
        }finally {
            screenManger.restoreScreen();
        }
    }

    public void LoadImages(){

        bgImage = loadImage("image/background.jpg");
        Image player1 = loadImage("image/player1.png");
        Image player2 = loadImage("image/player2.png");
        Image player3 = loadImage("image/player3.png");
        //create the animation
        animation = new Animation();
        animation.addFrames(player1,250);
        animation.addFrames(player2,150);
        animation.addFrames(player1,150);
        animation.addFrames(player2,150);
        animation.addFrames(player3,200);
        animation.addFrames(player2,150);
    }

    private Image loadImage(String path){
        return new ImageIcon(path).getImage();
    }

    public void animationLoop(){

        long startTime = System.currentTimeMillis();
        long currentTime = startTime;

        while (currentTime - startTime < DEMO_TIME){
            long elapsedTime = System.currentTimeMillis() - currentTime;
            currentTime += elapsedTime;
            //update animation.
            animation.update(elapsedTime);

            //draw to screen.
            Graphics graphics = screenManger.getFullScreenWindow().getGraphics();
            draw(graphics);
            graphics.dispose();

            //take a nap
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
            }
        }
    }
    public void draw(Graphics graphics){

        //draw background
        graphics.drawImage(bgImage,0,0,null);
        //draw image
        graphics.drawImage(animation.getImage(),0,0,null);
    }
}

