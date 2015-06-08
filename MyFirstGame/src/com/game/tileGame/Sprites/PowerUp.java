package com.game.tileGame.Sprites;

import com.game.reusable.graphics.Animation;
import com.game.reusable.graphics.Sprite;

/**
 * Created by chenhao on 5/17/2015.
 */
public abstract class PowerUp extends Sprite{

    public PowerUp(Animation animation) {
        super(animation);
    }

    @Override
    public PowerUp clone() throws CloneNotSupportedException {
        return (PowerUp)super.clone();
    }

    public static class Star extends PowerUp{

        public Star(Animation animation) {
            super(animation);
        }
    }

    public static class Music extends PowerUp{

        public Music(Animation animation) {
            super(animation);
        }
    }

    public static class Goal extends PowerUp{

        public Goal(Animation animation) {
            super(animation);
        }
    }
}
