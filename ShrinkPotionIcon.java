import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 *  Icon that displays Shrink potion and its description
 * 
 * @author Julian
 * @version 2025
 */
public class ShrinkPotionIcon extends ShopIcons
{
    private ShrinkPotion shrinkPotion;
    /** 
     * Constructor for the Shrink Potion Icon
     */
    public ShrinkPotionIcon()
    {
        image = new GreenfootImage("shrinkptIcon.jpg");
        image.scale(image.getWidth()/18, image.getHeight()/18);
        imageSetup(image);
        
        shrinkPotion = new ShrinkPotion(false);
        
        this.price = 15;
        this.itemIndex = 2;
        this.description = "Shrinks player| Purchase for 15 orbs?"; 
    }

    /**
     * Writes the description of the Potion
     */
    protected void description()
    {
        textWriter(description, true);
        if (shrinkPotion != null && shrinkPotion.getWorld() == null) {
            getWorld().addObject(shrinkPotion, getWorld().getWidth()/2 + potionX, getWorld().getHeight()/2 + potionY);
        }
    }
    
    /**
     * Cleans everything up once mouse stops hovering over icon or
     * player closes store
     */
    protected void cleanUp()
    {
        removeText();
        if(getWorld() != null && shrinkPotion != null && shrinkPotion.getWorld() != null)
        {
            getWorld().removeObject(shrinkPotion);
        }
    }
}
