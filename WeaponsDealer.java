import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class WeaponsDealer here.
 * 
 * @author Julian Xia 
 * @version 2026
 */
public class WeaponsDealer extends NPC
{
    public WeaponsDealer()
    {
        image = new GreenfootImage("weaponsDealer.png");
        image.scale(image.getWidth()/3, image.getHeight()/3);
        setImage(image);
    }
    
    public void act()
    {
        super.act();
    }

    public void dialogue()
    {
        textWriter("Would you like to | purchase weapons? [E]", true);
        //textBoxWriter(options);
        if (("e").equals(Greenfoot.getKey())) {
            removeText();
        }
    }
}
