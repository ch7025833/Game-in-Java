import com.game.reusable.graphics.Animation;
import com.game.reusable.graphics.ScreenManger;
import com.game.reusable.graphics.Sprite;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by chenhao on 5/13/2015.
 */
public class SpriteTest2 {
    public static void main(String[] args) {
        SpriteTest2 test2 =  new SpriteTest2();
        test2.run();
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
    private static final long FADE_TIME = 1000;
    private static final int NUM_SPRITES = 3;

    private ScreenManger screenManger;
    private Image bgImage;
    private Sprite[] sprite;

    public void loadImages(){
        //load images.
        bgImage = loadImage("image/background.jpg");
        Image player1 = loadImage("image/player1.png");
        Image player2 = loadImage("image/player2.png");
        Image player3 = loadImage("image/player3.png");

        //create animation and sprite.

        sprite = new Sprite[NUM_SPRITES];
        for(int i = 0; i < NUM_SPRITES; i++){
            Animation animation = new Animation();
            animation.addFrames(player1,250);
            animation.addFrames(player2,150);
            animation.addFrames(player1,150);
            animation.addFrames(player2,150);
            animation.addFrames(player3,200);
            animation.addFrames(player2,150);
            sprite[i] = new Sprite(animation);

            //select random start location
            sprite[i].setX((float)Math.random() * (screenManger.getWidth() - sprite[i].getWidth()));
            sprite[i].setY((float)Math.random() * (screenManger.getHeight() - sprite[i].getHeight()));

            //select random velocity
            sprite[i].setVelocityX((float)Math.random() - 0.5f);
            sprite[i].setVelocityY((float)Math.random() - 0.5f);
        }
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
            drawFade(graphics2D, currentTime - startTime);
            graphics2D.dispose();
            screenManger.update();

            //take a nap
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
            }

        }
    }

    public void drawFade(Graphics2D graphics2D,long currentTime){
        long time = 0;
        if (currentTime < FADE_TIME){
            time = FADE_TIME - currentTime;
        }else if (currentTime > DEMO_TIME - FADE_TIME){
            time = FADE_TIME - DEMO_TIME + currentTime;
        }else {
            return;
        }

        Byte numberBars = 8;
        int barHeight = screenManger.getHeight() / numberBars;
        int blackHeight = (int)(time * barHeight / FADE_TIME);

        graphics2D.setColor(Color.BLACK);
        for (int i = 0; i < numberBars; i++){
            int y = i * barHeight + (barHeight - blackHeight) / 2;
            graphics2D.fillRect(0,y,screenManger.getWidth(),blackHeight);
        }
    }

    public void update(long elapsedTime) {

        for (int i = 0; i < NUM_SPRITES; i++) {

            Sprite  s = sprite[i];

            if (s.getX() < 0) {
                s.setVelocityX(Math.abs(s.getVelocityX()));
            } else if (s.getX() + s.getWidth() >= screenManger.getWidth()) {
                s.setVelocityX(-Math.abs(s.getVelocityX()));
            }

            if (s.getY() < 0) {
                s.setVelocityY(Math.abs(s.getVelocityY()));
            } else if (s.getY() + s.getHeight() >= screenManger.getHeight()) {
                s.setVelocityY(-Math.abs(s.getVelocityY()));
            }

            //update sprite
            s.update(elapsedTime);
        }
    }

    public void draw(Graphics2D graphics2D) {
        //draw the background
        graphics2D.drawImage(bgImage, 0, 0, null);

        AffineTransform affineTransform = new AffineTransform();

        for (int i = 0; i < NUM_SPRITES; i++) {

            Sprite s = sprite[i];

            //translate the sprite
            affineTransform.setToTranslation(s.getX(),s.getY());

            //if the sprite is moving left,flip the image
            if (s.getVelocityX() < 0){
                affineTransform.scale(-1,1);
                affineTransform.translate(-s.getWidth(),0);
            }
            //draw sprite
            graphics2D.drawImage(s.getImage(),affineTransform,null);
        }
    }

}
