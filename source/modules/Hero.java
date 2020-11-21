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
    int dyMax = 35;
    int xDirection = 1;
    int gravity;
    boolean grounded = false;
    int health = 100;
    int weapon = Projectile.bullet;
    ArrayList<Projectile> bullets = new ArrayList<Projectile>();
    final int frameWidth = 80;
    final int frameHeight = 80;
    static final int imageWidth = 128;
    static final int imageHeight = 128;
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

    public Hero(int posX, int posY, int g)
    {
        super(posX, posY, imageWidth / 3, 7 * imageHeight / 10, null);
        hitBox.width = imageWidth / 3;
        hitBox.height = 7 * imageHeight / 10;
        gravity = g;
        animations[idleState] = new Animation(
            getImage("sprites/SAMUS_IDLE.png", 3 * frameWidth, frameHeight), 
            frameWidth, frameHeight, imageWidth, imageHeight,
            3, 30, true
        );
        animations[runState] = new Animation(
            getImage("sprites/SAMUS_RUN.png", 8 * frameWidth, frameHeight),
            frameWidth, frameHeight, imageWidth, imageHeight,
            8, 2, true
        );
        animations[crouchState] = new Animation(
            getImage("sprites/SAMUS_CROUCH.png", 2 * frameWidth, frameHeight),
            frameWidth, frameHeight, imageWidth, imageHeight,
            2, 2, false
        );
        animations[jumpState] = new Animation(
            getImage("sprites/SAMUS_JUMP.png", 5 * frameWidth, frameHeight), 
            frameWidth, frameHeight, imageWidth, imageHeight,
            5, 2, false
        );
        animations[shootState] = new Animation(
            getImage("sprites/SAMUS_SHOOT.png", 3 * frameWidth, frameHeight), 
            frameWidth, frameHeight, imageWidth, imageHeight,
            3, 2, false
        );
        animations[whipState] = new Animation(
            getImage("sprites/SAMUS_WHIP.png", 9 * frameWidth, frameHeight), 
            frameWidth, frameHeight, imageWidth, imageHeight,
            9, 2, false
        );
        animations[hitState] = new Animation(
            getImage("sprites/SAMUS_HIT.png", 8 * frameWidth, frameHeight), 
            frameWidth, frameHeight, imageWidth, imageHeight,
            8, 2, false
        );
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
            // animation ended
            if (!animations[animationSelect].render(g, x - offSetX, y - offSetY, xDirection))
            {
                if (dx != 0)
                {
                    animations[animationSelect].index = 0;
                    animationSelect = runState;
                }
                else if (animationSelect != crouchState)
                {
                    animations[animationSelect].index = 0;
                    animationSelect = idleState;
                }
                else if (animationSelect == crouchState)
                {
                    animations[animationSelect].index = animations[animationSelect].frameDuration;
                }
                animations[animationSelect].render(g, x - offSetX, y - offSetY, xDirection);
            }
        }
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(health), x + (3 * width / 5), y);
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
            if (animationSelect != runState)
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
            if (animationSelect != crouchState)
            {
                animations[animationSelect].index = 0;
                animationSelect = crouchState;
            }
        }
    }

    public void endCrouch()
    {
        if (animationSelect == crouchState)
        {
            animations[crouchState].index = 0;
            animationSelect = idleState;
        }
    }

    public void swapWeapon()
    {
        if (weapon == Projectile.bullet)
        {
            weapon = Projectile.beam;
        }
        else
        {
            weapon = Projectile.bullet;
        }
    }

    public void update()
    {
        x += dx;
        y += dy;
        // need to update hitbox size based on animation
        hitBox.setLocation(x + (imageWidth / 3), y + (imageWidth / 8));
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
            bullets.add(new Projectile(x + width, y + (height / 4), xDirection, weapon));
        }
        else
        {
            bullets.add(new Projectile(x, y + (height / 4), xDirection, weapon));
        }
    }

    public void whip()
    {
        animations[animationSelect].index = 0; // reset previous animation
        animationSelect = whipState;
    }
}