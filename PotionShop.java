import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class PotionShop here.
 * 
 * @author Julian
 * @version 2025
 */
public class PotionShop extends ShopUI
{
    ArrayList<ShopIcons> items;
    public PotionShop()
    {
        image = new GreenfootImage ("potionsUI.png");
        image.scale(image.getWidth() + 100, image.getHeight() + 100);
        setImage(image);
    }
    
    public void act()
    {
        // Add your action code here.
    }
    
    protected void addedToWorld(World world)
    {
        addItems();
    }
    
    private void addItems()
    {
        items = new ArrayList<>();
        items.add(new HealthPotionIcon());
        items.add(new HealthPotionIcon());
        items.add(new HealthPotionIcon());
        items.add(new HealthPotionIcon());
        // Call the manager from the superclass
        iconManager(items);
    }
}
