import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Items class abstract base for all items and usable objects in game
 * Manages images, and handles click to use when items are in 
 * inventory. 
 * 
 * @author Julian
 * @version 2026
 * 
 * 
 */
public abstract class Items extends Actor
{
    protected GreenfootImage image; 
    protected boolean isInInventory;
    
    /**
     * Constructor for the items class
     * 
     * @param isInInventory -> true if the item is being created
     * for the inventory, false if it is in the gameWorld. 
     */
    public Items(boolean isInInventory) {
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
    
    /**
     * Each usable item must have its own effect in inventory
     * Must be overriden by item subclasses
     * 
     * @param player The player object that will get the items effect
     */
    protected void effect(Player player)
    {
        
    }
}
