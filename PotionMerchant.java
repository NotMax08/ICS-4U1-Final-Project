import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Merchant here.
 * 
 * @author Julian 
 * @version 2025
 */
public class PotionMerchant extends NPC
{   
    public PotionMerchant()
    {
        image = new GreenfootImage("potionMerchant.png");
        image.scale(image.getWidth()/3, image.getHeight()/3);
        setImage(image);
    }
    
    public void act()
    {
        super.act();
    }
    
    public void dialogue()
    {
        textBoxWriter("Would you like to | purchase potions? [E]", true);
        //textBoxWriter(options);
        if (("e").equals(Greenfoot.getKey())) {
                removeText();
        }
    }
    
    private void openShop()
    {
        
    }
}
