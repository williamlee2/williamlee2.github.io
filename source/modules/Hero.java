package modules;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

public class Hero extends Entity
{
    int dx = 0;
    int dy = 0;
    int dxMax = 15;
    int dyMax = 50;
    int xDirection = 1;
    int gravity;
    boolean grounded = false;
    int health = 100;
    boolean crouch = false;
    ArrayList<Projectile> bullets = new ArrayList<Projectile>();
    Animation[] animations = new Animation[7];
    int animationSelect = 0;

    public Hero(int posX, int posY, int w, int h, Color c, int g)
    {
        super(posX, posY, w, h, c);
        gravity = g;
    }

    public Hero(int posX, int posY, int w, int h, int g)
    {
        super(posX, posY, w, h, null);
        gravity = g;
        animations[0] = new Animation(getImage("sprites/SAMUS_IDLE.png", 3 * w, h), w, h, 3, 30, true);
        animations[1] = new Animation(getImage("sprites/SAMUS_RUN.png", 8 * w, h), w, h, 8, 2, true);
        animations[2] = new Animation(getImage("sprites/SAMUS_CROUCH.png", 2 * w, h / 2), w, h / 2, 2, 2, false);
        animations[3] = new Animation(getImage("sprites/SAMUS_JUMP.png", 5 * w, h / 2), w, h / 2, 5, 2, false);
        animations[4] = new Animation(getImage("sprites/SAMUS_SHOOT.png", 3 * w, h), w, h, 3, 2, false);
        animations[5] = new Animation(getImage("sprites/SAMUS_WHIP.png", 9 * w, h), w, h, 9, 2, false);
        animations[6] = new Animation(getImage("sprites/SAMUS_HIT.png", 8 * w, h), w, h, 8, 2, false);
    }

    public void render(Graphics g, int offSetX, int offSetY)
    {
        if (color != null)
        {
            g.setColor(color);
            g.fillRect(x - offSetX, y - offSetY, width, height);
        }
        else
        {
            if (!animations[animationSelect].render(g, x, y, xDirection))
            {
                animations[animationSelect].index = 0;
                animationSelect = 0;
                animations[animationSelect].render(g, x, y, xDirection);
            }
        }
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(health), x + (width / 3), y);
    }

    public void move(int direction, boolean yAxis)
    {
        // vertical
        if (yAxis)
        {
            // only jump if on top of ground
            if (grounded)
            {
                animations[animationSelect].index = 0;
                animationSelect = 3;
                dy = dxMax * direction;
            }
        }
        // horizontal
        else
        {
            dx = dxMax * direction;
            xDirection = direction;
            animations[animationSelect].index = 0;
            animationSelect = 1; // run
        }
    }

    public void endMove(int axis)
    {
        if (axis == 1)
        {
            animations[animationSelect].index = 0;
            animationSelect = 0; // idle
            dx = 0;
        }
    }

    public void startCrouch()
    {
        if (grounded)
        {
            if (animationSelect != 2)
            {
                animations[animationSelect].index = 0;
                animationSelect = 2; //crouch
            }
        }
    }

    public void endCrouch()
    {
        animations[2].index = 0;
        animationSelect = 0;
    }

    public void swapWeapon()
    {
        
    }

    public void update()
    {
        x += dx;
        y += dy;
        // need to update hitbox size based on animation
        hitBox.setLocation(x, y);
        dy += gravity;
        if (Math.abs(dy) > Math.abs(dyMax))
        {
            dy = (dy / Math.abs(dy)) * dyMax;
        }
    }

    public void shoot()
    {
        animations[animationSelect].index = 0; // reset previous animation
        animationSelect = 4; // shoot
        if (xDirection == 1)
        {
            bullets.add(new Projectile(x + width, y + (height / 4), 
                    64, 16, 
                    "sprites/BULLET.png", xDirection
                )
            );
        }
        else
        {
            bullets.add(new Projectile(x, y + (height / 4), 
                    64, 16, 
                    "sprites/BULLET.png", xDirection
                )
            );
        }
    }
}