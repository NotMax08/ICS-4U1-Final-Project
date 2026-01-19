import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class StrengthPotionIcon here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class StrengthPotionIcon extends ShopIcons
{
    private String description = "Increases damage to enemies|Purchase for 25?";
    
    public StrengthPotionIcon()
    {
        image = new GreenfootImage("strengthptIcon.jpg");
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
