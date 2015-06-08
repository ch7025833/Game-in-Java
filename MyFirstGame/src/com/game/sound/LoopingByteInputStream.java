package com.game.sound;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by chenhao on 5/16/2015.
 */
public class LoopingByteInputStream extends ByteArrayInputStream {

    private boolean closed;

    public LoopingByteInputStream(byte[] buffer){
        super(buffer);
        closed = false;
    }

    @Override
    public int read(byte[] buffer,int offset,int length){
        if (closed){
            return -1;
        }
        int totalByteRead = 0;

        while (totalByteRead < length){
            int numByteRead = super.read(buffer,offset + totalByteRead, length - totalByteRead);
            if (numByteRead > 0){
                totalByteRead += numByteRead;
            }else {
                reset();
            }
        }
        return totalByteRead;
    }

    @Override
    public void close() throws IOException{
        super.close();
        closed = true;
    }
}
