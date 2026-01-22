import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Potions here.
 * 
 * @author Julian
 * @version 2026
 * 
 * 
 */
public abstract class ShopItems extends Actor
{
    protected int price;
    protected GreenfootImage image; 
    protected boolean isInInventory;
    public ShopItems(boolean isInInventory)
    {
        this.isInInventory = isInInventory;
    }
    
    public void act()
    {
        if(isInInventory)
        {
            if(Greenfoot.mouseClicked(this))
            {
                effect();
            }
        }
    }
    
    public void effect(){
        
    }
}
