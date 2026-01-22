import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class ShrinkPotionIcon here.
 * 
 * @author Julian
 * @version 2025
 */
public class ShrinkPotionIcon extends ShopIcons
{
    private ShrinkPotion shrinkPotion;
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


    protected void description()
    {
        textWriter(description, true);
        if (shrinkPotion != null && shrinkPotion.getWorld() == null) {
            getWorld().addObject(shrinkPotion, getWorld().getWidth()/2 + potionX, getWorld().getHeight()/2 + potionY);
        }
    }

    protected void cleanUp()
    {
        removeText();
        if(getWorld() != null && shrinkPotion != null && shrinkPotion.getWorld() != null)
        {
            getWorld().removeObject(shrinkPotion);
        }
    }

    public void removeIcon()
    {
        cleanUp();
        getWorld().removeObject(this);
    }
}
