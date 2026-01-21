import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class ShieldPotionIcon here.
 * 
 * @author Julian
 * @version 2026
 */
public class KeyIcon extends ShopIcons
{
    private Key key; 
    
    public KeyIcon()
    {
        image = new GreenfootImage("keyIcon.jpg");
        image.scale(image.getWidth()/18, image.getHeight()/18);
        imageSetup(image);
        
        key = new Key();
        
        this.price = 0;
        this.itemIndex = 1;
        this.description = "???";
    }
    
    public void act()
    {
        super.act();
    }
    
    protected void description()
    {
        textWriter(description, false);
        if (key != null && key.getWorld() == null) {
            getWorld().addObject(key, getWorld().getWidth()/2 + potionX, getWorld().getHeight()/2 + potionY);
        }
    }
    
    protected void cleanUp()
    {
        removeText();
        if(getWorld() != null && key != null && key.getWorld() != null)
        {
            getWorld().removeObject(key);
        }
    }
    
    public void removeIcon()
    {
        cleanUp();
        getWorld().removeObject(this);
    }
}
