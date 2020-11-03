package modules;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Image;
import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;

public class Entity implements Comparable<Entity>
{
    Integer x = 0;
    int y = 0;
    int length = 0;
    int width = 0;
    Color color = null;
    Rectangle hitBox;
    Image sprite = null;

    public Entity(int posX, int posY, int l, int w, Color c)
    {
        x = posX;
        y = posY;
        length = l;
        width = w;
        color = c;
        hitBox = new Rectangle(x, y, width, length);
    }

    public Entity(int posX, int posY, int l, int w, Image i)
    {
        x = posX;
        y = posY;
        length = l;
        width = w;
        sprite = i;
        hitBox = new Rectangle(x, y, width, length);
    }

    public void paint(Graphics g, int offSetX, int offSetY)
    {
        if (sprite == null)
        {
            g.setColor(color);
            g.fillRect(x - offSetX, y - offSetY, length, width);
        }
        else
        {
            g.drawImage(sprite, x - offSetX, y - offSetY, null);
        }
    }

    public int compareTo(Entity e) 
    {
        return this.x.compareTo(e.x);
    }
}