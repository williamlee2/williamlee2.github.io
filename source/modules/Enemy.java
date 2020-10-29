package modules;

import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;

public class Enemy extends Entity
{
    int dx;
    int dy;
    int gravity;
    int dxMax = 25;
    int dyMax = 25;
    int health = 100;

    public Enemy(int posX, int posY, int l, int w, Color c, int g)
    {
        super(posX, posY, l, w, c);
        gravity = g;
        // initial speeds either 25 or -25, avoid zero speeds
        dx = 25 * ThreadLocalRandom.current().nextInt(2);
        dy = 25 * ThreadLocalRandom.current().nextInt(2);
        if (dx == 0)
        {
            dx = -25;
        }
        if (dy == 0)
        {
            dy = -25;
        }
    }

    public void move()
    {
        if (dy != 0)
        {
            dy += gravity;
        }
        if (Math.abs(dy) > Math.abs(dyMax))
        {
            dy = (dy / Math.abs(dy)) * dyMax;
        }
        x += dx;
        y += dy;
        hitBox.setLocation(x, y);
    }

    public void hit(int damage)
    {
        health -= damage;
    }
}