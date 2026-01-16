import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class HealthPotionIcon here.
 * 
 * @author Julian
 * @version 2025
 */
public class HealthPotionIcon extends ShopIcons
{
    private String description = "Restores 25 HP.|Purchase for 25 gems?"; 
    public HealthPotionIcon()
    {
        image = new GreenfootImage("healthptIcon.jpg");
        image.scale(image.getWidth()/18, image.getHeight()/18);
        imageSetup(image);
    }
    
    public void act()
    {
        super.act();
        if(!isHovering)
        {
            removeText();
        }
    }
    
    protected void description()
    {
        textWriter(description, true);
    }
}
