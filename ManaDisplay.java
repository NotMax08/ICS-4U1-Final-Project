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
    private static final int MANA_WIDTH = 300;
    private static final int MANA_HEIGHT = 55;
    
    private int previousMana = -1;
    
    public ManaDisplay(int screenX, int screenY, Camera camera, Player player){
        super(screenX, screenY, camera, player);
        
        initializeImages();
    }
    
    private void initializeImages(){
        manaImages = new GreenfootImage[9]; // 0 through 8
        for(int i = 0; i < manaImages.length; i++){
            manaImages[i] = new GreenfootImage("mana" + i + ".png");
            manaImages[i].scale(MANA_WIDTH, MANA_HEIGHT);
        }
        updateDisplay();
    }
    
    @Override
    protected void updateDisplay(){
        int currentMana = Math.min(player.getMana(), 8);
        
        if(currentMana != previousMana){
            setImage(manaImages[currentMana]);
            previousMana = currentMana;
        }
    }
}
