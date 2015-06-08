import javax.swing.*;
import java.awt.*;

/**
 * Created by chenhao on 5/12/2015.
 */
public class ImageTest extends JFrame {

    public static void main(String[] args) {

        DisplayMode displayMode;

        if (args.length == 3){

            displayMode = new DisplayMode(Integer.parseInt(args[0]),
                    Integer.parseInt(args[1]),
                    Integer.parseInt(args[2]),
                    DisplayMode.REFRESH_RATE_UNKNOWN);
        }else {
            displayMode = new DisplayMode(800,600,16,DisplayMode.REFRESH_RATE_UNKNOWN);
        }

        ImageTest imageTest = new ImageTest();
        imageTest.run(displayMode);
    }

    private static final int FONT_SIZE = 24;
    private static final long DEMO_TIME = 10000;

    private SimpleScreenManger screenManger;
    private Image bgImage;
    private Image opaqueImage;
    private Image transparentImage;
    private Image translucentImage;
    private Image antiAliasedImage;
    private boolean imagesLoaded;

    public void run(DisplayMode displayMode){

        setBackground(Color.blue);
        setForeground(Color.white);
        setFont(new Font("Dialog", Font.PLAIN,FONT_SIZE));
        imagesLoaded = false;

        screenManger = new SimpleScreenManger();

        try{
            screenManger.setFullScreen(displayMode,this);
            LoadImages();
            try{
                Thread.sleep(DEMO_TIME);
            }catch (InterruptedException e) {
            }
        }finally {
            screenManger.restoreScreen();
        }
    }

    public void LoadImages(){

        bgImage = loadImage("image/background.jpg");
        opaqueImage = loadImage("image/opaque.png");
        transparentImage = loadImage("image/transparent.png");
        translucentImage = loadImage("image/translucent.png");
        antiAliasedImage = loadImage("image/antialiased.png");
        imagesLoaded = true;
        //signal to AWT to repaint this window.
        repaint();
    }

    private Image loadImage(String path){
        return new ImageIcon(path).getImage();
    }

    @Override
    public void paint(Graphics graphics) {
        //set text anti-aliasing
        if (graphics instanceof Graphics2D){
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                 RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        //draw the images.
        if (imagesLoaded){

            graphics.drawImage(bgImage,0,0,null);
            drawImage(graphics,opaqueImage,0,0,"Opaque");
            drawImage(graphics,transparentImage,320,0,"Transparent");
            drawImage(graphics,translucentImage,0,300,"Translucent");
            drawImage(graphics,antiAliasedImage,320,300,"Translucent (Anti aliased)");
        }
        else {
            graphics.drawString("Loading Image",5,FONT_SIZE);
        }
    }

    public void drawImage(Graphics graphics,Image image,int x,int y, String caption){

        graphics.drawImage(image,x,y,null);
        graphics.drawString(caption,x+5,y+FONT_SIZE+image.getHeight(null));
    }
}
