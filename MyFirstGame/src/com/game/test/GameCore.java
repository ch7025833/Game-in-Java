package com.game.test;

import com.game.reusable.graphics.ScreenManger;

import javax.swing.*;
import java.awt.*;

/**
 * Created by chenhao on 5/13/2015.
 */
public abstract class GameCore {

    protected static final int FONT_SIZE = 24;

    private static final DisplayMode POSSIBLE_MODES[] = {
            new DisplayMode(800,600,32,0),
            new DisplayMode(800,600,24,0),
            new DisplayMode(800,600,16,0),
            new DisplayMode(640,480,32,0),
            new DisplayMode(640,480,24,0),
            new DisplayMode(640,480,16,0),
    };

    private boolean isRunning;
    protected ScreenManger screenManger;

    public void stop(){
        isRunning = false;
    }

    public void run(){
        try {
            init();
            gameLoop();
        }finally {
            screenManger.restoreScreen();
            lazilyExit();
        }
    }

    /*set the full screen mode and initialize the objects*/
    public void init(){
        screenManger = new ScreenManger();
        DisplayMode displayMode = screenManger.findFirstCompatibleMode(POSSIBLE_MODES);
        screenManger.setFullScreen(displayMode);

        Window window = screenManger.getFullScreenWindow();
        window.setFont(new Font("Dialog", Font.PLAIN,FONT_SIZE));
        window.setBackground(Color.blue);
        window.setForeground(Color.white);

        isRunning = true;
    }

    public Image loadImage(String path){
        return new ImageIcon(path).getImage();
    }

    /*run through the gameLoop until the stop() is called*/

    public void gameLoop(){

        long startTime = System.currentTimeMillis();
        long currentTime = startTime;

        while(isRunning){
            long elapsedTime = System.currentTimeMillis() - currentTime;
            currentTime += elapsedTime;

            //update
            update(elapsedTime);

            //draw the screen
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

    public void update(long elapsedTime){}

    /*draw the screen, must be override*/
    public abstract void draw(Graphics2D graphics2D);

    public void lazilyExit(){
        Thread thread = new Thread(){
            @Override
            public void run(){
                //first,wait for the VM exit on its own.
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    //system is still running,so force to exit
                    System.exit(0);
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

}
