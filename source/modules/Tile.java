package modules;

import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics;

public class Tile extends Entity
{
    boolean collision;

    public Tile(int posX, int posY, int w, int h, Color c, boolean b)
    {
        super(posX, posY, w, h, c);
        collision = b;
    }

    public Tile(int posX, int posY, int w, int h, Image background, boolean b)
    {
        super(posX, posY, w, h, Color.BLACK);
        sprite = background;
        collision = b;
    }

    public void paint(Graphics g, int offSetX, int offSetY)
    {
        if (sprite != null)
        {
            g.drawImage(
                sprite, 
                x - offSetX, y - offSetY,
                x - offSetX + width, y - offSetY + height,
                x, y,
                x + width, y + height,
                null
            );
        }
        else
        {
            g.setColor(color);
            g.drawRect(x, y, width, height);
        }
    }
}