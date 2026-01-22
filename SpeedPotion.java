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
    public SpeedPotion(boolean isInInventory)
    {
        super(isInInventory);
        image = new GreenfootImage("pt2.png");
        image.scale(image.getWidth()/4, image.getHeight()/4);
        setImage(image);
    }
    
        public void effect(Player player)
    {
        player.activateSpeedBoost(); 
        player.updateItemCount(1, -1);
        System.out.println("Speed Potion Activated!");
    }
}
