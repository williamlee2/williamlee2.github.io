package modules;

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.ThreadLocalRandom;

public class Projectile extends Entity
{
    int damage;
    int dx = 25;
    int dy = 0;

    public Projectile(int posX, int posY, int w, int h, String spritePath, int direction)
    {
        super(posX, posY, w, h, spritePath, w, h);
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
            g.drawImage(sprite, x, y, width, height, null);
        }
        else
        {
            g.drawImage(sprite, x + width, y, -width, height, null);
        }
    }
}
