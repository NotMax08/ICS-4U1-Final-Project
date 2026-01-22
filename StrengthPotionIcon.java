import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Icon that displays Strength potion and its description
 * 
 * @author Julian 
 * @version 2026
 */
public class StrengthPotionIcon extends ShopIcons
{
    private StrengthPotion strengthPotion;
    
    /** 
     * constructor for Strength potion
     */
    public StrengthPotionIcon()
    {
        image = new GreenfootImage("strengthptIcon.jpg");
        image.scale(image.getWidth()/18, image.getHeight()/18);
        imageSetup(image);
        
        strengthPotion = new StrengthPotion(false);
        
        this.price = 25;
        this.itemIndex = 0;
        this.description = "Increases damage to enemies|Purchase for 25?";
    }
    
    /**
     * write the description for strength potion
     */
    protected void description()
    {
        textWriter(description, true);
        if (strengthPotion != null && strengthPotion.getWorld() == null) {
            getWorld().addObject(strengthPotion, getWorld().getWidth()/2 + potionX, getWorld().getHeight()/2 + potionY);
        }
    }
    
    /** 
     * cleans everything up
     */
    protected void cleanUp()
    {
        removeText();
        if(getWorld() != null && strengthPotion != null && strengthPotion.getWorld() != null)
        {
            getWorld().removeObject(strengthPotion);
        }
    }
}
