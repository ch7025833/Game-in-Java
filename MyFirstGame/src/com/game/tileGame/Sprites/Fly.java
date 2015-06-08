package com.game.tileGame.Sprites;

import com.game.reusable.graphics.Animation;

/**
 * Created by chenhao on 5/17/2015.
 */
public class Fly extends Creature{

    public Fly(Animation left, Animation right, Animation deadLeft, Animation deadRight) {
        super(left, right, deadLeft, deadRight);
    }

    @Override
    public float getMaxSpeed() {
        return 0.2f;
    }

    @Override
    public boolean isFlying() {
        return isAlive();
    }
}
