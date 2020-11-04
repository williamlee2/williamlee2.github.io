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
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A) {
            lvl.samus.endMove(1);
        }

        if (key == KeyEvent.VK_D) {
            lvl.samus.endMove(1);
        }

        if (key == KeyEvent.VK_S) {
            lvl.samus.endCrouch();
        }

        if (key == KeyEvent.VK_W) {
            lvl.samus.swapWeapon();
        }
    }

    public void keyPressed(KeyEvent e) 
    {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            lvl.cameraX += -lvl.tileWidth;
            lvl.camera.setLocation(lvl.cameraX, lvl.cameraY);
        }

        if (key == KeyEvent.VK_RIGHT) {
            lvl.cameraX += lvl.tileWidth;
            lvl.camera.setLocation(lvl.cameraX, lvl.cameraY);
        }

        if (key == KeyEvent.VK_UP) {
            lvl.cameraY += -lvl.tileHeight;
            lvl.camera.setLocation(lvl.cameraX, lvl.cameraY);
        }

        if (key == KeyEvent.VK_DOWN) {
            lvl.cameraY += lvl.tileHeight;
            lvl.camera.setLocation(lvl.cameraX, lvl.cameraY);
        }

        if (key == KeyEvent.VK_SPACE) {
            lvl.samus.move(-1, true);
        }

        if (key == KeyEvent.VK_A) {
            lvl.samus.move(-1, false);
        }

        if (key == KeyEvent.VK_S) {
            lvl.samus.startCrouch();
        }

        if (key == KeyEvent.VK_D) {
            lvl.samus.move(1, false);
        }

        // change to trigger on mouse left click
        if (key == KeyEvent.VK_K)
        {
            lvl.samus.shoot();
        }
    }
}