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
        background = Entity.getImage("sprites/TEST_BACKGROUND.png", screenWidth, screenHeight);

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

        generatePath(mapWidth - 2, mapHeight - 2);

        // monsters on map
        for (int i = 0; i < 3; i++)
        {
            // random spawn locations inside map
            int x = ThreadLocalRandom.current().nextInt(1, 6) * tileWidth;
            int y = (mapHeight - 4) * tileHeight;
            Enemy e = new Enemy(x, y, spriteWidth, spriteHeight, gravity);
            enemies.add(e);
        }
        Enemy.setXBound(mapWidth * tileWidth);

        samus = new Hero((mapWidth / 2) * tileWidth, (mapHeight - 3) * tileHeight, gravity);
        
    }

    public void generatePath(int xTile, int yTile)
    {
        if (yTile <= 2 || xTile - 3 < 1)
        {
            return;
        }
        
        map[xTile][yTile].collision = true;

        // int up = ThreadLocalRandom.current().nextInt(2);
        // int left = 0;
        // int right = 0;
        // if (xTile + 2 < mapWidth - 1)
        // {
        //     right = ThreadLocalRandom.current().nextInt(1, 11);
        // }
        // if (xTile - 2 > 1)
        // {
        //     left = 10 - right;
        // }

        // if (left >= right)
        // {
        //     generatePath(xTile - 2, yTile - up);
        // }
        // else if (left < right)
        // {
        //     generatePath(xTile + 2, yTile - up);
        // }

        generatePath(xTile - 3, yTile - 2);
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
                    map[i][j].render(g, cameraX, cameraY);
                }
            }
        }

        // only render visible enemies
        for (Enemy e: enemies)
        {
            if (camera.contains(e.hitBox))
            {
                e.render(g, cameraX, cameraY);
            }
        }

        // samus should always be visible...
        if (camera.contains(samus.hitBox))
        {
            samus.render(g, cameraX, cameraY);
        }

        for (Projectile p: samus.bullets)
        {
            if (camera.contains(p.hitBox))
            {
                p.render(g, cameraX, cameraY);
            }
        }

        // render UI/misc elements
        g.setColor(Color.GREEN);
        g.drawString("Developed by: William", tileWidth, tileHeight + 25);
        g.drawString("Art by: Soulfire, Heat-Park", tileWidth, tileHeight + 50);
        g.drawString("Movement: A, S, D", tileWidth, tileHeight + 75);
        g.drawString("Jump: Space", tileWidth, tileHeight + 100);
        g.drawString("Shoot: K", tileWidth, tileHeight + 125);
        g.drawString("Whip: L", tileWidth, tileHeight + 150);
    }

    public int checkCollision(Rectangle box, int dx, int dy)
    {
        int collisionCode = 0;
        int nextLeft = (box.x + dx) / tileWidth;
        int nextRight = (box.x + box.width + dx) / tileWidth;
        int nextTop = (box.y + dy) / tileHeight;
        int nextBottom = (box.y + box.height + dy) / tileHeight;
        int left = box.x / tileWidth;
        int right = (box.x + box.width) / tileWidth;
        int top = box.y / tileHeight;
        int bottom = (box.y + box.height) / tileHeight;
        if (dx > 0)
        {
            if (map[nextRight][top].collision || map[nextRight][bottom].collision)
            {
                collisionCode += 10;
            }
        }
        else if (dx < 0)
        {
            if (map[nextLeft][top].collision || map[nextLeft][bottom].collision)
            {
                collisionCode += 20;
            }
        }
        if (dy > 0)
        {
            if (map[right][nextBottom].collision || map[left][nextBottom].collision)
            {
                collisionCode += 1;
            }
        }
        else if (dy < 0)
        {
            if (map[right][nextTop].collision || map[left][nextTop].collision)
            {
                collisionCode += 2;
            }
        }
        return collisionCode;
    }

    // update projectiles, samus, enemies
    public void updateGame()
    {
        int collision;
        ArrayList<Projectile> garbage = new ArrayList<Projectile>();

        for (int i = 0; i < samus.bullets.size(); i++)
        {
            Projectile p = samus.bullets.get(i);
            collision = checkCollision(p.hitBox, p.dx, p.dy);
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
        // check collisions
        collision = checkCollision(samus.hitBox, samus.dx, samus.dy);
        if (collision != 0)
        {
            if ((collision / 10) == 1 || (collision / 10) == 2)
            {
                samus.dx = 0;
            }
            if ((collision % 10) == 1 || (collision % 10) == 2)
            {
                samus.dy = 0;
            }
        }
        
        
        samus.grounded = map[(samus.hitBox.x + samus.hitBox.width) / tileWidth][(samus.hitBox.y + samus.hitBox.height + 10) / tileHeight].collision;
        samus.update();

        if (enemies.isEmpty())
        {
            int x = ThreadLocalRandom.current().nextInt(1, (mapWidth - 1)) * tileWidth;
            int y = (mapHeight - 4) * tileHeight;
            Enemy e = new Enemy(x, y, spriteWidth, spriteHeight, gravity);
            enemies.add(e);
        }
        else
        {
            for (Enemy e : enemies)
            {
                // check horizontal collision
                collision = checkCollision(e.hitBox, e.dx, e.dy);
                if (collision != 0)
                {
                    if ((collision / 10) == 1 || (collision / 10) == 2)
                    {
                        e.dx = 0;
                    }
                    if ((collision % 10) == 1 || (collision % 10) == 2)
                    {
                        e.dy = 0;
                    }
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
