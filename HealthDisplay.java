import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class HealthDisplay here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HealthDisplay extends ScrollingActor
{
    // Images
    private GreenfootImage full = new GreenfootImage("healthFull.png");
    private GreenfootImage empty = new GreenfootImage("healthEmpty.png");
    
    private int screenX, screenY;
    private Player player;
    
    public HealthDisplay(int screenX, int screenY, Camera camera){
        super(camera);
        this.screenX = screenX;
        this.screenY = screenY;
        
    }
    
    /**
     * Act - do whatever the HealthDisplay wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
    }
}
