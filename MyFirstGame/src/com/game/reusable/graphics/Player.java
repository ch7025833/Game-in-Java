package com.game.reusable.graphics;

/**
 * Created by chenhao on 5/14/2015.
 */
public class Player extends Sprite {

    public static final int STATE_NORMAL = 0;
    public static final int STATE_JUMPING = 1;

    public static final float SPEED = 0.3f;
    public static final float GRAVITY = 0.002f;

    private int floorY;
    private int state;

    public Player(Animation animation){
        super(animation);
        state = STATE_NORMAL;
    }

    public int getState(){
        return state;
    }

    public void setState(int state){
        this.state = state;
    }

    /*set the location of the floor,where the player starts and lands after jumping*/
    public void setFloorY(int floorY){
        this.floorY = floorY;
        setY(floorY);
    }

    /*cause the player to jump*/
    public void jump(){
        setVelocityY(-1);
        state = STATE_JUMPING;
    }

    /**
     * Update the player's position and animation.Also,sets the player's state to NORMAL if a jumping Player landed on the floor
     */
    @Override
    public void update(long elapsedTime) {
        //set vertical velocity(gravity)
        if (getState() == STATE_JUMPING){
            setVelocityY(getVelocityY() + GRAVITY * elapsedTime);
        }

        //move the player
        super.update(elapsedTime);

        //check if player landed on floor
        if (getState() == STATE_JUMPING && getY() >= floorY){
            setVelocityY(0);
            setY(floorY);
            setState(STATE_NORMAL);
        }
    }
}
