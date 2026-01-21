import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Key here.
 * 
 * @author Julian 
 * @version 2026
 */
public class Key extends ShopItems
{
    /**
     * Act - do whatever the Key wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        image = new GreenfootImage("key.png");
        image.scale(image.getWidth()/3, image.getHeight()/3);
        setImage(image);
    }
}
