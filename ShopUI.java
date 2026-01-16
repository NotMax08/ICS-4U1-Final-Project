import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList; 

/**
 * Write a description of class ShopUI here.
 * 
 * @author Julian, Google Ai studio (helped style)
 * @version 2025
 */
public abstract class ShopUI extends Actor
{
    public GreenfootImage image;
    protected int startXOffset = -60; 
    protected int startYOffset = -40;
    protected int spacing = 10;
    
    public void act()
    {
        // Add your action code here.
    }
    
    protected void iconManager(ArrayList<ShopIcons> shopIcons)
    {
        System.out.println("nihao");
        //if (getWorld() == null) return;
        
        System.out.println("hello");
        for (int i = 0; i < shopIcons.size(); i++)
        {
            ShopIcons icon = shopIcons.get(i);
            
            int col = i % 3; // 0, 1, 2, 0, 1, 2...
            int row = i / 3; // 0, 0, 0, 1, 1, 1...

            // Get dimensions of the icon's image
            int iconW = icon.getImage().getWidth();
            int iconH = icon.getImage().getHeight();
            
            //This part done by Google AI studio
            // Calculate position
            // Center of ShopUI + starting offset + (the index * (size + 10px spacing))
            int xPos = getX() + startXOffset + (col * (iconW + spacing));
            int yPos = getY() + startYOffset + (row * (iconH + spacing));

            getWorld().addObject(icon, xPos, yPos);
        }
    }
}
