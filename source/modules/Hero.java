package modules;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

public class Hero extends Entity
{
    int dx = 0;
    int dy = 0;
    int dxMax = 25;
    int dyMax = 25;
    int xDirection = 1;
    int gravity;
    boolean crouch = false;
    ArrayList<Projectile> bullets = new ArrayList<Projectile>();

    public Hero(int posX, int posY, int w, int h, Color c, int g)
    {
        super(posX, posY, w, h, c);
        gravity = g;
    }

    public Hero(int posX, int posY, int w, int h, String spritePath, int g)
    {
        super(posX, posY, w, h, spritePath, w, h);
        gravity = g;
    }

    public void move(int direction, boolean yAxis)
    {
        // vertical
        if (yAxis)
        {
            // only jump if at rest
            if (dy == gravity)
            {
                dy = dxMax * direction;
            }
        }
        // horizontal
        else
        {
            dx = dxMax * direction;
            xDirection = direction;
            // 1 = right -1 = left
            if (direction == 1)
            {
                sprite = getImage("modules/SAMUS_RIGHT.png", width, height);
            }
            else
            {
                sprite = getImage("modules/SAMUS_LEFT.png", width, height);
            }
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
        x += dx;
        y += dy;
        hitBox.setLocation(x, y);
        dy += gravity;
        if (Math.abs(dy) > Math.abs(dyMax))
        {
            dy = (dy / Math.abs(dy)) * dyMax;
        }
    }

    public void shoot()
    {
        if (xDirection == 1)
        {
            bullets.add(new Projectile(x + dx, y + (height / 3), 
                    64, 16, 
                    "modules/BULLET_RIGHT.png", xDirection
                )
            );
        }
        else
        {
            bullets.add(new Projectile(x + dx, y + (height / 3), 
                    64, 16, 
                    "modules/BULLET_LEFT.png", xDirection
                )
            );
        }
    }
}