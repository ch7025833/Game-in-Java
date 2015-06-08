package com.game.sound;

import com.game.util.ThreadPool;
import javax.sound.sampled.*;
import java.io.*;

/**
 * Created by chenhao on 5/16/2015.
 */

/*this manages the sound playback
* Use the threadPool to reduce the lag of playing sounds*/
public class SoundManager extends ThreadPool {

    private AudioFormat playBackFormat;
    private ThreadLocal localLine;
    private ThreadLocal localBuffer;
    private Object pausedLock;
    private boolean paused;

    public SoundManager(AudioFormat playBackFormat){
        this(playBackFormat,getMaxSimultaneousSounds(playBackFormat));
    }

    public SoundManager(AudioFormat playBackFormat,int maxSimultaneousSounds){
        super(maxSimultaneousSounds);
        this.playBackFormat = playBackFormat;
        localLine = new ThreadLocal();
        localBuffer = new ThreadLocal();
        pausedLock = new Object();
        //notify threads in pool it's okay to start
        synchronized (this){
            notifyAll();
        }
    }

    /*get the maximum number of simultaneous sounds with the specified AudioFormat that the default mixer can play*/
    public static int getMaxSimultaneousSounds(AudioFormat audioFormat){

        DataLine.Info lineInfo = new DataLine.Info(SourceDataLine.class,audioFormat);
        Mixer mixer = AudioSystem.getMixer(null);
        return mixer.getMaxLines(lineInfo);
    }

    /*do the clean up before closing*/
    protected void cleanUp(){
        //signal to unpause
        setPaused(false);

        //close the mixer(stops any running sounds)
        Mixer mixer = AudioSystem.getMixer(null);
        if (mixer.isOpen()){
            mixer.close();
        }
    }

    @Override
    public void close(){
        cleanUp();
        super.close();
    }

    @Override
    public void join() {
        cleanUp();
        super.join();
    }

    /*set the paused state.Sounds may not pause immediately*/
    public void setPaused(boolean paused){
        if (this.paused != paused){
            synchronized (pausedLock){
                this.paused = paused;
                if (!paused){
                    //restart sounds
                    pausedLock.notifyAll();
                }
            }
        }
    }

    public boolean isPaused(){
        return paused;
    }

    public Sound getSound(String fileName){
        return getSound(getAudioInputStream(fileName));
    }

    public Sound getSound(AudioInputStream audioInputStream){
        if (audioInputStream == null){
            return null;
        }
        //get the number of bytes to read
        int length = (int)(audioInputStream.getFrameLength() * audioInputStream.getFormat().getFrameSize());

        //read the entire stream
        byte[] samples = new byte[length];
        DataInputStream inputStream = new DataInputStream(audioInputStream);
        try {
            inputStream.readFully(samples);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Sound(samples);
    }

    public AudioInputStream getAudioInputStream(String fileName){
        try {
            //open source file
            AudioInputStream source = AudioSystem.getAudioInputStream(new File(fileName));

            //convert to playback format
            return AudioSystem.getAudioInputStream(playBackFormat,source);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream play(Sound sound){
        return play(sound,null,null);
    }

    public InputStream play(Sound sound,SoundFilter filter,Boolean loop){
        InputStream inputStream;
        if (sound != null){
            if (loop){
                inputStream = new LoopingByteInputStream(sound.getSamples());
            }else {
                inputStream = new ByteArrayInputStream(sound.getSamples());
            }
            return play(inputStream,filter);
        }
        return null;
    }

    public InputStream play(InputStream inputStream){
        return play(inputStream,null);
    }

    private InputStream play(InputStream inputStream, SoundFilter filter) {
        if (inputStream != null){
            if (filter != null){
                inputStream = new FilteredSoundStream(inputStream,filter);
            }
            runTask(new SoundPlayer(inputStream));
        }
        return inputStream;
    }

    protected void threadStarted(){
        //wait for the SoundManger constructor to finish
        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        //use a short,100 ms (1/ 10 th sec) buffer to filters that changes in real-time.
        int bufferSize = playBackFormat.getFrameSize() * Math.round(playBackFormat.getSampleRate() / 10);

        //create,open,and start the line
        SourceDataLine line;
        DataLine.Info lineInfo = new DataLine.Info(SourceDataLine.class,playBackFormat);
        try {
            line = (SourceDataLine)AudioSystem.getLine(lineInfo);
            line.open(playBackFormat,bufferSize);
        } catch (LineUnavailableException e) {
            //when lien is not available,signal to end this thread
            Thread.currentThread().interrupt();
            return;
        }
        line.start();

        //create buffer
        byte[] buffer = new byte[bufferSize];
        //set this thread's locals
        localLine.set(line);
        localBuffer.set(buffer);
    }

    protected void threadStopped(){
        SourceDataLine line = (SourceDataLine)localLine.get();
        if (line != null){
            line.drain();
            line.close();
        }
    }

    protected class SoundPlayer implements Runnable{

        private InputStream source;

        public SoundPlayer(InputStream source){
            this.source = source;
        }

        @Override
        public void run() {
            //get line and buffer from ThreadLocals
            SourceDataLine line = (SourceDataLine)localLine.get();
            byte[] buffer = (byte[])localBuffer.get();
            if (line == null || buffer == null){
                //the line is unavailable
                return;
            }

            //copy the data to the line
            try {
                int numBytesRead = 0;
                while (numBytesRead != -1){
                    //if paused,wait until unpaused
                    synchronized (pausedLock){
                        if (paused){
                            try {
                                pausedLock.wait();
                            } catch (InterruptedException e) {
                                return;
                            }
                        }
                    }
                    //copy data
                    numBytesRead = source.read(buffer,0,buffer.length);
                    if (numBytesRead != -1){
                        line.write(buffer,0,numBytesRead);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
