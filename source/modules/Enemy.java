package modules;

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.ThreadLocalRandom;

public class Enemy extends Entity
{
    static int xBound;
    static int gravity;
    static int dxMax = 10;
    static int dyMax = 25;
    int direction = 0;
    int dx = 0;
    int dy = 0;
    int health = 100;

    public Enemy(int posX, int posY, int l, int w, int g)
    {
        super(posX, posY, l, w, Color.GREEN);
        gravity = g;
        direction = ThreadLocalRandom.current().nextInt(2);
        // 1 = right -1 = left
        if (direction == 0)
        {
            direction = -1;
        }
    }

    public static void setXBound(int mapWidth)
    {
        xBound = mapWidth;
    }

    public void render(Graphics g, int offSetX, int offSetY)
    {
        super.render(g, offSetX, offSetY);
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(health), x + (width / 3) - offSetX, y - offSetY);
    }

    public void move()
    {
        x += dx;
        y += dy;
        hitBox.setLocation(x, y);
        dy += gravity;
        if (Math.abs(dy) > Math.abs(dyMax))
        {
            dy = (dy / Math.abs(dy)) * dyMax;
        }
        int change = ThreadLocalRandom.current().nextInt(0, 10);
        if (change == 0)
        {
            int nextMove = ThreadLocalRandom.current().nextInt(0, xBound);
            if (nextMove < x)
            {
                // accelerate left
                if (dx > -dxMax)
                {
                    dx -= dxMax / 10;
                }
            }
            else
            {
                // accelerate right
                if (dx < dxMax)
                {
                    dx += dxMax / 10;
                }
            }
        }
    }

    public void hit(int damage)
    {
        health -= damage;
        if (health < 75 && health >= 50)
        {
            color = Color.BLUE;
        }
        else if (health < 50 && health >= 25)
        {
            color = Color.ORANGE;
        }
        else if (health < 25)
        {
            color = Color.RED;
        }
    }
}