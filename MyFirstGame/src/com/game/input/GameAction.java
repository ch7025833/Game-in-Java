package com.game.input;

/**
 * Created by chenhao on 5/13/2015.
 */
public class GameAction {
    /*abstract for the game actions*/

    public static final int NORMAL = 0;
    public static final int DETECT_INITIAL_PRESS_ONLY = 1;

    private static final int STATE_RELEASED = 0;
    private static final int STATE_PRESSED = 1;
    private static final int STATE_WAITING_FOR_RELEASE = 2;

    private String name;
    private int behavior;
    private int amount;
    private int state;

    /*create the new GameAction with the normal behavior*/
    public GameAction(String name){
        this(name,NORMAL);
    }

    /*create the game action with the specific behavior*/
    public GameAction(String name,int behavior){
        this.name = name;
        this.behavior = behavior;
        reset();
    }

    /*get the name of the action*/
    public String getName(){
        return name;
    }

    /*resets this GameAction so that it appears like it hasn't been pressed*/
    public void reset(){
        //reset the state.
        state = STATE_RELEASED;
        amount = 0;
    }

    /*tap the gameAction,pressed and released*/
    public synchronized void tap(){
        press();
        release();
    }

    /*signals that the key has been pressed*/
    public synchronized void press(){
        press(1);
    }

    /*signals that the key was pressed a specified number of times,or that the mouse moved a specified distance*/
    public synchronized void press(int amount){
        if (state != STATE_WAITING_FOR_RELEASE){
            this.amount += amount;
            state = STATE_PRESSED;
        }
    }

    /*signals that the key is released*/
    public synchronized void release(){
        state = STATE_RELEASED;
    }

    /*returns whether the key was pressed or not since last check*/
    public synchronized boolean isPressed(){
        return (getAmount() != 0);
    }

    /**
     * For keys,this is the number of times the key was pressed since it was last checked.
     * For mouse movement,this is the distance of the mouse movement.
     */
    public synchronized int getAmount(){
        int retVal = amount;
        if (retVal != 0){
            if (state == STATE_RELEASED){
                amount = 0;
            }else if (behavior == DETECT_INITIAL_PRESS_ONLY){
                state = STATE_WAITING_FOR_RELEASE;
                amount = 0;
            }
        }
        return retVal;
    }


}
