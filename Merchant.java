import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Merchant here.
 * 
 * @author Julian 
 * @version 2025
 */
public class Merchant extends NPC
{
    private GreenfootImage image;
    
    public Merchant()
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
        TextBox options = new TextBox("Would you like to purchase weapons? [E]", fontSize);
        textBoxWriter(options);
        if (("e").equals(Greenfoot.getKey())  && options != null) {
                getWorld().removeObject(options);
        }
    }
}
