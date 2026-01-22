import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class HealthPotion here.
 * 
 * @author Julian 
 * @version 2026
 * 
 * Credits: Rafaelchm on OpenGameArt.org 
 */
public class ShrinkPotion extends Items
{
    /**
     * @param isInInventory -> true if the object is in 
     * player's inventory 
     */
    public ShrinkPotion(boolean isInInventory)
    {
        super(isInInventory);
        image = new GreenfootImage("pt3.png");
        image.scale(image.getWidth()/4, image.getHeight()/4);
        setImage(image);
    }
    
    /**
     * effect that shrinks player size down for 10 seconds
     * @param player -> player that gets the buff
     */
    @Override
    protected void effect(Player player)
    {
        player.shrinkPlayer();
        player.updateItemCount(2, -1); // decrease count at index 2
    }
}

