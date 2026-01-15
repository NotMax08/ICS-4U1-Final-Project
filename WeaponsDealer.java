import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class WeaponsDealer here.
 * 
 * @author Julian Xia 
 * @version 2025
 */
public class WeaponsDealer extends NPC
{
    public void act()
    {
        // Add your action code here.
    }

    public void dialogue()
    {
        textBoxWriter("Would you like to | purchase weapons? [E]", true);
        //textBoxWriter(options);
        if (("e").equals(Greenfoot.getKey())) {
            removeText();
        }
    }
}
