package modules;

import java.awt.Color;

public class Projectile extends Entity
{
    int damage = 100;
    int dx = 25;
    int dy = 0;

    public Projectile(int posX, int posY, int l, int w, Color c, int direction)
    {
        super(posX, posY, l, w, c);
        dx *= direction;
    }

    public void move()
    {
        x += dx;
        hitBox.setLocation(x, y);
    }
}
