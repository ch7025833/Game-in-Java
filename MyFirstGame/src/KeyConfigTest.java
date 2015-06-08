import com.game.input.GameAction;
import com.game.input.InputManager;
import org.jcp.xml.dsig.internal.MacOutputStream;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhao on 5/15/2015.
 */
public class KeyConfigTest extends MenuTest {

    public static void main(String[] args) {
        new KeyConfigTest().run();
    }

    private static final String INSTRUCTIONS = "<html>Click an action's input box to change its keys."
            +"<br>An action can have at most three keys associated "
            +"with it.<br>Press Backspace to clear an action's key.";

    private JPanel dialog;
    private JButton okButton;
    private List<InputComponent> inputs;

    @Override
    public void init() {
        super.init();

        inputs = new ArrayList();

        //create list of GameActions and mapped keys
        JPanel configPanel = new JPanel(new GridLayout(5,2,2,2));
        addActionConfig(configPanel,moveLeft);
        addActionConfig(configPanel,moveRight);
        addActionConfig(configPanel,jump);
        addActionConfig(configPanel,pause);
        addActionConfig(configPanel,exit);

        //create the panel containing the OK button
        JPanel bottomPanel = new JPanel(new FlowLayout());
        okButton = new JButton("OK");
        okButton.setFocusable(false);
        okButton.addActionListener(this);
        bottomPanel.add(okButton);

        //create the panel containing the instructions.
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel(INSTRUCTIONS));

        //create the dialog border
        Border border = BorderFactory.createLineBorder(Color.black);

        //create the config dialog.
        dialog = new JPanel(new BorderLayout());
        dialog.add(topPanel,BorderLayout.NORTH);
        dialog.add(configPanel,BorderLayout.CENTER);
        dialog.add(bottomPanel,BorderLayout.SOUTH);
        dialog.setBorder(border);
        dialog.setVisible(false);
        dialog.setSize(dialog.getPreferredSize());

        //center the dialog
        dialog.setLocation(
                (screenManger.getWidth() - dialog.getWidth()) / 2,
                (screenManger.getHeight() - dialog.getHeight()) / 2
        );

        //add the dialog to the "modal dialog" layer of the screen's layered panel.
        ((JFrame)screenManger.getFullScreenWindow()).getLayeredPane().add(dialog,JLayeredPane.MODAL_LAYER);
    }

    private void addActionConfig(JPanel configPanel,GameAction action){
        JLabel label = new JLabel(action.getName(),JLabel.RIGHT);
        InputComponent input = new InputComponent(action);
        configPanel.add(label);
        configPanel.add(input);
        inputs.add(input);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        super.actionPerformed(actionEvent);
        if (actionEvent.getSource() == okButton){
            //hides the config dialog
            configAction.tap();
        }
    }

    @Override
    public void checkSystemInput() {
        super.checkSystemInput();
        if (configAction.isPressed()){
            //hide or show the config dialog
            boolean show = !dialog.isVisible();
            dialog.setVisible(show);
            setPaused(show);
        }
    }

    private void resetInputs(){
        for (int i = 0; i < inputs.size(); i++){
            ((InputComponent)inputs.get(i)).setText();
        }
    }

    class InputComponent extends JTextField{

        private GameAction action;

        public InputComponent(GameAction action){
            this.action = action;
            setText();
            enableEvents(KeyEvent.KEY_EVENT_MASK |
                    MouseEvent.MOUSE_EVENT_MASK |
                    MouseEvent.MOUSE_MOTION_EVENT_MASK |
                    MouseEvent.MOUSE_WHEEL_EVENT_MASK);
        }

        private void setText(){
            String text = "";
            List list = inputManager.getMaps(action);
            if (list.size() > 0){
                for (int i = 0; i < list.size(); i++){
                    text += list.get(i) + ", ";
                }
                //remove the last comma
                text = text.substring(0,text.length() - 2);
            }

            //make sure we don't get deadlock
            synchronized (getTreeLock()){
                setText(text);
            }
        }

        private void mapGameAction(int code,boolean isMouseMap){
            if (inputManager.getMaps(action).size() >= 3){
                inputManager.clearMap(action);
            }
            if (isMouseMap){
                inputManager.mapToMouse(action,code);
            }else {
                inputManager.mapToKey(action,code);
            }
            resetInputs();
            screenManger.getFullScreenWindow().requestFocus();
        }

        //alternative way to intercept key events
        @Override
        protected void processKeyEvent (KeyEvent event){
            if (event.getID() == event.KEY_PRESSED){
                //if backspace is pressed,clear the map
                if (event.getKeyCode() == KeyEvent.VK_BACK_SPACE && inputManager.getMaps(action).size() > 0){
                    inputManager.clearMap(action);
                    setText("");
                    screenManger.getFullScreenWindow().requestFocus();
                }else {
                    mapGameAction(event.getKeyCode(), false);
                }
            }
            event.consume();
        }

        //alternative way to intercept the mouse events
        @Override
        protected void processMouseEvent(MouseEvent event){
            if (event.getID() == event.MOUSE_PRESSED) {
                if (hasFocus()){
                    int code = InputManager.getMouseButtonCode(event);
                    mapGameAction(code,true);
                }else {
                    requestFocus();
                }
            }
            event.consume();
        }

        //alternative way to intercept the mouse motion event
        @Override
        protected void processMouseMotionEvent(MouseEvent event){
            event.consume();
        }

        @Override
        protected void processMouseWheelEvent(MouseWheelEvent event){
            if (hasFocus()){
                int code = InputManager.MOUSE_MOVE_DOWN;
                if (event.getWheelRotation() < 0){
                    code = InputManager.MOUSE_WHEEL_UP;
                }
                mapGameAction(code,true);
            }
            event.consume();
        }

    }
}
