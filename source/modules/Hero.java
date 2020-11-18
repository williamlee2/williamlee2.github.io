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
    final int idleState = 0;
    final int runState = 1;
    final int crouchState = 2;
    final int jumpState = 3;
    final int shootState = 4;
    final int whipState = 5;
    final int hitState = 6;
    int animationSelect = idleState;

    public Hero(int posX, int posY, int w, int h, Color c, int g)
    {
        super(posX, posY, w, h, c);
        gravity = g;
    }

    public Hero(int posX, int posY, int w, int h, int g)
    {
        super(posX, posY, w, h, null);
        gravity = g;
        animations[idleState] = new Animation(getImage("sprites/SAMUS_IDLE.png", 3 * Animation.frameWidth, Animation.frameHeight), 3, 30, true);
        animations[runState] = new Animation(getImage("sprites/SAMUS_RUN.png", 8 * Animation.frameWidth, Animation.frameHeight), 8, 2, true);
        animations[crouchState] = new Animation(getImage("sprites/SAMUS_CROUCH.png", 2 * Animation.frameWidth, Animation.frameHeight), 2, 2, false);
        animations[jumpState] = new Animation(getImage("sprites/SAMUS_JUMP.png", 5 * Animation.frameWidth, Animation.frameHeight), 5, 2, false);
        animations[shootState] = new Animation(getImage("sprites/SAMUS_SHOOT.png", 3 * Animation.frameWidth, Animation.frameHeight), 3, 2, false);
        animations[whipState] = new Animation(getImage("sprites/SAMUS_WHIP.png", 9 * Animation.frameWidth, Animation.frameHeight), 9, 2, false);
        animations[hitState] = new Animation(getImage("sprites/SAMUS_HIT.png", 8 * Animation.frameWidth, Animation.frameHeight), 8, 2, false);
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
                animationSelect = idleState;
                animations[animationSelect].render(g, x, y, xDirection);
            }
        }
        g.setColor(Color.WHITE);
        if (xDirection == 1)
        {
            g.drawString(String.valueOf(health), x + (width / 3), y);
        }
        else
        {
            g.drawString(String.valueOf(health), x - (2 * width / 3), y);
        }
        g.drawRect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
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
                animationSelect = jumpState;
                dy = dyMax * direction;
            }
        }
        // horizontal
        else
        {
            dx = dxMax * direction;
            xDirection = direction;
            if (animationSelect != 1)
            {
                animations[animationSelect].index = 0;
                animationSelect = runState; 
            }
        }
    }

    public void endMove(int axis)
    {
        if (axis == 1)
        {
            animations[animationSelect].index = 0;
            animationSelect = idleState;
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
                animationSelect = crouchState;
            }
            else
            {
                animations[animationSelect].index = animations[animationSelect].frameDuration;
            }
        }
    }

    public void endCrouch()
    {
        animations[2].index = 0;
        animationSelect = idleState;
    }

    public void swapWeapon()
    {
        
    }

    public void update()
    {
        x += dx;
        y += dy;
        // need to update hitbox size based on animation
        if (xDirection == 1)
        {
            hitBox.setLocation(x, y);
        }
        else
        {
            hitBox.setLocation(x - width, y);
        }
        dy += gravity;
        if (Math.abs(dy) > Math.abs(dyMax))
        {
            dy = (dy / Math.abs(dy)) * dyMax;
        }
    }

    public void shoot()
    {
        animations[animationSelect].index = 0; // reset previous animation
        animationSelect = shootState;
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