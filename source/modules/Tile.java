package modules;

import java.awt.Color;
import java.awt.Graphics;

public class Tile extends Entity
{
    boolean collision;

    public Tile(int posX, int posY, int l, int w, Color c, boolean b)
    {
        super(posX, posY, l, w, c);
        collision = b;
    }

    public void paint(Graphics g, int offSetX, int offSetY)
    {
        g.drawRect(x - offSetX, y - offSetY, width, length);
    }
}