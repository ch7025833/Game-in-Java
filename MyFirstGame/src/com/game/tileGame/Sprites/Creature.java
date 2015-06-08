package com.game.tileGame.Sprites;

import com.game.reusable.graphics.Animation;
import com.game.reusable.graphics.Sprite;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * Created by chenhao on 5/17/2015.
 */
public abstract class Creature extends Sprite {

    /*a creature is a Sprite that is affected by gravity and can die.
    * It has four Animations:moving left,moving right,dying on the left,dying on the right*/

    private static final int DIE_TIME = 1000;

    public static final int STATE_NORMAL = 0;
    public static final int STATE_DYING = 1;
    public static final int STATE_DEAD = 2;

    private Animation left;
    private Animation right;
    private Animation deadLeft;
    private Animation deadRight;
    private int state;
    private long stateTime;

     public Creature(Animation left,Animation right,Animation deadLeft,Animation deadRight) {
         super(right);
         this.left = left;
         this.right = right;
         this.deadLeft = deadLeft;
         this.deadRight = deadRight;
         state = STATE_NORMAL;
    }

    @Override
    public Sprite clone() throws CloneNotSupportedException {

        Constructor constructor = getClass().getConstructors()[0];
        try {
            return (Sprite)constructor.newInstance(
                    left.clone(),
                    right.clone(),
                    deadRight.clone(),
                    deadLeft.clone());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public float getMaxSpeed(){
        return 0;
    }


    public void wakeUp(){
        //wakes up the creature when the creature first appears on screen.
        //Normally,the creature starts moving left;
        if (getState() == STATE_NORMAL && getVelocityX() == 0){
            setVelocityX(-getMaxSpeed());
        }
    }

    private int getState() {
        return state;
    }

    public void setState(int state){
        if (this.state != state){
            this.state = state;
            stateTime = 0;
            if (state == STATE_DYING){
                setVelocityX(0.0f);
                setVelocityY(0.0f);
            }
        }
    }

    public boolean isAlive(){
        return (state == STATE_NORMAL);
    }

    public boolean isFlying(){
        return false;
    }

    /*called before update() if the creature collided with a tile horizontally*/
    public void collideHorizontal(){
        setVelocityX(-getVelocityX());
    }

    public void collideVertical(){
        setVelocityY(0.0f);
    }

    public void update(long elapsedTime){
        //select the correct Animation
        Animation newAnimation = getAnimation();
        if (getVelocityX() < 0){
            newAnimation = left;
        }else if (getVelocityX() > 0){
            newAnimation = right;
        }
        if (state == STATE_DYING && newAnimation == left){
            newAnimation = deadLeft;
        }else if (state == STATE_DYING && newAnimation == right){
            newAnimation = deadRight;
        }
        //update the Animation
        if (newAnimation != getAnimation()){
            setAnimation(newAnimation);
            newAnimation.start();
        }else {
            getAnimation().update(elapsedTime);
        }

        //update to dead state
        stateTime += elapsedTime;
        if (state == STATE_DYING && stateTime >= DIE_TIME){
            setState(STATE_DEAD);
        }
    }
}
