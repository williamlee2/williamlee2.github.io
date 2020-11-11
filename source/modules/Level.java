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

public class Level extends Canvas
{
    final int mapWidth = 30;
    final int mapHeight = 16;
    final int tileWidth = 64;
    final int tileHeight = 64;
    Tile[][] map = new Tile[mapWidth][mapHeight];
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    Hero samus;
    final int spriteWidth = 64;
    final int spriteHeight = 128;
    final int gravity = 5;
    int screenWidth;
    int screenHeight;
    Rectangle camera;
    int cameraX = 0;
    int cameraY = 0;
    boolean playing = true;
    Image background;

    public Level(int width, int height)
    {        
        camera = new Rectangle(cameraX, cameraY, width, height);
        screenWidth = width;
        screenHeight = height;
        setSize(width, height);
        background = Entity.getImage("modules/TEST_BACKGROUND.png", screenWidth, screenHeight);

        // map tiles
        for (int i = 0; i < mapWidth; i++)
        {
            for (int j = 0; j < mapHeight; j++)
            {
                if (i == 0 || j == 0 || i == mapWidth - 1 || j == mapHeight - 1)
                {
                    // edges of map
                    map[i][j] = new Tile(i * tileWidth, j * tileHeight, 
                        tileWidth, tileHeight, 
                        Color.WHITE, true
                    );
                }
                else
                {
                    map[i][j] = new Tile(i * tileWidth, j * tileHeight, 
                        tileWidth, tileHeight,
                        Color.WHITE, false
                    );
                }
            }
        }

        // monsters on map
        for (int i = 0; i < 5; i++)
        {
            // random spawn locations inside map
            int x = ThreadLocalRandom.current().nextInt(1, (mapWidth - 1)) * tileWidth;
            int y = (mapHeight / 2) * tileHeight;
            Enemy e = new Enemy(x, y, spriteWidth, spriteHeight, Color.PINK, gravity);
            enemies.add(e);
        }

        samus = new Hero(
            (mapWidth / 2) * tileWidth, (mapHeight / 2) * tileHeight, 
            spriteWidth, spriteHeight, 
            "modules/SAMUS_RIGHT.png", gravity
        );
        
    }

    public void render(Graphics g)
    {
        g.drawImage(background, 0, 0, null);
        
        for (int i = 0; i < mapWidth; i++)
        {
            for (int j = 0; j < mapHeight; j++)
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

    public int checkCollision(int position, int dimension, int velocity, int axis, int axisPosition)
    {
        if (velocity == 0)
        {
            return 0;
        }
        int p;
        int pd;
        int ap;
        int mapBound;
        // next positions
        if (axis == 0)
        {
            // vertical
            ap = axisPosition / tileHeight;
            pd = ((position + dimension) / tileHeight) + (velocity / Math.abs(velocity));
            p = (position / tileHeight) + (velocity / Math.abs(velocity));
            mapBound = mapHeight;
        }
        else
        {
            // horizontal
            ap = axisPosition / tileWidth;
            pd = ((position + dimension) / tileWidth) + (velocity / Math.abs(velocity));
            p = (position / tileWidth) + (velocity / Math.abs(velocity));
            mapBound = mapWidth;
        }
        
        if ((0 <= p && p < mapBound) && (0 <= pd && pd < mapBound))
        {
            if (axis == 0)
            {
                // collision above
                if (map[ap][p].collision)
                {
                    return 1;
                }
                // collision below
                else if (map[ap][pd].collision)
                {
                    return 2;
                }
            }
            else
            {
                // collision left
                if (map[p][ap].collision)
                {
                    return 3;
                }
                // collision right
                else if (map[pd][ap].collision)
                {
                    return 4;
                }
            }
            return 0; // no collision
        }
        else
        {
            // out of bounds
            return -1;
        }
    }


    // update projectiles, samus, enemies
    public void updateGame()
    {
        int collision;
        ArrayList<Projectile> garbage = new ArrayList<Projectile>();

        for (int i = 0; i < samus.bullets.size(); i++)
        {
            Projectile p = samus.bullets.get(i);
            collision = checkCollision(p.x, p.width, p.dx, 1, p.y);
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
        // check horizontal collision
        collision = checkCollision(samus.x, samus.width, samus.dx, 1, samus.y);
        if (collision != 0)
        {
            if (collision == -1)
            {
                samus.dx = 0;
            }
            else if (collision == 1)
            {
                samus.dx = tileWidth - (samus.x % tileWidth);
            }
            else if (collision == 2)
            {
                samus.dx = tileWidth - ((samus.x + samus.width) % tileWidth);
            }
        }
        // check vertical collision
        collision = checkCollision(samus.y, samus.height, samus.dy, 0, samus.x);
        if (collision != 0)
        {
            if (collision == -1)
            {
                samus.dy = 0;
            }
            else if (collision == 1)
            {
                samus.dy = tileHeight - (samus.y % tileHeight);
            }
            else if (collision == 2)
            {
                samus.dy = tileHeight - ((samus.y + samus.height) % tileHeight);
            }
        }
        samus.grounded = map[samus.x / tileWidth][(samus.y + samus.height) / tileHeight].collision;
        samus.update();

        if (enemies.isEmpty())
        {
            int x = ThreadLocalRandom.current().nextInt(1, (mapWidth - 1)) * tileWidth;
            Enemy e = new Enemy(x, (mapHeight / 2) * tileHeight, spriteWidth, spriteHeight, Color.PINK, gravity);
            enemies.add(e);
        }
        else
        {
            for (Enemy e : enemies)
            {
                // check horizontal collision
                collision = checkCollision(e.x, e.width, e.dx, 1, e.y);
                if (collision != 0)
                {
                    e.dx = 0;
                }
                // check vertical collision
                collision = checkCollision(e.y, e.height, e.dy, 0, e.x);
                if (collision != 0)
                {
                    e.dy = 0;
                }
                e.move();
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
