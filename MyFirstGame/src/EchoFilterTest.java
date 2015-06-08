import com.game.sound.EchoFilter;
import com.game.sound.FilteredSoundStream;
import com.game.sound.SimpleSoundPlayer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by chenhao on 5/16/2015.
 */
public class EchoFilterTest {

    public static void main(String[] args) {
        SimpleSoundPlayer soundPlayer = new SimpleSoundPlayer("sounds/voice.wav");

        InputStream inputStream = new ByteArrayInputStream(soundPlayer.getSamples());

        //create an echo with a 11025-sample buffer(1/4 sec for 44100HZ sound) and a 60% decay
        EchoFilter filter = new EchoFilter(11025,0.6f);

        inputStream = new FilteredSoundStream(inputStream,filter);

        soundPlayer.play(inputStream);

        System.exit(0);
    }
}
