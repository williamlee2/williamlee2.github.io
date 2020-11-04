package modules;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Image;
import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;

public class Entity implements Comparable<Entity>
{
    Integer x = 0;
    int y = 0;
    int height = 0;
    int width = 0;
    Color color = null;
    Rectangle hitBox;
    Image sprite = null;

    public Entity(int posX, int posY, int w, int h, Color c)
    {
        x = posX;
        y = posY;
        height = h;
        width = w;
        color = c;
        hitBox = new Rectangle(x, y, width, height);
    }

    public Entity(int posX, int posY, int w, int h, String spritePath, int spriteWidth, int spriteHeight)
    {
        x = posX;
        y = posY;
        height = h;
        width = w;
        sprite = getImage(spritePath, spriteWidth, spriteHeight);
        if (sprite == null)
        {
            color = Color.RED;
        }
        hitBox = new Rectangle(x, y, width, height);
    }

    public Image getImage(String fileName, int width, int height)
    {
        try 
        {
            BufferedImage bufferedSprite = ImageIO.read(new File(fileName));
            return bufferedSprite.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        } 
        catch (IOException e) 
        {
            System.out.println("Failed to load background");
            return null;
        }
    }

    public void paint(Graphics g, int offSetX, int offSetY)
    {
        if (sprite == null)
        {
            g.setColor(color);
            g.fillRect(x - offSetX, y - offSetY, width, height);
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