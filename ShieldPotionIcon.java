import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class ShieldPotionIcon here.
 * 
 * @author Julian
 * @version 2026
 */
public class ShieldPotionIcon extends ShopIcons
{
    private ShieldPotion shieldPotion; 
    
    public ShieldPotionIcon()
    {
        image = new GreenfootImage("shieldptIcon.jpg");
        image.scale(image.getWidth()/18, image.getHeight()/18);
        imageSetup(image);
        
        shieldPotion = new ShieldPotion(false);
        
        this.price = 10;
        this.itemIndex = 1;
        this.description = "Gives player shield|Purchase for $10?";
    }
    
    public void act()
    {
        super.act();
    }
    
    protected void description()
    {
        textWriter(description, true);
        if (shieldPotion != null && shieldPotion.getWorld() == null) {
            getWorld().addObject(shieldPotion, getWorld().getWidth()/2 + potionX, getWorld().getHeight()/2 + potionY);
        }
    }
    
    protected void cleanUp()
    {
        removeText();
        if(getWorld() != null && shieldPotion != null && shieldPotion.getWorld() != null)
        {
            getWorld().removeObject(shieldPotion);
        }
    }
    
    public void removeIcon()
    {
        cleanUp();
        getWorld().removeObject(this);
    }
}
