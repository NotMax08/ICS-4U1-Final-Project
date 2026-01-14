import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Displays the ability icons, fixed to a position of the camera screen
 * 
 * @author Robin 
 */
public class AbilityDisplay extends Display
{
    // Image variables
    private GreenfootImage slashIcon, magicIcon;
    private GreenfootImage activeDisplay;
    private GreenfootImage cooldownDisplay;
    private int screenX, screenY;
    private Player player;

    // State tracking
    private boolean lastMagicState = false;
    private int lastCooldown = -1;

    // Image constants
    private static final int ACTIVE_TRANSPARENCY = 240;
    private static final int COOLDOWN_TRANSPARENCY = 100;
    private static final int HIDDEN_TRANSPARENCY = 0;
    private static final int ICON_SIZE = 85;
    private static final int ICON_SPACING = 10;

    public AbilityDisplay(int screenX, int screenY, Camera camera, Player player){
        super(camera);
        this.screenX = screenX;
        this.screenY = screenY;
        this.player = player;

        // Scale images
        slashIcon = new GreenfootImage("slashIcon.png");
        slashIcon.scale(ICON_SIZE, ICON_SIZE);

        magicIcon = new GreenfootImage("magicIcon.png");
        magicIcon.scale(ICON_SIZE, ICON_SIZE);

        initializeDisplays();
    }

    public void act()
    {
        setLocation(screenX, screenY); // Keep at fixed location
        updateDisplay();
    }

    private void initializeDisplays(){
        boolean magicUnlocked = GameWorld.magicUnlocked;
        activeDisplay = createDisplay(ACTIVE_TRANSPARENCY, magicUnlocked);
        cooldownDisplay = createDisplay(COOLDOWN_TRANSPARENCY, magicUnlocked);
        lastMagicState = magicUnlocked;
    }

    private GreenfootImage createDisplay(int transparency, boolean magicUnlocked){
        int totalWidth = magicUnlocked ? (ICON_SIZE * 3 + 25) : ICON_SIZE;
        GreenfootImage display = new GreenfootImage(totalWidth, ICON_SIZE);

        if(magicUnlocked){
            GreenfootImage magicCopy = new GreenfootImage(magicIcon);
            magicCopy.setTransparency(transparency);
            display.drawImage(magicCopy, 0, 0);

            GreenfootImage slashCopy = new GreenfootImage(slashIcon);
            slashCopy.setTransparency(transparency);
            display.drawImage(slashCopy, ICON_SIZE + ICON_SPACING, 0);
        }else{
            GreenfootImage slashCopy = new GreenfootImage(slashIcon);
            slashCopy.setTransparency(transparency);
            display.drawImage(slashCopy, 0, 0);
        }

        return display;
    }

    private void updateDisplay(){
        boolean magicUnlocked = GameWorld.magicUnlocked;
        int cooldown = player.getAbilityCooldown();
        boolean onCooldown = cooldown > 0;

        if(magicUnlocked != lastMagicState){
            initializeDisplays();

            lastCooldown = -1;
        }

        if(cooldown != lastCooldown){
            setImage(onCooldown ? cooldownDisplay : activeDisplay);
            lastCooldown = cooldown;
        }
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
