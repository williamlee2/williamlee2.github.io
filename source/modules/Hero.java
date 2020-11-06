package modules;

import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

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
    Image[] sprites = new Image[4];
    Queue<Integer> animation = new LinkedList<Integer>();

    public Hero(int posX, int posY, int w, int h, Color c, int g)
    {
        super(posX, posY, w, h, c);
        gravity = g;
    }

    public Hero(int posX, int posY, int w, int h, String spritePath, int g)
    {
        super(posX, posY, w, h, spritePath, w, h);
        gravity = g;
        sprites[0] = getImage("modules/SAMUS_RIGHT.png", w, h);
        sprites[1] = getImage("modules/SAMUS_LEFT.png", w, h);
        sprites[2] = getImage("modules/SAMUS_SHOOT_RIGHT.png", 128, 128);
        sprites[3] = getImage("modules/SAMUS_SHOOT_LEFT.png", 128, 128);
        animation.add(0);
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
                animation.add(0);
            }
            else
            {
                animation.add(1);
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

    public void update()
    {
        x += dx;
        y += dy;
        hitBox.setLocation(x, y);
        dy += gravity;
        if (Math.abs(dy) > Math.abs(dyMax))
        {
            dy = (dy / Math.abs(dy)) * dyMax;
        }

        int index = animation.poll();
        sprite = sprites[index];
        if (animation.isEmpty())
        {
            animation.add(index);
        }
    }

    public void shoot()
    {
        if (xDirection == 1)
        {
            for (int i = 0; i < 4; i++)
            {
                animation.add(2);
            }
            animation.add(0);
            bullets.add(new Projectile(x + width, y + (height / 4), 
                    64, 16, 
                    "modules/BULLET_RIGHT.png", xDirection
                )
            );
        }
        else
        {
            for (int i = 0; i < 4; i++)
            {
                animation.add(3);
            }
            animation.add(1);
            bullets.add(new Projectile(x, y + (height / 4), 
                    64, 16, 
                    "modules/BULLET_LEFT.png", xDirection
                )
            );
        }
    }
}