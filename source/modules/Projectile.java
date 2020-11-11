package modules;

import java.awt.Color;

public class Projectile extends Entity
{
    int damage = 50;
    int dx = 25;
    int dy = 0;

    public Projectile(int posX, int posY, int w, int h, String spritePath, int direction)
    {
        super(posX, posY, w, h, spritePath, w, h);
        dx *= direction;
    }

    public void move()
    {
        x += dx;
        hitBox.setLocation(x, y);
    }
}
