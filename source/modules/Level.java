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
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();
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
                    map[i][j] = new Tile(i * tileLength, j * tileWidth, tileLength, tileWidth, Color.YELLOW, false);
                }
            }
        }

        for (int i = 0; i < 10; i++)
        {
            int x = ThreadLocalRandom.current().nextInt(tileLength + 1, (mapLength - 1) * tileLength);
            int y = ThreadLocalRandom.current().nextInt(tileWidth + 1, (mapWidth - 1) * tileWidth);
            Enemy e;
            if (i == 0)
            {
                e = new Enemy(x, y, spriteLength, spriteWidth, Color.BLUE);
            }
            else
            {
                e = new Enemy(x, y, spriteLength, spriteWidth, Color.BLACK);
            }
            enemies.add(e);
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

        for (Enemy e : enemies)
        {
            if (camera.contains(e.hitBox))
            {
                e.paint(g, cameraX, cameraY);
            }
        }
    }

    public void update(Graphics g) 
    {
        paint(g);
    }

    public void start()
    {
        try
        {
            while(running)
            {
                for (Enemy e : enemies)
                {
                    int posX = e.x;
                    int posY = e.y;
                    int mapX = (e.x + e.dx) / tileWidth;
                    int mapY = (e.y + e.dy) / tileLength;
                    if (map[mapX][posY / tileLength].collision)
                    {
                        e.dx *= -1;
                    }
                    if (map[posX / tileWidth][mapY].collision)
                    {
                        e.dy *= -1;
                    }
                    e.move();
                }
                repaint();
                Thread.sleep(250);
            }
        }
        catch (InterruptedException x)
        {
            running = false;
        }
    }
}
