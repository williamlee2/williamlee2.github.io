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

    public Tile(int posX, int posY, int w, int h, String spritePath, int spriteWidth, int spriteHeight, boolean b)
    {
        super(posX, posY, w, h, spritePath, spriteWidth, spriteHeight);
        collision = b;
    }

    public void paint(Graphics g, int offSetX, int offSetY)
    {
        if (color == null)
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
            super.paint(g, offSetX, offSetY);
        }
    }
}