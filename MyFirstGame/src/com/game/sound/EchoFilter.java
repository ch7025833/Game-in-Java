package com.game.sound;

/**
 * Created by chenhao on 5/16/2015.
 */
public class EchoFilter extends SoundFilter {

    private short[] delayBuffer;
    private int delayBufferPos;
    private float decay;

    public EchoFilter(int numDelaySamples, float decay){

        delayBuffer = new short[numDelaySamples];
        this.decay = decay;
    }

    @Override
    public int getRemainingSize() {

        float finalDecay = 0.01f;

        //derived from Math.pow(decay,x) <= finalDecay
        int numRemainingBuffers = (int) Math.ceil(Math.log(finalDecay) / Math.log(decay));

        int bufferSize = delayBuffer.length * 2;
        return bufferSize * numRemainingBuffers;
    }

    public void reset(){
        for (int i = 0; i < delayBuffer.length; i++){
            delayBuffer[i] = 0;
        }
        delayBufferPos = 0;
    }

    /*Filter the sound samples to add an echo.
    * The samples played are added to the sound in the delay buffer
    * multiplied by the decay rate.The result is the stored in the delay buffer,
    * so multiple echoes are heard*/
    @Override
    public void filter(byte[] samples, int offset, int length) {

        for (int i = offset; i < offset + length; i += 2){
            //update the sample
            short oldSample = getSample(samples,i);
            short newSample = (short)(oldSample + decay * delayBuffer[delayBufferPos]);
            setSample(samples,i,newSample);

            //update the delay buffer
            delayBuffer[delayBufferPos] = newSample;
            delayBufferPos++;
            if (delayBufferPos == delayBuffer.length){
                delayBufferPos = 0;
            }
        }
    }
}
