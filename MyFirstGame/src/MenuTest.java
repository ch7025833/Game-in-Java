import com.game.input.GameAction;
import com.game.reusable.graphics.NullRepaintManager;

import javax.swing.*;
import javax.swing.text.html.ObjectView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by chenhao on 5/15/2015.
 */
public class MenuTest extends InputManagerTest implements ActionListener{

    public static void main(String[] args) {
        new MenuTest().run();
    }

    protected GameAction configAction;

    private JButton playButton;
    private JButton configButton;
    private JButton quitButton;
    private JButton pauseButton;
    private JPanel playButtonSpace;

    @Override
    public void init() {
        super.init();
        //make sure the Swing components don't paint themselves.
        NullRepaintManager.install();

        //create an additional GameAction for "config"
        configAction = new GameAction("config");

        //create buttons;
        quitButton = createButton("quit", "Quit");
        playButton = createButton("play", "Continue");
        pauseButton = createButton("pause","Pause");
        configButton = createButton("config", "Change Settings");

        //create the space where the play/pause buttons go.
        playButtonSpace = new JPanel();
        playButtonSpace.setOpaque(false);
        playButtonSpace.add(pauseButton);

        JFrame frame = (JFrame) super.screenManger.getFullScreenWindow();
        Container contentPane = frame.getContentPane();

        //make sure the contentPane is transparent.
        if (contentPane instanceof JComponent){
            ((JComponent)contentPane).setOpaque(false);
        }

        //add components to the screen's content pane.
        contentPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        contentPane.add(playButtonSpace);
        contentPane.add(configButton);
        contentPane.add(quitButton);

        //explicitly lay out components
        frame.validate();

    }

    @Override
    public void draw(Graphics2D graphics2D) {
        super.draw(graphics2D);
        JFrame frame = (JFrame) super.screenManger.getFullScreenWindow();
        //the layered pane contains things like popups(tooltips,popup menu) and the content pane.
        frame.getLayeredPane().paintComponents(graphics2D);
    }

    @Override
    public void setPaused(boolean p) {
        super.setPaused(p);
        playButtonSpace.removeAll();
        if (isPaused()){
            playButtonSpace.add(playButton);
        }else {
            playButtonSpace.add(pauseButton);
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object src = actionEvent.getSource();
        if (src == quitButton){
            //fire the exit gameAction
            super.exit.tap();
        }else if (src == configButton){
            configAction.tap();
        }else if (src == playButton || src == pauseButton){
            //fire the pause gameAction
            super.pause.tap();
        }
    }

    public JButton createButton(String name, String toolTip){
        //load the image
        String imagePath = "image/menu/" + name +".png";
        ImageIcon iconRollover = new ImageIcon(imagePath);
        int w = iconRollover.getIconWidth();
        int h = iconRollover.getIconHeight();

        //get the cursor for this button
        Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

        //make translucent default image
        Image image = screenManger.createCompatibleImage(w,h,Transparency.TRANSLUCENT);

        Graphics2D graphics2D = (Graphics2D)image.getGraphics();
        Composite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        graphics2D.setComposite(alpha);
        graphics2D.drawImage(iconRollover.getImage(),0,0,null);
        graphics2D.dispose();
        ImageIcon iconDefault = new ImageIcon(image);

        //make a pressed image
        image = screenManger.createCompatibleImage(w,h,Transparency.TRANSLUCENT);
        graphics2D = (Graphics2D)image.getGraphics();
        graphics2D.drawImage(iconRollover.getImage(),2,2,null);
        graphics2D.dispose();
        ImageIcon iconPressed = new ImageIcon(image);

        //create the button;
        JButton button = new JButton();
        button.addActionListener(this);
        button.setIgnoreRepaint(true);
        button.setFocusable(false);
        button.setToolTipText(toolTip);
        button.setBorder(null);
        button.setContentAreaFilled(false);
        button.setCursor(cursor);
        button.setIcon(iconDefault);
        button.setRolloverIcon(iconRollover);
        button.setPressedIcon(iconPressed);

        return button;
    }
}
