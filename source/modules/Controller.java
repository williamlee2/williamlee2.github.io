package modules;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controller implements KeyListener
{
    Level lvl;

    public Controller(Level gameLevel)
    {
        lvl = gameLevel;
        gameLevel.addKeyListener(this);
        gameLevel.setFocusable(true);
    }

    public void keyTyped(KeyEvent e) 
    {
       
    }

    public void keyReleased(KeyEvent e) 
    {
        
    }

    public void keyPressed(KeyEvent e) 
    {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            lvl.cameraX += -100;
            lvl.repaint();
        }

        if (key == KeyEvent.VK_RIGHT) {
            lvl.cameraX += 100;
            lvl.repaint();
        }

        if (key == KeyEvent.VK_UP) {
            lvl.cameraY += -100;
            lvl.repaint();
        }

        if (key == KeyEvent.VK_DOWN) {
            lvl.cameraY += 100;
            lvl.repaint();
        }
    }
}