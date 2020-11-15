package modules;

import java.awt.Image;
import java.awt.Graphics;

public class Animation
{
    Image sprites;
    int frameWidth;
    int frameHeight;
    int frameCount;
    boolean looped;
    int index = 0;

    public Animation(Image spriteSheet, int w, int h, int fc, boolean l)
    {
        sprites = spriteSheet;
        frameWidth = w;
        frameHeight = h;
        frameCount = fc;
        looped = l;
    }

    public boolean render(Graphics g, int x, int y, int direction)
    {
        if (index + 1 >= frameCount)
        {
            if (looped)
            {
                index = 0;
            }
            else
            {
                return false;
            }
        }
        if (direction == 1)
        {
            g.drawImage(
                sprites, 
                x, y, 
                x + frameWidth, y + frameHeight, 
                index * frameWidth, 0, 
                (index + 1) * frameWidth, frameHeight, 
                null
            );
            index++;
            return true;
        }
        else
        {
            g.drawImage(
                sprites, 
                x + frameWidth, y,
                x - frameWidth, y + frameHeight, 
                index * frameWidth, 0,
                (index + 1) * frameWidth, frameHeight, 
                null
            );
            index++;
            return true;
        }
    }
}