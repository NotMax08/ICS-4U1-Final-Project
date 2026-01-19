import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class ShieldPotionIcon here.
 * 
 * @author Julian
 * @version 2026
 */
public class ShieldPotionIcon extends ShopIcons
{
    private String description = "Gives player shield|Purchase for 25?";
    
    public ShieldPotionIcon()
    {
        image = new GreenfootImage("shieldptIcon.jpg");
        image.scale(image.getWidth()/18, image.getHeight()/18);
        imageSetup(image);
    }
    
    public void act()
    {
        super.act();
    }
    
    protected void description()
    {
        textWriter(description, true);
    }
}
