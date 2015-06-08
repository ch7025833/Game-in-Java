import com.game.reusable.graphics.Animation;
import com.game.reusable.graphics.ScreenManger;

import javax.swing.*;
import java.awt.*;

/**
 * Created by chenhao on 5/12/2015.
 */
public class AnimationTest2 {

    public static void main(String[] args) {
        AnimationTest2 animationTest2 = new AnimationTest2();
        animationTest2.run();
    }

    private static final DisplayMode POSSIBLE_MODES[] = {
            new DisplayMode(800,600,32,0),
            new DisplayMode(800,600,24,0),
            new DisplayMode(800,600,16,0),
            new DisplayMode(640,480,32,0),
            new DisplayMode(640,480,24,0),
            new DisplayMode(640,480,16,0),
    };

    private static final long DEMO_TIME = 10000;

    private ScreenManger screenManger;
    private Image bgImage;
    private Animation animation;

    public void loadImages(){
        //load images.
        bgImage = loadImage("image/background.jpg");
        Image player1 = loadImage("image/player1.png");
        Image player2 = loadImage("image/player2.png");
        Image player3 = loadImage("image/player3.png");

        //create animation.
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

    public void  run(){
        screenManger = new ScreenManger();
        try{
            DisplayMode displayMode = screenManger.findFirstCompatibleMode(POSSIBLE_MODES);
            screenManger.setFullScreen(displayMode);
            loadImages();
            animationLoop();
        }finally {
            screenManger.restoreScreen();
        }
    }

    public void animationLoop(){
        long startTime = System.currentTimeMillis();
        long currentTime = startTime;

        while (currentTime - startTime < DEMO_TIME){

            long elapsedTime = System.currentTimeMillis() - currentTime;
            currentTime += elapsedTime;

            //update animation
            animation.update(elapsedTime);

            //draw and update the screen
            Graphics2D graphics2D = screenManger.getGraphics();
            draw(graphics2D);
            graphics2D.dispose();
            screenManger.update();

            //take a nap
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
            }

        }
    }

    public void draw(Graphics2D graphics2D){
        //draw the background
        graphics2D.drawImage(bgImage,0,0,null);

        //draw image
        graphics2D.drawImage(animation.getImage(),0,0,null);
    }

}

