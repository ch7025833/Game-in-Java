import com.game.test.GameCore;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.renderable.RenderableImage;
import java.util.LinkedList;

/**
 * Created by chenhao on 5/13/2015.
 */
public class MouseTest extends GameCore implements MouseListener,KeyListener,MouseMotionListener,MouseWheelListener{

    public static void main(String[] args) {
        new MouseTest().run();
    }

    private static final int TRAIL_SIZE = 10;
    private static final Color[] COLORS = {Color.white,Color.black,Color.yellow,Color.magenta};

    private LinkedList trailList;
    private boolean trailMode;
    private int colorIndex;

    @Override
    public void init() {
        super.init();
        trailList = new LinkedList();

        Window window = screenManger.getFullScreenWindow();
        window.addMouseListener(this);
        window.addMouseMotionListener(this);
        window.addMouseWheelListener(this);
        window.addKeyListener(this);
    }

    @Override
    public synchronized void draw(Graphics2D graphics2D) {

        int count = trailList.size();

        if (count > 1 && !trailMode){
            count = 1;
        }

        Window window = screenManger.getFullScreenWindow();

        //draw background
        graphics2D.setColor(window.getBackground());
        graphics2D.fillRect(0,0, screenManger.getWidth(),screenManger.getHeight());

        //draw the instructions
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setColor(window.getForeground());
        graphics2D.drawString("Mouse test. Press Escape to exit",5,FONT_SIZE);

        //draw mouse trail
        for (int i=0; i < count; i++){
            Point point =(Point)trailList.get(i);
            graphics2D.drawString("Hello World!",point.x,point.y);
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        //called after mouse is released,ignore it
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        trailMode = !trailMode;
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        //nothing
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
    public void keyTyped(KeyEvent keyEvent) {
        //do nothing
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE){
            stop();
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
        Point point = new Point(mouseEvent.getX(),mouseEvent.getY());
        trailList.addFirst(point);
        while (trailList.size() > TRAIL_SIZE){
            trailList.removeLast();
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        colorIndex = (colorIndex + mouseWheelEvent.getWheelRotation()) % COLORS.length;

        if (colorIndex < 0 ){
            colorIndex += COLORS.length;
        }
        Window window = screenManger.getFullScreenWindow();
        window.setForeground(COLORS[colorIndex]);
    }
}
