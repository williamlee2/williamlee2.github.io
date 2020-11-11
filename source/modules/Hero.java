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
    boolean grounded = false;
    int health = 100;
    int frameCount = 0;
    boolean crouch = false;
    ArrayList<Projectile> bullets = new ArrayList<Projectile>();
    Image[] sprites = new Image[12];
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
        sprites[2] = getImage("modules/SAMUS_SHOOT_RIGHT_1.png", 64, 128);
        sprites[3] = getImage("modules/SAMUS_SHOOT_RIGHT_2.png", 128, 128);
        sprites[4] = getImage("modules/SAMUS_SHOOT_RIGHT_3.png", 128, 128);
        sprites[5] = getImage("modules/SAMUS_SHOOT_LEFT_1.png", 64, 128);
        sprites[6] = getImage("modules/SAMUS_SHOOT_LEFT_2.png", 128, 128);
        sprites[7] = getImage("modules/SAMUS_SHOOT_LEFT_3.png", 128, 128);
        sprites[8] = getImage("modules/SAMUS_CROUCH_RIGHT_1.png", 64, 96);
        sprites[9] = getImage("modules/SAMUS_CROUCH_RIGHT_2.png", 64, 64);
        sprites[10] = getImage("modules/SAMUS_CROUCH_LEFT_1.png", 64, 96);
        sprites[11] = getImage("modules/SAMUS_CROUCH_LEFT_2.png", 64, 64);
        animation.add(0);
    }

    public void paint(Graphics g, int offSetX, int offSetY)
    {
        super.paint(g, offSetX, offSetY);
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
        if (grounded)
        {
            if (crouch == false)
            {
                if (xDirection == 1)
                {
                    // crouch right
                    animation.add(8);
                }
                else
                {
                    // crouch left
                    animation.add(10);
                }
                // add frame 1
            }
            // add frame 2
            if (xDirection == 1)
            {
                // crouch right
                animation.add(9);
            }
            else
            {
                // crouch left
                animation.add(11);
            }
            crouch = true;
        }
    }

    public void endCrouch()
    {
        if (crouch)
        {
            // add idle frame
            crouch = false;
            if (xDirection == 1)
            {
                animation.add(0);
            }
            else
            {
                animation.add(1);
            }
        }
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

        if (frameCount == 1)
        {
            int index = animation.poll();
            sprite = sprites[index];
            frameCount = 0;
            if (animation.isEmpty())
            {
                animation.add(index);
            }
        }
        else
        {
            frameCount++;
        }
    }

    public void shoot()
    {
        if (xDirection == 1)
        {
            animation.add(2);
            animation.add(3);
            animation.add(4);
            animation.add(0);
            bullets.add(new Projectile(x + width, y + (height / 4), 
                    64, 16, 
                    "modules/BULLET_RIGHT.png", xDirection
                )
            );
        }
        else
        {
            animation.add(5);
            animation.add(6);
            animation.add(7);
            animation.add(1);
            bullets.add(new Projectile(x, y + (height / 4), 
                    64, 16, 
                    "modules/BULLET_LEFT.png", xDirection
                )
            );
        }
    }
}