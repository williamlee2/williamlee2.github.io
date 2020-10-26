package modules;

import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;

public class Enemy extends Entity
{
    int dx;
    int dy;
    int gravity = 10;

    public Enemy(int posX, int posY, int l, int w, Color c)
    {
        super(posX, posY, l, w, c);
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
        x += dx;
        y += dy;
        dy += gravity;
        hitBox.setLocation(x, y);
    }
}