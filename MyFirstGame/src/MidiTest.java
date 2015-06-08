import com.game.sound.MidiPlayer;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

/**
 * Created by chenhao on 5/16/2015.
 */
public class MidiTest implements MetaEventListener {

    private static final int DRUM_TRACK = 1;

    public static void main(String[] args) {
        new MidiTest().run();
    }

    private MidiPlayer player;

    public void run(){

        player = new MidiPlayer();

        //load a sequence
        Sequence sequence = player.getSequence("sounds/music.midi");

        //play the sequence
        player.play(sequence,true);

        //turn off the drums
        System.out.println("Playing (without drums)...");
        Sequencer sequencer = player.getSequencer();
        sequencer.setTrackMute(DRUM_TRACK,true);
        sequencer.addMetaEventListener(this);

    }

    @Override
    public void meta(MetaMessage metaMessage) {
        if (metaMessage.getType() == MidiPlayer.END_OF_TRACK_MESSAGE){
            Sequencer sequencer = player.getSequencer();
            if (sequencer.getTrackMute(DRUM_TRACK)){
                //turn on the drum track
                System.out.println("Turning on drums...");
                sequencer.setTrackMute(DRUM_TRACK,false);
            }else {
                //close the sequencer and exit
                System.out.println("Exiting...");
                player.close();
                System.exit(0);
            }
        }
    }
}
