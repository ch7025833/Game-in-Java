package com.game.tileGame.Sprites;

import com.game.reusable.graphics.Animation;

/**
 * Created by chenhao on 5/17/2015.
 */
public class Grub extends Creature {

    public Grub(Animation left, Animation right, Animation deadLeft, Animation deadRight) {
        super(left, right, deadLeft, deadRight);
    }

    @Override
    public float getMaxSpeed() {
        return 0.05f;
    }

}
