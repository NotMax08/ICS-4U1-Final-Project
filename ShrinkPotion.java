import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class HealthPotion here.
 * 
 * @author Julian 
 * @version 2026
 * 
 * Credits: Rafaelchm on OpenGameArt.org 
 */
public class ShrinkPotion extends ShopItems
{
    public ShrinkPotion(boolean isInInventory)
    {
        super(isInInventory);
        image = new GreenfootImage("pt3.png");
        image.scale(image.getWidth()/4, image.getHeight()/4);
        setImage(image);
    }
    public void act()
    {
        // Add your action code here.
    }
}
