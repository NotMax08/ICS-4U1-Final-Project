import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class ShieldPotion here.
 * 
 * @author Julian 
 * @version 2026
 * 
 * Rafaelchm on OpenGameArt.org
 */
public class ShieldPotion extends ShopItems
{
    public ShieldPotion(boolean isInInventory)
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
}
