import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Displays the ability icons, fixed to a position of the camera screen
 * 
 * 
 * @author Robin 
 */
public class AbilityDisplay extends ScrollingActor
{
    // Image variables
    private GreenfootImage slashIcon, magicIcon;
    private GreenfootImage currentDisplay;
    private int screenX, screenY;
    private Player player;
    
    // Image constants
    private static final int ACTIVE_TRANSPARENCY = 240;
    private static final int COOLDOWN_TRANSPARENCY = 100;
    private static final int HIDDEN_TRANSPARENCY = 0;
    private static final int ICON_SPACING = 95; // to change

    public AbilityDisplay(int screenX, int screenY, Camera camera, Player player){
        super(camera);
        this.screenX = screenX;
        this.screenY = screenY;
        this.player = player;

        // Scale images
        slashIcon = new GreenfootImage("slashIcon.png");
        slashIcon.scale(85, 85);
        
        magicIcon = new GreenfootImage("magicIcon.png");
        magicIcon.scale(85, 85);
        
        updateDisplay();
    }

    public void act()
    {
        setLocation(screenX, screenY); // Keep at fixed location
        updateDisplay();
    }
    
    private void updateDisplay(){
        boolean magicUnlocked = GameWorld.magicUnlocked;
        int cooldown = player.getAbilityCooldown();
        boolean onCooldown = cooldown > 0;
        
        int transparency = onCooldown ? COOLDOWN_TRANSPARENCY : ACTIVE_TRANSPARENCY;
        
        int totalWidth = magicUnlocked ? (85 * 2 + ICON_SPACING) : 85;
        
        currentDisplay = new GreenfootImage(totalWidth, 85);
        
        GreenfootImage slashCopy = new GreenfootImage(slashIcon);
        slashCopy.setTransparency(transparency);
        currentDisplay.drawImage(slashCopy, 0, 0);
        
        if(magicUnlocked){
            GreenfootImage magicCopy = new GreenfootImage(magicIcon);
            magicCopy.setTransparency(transparency);
            currentDisplay.drawImage(magicCopy, 85 + ICON_SPACING, 0);
        }
        
        setImage(currentDisplay);
    }

    private void cycleCooldown(){
        int num = player.getAbilityCooldown();
        if(!GameWorld.magicUnlocked){
            magicIcon.setTransparency(0);
            if(num == 0){
                slashIcon.setTransparency(240);
            }else{
                slashIcon.setTransparency(100);
            }
        }else {
            if(num == 0){
                slashIcon.setTransparency(240);
                magicIcon.setTransparency(240);
            }else{
                slashIcon.setTransparency(100);
                magicIcon.setTransparency(100);
            }
        }
    }
}
