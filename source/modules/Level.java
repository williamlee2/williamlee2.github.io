package modules;

import java.awt.Rectangle;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Collections;
import java.lang.Thread;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;

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
    final int spriteWidth = 64;
    final int gravity = 5;
    int screenWidth;
    int screenHeight;
    Rectangle camera;
    int cameraX = 0;
    int cameraY = 0;
    boolean playing = true;
    Image sprite;
    Image samusSprite;

    public Level(int width, int height)
    {        
        camera = new Rectangle(cameraX, cameraY, width, height);
        screenWidth = width;
        screenHeight = height;
        setSize(width, height);

        sprite = getImage("modules/TEST_BACKGROUND.png", width, height);
        
        for (int i = 0; i < mapLength; i++)
        {
            for (int j = 0; j < mapWidth; j++)
            {
                if (i == 0 || j == 0 || i == mapLength - 1 || j == mapWidth - 1)
                {
                    // edges of map
                    if (sprite == null)
                    {
                        map[i][j] = new Tile(i * tileLength, j * tileWidth, tileLength, tileWidth, Color.GREEN, true);
                    }
                    else
                    {
                        map[i][j] = new Tile(i * tileLength, j * tileWidth, tileLength, tileWidth, sprite, true);
                    }
                }
                else
                {
                    if (sprite == null)
                    {
                        map[i][j] = new Tile(i * tileLength, j * tileWidth, tileLength, tileWidth, Color.YELLOW, false);
                    }
                    else
                    {
                        map[i][j] = new Tile(i * tileLength, j * tileWidth, tileLength, tileWidth, sprite, false);
                    }
                }
            }
        }

        for (int i = 0; i < 5; i++)
        {
            // random spawn locations inside map
            int x = ThreadLocalRandom.current().nextInt(1, (mapLength - 1)) * tileLength;
            int y = (mapWidth / 2) * tileWidth;
            Enemy e = new Enemy(x, y, spriteLength, spriteWidth, Color.WHITE, gravity);
            enemies.add(e);
        }

        samusSprite = getImage("modules/SAMUS_RIGHT.png", spriteLength, spriteWidth);
        if (samusSprite == null)
        {
            samus = new Hero(
                (mapLength / 2) * tileLength, (mapWidth / 2) * tileWidth, 
                spriteLength, spriteWidth, 
                Color.BLUE, gravity
            );
        }
        else
        {
            samus = new Hero(
                (mapLength / 2) * tileLength, (mapWidth / 2) * tileWidth, 
                spriteLength, spriteWidth, 
                samusSprite, gravity
            );
        }
    }

    public Image getImage(String fileName, int width, int height)
    {
        try 
        {
            BufferedImage bufferedSprite = ImageIO.read(new File(fileName));
            return bufferedSprite.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        } 
        catch (IOException e) 
        {
            System.out.println("Failed to load background");
            return null;
        }
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

        // only render visible enemies
        for (Enemy e: enemies)
        {
            if (camera.contains(e.hitBox))
            {
                e.paint(g, cameraX, cameraY);
            }
        }

        // samus should always be visible...
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


    // update projectiles, samus, enemies
    public void updateGame()
    {
        int collision;
        ArrayList<Projectile> garbage = new ArrayList<Projectile>();

        for (int i = 0; i < samus.bullets.size(); i++)
        {
            Projectile p = samus.bullets.get(i);
            collision = checkCollision(p.x, p.y, p.dx, p.dy);
            Boolean hit = false;
            if (p.dx > 0)
            {
                Collections.sort(enemies);
            }
            else
            {
                Collections.sort(enemies, Collections.reverseOrder());
            }
            // check if bullet hits enemy
            for (Enemy e: enemies)
            {
                if (e.hitBox.intersects(p.hitBox))
                {
                    hit = true;
                    e.hit(p.damage);
                    if (e.health <= 0)
                    {
                        enemies.remove(e);
                    }
                    garbage.add(p);
                    break;
                }
            }
            if (!hit)
            {
                if (collision != 0)
                {
                    // out of bounds
                    garbage.add(p);
                }
                else
                {
                    p.move();
                }
            }
        }

        for (Projectile p: garbage)
        {
            samus.bullets.remove(p);
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

        for (Enemy e : enemies)
        {
            collision = checkCollision(e.x, e.y, e.dx, e.dy);
            if (collision == 1 || collision == 3)
            {
                e.dx = 0;
            }
            if (collision >= 2)
            {
                e.dy = 0;
            }
            e.move();
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
                updateGame();
                do
                {
                    do
                    {
                        Graphics g = bufferStrat.getDrawGraphics();
                        render(g);
                        g.dispose();
                    } while (bufferStrat.contentsRestored());
                    bufferStrat.show();
                } while (bufferStrat.contentsLost());
                // framerate and gamespeed
                Thread.sleep(30);
            }
        }
        catch (InterruptedException x)
        {
            playing = false;
        }
    }
}
