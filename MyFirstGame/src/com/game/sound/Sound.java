package com.game.sound;

/**
 * Created by chenhao on 5/16/2015.
 */
public class Sound {

    private byte[] samples;

    public Sound(byte[] samples){
        this.samples = samples;
    }

    public byte[] getSamples(){
        return samples;
    }
}
