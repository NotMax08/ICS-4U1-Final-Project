import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Potions here.
 * 
 * @author Julian
 * @version 2026
 * 
 * Images all from Rafaelchm on OpenGameArt.org
 */
public abstract class Potions extends Actor
{
    public int price;
    public boolean inInventory = false; 
    public GreenfootImage image; 
    public void act()
    {
        // Add your action code here.
    }
    
    /**
     * Method to make a white square border on potion image when mouse hovers over image.
     */
    public void hoverEffect()
    {
        
    }
}
