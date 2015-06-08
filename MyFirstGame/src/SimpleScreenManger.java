import javax.swing.*;
import java.awt.*;

/**
 * Created by chenhao on 5/12/2015.
 */
public class SimpleScreenManger {

    /*
    * manges initialization about the screen display and full-screen.
    *
    * */

    private GraphicsDevice device;

    public SimpleScreenManger(){
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = environment.getDefaultScreenDevice();
    }

    /*
    enter the full-screen and change the display mode
    * */
    public void setFullScreen(DisplayMode displayMode,JFrame window){

        window.setUndecorated(true);
        window.setResizable(false);

        device.setFullScreenWindow(window);
        if (displayMode != null && device.isDisplayChangeSupported()){
            try {
                device.setDisplayMode(displayMode);
            }catch (IllegalArgumentException ex){
                //ignore
            }
        }
    }

    /*
    * return the window current used
    * */
    public Window getFullScreenWindow(){
        return device.getFullScreenWindow();
    }

    /*
    * restore the screen's display mode*/
    public void restoreScreen(){
        Window window = device.getFullScreenWindow();
        if (window != null){
            //release all the resources used by this window.
            window.dispose();
        }
        try {
            device.setFullScreenWindow(null);
        }catch (IllegalArgumentException e){

        }

    }

 }
