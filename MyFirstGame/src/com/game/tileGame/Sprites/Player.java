package com.game.tileGame.Sprites;

import com.game.reusable.graphics.Animation;

/**
 * Created by chenhao on 5/17/2015.
 */
public class Player extends Creature {

    private static final float JUMP_SPEED = -0.95f;

    private boolean onGround;

    public Player(Animation left, Animation right, Animation deadLeft, Animation deadRight) {
        super(left, right, deadLeft, deadRight);
    }

    @Override
    public void collideHorizontal() {
        setVelocityX(0);
    }

    @Override
    public void collideVertical() {
        //check if collided with ground
        if (getVelocityY() > 0){
            onGround = true;
        }
        setVelocityY(0);
    }

    @Override
    public void setY(float y) {
        //check if falling
        if (Math.round(y) > Math.round(getY())){
            onGround = false;
        }
        super.setY(y);
    }

    public void jump(boolean forceJump){
        if (onGround || forceJump){
            onGround = false;
            setVelocityY(JUMP_SPEED);
        }
    }

    @Override
    public float getMaxSpeed() {
        return 0.5f;
    }
}
