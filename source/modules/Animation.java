package modules;

import java.awt.Image;
import java.awt.Graphics;

public class Animation
{
    Image sprites;
    int frameWidth;
    int frameHeight;
    int imageWidth;
    int imageHeight;
    int frameCount;
    int frameDuration;
    boolean looped;
    int index = 0;

    public Animation(Image spriteSheet, int fw, int fh, int iw, int ih, int fc, int fd, boolean l)
    {
        sprites = spriteSheet;
        frameWidth = fw;
        frameHeight = fh;
        imageWidth = iw;
        imageHeight = ih;
        frameCount = fc;
        frameDuration = fd;
        looped = l;
    }

    public boolean render(Graphics g, int x, int y, int direction)
    {
        if ((index / frameDuration) + 1 > frameCount)
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
                x + imageWidth, y + imageHeight, 
                (index / frameDuration) * frameWidth, 0, 
                ((index / frameDuration) + 1) * frameWidth, frameHeight, 
                null
            );
            index++;
            return true;
        }
        else
        {
            g.drawImage(
                sprites, 
                x + imageWidth, y,
                x, y + imageHeight, 
                (index / frameDuration) * frameWidth, 0,
                ((index / frameDuration) + 1) * frameWidth, frameHeight, 
                null
            );
            index++;
            return true;
        }
    }
}