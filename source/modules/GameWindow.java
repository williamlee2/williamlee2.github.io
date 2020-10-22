package modules;

import java.awt.Rectangle;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

class Level extends Canvas
{
    ArrayList<Entity> entities = new ArrayList<Entity>();
    final int spriteLength = 32;
    final int spriteWidth = 32;
    int screenWidth;
    int screenHeight;
    Rectangle camera;
    int cameraX = 0;
    int cameraY = 0;

    public Level(int width, int height)
    {
        setFocusable(true);
        camera = new Rectangle(0, 0, width, height);
        screenWidth = width;
        screenHeight = height;
        setSize(width, height);
        int x = ThreadLocalRandom.current().nextInt(100, screenWidth - 100);
        int y = ThreadLocalRandom.current().nextInt(100, screenHeight - 100);
        Entity hero = new Entity(x, y, spriteLength, spriteWidth, Color.BLUE);
        entities.add(hero);
        for (int i = 0; i < 10; i++)
        {
            x = ThreadLocalRandom.current().nextInt(100, screenWidth - 100);
            y = ThreadLocalRandom.current().nextInt(100, screenHeight - 100);
            Entity enemy = new Entity(x, y, spriteLength, spriteWidth, Color.BLACK);
            entities.add(enemy);
        }
    }

    public void paint(Graphics g)
    {
        for (Entity e : entities)
        {
            if (camera.contains(e.hitBox))
            {
                e.paint(g, cameraX, cameraY);
            }
        }
    }
}

class Controller implements KeyListener
{
    Level lvl;

    public Controller(Level gameLevel)
    {
        lvl = gameLevel;
        gameLevel.addKeyListener(this);
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
    }

}