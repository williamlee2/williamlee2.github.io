package modules;

import javax.swing.JFrame;
import java.awt.Toolkit;
import java.awt.Dimension;

public class GameWindow 
{
    Level lvl;

    public GameWindow()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        lvl = new Level(screenSize.width, screenSize.height);
        Controller input = new Controller(lvl);
        
        JFrame frame = new JFrame("Immortal Hero");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.add(lvl);
        frame.setVisible(true);

        lvl.start();
    }

}