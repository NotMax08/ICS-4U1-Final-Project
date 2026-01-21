import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Superclass for objects that need to be displayed on the screen during the game.
 * Handles common functionality for icons that scroll with the screen
 * 
 * @author Robin and Claude
 */
public abstract class Display extends ScrollingActor
{
    // Images
    protected GreenfootImage activeImage;
    protected GreenfootImage inactiveImage;
    // Fixed position on screen
    protected int screenX, screenY;
    // Player reference
    protected Player player;
    
    /**
     * Constructor for displays that do not need player reference
     */
    public Display(int screenX, int screenY, Camera camera){
        super(camera);
        this.screenX = screenX;
        this.screenY = screenY;
    }
    /**
     * Constructor for displays that need player reference
     */    
    public Display(int screenX, int screenY, Camera camera, Player player){
        super(camera);
        this.screenX = screenX;
        this.screenY = screenY;
        this.player = player;
    }
    
    public void act()
    {
        maintainScreenPosition();
        updateDisplay();
    }
    
    // Keeps the displays at the desired locations
    protected void maintainScreenPosition(){
        setLocation(screenX, screenY);
    }
    
    protected abstract void updateDisplay();
    
    /**
     * Helper method to create scaled images
     */
    protected GreenfootImage scaleImage(String filename, int width, int height){
        GreenfootImage img = new GreenfootImage(filename);
        img.scale(width, height);
        return img;
    }
    
    /**
     * Helper method to create a scaled image with transparency
     */
    protected GreenfootImage scaleImage(String filename, int width, int height, int transparency){
        GreenfootImage img = new GreenfootImage(filename);
        img.scale(width, height);
        img.setTransparency(transparency);
        return img;
    }
    
    // Getters for screen position
    public int getScreenX(){
        return screenX;
    }
    public int getScreenY(){
        return screenY;
    }
    // Setters to change screen position
    public void setScreenPosition(int x, int y){
        this.screenX = x;
        this.screenY = y;
    }

}
