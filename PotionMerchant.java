import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Merchant here.
 * 
 * @author Julian 
 * @version 2025
 */
public class PotionMerchant extends NPC
{
    private GreenfootImage image;
    
    public PotionMerchant()
    {
        image = new GreenfootImage("merchant.png");
        image.scale(image.getWidth()/3, image.getHeight()/3);
        setImage(image);
    }
    public void act()
    {
        super.act();
    }
    
    public void dialogue()
    {
        textBoxWriter("Would you like to | purchase weapons? [E]", true);
        //textBoxWriter(options);
        if (("e").equals(Greenfoot.getKey())) {
                removeText();
        }
    }
}
