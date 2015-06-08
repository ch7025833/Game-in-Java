package com.game.sound;

import javax.sound.sampled.*;
import java.io.*;

/**
 * Created by chenhao on 5/16/2015.
 */
public class SimpleSoundPlayer {

    public static void main(String[] args) {
        //load the sound
        SimpleSoundPlayer soundPlayer = new SimpleSoundPlayer("sounds/voice.wav");

        //create the stream to play
        InputStream stream = new ByteArrayInputStream(soundPlayer.getSamples());

        //play the sound
        soundPlayer.play(stream);

        //exit(for the bug of java Sound class,must explicitly call the exit)
        System.exit(0);
    }

    private AudioFormat format;
    private byte[] samples;

    /*open a sound from a file*/
    public SimpleSoundPlayer(String filename){
        try {
            //open the audio input stream
            AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filename));
            format = stream.getFormat();

            //get the audio samples
            samples = getSamples(stream);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getSamples(){
        return samples;
    }

    private byte[] getSamples(AudioInputStream audioInputStream){
        int length = (int) (audioInputStream.getFrameLength() * format.getFrameSize());

        //read the entire stream
        byte[] samples = new byte[length];
        DataInputStream inputStream = new DataInputStream(audioInputStream);
        try {
            inputStream.readFully(samples);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return samples;
    }

    public void play(InputStream source){
        //use a short,100ms buffer for real-time change to the sound stream
        int bufferSize = format.getFrameSize() * Math.round(format.getSampleRate() / 10);
        byte[] buffer = new byte[bufferSize];

        //create a line to play to
        SourceDataLine line;
        try {
            DataLine.Info info = new DataLine.Info(SourceDataLine.class,format);
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format,bufferSize);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            return;
        }

        //start the line
        line.start();

        //copy data to the line
        try {
            int numBytesRead = 0;
            while (numBytesRead != -1){
                numBytesRead = source.read(buffer,0,buffer.length);
                if (numBytesRead != -1){
                    line.write(buffer,0,numBytesRead);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //wait until all data is played
        line.drain();

        //close the line
        line.close();
    }
}
