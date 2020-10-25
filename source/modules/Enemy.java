package modules;

import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;

public class Enemy extends Entity
{
    int dx;
    int dy;

    public Enemy(int posX, int posY, int l, int w, Color c)
    {
        super(posX, posY, l, w, c);
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
        hitBox.setLocation(x, y);
    }
}