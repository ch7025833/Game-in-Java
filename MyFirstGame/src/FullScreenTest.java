import javax.swing.*;
import java.awt.*;

/**
 * Created by chenhao on 5/12/2015.
 */
public class FullScreenTest extends JFrame{

    public static void main(String[] args) {

        DisplayMode displayMode;

        if (args.length == 3){

            //create the displayMode from the argument in the command line.
            displayMode = new DisplayMode(Integer.parseInt(args[0]),
                    Integer.parseInt(args[1]),
                    Integer.parseInt(args[2]),
                    DisplayMode.REFRESH_RATE_UNKNOWN);


        }else {
            //create the default displayMode.
            displayMode = new DisplayMode(800,600,16,DisplayMode.REFRESH_RATE_UNKNOWN);
        }

        FullScreenTest test = new FullScreenTest();
        test.run(displayMode);
    }

    private static final long DEMO_TIME = 5000;

    public void run(DisplayMode displayMode){
        //set the frame properties.
        setBackground(Color.BLUE);
        setForeground(Color.white);
        setFont(new Font("Dialog",Font.PLAIN,24));

        SimpleScreenManger screenManger = new SimpleScreenManger();
        try {
            screenManger.setFullScreen(displayMode,this);
            try {
                Thread.sleep(DEMO_TIME);
            } catch (InterruptedException e) {
            }
        }finally {
            screenManger.restoreScreen();
        }
    }

    @Override
    public void paint(Graphics graphics) {
        //antialiasing the text,make it not so jagged
        if (graphics instanceof Graphics2D){

            Graphics2D g2d = (Graphics2D) graphics;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                 RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        graphics.drawString("Hello World!",20,50);
    }
}
