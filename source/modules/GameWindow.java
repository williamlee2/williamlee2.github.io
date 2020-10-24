package modules;

import javax.swing.JFrame;

public class GameWindow 
{
    public GameWindow(Level lvl)
    {       
        JFrame frame = new JFrame("Immortal Hero");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.add(lvl);
        frame.setVisible(true);

        lvl.start();
    }

}