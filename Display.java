import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Superclass for objects that need to be displayed on the screen during the game
 * 
 * @author Robin
 */
public abstract class Display extends ScrollingActor
{
    // Images
    protected GreenfootImage activeImage;
    protected GreenfootImage inactiveImage;
    protected int screenX, screenY;
    
    protected boolean isActive;
    
    public Display(Camera camera){
        super(camera);
    }
    
    
    public void act()
    {
        // Add your action code here.
    }
}
