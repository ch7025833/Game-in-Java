import com.game.input.GameAction;
import com.game.input.InputManager;
import com.game.reusable.graphics.Animation;
import com.game.reusable.graphics.Sprite;
import com.game.sound.Filter3D;
import com.game.sound.FilteredSoundStream;
import com.game.sound.LoopingByteInputStream;
import com.game.sound.SimpleSoundPlayer;
import com.game.test.GameCore;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chenhao on 5/16/2015.
 */
public class Filter3DTest extends GameCore {

    public static void main(String[] args) {
        new Filter3DTest().run();
    }

    private Sprite fly;
    private Sprite listener;
    private InputManager inputManager;
    private GameAction exit;

    private SimpleSoundPlayer bzzSound;
    private InputStream bzzSoundStream;

    @Override
    public void init() {
        super.init();

        //set up the input manager
        exit = new GameAction("exit",GameAction.DETECT_INITIAL_PRESS_ONLY);
        inputManager = new InputManager(screenManger.getFullScreenWindow());
        inputManager.mapToKey(exit, KeyEvent.VK_ESCAPE);
        inputManager.setCursor(InputManager.INVISIBLE_CURSOR);

        createSprites();

        //load the sound
        bzzSound = new SimpleSoundPlayer("sounds/fly-bzz.wav");

        //create the 3D filter
        Filter3D filter3D = new Filter3D(fly,listener,screenManger.getHeight());

        //create the filtered sound stream
        bzzSoundStream = new FilteredSoundStream(new LoopingByteInputStream(bzzSound.getSamples()),filter3D);

        //play the sound in a separate thread
        new Thread(){
            @Override
            public void run(){
                bzzSound.play(bzzSoundStream);
            }
        }.start();
    }

    private void createSprites(){
        //load the images
        Image fly1 = loadImage("image/fly1.png");
        Image fly2 = loadImage("image/fly2.png");
        Image fly3 = loadImage("image/fly3.png");
        Image ear = loadImage("image/ear.png");

        //create fly sprite
        Animation animation = new Animation();
        animation.addFrames(fly1,50);
        animation.addFrames(fly2,50);
        animation.addFrames(fly3,50);
        animation.addFrames(fly2,50);

        fly = new Sprite(animation);

        //create the listener sprite
        animation = new Animation();
        animation.addFrames(ear,0);
        listener = new Sprite(animation);
        listener.setX((screenManger.getWidth() - listener.getWidth()) / 2);
        listener.setY((screenManger.getHeight() - listener.getHeight()) / 2);
    }

    @Override
    public void update(long elapsedTime) {
        if (exit.isPressed()){
            stop();
        }else {
            listener.update(elapsedTime);
            fly.update(elapsedTime);
            fly.setX(inputManager.getMouseX());
            fly.setY(inputManager.getMouseY());
        }
    }

    @Override
    public void stop() {
        super.stop();
        //stop the bzz sound
        try {
            bzzSoundStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        //draw background
        graphics2D.setColor(new Color(0x33cc33));
        graphics2D.fillRect(0,0,screenManger.getWidth(),screenManger.getHeight());

        //draw listener
        graphics2D.drawImage(listener.getImage(),Math.round(listener.getX()),Math.round(listener.getY()),null);

        //draw fly
        graphics2D.drawImage(fly.getImage(),Math.round(fly.getX()),Math.round(fly.getY()),null);
    }
}
