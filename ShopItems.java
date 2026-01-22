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
    protected GreenfootImage image; 
    protected boolean isInInventory;
    
    public ShopItems(boolean isInInventory) {
        this.isInInventory = isInInventory;
    }
    
    public void act() {
        // If this is in the inventory and is clicked
        if (isInInventory && Greenfoot.mouseClicked(this)) {
            // Find the player in the world
            Player p = (Player) getWorld().getObjects(Player.class).get(0);
            effect(p);
        }
    }
    
    public void effect(Player player)
    {
        
    }
}
