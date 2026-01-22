import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class ShieldPotion here.
 * 
 * @author Julian 
 * @version 2026
 * 
 * Rafaelchm on OpenGameArt.org
 */
public class SpeedPotion extends Items
{
    /**
     * @param isInInventory -> true if the object is in 
     * player's inventory 
     */
    public SpeedPotion(boolean isInInventory)
    {
        super(isInInventory);
        image = new GreenfootImage("pt2.png");
        image.scale(image.getWidth()/4, image.getHeight()/4);
        setImage(image);
    }
    
    /**
     * effect that increases player speed and lasts 60s
     * @param player -> player that gets the buff
     */
    @Override
    protected void effect(Player player)
    {
        player.activateSpeedBoost(); 
        player.updateItemCount(1, -1);
    }
}
