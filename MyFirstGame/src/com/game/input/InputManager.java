package com.game.input;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Created by chenhao on 5/13/2015.
 */
public class InputManager implements KeyListener,MouseWheelListener,MouseMotionListener,MouseListener{


    /**
     * An invisible cursor
     */
    public static final Cursor INVISIBLE_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
            Toolkit.getDefaultToolkit().getImage(""),
            new Point(0,0),
            "invisible");

    //mouse code
    public static final int MOUSE_MOVE_LEFT = 0;
    public static final int MOUSE_MOVE_RIGHT = 1;
    public static final int MOUSE_MOVE_UP = 2;
    public static final int MOUSE_MOVE_DOWN = 3;
    public static final int MOUSE_WHEEL_UP = 4;
    public static final int MOUSE_WHEEL_DOWN = 5;
    public static final int MOUSE_MOVE_BUTTON_1 = 6;
    public static final int MOUSE_MOVE_BUTTON_2 = 7;
    public static final int MOUSE_MOVE_BUTTON_3 = 8;

    private static final int NUM_MOUSE_CODES = 9;

    /**
     * Key code are in KeyEvent,most are less than 600.
     */
    private static final int NUM_KEY_CODES = 600;

    private GameAction[] keyActions = new GameAction[NUM_KEY_CODES];
    private GameAction[] mouseActions = new GameAction[NUM_MOUSE_CODES];

    private Point mouseLocation;
    private Point centerLocation;
    private Component component;
    private Robot robot;
    private boolean isRecentering;

    /**
     * create the input manager that listens to the input from the specific component.
     */
    public InputManager(Component component){
        this.component = component;
        mouseLocation = new Point();
        centerLocation = new Point();

        //register the listeners
        component.addKeyListener(this);
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
        component.addMouseWheelListener(this);

        //allow input of the TAB key and other keys normally used for focus traversal(not used for focus traversal)
        component.setFocusTraversalKeysEnabled(false);
    }

    /*set the cursor of the input component*/
    public void setCursor(Cursor cursor){
        component.setCursor(cursor);
    }

    /**
     * sets whether the relative mouse mode is on or not.
     * For relative mouse mode,the mouse is locked in the center,only the mouse motion is measured.
     * For normal mode,the mouse is free to move about the screen.
     */
    public void setRelativeMouseMode(boolean mode){
        if ( mode == isRelativeMouseMode()){
            return;
        }
        if (mode){
            try {
                robot = new Robot();
                recenterMouse();
            } catch (AWTException e) {
                robot = null;
            }
        }else {
            robot = null;
        }
    }

    /*return whether the relative mode is on or not,depend on the robot variable*/
    public boolean isRelativeMouseMode(){
        return (robot != null);
    }

    /**
     * Maps a GameAction to a specific Key.The key codes are defined in KeyEvent.
     * If the key already has a GameAction mapped to it,the new GameAction overwrites it.
     */
    public void mapToKey(GameAction gameAction,int keyCode){
        keyActions[keyCode] = gameAction;
    }

    /**
     * Maps a GameAction to a specific mouse action.The mouse codes are defined in the InputManager.
     * If the mouse action already has a GameAction mapped to it,the new GameAction overwrites it.
     */
    public void mapToMouse(GameAction gameAction,int mouseCode){
        mouseActions[mouseCode] = gameAction;
    }

    /**
     * clear all mapped keys and mouse actions to this GameAction
     */
    public void clearMap(GameAction gameAction){
        for (int i = 0; i < keyActions.length; i++){
            if (keyActions[i] == gameAction){
                keyActions[i] = null;
            }
        }

        for (int i = 0; i < mouseActions.length; i++){
            if (mouseActions[i] == gameAction){
                mouseActions[i] = null;
            }
        }

        gameAction.reset();
    }

    /**
     * get a list of names of the keys and mouse actions mapped to this GameAction.
     * Each entry in the list is a String.
     */
    public java.util.List<String> getMaps(GameAction gameAction){

        ArrayList<String> list = new ArrayList<String>();

        for (int i = 0; i < keyActions.length; i++){
            if (keyActions[i] == gameAction){
               list.add(getKeyName(i));
            }
        }

        for (int i = 0; i < mouseActions.length; i++){
            if (mouseActions[i] == gameAction){
                list.add(getMouseName(i));
            }
        }
        return list;
    }

    /**
     * reset all GameActions so they appear like they haven't been pressed
     */
    public void resetAllGameActions(){
        for (int i = 0; i < keyActions.length; i++){
            if (keyActions[i] != null){
                keyActions[i].reset();
            }
        }

        for (int i = 0; i < mouseActions.length; i++){
            if (mouseActions[i] != null){
                mouseActions[i].reset();
            }
        }
    }

    /*get the name of the keyCode*/
    public static String getKeyName(int keyCode){
        return KeyEvent.getKeyText(keyCode);
    }

    /*get the name of the mouseCode*/
    public static String getMouseName(int mouseCode){
        switch (mouseCode){
            case MOUSE_MOVE_LEFT: return "Mouse Left";
            case MOUSE_MOVE_RIGHT:return "Mouse Right";
            case MOUSE_MOVE_UP:return "Mouse Up";
            case MOUSE_MOVE_DOWN:return "Mouse Down";
            case MOUSE_WHEEL_UP:return "Mouse Wheel Up";
            case MOUSE_WHEEL_DOWN:return "Mouse Wheel Down";
            case MOUSE_MOVE_BUTTON_1:return "Mouse Button 1";
            case MOUSE_MOVE_BUTTON_2:return "Mouse Button 2";
            case MOUSE_MOVE_BUTTON_3:return "Mouse Button 3";
            default:return "Unknown mouse code" + mouseCode;
        }
    }

    /*get the x position of the mouse*/
    public int getMouseX(){
        return mouseLocation.x;
    }

    /*get the y position of the mouse*/
    public int getMouseY(){
        return mouseLocation.y;
    }

    /*use the robot to make the mouse in the center of the screen.
    * Robot may be unavailable in some platform*/
    private synchronized void recenterMouse(){
        if (robot != null && component.isShowing()){
            centerLocation.x = component.getWidth() / 2;
            centerLocation.y = component.getHeight() / 2;
            SwingUtilities.convertPointToScreen(centerLocation, component);
            isRecentering = true;
            robot.mouseMove(centerLocation.x,centerLocation.y);
        }
    }

    private GameAction getKeyAction(KeyEvent event){
        int keyCode = event.getKeyCode();
        if (keyCode < keyActions.length){
            return keyActions[keyCode];
        }else {
            return null;
        }
    }

    /*get the mouse code for the button of the mouseEvent*/
    public static int getMouseButtonCode(MouseEvent event){
        switch (event.getButton()){
            case MOUSE_MOVE_BUTTON_1:
                return MOUSE_MOVE_BUTTON_1;
            case MOUSE_MOVE_BUTTON_2:
                return MOUSE_MOVE_BUTTON_2;
            case MOUSE_MOVE_BUTTON_3:
                return MOUSE_MOVE_BUTTON_3;
            default:
                return -1;
        }
    }

    private GameAction getMouseButtonAction(MouseEvent event){
        int mouseCode = getMouseButtonCode(event);
        if (mouseCode == -1){
            return null;
        }else {
            return mouseActions[mouseCode];
        }
    }



    public void keyTyped(KeyEvent keyEvent) {
        keyEvent.consume();
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        GameAction gameAction = getKeyAction(keyEvent);
        if (gameAction != null){
            gameAction.press();
        }
        //the event is done here
        keyEvent.consume();
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        GameAction gameAction = getKeyAction(keyEvent);
        if (gameAction != null){
            gameAction.release();
        }
        keyEvent.consume();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        //do nothing
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        GameAction gameAction = getMouseButtonAction(mouseEvent);
        if (gameAction != null){
            gameAction.press();
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        GameAction gameAction = getMouseButtonAction(mouseEvent);
        if (gameAction != null){
            gameAction.release();
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        mouseMoved(mouseEvent);
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        mouseMoved(mouseEvent);
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        mouseMoved(mouseEvent);
    }

    @Override
    public synchronized void mouseMoved(MouseEvent mouseEvent) {
        if (isRecentering &&
                centerLocation.x == mouseEvent.getX() &&
                centerLocation.y == mouseEvent.getY()){
            isRecentering = false;
        }else {
            int dx = mouseEvent.getX() - mouseLocation.x;
            int dy = mouseEvent.getY() - mouseLocation.y;
            mouseHelper(MOUSE_MOVE_LEFT,MOUSE_MOVE_RIGHT,dx);
            mouseHelper(MOUSE_WHEEL_UP,MOUSE_MOVE_DOWN,dy);
            if (isRelativeMouseMode()){
                recenterMouse();
            }
        }
        mouseLocation.x = mouseEvent.getX();
        mouseLocation.y = mouseEvent.getY();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        mouseHelper(MOUSE_WHEEL_UP,MOUSE_WHEEL_DOWN,mouseWheelEvent.getWheelRotation());
    }

    private void mouseHelper(int codeNeg,int codePos, int amount){

        GameAction gameAction;
        if (amount < 0){
            gameAction = mouseActions[codeNeg];
        }else {
            gameAction = mouseActions[codePos];
        }
        if (gameAction != null){
            gameAction.press(Math.abs(amount));
            gameAction.release();
        }
    }
}
