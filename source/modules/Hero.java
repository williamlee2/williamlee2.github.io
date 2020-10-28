package modules;

import java.awt.Color;
import java.util.ArrayList;

public class Hero extends Entity
{
    int dx = 0;
    int dy = 0;
    int dxMax = 25;
    int dyMax = 25;
    int gravity;
    boolean crouch = false;
    ArrayList<Projectile> bullets = new ArrayList<Projectile>();

    public Hero(int posX, int posY, int l, int w, Color c, int g)
    {
        super(posX, posY, l, w, c);
        gravity = g;
    }

    public void move(int direction, boolean yAxis)
    {
        // vertical
        if (yAxis)
        {
            dy = dxMax * direction;
        }
        // horizontal
        else
        {
            dx = dxMax * direction;
        }
    }

    public void endMove(int direction)
    {
        if (direction == 1)
        {
            dx = 0;
        }
    }

    public void startCrouch()
    {
        crouch = true;
    }

    public void endCrouch()
    {
        crouch = false;
    }

    public void swapWeapon()
    {
        
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

    public void shoot()
    {
        bullets.add(new Projectile(x + dx, y, 16, 16, Color.RED));
    }
}