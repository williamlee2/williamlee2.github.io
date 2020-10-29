package modules;

import java.awt.Rectangle;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Hashtable;
import java.util.Stack;
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
    Hashtable<Integer, Enemy> enemies = new Hashtable<Integer, Enemy>();
    Hero samus;
    final int spriteLength = 32;
    final int spriteWidth = 32;
    final int gravity = 10;
    int screenWidth;
    int screenHeight;
    Rectangle camera;
    int cameraX = 0;
    int cameraY = 0;
    boolean playing = true;

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
            Enemy e = new Enemy(x, y, spriteLength, spriteWidth, Color.BLACK, gravity);
            enemies.put(x, e);
        }

        samus = new Hero(
            (mapLength / 2) * tileLength, (mapWidth / 2) * tileWidth, 
            spriteLength, spriteWidth, 
            Color.BLUE, gravity
        );
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

        for (Enemy e : enemies.values())
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

        for (Projectile p: samus.bullets)
        {
            if (camera.contains(p.hitBox))
            {
                p.paint(g, cameraX, cameraY);
            }   
        }
    }

    public int checkCollision(int x, int y, int dx, int dy)
    {
        int collision = 0;
        // current position
        int posX = x / tileWidth;
        int posY = y / tileLength;
        int mapX;
        int mapY;
        // next position
        if (dx != 0)
        {
            mapX = posX + (dx / Math.abs(dx));
        }
        else
        {
            mapX = posX;
        }
        if (dy != 0)
        {
            mapY = posY + (dy / Math.abs(dy));
        }
        else
        {
            mapY = posY;
        }
        if ((0 <= mapX && mapX < mapLength) && (0 <= mapY && mapY < mapWidth))
        {
            // check for future collisions
            if (map[mapX][posY].collision)
            {
                // horizontal collision
                collision += 1;
            }
            if (map[posX][mapY].collision)
            {
                // vertical collision
                collision += 2;
            }
        }
        else
        {
            // out of bounds
            if (mapX >= mapLength || mapX < 0)
            {
                collision += 1;
            }
            if (mapY >= mapWidth || mapY < 0)
            {
                collision += 2;
            }
        }
        return collision;
    }

    public void update()
    {
        int collision;
        Stack<Integer> garbage = new Stack<Integer>();

        // update projectiles
        for (int i = 0; i < samus.bullets.size(); i++)
        {
            Projectile p = samus.bullets.get(i);
            collision = checkCollision(p.x, p.y, p.dx, p.dy);
            // check if bullet hits enemy
            if (enemies.containsKey(p.x))
            {
                // hit
                Enemy e = enemies.get(p.x);
                if (e.hitBox.contains(p.hitBox)) 
                {
                    e.hit(p.damage);
                    garbage.push(i);
                }
                else
                {
                    p.move();
                }
            }
            else
            {
                if (collision != 0)
                {
                    // out of bounds
                    garbage.push(i);
                }
                else
                {
                    p.move();
                }
            }
        }

        while(!garbage.isEmpty())
        {
            samus.bullets.remove(garbage.pop());
        }

        // update samus
        collision = checkCollision(samus.x, samus.y, samus.dx, samus.dy);
        if (collision == 1 || collision == 3)
        {
            samus.dx = 0;
        }
        if (collision >= 2)
        {
            samus.dy = 0;
        }
        samus.move();

        // update enemies
        Stack<Enemy> temp = new Stack<Enemy>();
        for (Enemy e : enemies.values())
        {
            temp.push(e);
            collision = checkCollision(e.x, e.y, e.dx, e.dy);
            if (collision == 1 || collision == 3)
            {
                e.dx *= -1;
            }
            if (collision >= 2)
            {
                e.dy = 0;
            }
            e.move();
        }

        enemies.clear();
        while(!temp.empty())
        {
            Enemy e = temp.pop();
            if (e.health > 0)
            {
                enemies.put(e.x, e);
            }
        }
    }

    public void start()
    {
        try
        {
            createBufferStrategy(2);
            BufferStrategy bufferStrat = getBufferStrategy();
            // game + rendering loop
            while(playing)
            {
                do
                {
                    do
                    {
                        update();
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
            playing = false;
        }
    }
}
