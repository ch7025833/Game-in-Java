import com.game.reusable.graphics.Animation;
import com.game.reusable.graphics.ScreenManger;
import com.game.reusable.graphics.Sprite;

import javax.swing.*;
import java.awt.*;

/**
 * Created by chenhao on 5/12/2015.
 */
public class SpriteTest1 {

    public static void main(String[] args) {
        SpriteTest1 test1 =  new SpriteTest1();
        test1.run();
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
    private Sprite sprite;

    public void loadImages(){
        //load images.
        bgImage = loadImage("image/background.jpg");
        Image player1 = loadImage("image/player1.png");
        Image player2 = loadImage("image/player2.png");
        Image player3 = loadImage("image/player3.png");

        //create animation and sprite.
        Animation animation = new Animation();
        animation.addFrames(player1,250);
        animation.addFrames(player2,150);
        animation.addFrames(player1,150);
        animation.addFrames(player2,150);
        animation.addFrames(player3,200);
        animation.addFrames(player2,150);
        sprite = new Sprite(animation);

        //start the sprite off moving down and  to the right
        sprite.setVelocityX(0.2f);
        sprite.setVelocityY(0.2f);
    }

    private Image loadImage(String path){
        return new ImageIcon(path).getImage();
    }

    public void run(){
        screenManger = new ScreenManger();
        try {
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

            //update the sprite
            update(elapsedTime);

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

    public void update(long elapsedTime){
        //check sprite bounds
        if (sprite.getX() < 0){
            sprite.setVelocityX(Math.abs(sprite.getVelocityX()));
        }else if(sprite.getX() + sprite.getWidth() >= screenManger.getWidth()){
            sprite.setVelocityX(-Math.abs(sprite.getVelocityX()));
        }

        if (sprite.getY() < 0){
            sprite.setVelocityY(Math.abs(sprite.getVelocityY()));
        }else if (sprite.getY() + sprite.getHeight() >= screenManger.getHeight()){
            sprite.setVelocityY(-Math.abs(sprite.getVelocityY()));
        }

        //update sprite
        sprite.update(elapsedTime);
    }

    public void draw(Graphics2D graphics2D){
        graphics2D.drawImage(bgImage,0,0,null);

        //draw sprite
        graphics2D.drawImage(sprite.getImage(),
                Math.round(sprite.getX()),
                Math.round(sprite.getY()),
                null);
    }

}
