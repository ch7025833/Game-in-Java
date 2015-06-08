package com.game.reusable.graphics;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by chenhao on 5/12/2015.
 */
public class Animation {

    private ArrayList<AnimationFrame> frames;
    private int currentFrameIndex;
    private long animationTime;
    private long totalDuration;

    public Animation(){

        frames = new ArrayList<AnimationFrame>();
        totalDuration = 0;
        start();
    }

    public synchronized  void   addFrames(Image image,long duration){
        totalDuration += duration;
        frames.add(new AnimationFrame(image,totalDuration));
    }

    public synchronized void start(){
        animationTime = 0;
        currentFrameIndex = 0;
    }

    public synchronized void update(long elapsedTime){

        if (frames.size() > 1){
            animationTime += elapsedTime;

            if (animationTime >= totalDuration){
                //loop the frame.
                animationTime %= totalDuration;
                currentFrameIndex = 0;
            }

            while (animationTime > getFrame(currentFrameIndex).endTime){
                currentFrameIndex++;
            }
        }
    }

    public synchronized Image getImage(){

        if (frames.size() == 0){
            return  null;
        }
        else {
            return getFrame(currentFrameIndex).image;
        }
    }

    private AnimationFrame getFrame(int i){
        return frames.get(i);
    }

    private class AnimationFrame{
        Image image;
        long endTime;

        public AnimationFrame(Image image,long endTime){
            this.image = image;
            this.endTime = endTime;
        }
    }

    @Override
    public Animation clone() throws CloneNotSupportedException {
        ArrayList<AnimationFrame> newFrameList = new ArrayList<AnimationFrame>(this.frames);
        Animation newAnimation   = new Animation();
        newAnimation.frames = newFrameList;
        newAnimation.currentFrameIndex = currentFrameIndex;
        newAnimation.totalDuration = totalDuration;
        newAnimation.animationTime = animationTime;
        return newAnimation;
    }
}
