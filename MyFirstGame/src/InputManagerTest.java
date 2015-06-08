import com.game.input.GameAction;
import com.game.input.InputManager;
import com.game.reusable.graphics.Animation;
import com.game.reusable.graphics.Player;
import com.game.test.GameCore;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by chenhao on 5/14/2015.
 */
public class InputManagerTest extends GameCore {

    public static void main(String[] args) {
        new InputManagerTest().run();
    }

    protected GameAction jump;
    protected GameAction exit;
    protected GameAction moveLeft;
    protected GameAction moveRight;
    protected GameAction pause;
    protected InputManager inputManager;
    private Player player;
    private Image bgImage;
    private boolean paused;

    @Override
    public void init() {
        super.init();
        Window window = screenManger.getFullScreenWindow();
        inputManager = new InputManager(window);

        //use these lines for relative mouse mode
        //inputManager.setRelativeMouseMode(true);
        //inputManager.setCursor(InputManager.INVISIBLE_CURSOR);

        createGameActions();
        createSprite();
        paused = false;
    }

    public boolean isPaused(){
        return paused;
    }

    public void setPaused(boolean p){
        if (paused != p){
            paused = p;
            inputManager.resetAllGameActions();
        }
    }

    @Override
    public void update(long elapsedTime) {

        checkSystemInput();

        //check paused state
        if (!paused){
            //check game input
            checkGameInput();
            //update sprite
            player.update(elapsedTime);
        }
    }

    /*check input from GameActions that can be pressed regardless of whether the game is paused or not*/
    public void checkSystemInput(){
        if (pause.isPressed()){
            setPaused(!isPaused());
        }
        if (exit.isPressed()){
            stop();
        }
    }

    /*check input from GameActions that can be pressed only when the game is not paused*/
    public void checkGameInput(){
        float velocityX = 0;
        if (moveLeft.isPressed()){
            velocityX -= Player.SPEED;
        }
        if (moveRight.isPressed()){
            velocityX += Player.SPEED;
        }
        player.setVelocityX(velocityX);

        if (jump.isPressed() && player.getState() != Player.STATE_JUMPING){
            player.jump();
        }
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        //draw the background
        graphics2D.drawImage(bgImage,0,0,null);

        //draw sprite
        graphics2D.drawImage(player.getImage(),Math.round(player.getX()),Math.round(player.getY()),null);
    }

    /*create gameActions and map them to keys*/
    public void createGameActions(){
        jump = new GameAction("jump",GameAction.DETECT_INITIAL_PRESS_ONLY);
        exit = new GameAction("exit",GameAction.DETECT_INITIAL_PRESS_ONLY);
        moveLeft = new GameAction("moveLeft");
        moveRight = new GameAction("moveRight");
        pause = new GameAction("pause",GameAction.DETECT_INITIAL_PRESS_ONLY);

        inputManager.mapToKey(exit, KeyEvent.VK_ESCAPE);
        inputManager.mapToKey(pause,KeyEvent.VK_P);

        //jump with space or mouse button
        inputManager.mapToKey(jump,KeyEvent.VK_SPACE);
        inputManager.mapToMouse(jump, InputManager.MOUSE_MOVE_BUTTON_1);

        //move with the arrow keys
        inputManager.mapToKey(moveLeft,KeyEvent.VK_LEFT);
        inputManager.mapToKey(moveRight,KeyEvent.VK_RIGHT);

        //or with A and D
        inputManager.mapToKey(moveLeft,KeyEvent.VK_A);
        inputManager.mapToKey(moveRight,KeyEvent.VK_D);

        //use these lines to map player movement to the mouse
        /*inputManager.mapToMouse(moveLeft,InputManager.MOUSE_MOVE_LEFT);
        inputManager.mapToMouse(moveRight,InputManager.MOUSE_MOVE_RIGHT);*/

    }

    /*load images and creates the Play sprite*/
    private void createSprite(){

        bgImage = loadImage("image/background.jpg");
        Image player1 = loadImage("image/player1.png");
        Image player2 = loadImage("image/player2.png");
        Image player3 = loadImage("image/player3.png");

        //create animation
        Animation animation = new Animation();
        animation.addFrames(player1,250);
        animation.addFrames(player2,150);
        animation.addFrames(player1,150);
        animation.addFrames(player2,150);
        animation.addFrames(player3,200);
        animation.addFrames(player2,150);

        player = new Player(animation);
        player.setFloorY(screenManger.getHeight() - player.getHeight());
    }
}
