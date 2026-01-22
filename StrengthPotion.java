import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class StrengthPotion here.
 * 
 * @author Julian 
 * @version 2026
 * 
 * Credits: Rafaelchm on OpenGameArt.org
 */
public class StrengthPotion extends Items
{
    /**
     * @param isInInventory -> true if the object is in 
     * player's inventory 
     */
    public StrengthPotion(boolean isInInventory)
    {
        super(isInInventory);
        image = new GreenfootImage("pt4.png");
        image.scale(image.getWidth()/4, image.getHeight()/4);
        setImage(image);
    }
    
    /**
     * effect that increases player damage by 3 and lasts 30s
     * @param player -> player that gets the buff
     */
    @Override
    public void effect(Player player) {
        player.activateStrengthBoost(); 
        player.updateItemCount(0, -1); 
    }
}
