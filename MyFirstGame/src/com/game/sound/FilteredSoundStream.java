package com.game.sound;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chenhao on 5/16/2015.
 */
public class FilteredSoundStream extends FilterInputStream{

    private static final int REMAINING_SIZE_UNKNOWN = -1;

    private SoundFilter soundFilter;
    private int remainingSize;

    public FilteredSoundStream(InputStream inputStream,SoundFilter soundFilter){
        super(inputStream);
        this.soundFilter = soundFilter;
        remainingSize = REMAINING_SIZE_UNKNOWN;
    }

    @Override
    public int read(byte[] samples, int offset, int length) throws IOException {

        //read and filter the sound samples in the stream
        int bytesRead = super.read(samples,offset,length);
        if (bytesRead > 0){
            soundFilter.filter(samples,offset,bytesRead);
            return bytesRead;
        }

        //if there are no remaining bytes in the sound stream,
        //check if the filter has any remaining bytes ("echoes").
        if (remainingSize == REMAINING_SIZE_UNKNOWN){
            remainingSize = soundFilter.getRemainingSize();
            //round down to nearest multiple of 4
            //(typical frame size)
            remainingSize = remainingSize / 4 * 4;
        }
        if (remainingSize > 0){
            length = Math.min(length,remainingSize);

            //clear the buffer
            for (int i = offset; i < offset + length; i++){
                samples[i] = 0;
            }

            //filter the remaining bytes
            soundFilter.filter(samples,offset,length);
            remainingSize -= length;

            return length;
        }else {
            return -1;
        }
    }
}
