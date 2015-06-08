package com.game.reusable.graphics;

import java.awt.*;

/**
 * Created by chenhao on 5/12/2015.
 */
public class Sprite {

    private Animation animation;
    //position
    private float x;
    private float y;
    //velocity:speed and direction.(pixels per millisecond)
    private float dx;
    private float dy;

    public Sprite(Animation animation){
        this.animation = animation;
    }

    /*update the sprite's animation based on its velocity and position
    * */
    public void update(long elapsedTime){
        x += dx * elapsedTime;
        y += dy * elapsedTime;
        animation.update(elapsedTime);
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public void setX(float x){
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }

    /*get the sprite's width based on the animation's image's width*/
    public int getWidth(){
        return animation.getImage().getWidth(null);
    }

    public int getHeight(){
        return animation.getImage().getHeight(null);
    }

    public float getVelocityX(){
        return dx;
    }

    public float getVelocityY(){
        return dy;
    }

    /*set the horizontal velocity of the sprite in pixels per millisecond*/
    public void setVelocityX(float dx){
        this.dx = dx;
    }

    public void setVelocityY(float dy){
        this.dy =  dy;
    }

    public Image getImage(){
        return animation.getImage();
    }

    @Override
    public Sprite clone() throws CloneNotSupportedException {
        Sprite newSprite = new Sprite(animation.clone());
        newSprite.setY(y);
        newSprite.setX(x);
        newSprite.setVelocityX(dx);
        newSprite.setVelocityY(dy);
        return newSprite;
    }

    public Animation getAnimation(){
        return animation;
    }

    public void setAnimation(Animation newAnimation){
        animation = newAnimation;
    }
}
