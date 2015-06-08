package com.game.sound;

import com.game.reusable.graphics.Sprite;

/**
 * Created by chenhao on 5/16/2015.
 */
public class Filter3D extends SoundFilter {
    //when further,the sound is quieter


    //number of samples to shift when changing the volume.
    private static final int NUM_SHIFTING_SAMPLES = 500;

    private Sprite source;
    private Sprite listener;
    private int maxDistance;
    private float lastVolume;

    public Filter3D(Sprite source,Sprite listener,int maxDistance){
        this.source =  source;
        this.listener = listener;
        this.maxDistance = maxDistance;
        this.lastVolume = 0.0f;
    }

    @Override
    public void filter(byte[] samples, int offset, int length) {

        if (source == null || listener == null){
            //nothing to filter
            return;
        }

        //calculate the listener's distance from the sound source
        float dx = (source.getX() - listener.getX());
        float dy = (source.getY() - listener.getY());
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        //set volume from 0 (no sound) to 1
        float newVolume = (maxDistance - distance) / maxDistance;
        if (newVolume <= 0){
            newVolume = 0;
        }

        //set the volume of the sample
        int shift = 0;
        for (int i = offset; i < offset + length; i += 2){

            float volume = newVolume;

            //shift from the last volume to the new volume
            if (shift < NUM_SHIFTING_SAMPLES){
                volume = lastVolume + (newVolume - lastVolume) * shift / NUM_SHIFTING_SAMPLES;
                shift++;
            }

            //change the volume of the sample
            short oldSample = getSample(samples,i);
            short newSample = (short)(oldSample * volume);
            setSample(samples,i,newSample);
        }
        lastVolume = newVolume;
    }
}
