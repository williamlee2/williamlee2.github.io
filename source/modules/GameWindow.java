package modules;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

class Level extends Canvas
{
    ArrayList<Entity> entities = new ArrayList<Entity>();
    final int spriteLength = 64;
    final int spriteWidth = 32;
    final int boundX = 1900;
    final int boundY = 1000;

    public Level(int width, int height)
    {
        setSize(width, height);
        int x = ThreadLocalRandom.current().nextInt(100, boundX);
        int y = ThreadLocalRandom.current().nextInt(100, boundY);
        Entity hero = new Entity(x, y, spriteLength, spriteWidth, Color.BLUE);
        entities.add(hero);
        for (int i = 0; i < 10; i++)
        {
            x = ThreadLocalRandom.current().nextInt(100, boundX);
            y = ThreadLocalRandom.current().nextInt(100, boundY);
            Entity enemy = new Entity(x, y, spriteLength, spriteWidth, Color.BLACK);
            entities.add(enemy);
        }
    }

    public void paint(Graphics g)
    {
        for (Entity e : entities)
        {
            e.paint(g);
        }
    }
}

public class GameWindow 
{
    Level lvl;

    public GameWindow()
    {
        lvl = new Level(1920, 1080);
        
        JFrame frame = new JFrame("Immortal Hero");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(lvl);
        frame.pack();
        frame.setVisible(true);
    }

}