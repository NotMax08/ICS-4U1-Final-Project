import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Key Item, used to unlock the boss room
 * 
 * @author Julian 
 * @version 2026
 */
public class Key extends Items
{
    /**
     * @param isInInventory -> true if the object is in 
     * player's inventory 
     */
    public Key(boolean isInInventory)
    {
        super(isInInventory);
        image = new GreenfootImage("key.png");
        image.scale(image.getWidth()/5, image.getHeight()/5);
        setImage(image);
    }
}
