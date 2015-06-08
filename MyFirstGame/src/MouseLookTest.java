import com.game.test.GameCore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Created by chenhao on 5/13/15.
 */
public class MouseLookTest extends GameCore implements MouseMotionListener,KeyListener {

    public static void main(String[] args) {
        new MouseLookTest().run();
    }

    private Image bgImage;
    private Robot robot;
    private Point mouseLocation;
    private Point centerLocation;
    private Point imageLocation;
    private boolean relativeMouseMode;
    private boolean isRecentering;

    @Override
    public void init() {
        super.init();
        mouseLocation = new Point();
        centerLocation = new Point();
        imageLocation = new Point();
        relativeMouseMode = true;
        isRecentering = false;

        try {
            robot = new Robot();
            recenterMouse();
            mouseLocation.x = centerLocation.x;
            mouseLocation.y = centerLocation.y;
        } catch (AWTException e) {
            System.out.println("Couldn't create robots!");
        }

        Window window = screenManger.getFullScreenWindow();

        Cursor invisibleCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                Toolkit.getDefaultToolkit().createImage(""),
                new Point(0,0),
                "invisible"
        );
        window.setCursor(invisibleCursor);
        window.addMouseMotionListener(this);
        window.addKeyListener(this);
        bgImage = loadImage("image/background.jpg");
    }

    @Override
    public synchronized void draw(Graphics2D graphics2D) {
        int width = screenManger.getWidth();
        int height = screenManger.getHeight();

        //make sure the position is true;
        imageLocation.x %= width;
        imageLocation.y %= height;
        if (imageLocation.x < 0){
            imageLocation.x += width;
        }
        if (imageLocation.y < 0){
            imageLocation.y += height;
        }

        //draw the image in four places to cover the screen
        int x = imageLocation.x;
        int y = imageLocation.y;
        graphics2D.drawImage(bgImage,x,y,null);
        graphics2D.drawImage(bgImage,x - width,y, null);
        graphics2D.drawImage(bgImage,x,y - height, null);
        graphics2D.drawImage(bgImage,x - width, y - height,null);

        //draw the instructions
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.drawString("Press Space to change mouse modes", 5 ,FONT_SIZE);
        graphics2D.drawString("Press escape to exit the game", 5, FONT_SIZE * 2);
    }


    /*use the robot to recenter the mouse to center
    * may be unavailable to some systems.*/
    private synchronized void recenterMouse(){

        Window window =  screenManger.getFullScreenWindow();
        if (robot != null && window.isShowing()){
            centerLocation.x = window.getWidth() / 2;
            centerLocation.y = window.getHeight() / 2;
            SwingUtilities.convertPointToScreen(centerLocation,window);
            isRecentering = true;
            robot.mouseMove(centerLocation.x,centerLocation.y);
        }

    }



    @Override
    public void keyTyped(KeyEvent keyEvent) {
        //do nothing
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE){
            stop();
        }else if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE){
            relativeMouseMode = !relativeMouseMode;
        }


    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        //do nothing
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        mouseMoved(mouseEvent);
    }

    @Override
    public synchronized void mouseMoved(MouseEvent mouseEvent) {
        if (isRecentering && centerLocation.x == mouseEvent.getX() && centerLocation.y == mouseEvent.getY()){
            isRecentering = false;
        }else {
            //keep the relative mouse movement
            int dx = mouseEvent.getX() - mouseLocation.x;
            int dy = mouseEvent.getY() - mouseLocation.y;
            imageLocation.x += dx;
            imageLocation.y += dy;

            //recenter the mouse
            if (relativeMouseMode){
                recenterMouse();
            }
        }
        mouseLocation.x = mouseEvent.getX();
        mouseLocation.y = mouseEvent.getY();
    }


}
