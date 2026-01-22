import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class SpeedPotionIcon here.
 * 
 * @author Julian
 * @version 2026
 */
public class SpeedPotionIcon extends ShopIcons
{
    private SpeedPotion speedPotion; 
    
    public SpeedPotionIcon()
    {
        image = new GreenfootImage("shieldptIcon.jpg");
        image.scale(image.getWidth()/18, image.getHeight()/18);
        imageSetup(image);
        
        speedPotion = new SpeedPotion(false);
        
        this.price = 10;
        this.itemIndex = 1;
        this.description = "Gives player speed|Purchase for $10?";
    }
    
    public void act()
    {
        super.act();
    }
    
    protected void description()
    {
        textWriter(description, true);
        if (speedPotion != null && speedPotion.getWorld() == null) {
            getWorld().addObject(speedPotion, getWorld().getWidth()/2 + potionX, getWorld().getHeight()/2 + potionY);
        }
    }
    
    protected void cleanUp()
    {
        removeText();
        if(getWorld() != null && speedPotion != null && speedPotion.getWorld() != null)
        {
            getWorld().removeObject(speedPotion);
        }
    }
    
    public void removeIcon()
    {
        cleanUp();
        getWorld().removeObject(this);
    }
}
