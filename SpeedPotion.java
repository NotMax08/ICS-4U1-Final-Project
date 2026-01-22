import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class ShieldPotion here.
 * 
 * @author Julian 
 * @version 2026
 * 
 * Rafaelchm on OpenGameArt.org
 */
public class SpeedPotion extends ShopItems
{
    public SpeedPotion(boolean isInInventory)
    {
        super(isInInventory);
        image = new GreenfootImage("pt2.png");
        image.scale(image.getWidth()/4, image.getHeight()/4);
        setImage(image);
    }
    public void act()
    {
        // Add your action code here.
    }
    
        public void effect(Player player)
    {
        player.activateSpeedBoost(); 
        player.updateItemCount(1, -1);
        System.out.println("Speed Potion Activated!");
    }
}
