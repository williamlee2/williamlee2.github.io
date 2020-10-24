package modules;

import java.awt.Color;

public class Tile extends Entity
{
    boolean collision;

    public Tile(int posX, int posY, int l, int w, Color c, boolean b)
    {
        super(posX, posY, l, w, c);
        collision = b;
    }
}