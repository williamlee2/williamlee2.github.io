package modules;

import java.awt.Rectangle;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.lang.Thread;

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
    boolean running = true;

    public Level(int width, int height)
    {
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

    public void start()
    {
        try
        {
            while(running)
            {
                for (Entity e : entities)
                {
                    e.move();
                }
                repaint();
                Thread.sleep(500);
            }
        }
        catch (InterruptedException x)
        {
            running = false;
        }
    }
}
