package modules;

import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics;

public class Tile extends Entity
{
    boolean collision;

    public Tile(int posX, int posY, int l, int w, Color c, boolean b)
    {
        super(posX, posY, l, w, c);
        collision = b;
    }

    public Tile(int posX, int posY, int l, int w, Image i, boolean b)
    {
        super(posX, posY, l, w, i);
        collision = b;
    }

    public void paint(Graphics g, int offSetX, int offSetY)
    {
        if (color == null)
        {
            g.drawImage(
                sprite, 
                x - offSetX, y - offSetY,
                x - offSetX + length, y - offSetY + width,
                x, y,
                x + length, y + width,
                null
            );
        }
        else
        {
            g.drawRect(x - offSetX, y - offSetY, width, length);
        }
    }
}