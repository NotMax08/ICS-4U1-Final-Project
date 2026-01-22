import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Key here.
 * 
 * @author Julian 
 * @version 2026
 */
public class Key extends Items
{
    public Key(boolean isInInventory)
    {
        super(isInInventory);
        image = new GreenfootImage("key.png");
        image.scale(image.getWidth()/5, image.getHeight()/5);
        setImage(image);
    }
    public void act()
    {
    
    }
}
