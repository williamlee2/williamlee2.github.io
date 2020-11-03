package modules;

import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;

public class Enemy extends Entity
{
    int direction = 0;
    int dx = 0;
    int dy = 0;
    int gravity;
    int dxMax = 10;
    int dyMax = 25;
    int health = 100;

    public Enemy(int posX, int posY, int l, int w, Color c, int g)
    {
        super(posX, posY, l, w, c);
        gravity = g;
        direction = ThreadLocalRandom.current().nextInt(2);
        if (direction == 0)
        {
            direction = -1;
        }
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
        int nextMove = ThreadLocalRandom.current().nextInt(0, 75);
        if (nextMove == 0)
        {
            // change direction
            direction *= -1;
            dx = dxMax * direction;
        }
        else if (nextMove < 3)
        {
            // stop
            dx = 0;
        }
        else
        {
            dx = dxMax * direction;
        }
    }

    public void hit(int damage)
    {
        health -= damage;
    }
}