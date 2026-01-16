import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A display to show the amount of mana the player has 
 * Mana is gained everytime the player hits an enemy
 * Mana is used to cast the magic ability 
 * 
 * @author Robin
 */
public class ManaDisplay extends Display
{
    // Images
    private GreenfootImage[] manaImages;
    
    // Constants
    private static final int MANA_WIDTH = 75;
    private static final int MANA_HEIGHT = 20;
    
    public ManaDisplay(int screenX, int screenY, Camera camera, Player player){
        super(screenX, screenY, camera, player);
        
        initializeImages();
    }
    
    private void initializeImages(){
        manaImages = new GreenfootImage[9]; // 0 through 8
        for(int i = 0; i < manaImages.length; i++){
            manaImages[i] = new GreenfootImage("mana" + i + ".png");
        }
    }
    
    @Override
    protected void updateDisplay(){
        
    }
    /**
     * Act - do whatever the ManaDisplay wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
    }
}
