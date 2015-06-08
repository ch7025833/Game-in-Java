package com.game.sound;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by chenhao on 5/16/2015.
 */
public class MidiPlayer implements MetaEventListener{

    public static final int END_OF_TRACK_MESSAGE = 47;

    private Sequencer sequencer;
    private boolean loop;
    private boolean paused;

    public MidiPlayer(){
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequencer.addMetaEventListener(this);
        } catch (MidiUnavailableException e) {
            sequencer = null;
        }
    }

    public Sequence getSequence(String fileName){
        try {
            return MidiSystem.getSequence(new File(fileName));
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void play(Sequence sequence,boolean loop){
        if (sequencer != null && sequence != null){
            try {
                sequencer.setSequence(sequence);
                sequencer.start();
                this.loop = loop;
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void meta(MetaMessage metaMessage) {
        if (metaMessage.getType() == END_OF_TRACK_MESSAGE){
            if (sequencer != null && sequencer.isOpen() && loop){
                sequencer.setTickPosition(0);
                sequencer.start();
            }
        }

    }

    public void stop(){
        if (sequencer != null && sequencer.isOpen()){
            sequencer.stop();
            sequencer.setMicrosecondPosition(0);
        }
    }

    public void close(){
        if (sequencer != null && sequencer.isOpen()){
            sequencer.close();
        }
    }

    public Sequencer getSequencer(){
        return sequencer;
    }

    /*set the paused state,music may not immediately pause*/
    public void setPaused(boolean paused){
        if (this.paused != paused && sequencer != null){
            this.paused = paused;
            if (paused){
                sequencer.stop();
            }else {
                sequencer.start();
            }
        }
    }

    public boolean isPaused(){
        return paused;
    }
}
