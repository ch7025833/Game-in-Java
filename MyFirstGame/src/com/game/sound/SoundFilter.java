package com.game.sound;

/**
 * Created by chenhao on 5/16/2015.
 */
public abstract class SoundFilter {
    //assume all samples are 16-bits,signed,little-endian format

    public void reset(){
        //do nothing
    }

    public int getRemainingSize(){
        //default
        return 0;
    }

    public void filter(byte[] samples){
        filter(samples,0,samples.length);
    }

    public abstract void filter(byte[] samples, int offset,int length);

    public static short getSample(byte[] buffer,int position){
        return (short)((buffer[position+1] & 0xff) << 8 | (buffer[position] & 0xff));
    }

    public static void setSample(byte[] buffer, int position, short sample){
        buffer[position] = (byte)(sample & 0xff);
        buffer[position + 1] = (byte) ((sample >> 8) & 0xff);
    }



}
