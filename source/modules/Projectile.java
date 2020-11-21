package modules;

import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics;
import java.util.concurrent.ThreadLocalRandom;

public class Projectile extends Entity
{
    int damage;
    int dx = 25;
    int dy = 0;
    int xDirection;
    final int frameWidth = 27;
    final int frameHeight = 12;
    static final int imageWidth = 64;
    static final int imageHeight = 32;
    static final int bullet = 0;
    static final int beam = 1;
    int weaponType;
    Image bulletSprite = getImage("sprites/BULLET.png", imageWidth, imageHeight);
    Animation beamSprite = new Animation(
        getImage("sprites/BEAM.png", 3 * frameWidth, frameHeight), 
        frameWidth, frameHeight, imageWidth, imageHeight,
        3, 1, true
    );

    public Projectile(int posX, int posY, int direction, int weapon)
    {
        super(posX, posY, imageWidth, imageHeight, null);
        weaponType = weapon;
        xDirection = direction;
        dx *= direction;
        damage = ThreadLocalRandom.current().nextInt(25, 101);
    }

    public void move()
    {
        x += dx;
        hitBox.setLocation(x, y);
    }

    public void render(Graphics g, int offSetX, int offSetY)
    {
        if (dx > 0)
        {
            if (weaponType == bullet)
            {
                g.drawImage(bulletSprite, x - offSetX, y - offSetY, width, height, null);
            }
            else
            {
                beamSprite.render(g, x - offSetX, y - offSetY, xDirection);
            }
        }
        else
        {
            if (weaponType == bullet)
            {
                g.drawImage(bulletSprite, x + width - offSetX, y - offSetY, -width, height, null);
            }
            else
            {
                beamSprite.render(g, x - offSetX, y - offSetY, xDirection);
            }
        }
    }
}
