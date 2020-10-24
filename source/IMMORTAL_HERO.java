import modules.GameWindow;
import modules.Level;
import modules.Controller;
import java.awt.Toolkit;
import java.awt.Dimension;

class Game 
{
    public static void main(String args[])
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Level lvl = new Level(screenSize.width, screenSize.height);
        Controller input = new Controller(lvl);
        GameWindow app = new GameWindow(lvl);
    }
}