package modules;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Image;
import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;

public class Entity
{
    int x = 0;
    int y = 0;
    int length = 0;
    int width = 0;
    Color color;
    Rectangle hitBox;
    Image sprite;

    public Entity(int posX, int posY, int l, int w, Color c)
    {
        x = posX;
        y = posY;
        length = l;
        width = w;
        color = c;
        hitBox = new Rectangle(x, y, width, length);
    }

    public void paint(Graphics g, int offSetX, int offSetY)
    {
        g.setColor(color);
        g.fillRect(x - offSetX, y - offSetY, width, length);
    }

    public void move()
    {
        int dx = ThreadLocalRandom.current().nextInt(-1, 1);
        int dy = ThreadLocalRandom.current().nextInt(-1, 1);
        x += 50 * dx;
        y += 50 * dy;
    }
}