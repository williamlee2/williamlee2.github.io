package modules;

import java.awt.Color;

public class Hero extends Entity
{
    int dx = 0;
    int dy = 0;
    int maxSpeedX = 25;
    int maxSpeedY = 25;
    int gravity = 0;

    public Hero(int posX, int posY, int l, int w, Color c)
    {
        super(posX, posY, l, w, c);
    }

    public void move(int direction, boolean yAxis)
    {
        // vertical
        if (yAxis)
        {
            dx = 0;
            dy = maxSpeedY * direction;
        }
        // horizontal
        else
        {
            dy = 0;
            dx = maxSpeedX * direction;
        }
    }

    public void move()
    {
        x += dx;
        y += dy;
        dy += gravity;
        hitBox.setLocation(x, y);
    }

    public void shoot()
    {

    }
}