package com.game.reusable.graphics;

import sun.security.jca.JCAUtil;

import javax.swing.*;

/**
 * Created by chenhao on 5/15/2015.
 */
public class NullRepaintManager extends RepaintManager{
    /*don't do any repainting.Because we do all the rendering manually in the other thread*/
    /*repaint events aren't sent to the AWT*/
    public static void install(){
        RepaintManager repaintManager = new NullRepaintManager();
        repaintManager.setDoubleBufferingEnabled(false);
        RepaintManager.setCurrentManager(repaintManager);
    }

    @Override
    public void addInvalidComponent(JComponent component){
        //do nothing
    }

    @Override
    public void addDirtyRegion(JComponent component,int x,int y,int w,int h){
        //do nothing
    }

    @Override
    public void markCompletelyDirty(JComponent jComponent) {
        //do nothing
    }

    @Override
    public void paintDirtyRegions() {
        //do nothing
    }
}
