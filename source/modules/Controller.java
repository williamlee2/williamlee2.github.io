package modules;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controller implements KeyListener
{
    Level lvl;

    public Controller(Level gameLevel)
    {
        lvl = gameLevel;
        // respond to input
        gameLevel.addKeyListener(this);
        // allow canvas to receive input
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
            lvl.camera.setLocation(lvl.cameraX, lvl.cameraY);
        }

        if (key == KeyEvent.VK_RIGHT) {
            lvl.cameraX += 100;
            lvl.camera.setLocation(lvl.cameraX, lvl.cameraY);
        }

        if (key == KeyEvent.VK_UP) {
            lvl.cameraY += -100;
            lvl.camera.setLocation(lvl.cameraX, lvl.cameraY);
        }

        if (key == KeyEvent.VK_DOWN) {
            lvl.cameraY += 100;
            lvl.camera.setLocation(lvl.cameraX, lvl.cameraY);
        }

        if (key == KeyEvent.VK_W) {
            lvl.samus.move(-1, true);
        }

        if (key == KeyEvent.VK_A) {
            lvl.samus.move(-1, false);
        }

        if (key == KeyEvent.VK_S) {
            lvl.samus.move(1, true);
        }

        if (key == KeyEvent.VK_D) {
            lvl.samus.move(1, false);
        }
    }
}