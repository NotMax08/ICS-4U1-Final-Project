import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A display to show the health points of the player
 * 
 * @author Robin
 */
public class HealthDisplay extends Display
{
    // Images
    private GreenfootImage full;
    private GreenfootImage empty;
    public HealthDisplay(int screenX, int screenY, Camera camera, Player player){
        super(screenX, screenY, camera, player);
        
        
        // Initialize images
        full = new GreenfootImage("healthFull.png");
        empty = new GreenfootImage("healthEmpty.png");
        
        // Set intial image
        updateDisplay();
    }
    
    @Override
    protected void updateDisplay(){
        //TODO: implement display
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
