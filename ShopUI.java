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
    protected int startXOffset = 60; 
    protected int startYOffset = -97;
    protected int spacing = 10;
    
    private ArrayList<ShopIcons> activeIcons = new ArrayList<>();
    public void act()
    {
        // Add your action code here.
    }
    
    protected void iconManager(ArrayList<ShopIcons> shopIcons)
    {
        if (getWorld() == null) return;
        
        removeIcons();
        
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
            activeIcons.add(icon);
        }
    }
    
    public void removeIcons()
    {
        if (getWorld() == null) return;

        for (ShopIcons icon : activeIcons)
        {
            if (icon.getWorld() != null)
            {
                icon.removeText(); 
                getWorld().removeObject(icon);
            }
        }
        //clear references
        activeIcons.clear();
    }
}
