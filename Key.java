import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Key here.
 * 
 * @author Julian 
 * @version 2026
 */
public class Key extends ShopItems
{
    public Key(boolean isInInventory)
    {
        super(isInInventory);
        image = new GreenfootImage("key.png");
        image.scale(image.getWidth()/3, image.getHeight()/3);
        setImage(image);
    }
    public void act()
    {
    
    }
}
