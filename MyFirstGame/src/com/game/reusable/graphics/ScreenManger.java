package com.game.reusable.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.Window;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/**
 * Created by chenhao on 5/12/2015.
 */
public class ScreenManger {

    private GraphicsDevice device;

    public ScreenManger(){
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = graphicsEnvironment.getDefaultScreenDevice();
    }

    public DisplayMode[] getCompatibleDisplayModes(){
        return device.getDisplayModes();
    }

    public DisplayMode findFirstCompatibleMode(DisplayMode[] displayModes){
        DisplayMode[] goodModes = device.getDisplayModes();
        for(DisplayMode modeAvailable:displayModes){
            for (DisplayMode mode:goodModes){
                if (displayModesMatch(modeAvailable,mode)){
                    return mode;
                }
            }
        }
        return null;
    }

    public DisplayMode getCurrentDisplayMode(){
        return device.getDisplayMode();
    }

    /*
    * to see whether two display modes match,they should have the same resolution,bit depth and refresh rate
    * */
    public boolean displayModesMatch(DisplayMode mode1, DisplayMode mode2){

        if (mode1.getWidth() != mode2.getWidth() || mode1.getHeight() != mode2.getHeight()){
            return false;
        }
        if (mode1.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI
                && mode2.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI
                && mode1.getBitDepth() != mode2.getBitDepth()) {
            return false;
        }
        if (mode1.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN
                && mode2.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN
                && mode1.getRefreshRate() != mode2.getRefreshRate()){
            return false;
        }
        return true;
    }

    /*enter the full screen and change the display mode depends on the conditions*/
    public void setFullScreen(DisplayMode displayMode){
        JFrame frame = new JFrame();
        frame.setUndecorated(true);
        frame.setIgnoreRepaint(true);
        frame.setResizable(false);

        device.setFullScreenWindow(frame);
        if (displayMode != null
                && device.isDisplayChangeSupported()){
            try {
                device.setDisplayMode(displayMode);
            }catch (IllegalArgumentException e){}

        }
        //create two buffer.
        frame.createBufferStrategy(2);
    }

    /*get the graphics context for the display,use the double buffering,so application
    * must update to show any graphic change
    * */
    public Graphics2D getGraphics(){

        Window window = device.getFullScreenWindow();
        if (window != null){
            BufferStrategy bufferStrategy = window.getBufferStrategy();
            return (Graphics2D)bufferStrategy.getDrawGraphics();
        }else {
            return null;
        }
    }

    /*update the display*/
    public void update(){
        Window window = device.getFullScreenWindow();
        if (window != null){
            BufferStrategy bufferStrategy = window.getBufferStrategy();
            if (!bufferStrategy.contentsLost()){
                bufferStrategy.show();
            }
        }
        //sync some problems on some systems.
        Toolkit.getDefaultToolkit().sync();
    }

    public Window getFullScreenWindow(){
        return device.getFullScreenWindow();
    }

    //return 0 if not in full screen.
    public int getWidth(){
        Window window = device.getFullScreenWindow();
        if (window != null){
            return window.getWidth();
        }else {
            return 0;
        }
    }

    public int getHeight(){
        Window window = device.getFullScreenWindow();
        if (window != null){
            return window.getHeight();
        }else {
            return 0;
        }
    }

    //restore the screen display mode
    public void restoreScreen(){
        Window window = device.getFullScreenWindow();
        if (window != null){
            window.dispose();
        }
        device.setFullScreenWindow(null);
    }

    /*create the compatible image for the current display mode*/
    public BufferedImage createCompatibleImage(int width,int height,int transparency){
        Window window = device.getFullScreenWindow();
        if (window != null){
            GraphicsConfiguration graphicsConfiguration = window.getGraphicsConfiguration();
            return graphicsConfiguration.createCompatibleImage(width,height,transparency);
        }
        return null;
    }
  }
