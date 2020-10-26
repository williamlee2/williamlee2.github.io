package modules;

import java.awt.Rectangle;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.lang.Thread;
import java.awt.image.BufferStrategy;

public class Level extends Canvas
{
    final int mapLength = 30;
    final int mapWidth = 16;
    final int tileLength = 64;
    final int tileWidth = 64;
    Tile[][] map = new Tile[mapLength][mapWidth];
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    Hero samus;
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
                    // edges of map
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
            // random spawn locations inside map
            int x = ThreadLocalRandom.current().nextInt(tileLength + 1, (mapLength - 1) * tileLength);
            int y = ThreadLocalRandom.current().nextInt(tileWidth + 1, (mapWidth - 1) * tileWidth);
            Enemy e = new Enemy(x, y, spriteLength, spriteWidth, Color.BLACK);
            enemies.add(e);
        }

        samus = new Hero(500, 500, spriteLength, spriteWidth, Color.BLUE);
    }

    public void render(Graphics g)
    {
        g.clearRect(0, 0, screenWidth, screenHeight);
        
        for (int i = 0; i < mapLength; i++)
        {
            for (int j = 0; j < mapWidth; j++)
            {
                // only render visible tiles
                if (camera.contains(map[i][j].hitBox))
                {
                    map[i][j].paint(g, cameraX, cameraY);
                }
            }
        }

        for (Enemy e : enemies)
        {
            // only render visible enemies
            if (camera.contains(e.hitBox))
            {
                e.paint(g, cameraX, cameraY);
            }
        }

        if (camera.contains(samus.hitBox))
        {
            samus.paint(g, cameraX, cameraY);
        }
    }

    public void start()
    {
        try
        {
            createBufferStrategy(2);
            BufferStrategy bufferStrat = getBufferStrategy();
            while(running)
            {
                do
                {
                    do
                    {
                        for (Enemy e : enemies)
                        {
                            int posX = e.x;
                            int posY = e.y;
                            // map coordinates
                            int mapX = (e.x + e.dx) / tileWidth;
                            int mapY = (e.y + e.dy) / tileLength;
                            if ((0 <= mapX && mapX < mapLength) && (0 <= mapY && mapY < mapWidth))
                            {
                                // check for future collisions
                                if (map[mapX][posY / tileLength].collision)
                                {
                                    // check horizontal
                                    e.dx *= -1;
                                }
                                if (map[posX / tileWidth][mapY].collision)
                                {
                                    // check vertical
                                    e.dy = 0;
                                }
                            }
                            else
                            {
                                if (mapX >= mapLength || mapX < 0)
                                {
                                    e.dx *= -1;
                                }
                                else
                                {
                                    e.dy = 0;
                                }
                            }
                            e.move();
                        }
                        samus.move();

                        Graphics g = bufferStrat.getDrawGraphics();
                        render(g);
                        g.dispose();
                        // framerate and gamespeed
                        Thread.sleep(50);
                    } while (bufferStrat.contentsRestored());
                    bufferStrat.show();
                } while (bufferStrat.contentsLost());
            }
        }
        catch (InterruptedException x)
        {
            running = false;
        }
    }
}
