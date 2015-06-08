import com.game.test.GameCore;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

/**
 * Created by chenhao on 5/13/2015.
 */
public class KeyTest extends GameCore implements KeyListener {

    public static void main(String[] args) {
        new KeyTest().run();
    }

    private LinkedList<String> messages = new LinkedList<String>();

    @Override
    public void init() {
        super.init();

        Window window = screenManger.getFullScreenWindow();

        //allow TAB and other keys normally ,not for focus traversal.
        window.setFocusTraversalKeysEnabled(false);

        window.addKeyListener(this);

        addMessage("KeyInputTest. Press Escape to Exit.");
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        //draw the list of messages
        Window window = screenManger.getFullScreenWindow();

        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        //draw the background.
        graphics2D.setColor(window.getBackground());
        graphics2D.fillRect(0,0,screenManger.getWidth(),screenManger.getHeight());

        //draw the messages
        graphics2D.setColor(window.getForeground());
        int y = FONT_SIZE;
        for (int i = 0; i < messages.size(); i++){
            graphics2D.drawString(messages.get(i),5,y);
            y += FONT_SIZE;
        }

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

        //this is called after the released method,ignore this here,and consume the event.
        keyEvent.consume();
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

        int keyCode = keyEvent.getKeyCode();

        //exit the program
        if (keyCode == KeyEvent.VK_ESCAPE){
            stop();
        }else {
            addMessage("Pressed: " + keyEvent.getKeyText(keyCode));
            //make sure the key isn't processed for anything else.The event is consumed here.For example ALT + F is disabled here.Otherwise the F will be ignored because the function of ALT.
            keyEvent.consume();
        }

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

        int keyCode = keyEvent.getKeyCode();
        addMessage("Released: " + keyEvent.getKeyText(keyCode));

        keyEvent.consume();
    }

    public synchronized void addMessage(String message){

        messages.add(message);
        if (messages.size() >= screenManger.getHeight() / FONT_SIZE){
            messages.remove(0);
        }
    }

}
