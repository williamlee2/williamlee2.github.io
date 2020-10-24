package modules;

import java.awt.Rectangle;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.lang.Thread;

public class Level extends Canvas
{
    final int mapLength = 30;
    final int mapWidth = 16;
    final int tileLength = 64;
    final int tileWidth = 64;
    Tile[][] map = new Tile[mapLength][mapWidth];
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
        camera = new Rectangle(cameraX, cameraY, width, height);
        screenWidth = width;
        screenHeight = height;
        setSize(width, height);

        for (int i = 0; i < mapLength; i++)
        {
            for (int j = 0; j < mapWidth; j++)
            {
                if (i == 0 || j == 0 || i == mapLength - 1 || j == mapWidth - 1)
                {
                    map[i][j] = new Tile(i * tileLength, j * tileWidth, tileLength, tileWidth, Color.GREEN, true);
                }
                else
                {
                    map[i][j] = new Tile(i * tileLength, j * tileWidth, tileLength, tileWidth, Color.WHITE, false);
                }
            }
        }

        for (int i = 0; i < 10; i++)
        {
            int x = ThreadLocalRandom.current().nextInt(tileLength + 1, (mapLength - 1) * tileLength);
            int y = ThreadLocalRandom.current().nextInt(tileWidth + 1, (mapWidth - 1) * tileWidth);
            Entity e;
            if (i == 0)
            {
                e = new Entity(x, y, spriteLength, spriteWidth, Color.BLUE);
            }
            else
            {
                e = new Entity(x, y, spriteLength, spriteWidth, Color.BLACK);
            }
            entities.add(e);
        }
    }

    public void paint(Graphics g)
    {
        for (int i = 0; i < mapLength; i++)
        {
            for (int j = 0; j < mapWidth; j++)
            {
                if (camera.contains(map[i][j].hitBox))
                {
                    map[i][j].paint(g, cameraX, cameraY);
                }
            }
        }

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
                    int posX = e.x;
                    int posY = e.y;
                    e.move();
                    int mapX = e.x / tileWidth;
                    int mapY = e.y / tileLength;
                    if (map[mapX][mapY].collision)
                    {
                        e.x = posX;
                        e.y = posY;
                    }
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
