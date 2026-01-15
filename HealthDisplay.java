import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A display to show the health points of the player using skull icons
 * Skulls change from full to empty as the player takes damage
 * Adjusts to fit the player's max health as it increases
 * 
 * @author Robin
 */
public class HealthDisplay extends Display
{
    // Images
    private GreenfootImage fullSkull;
    private GreenfootImage emptySkull;
    
    // Constants
    private static final int SKULL_SIZE = 40;
    private static final int SKULL_SPACING = 20;
    
    private int lastHealth = -1;
    private int lastMaxHealth = -1;
    
    public HealthDisplay(int screenX, int screenY, Camera camera, Player player){
        super(screenX, screenY, camera, player);
        
        // Initialize images
        fullSkull = new GreenfootImage("healthFull.png");
        fullSkull.scale(SKULL_SIZE, SKULL_SIZE);
        
        emptySkull = new GreenfootImage("healthEmpty.png");
        emptySkull.scale(SKULL_SIZE, SKULL_SIZE);
        
        // Set intial display
        updateDisplay();
    }
    
    @Override
    protected void updateDisplay(){
        int currentHealth = player.getHealth();
        int maxHealth = player.getMaxHealth();
        int absMaxHealth = player.getAbsMaxHealth();
        
        if(currentHealth != lastHealth || maxHealth != lastMaxHealth){
            createHealthBar(currentHealth, maxHealth, absMaxHealth);
            lastHealth = currentHealth;
            lastMaxHealth = maxHealth;
        }
    }
    
    private void createHealthBar(int health, int maxHealth, int absMax){
        // Ensure valid values
        health = Math.max(0, Math.min(maxHealth, health));
        maxHealth = Math.max(1, maxHealth); // at least 1
        
        // Calculate total width needed for all skulls 
        int totalWidth = (SKULL_SIZE * absMax) + (SKULL_SPACING * absMax - 1);
        
        // Create image
        GreenfootImage healthBar = new GreenfootImage(totalWidth, SKULL_SIZE);
        
        // Draws each skull
        for(int i = 0 ; i < maxHealth; i++){
            int xPos = i * (SKULL_SIZE + SKULL_SPACING);
            
            // Draws full skull if this position is within current health
            // Draws empty skull if health is lower than this position
            // Doesnt draw an image if the max health is lower than the abs max
            if(i < health){
                healthBar.drawImage(fullSkull, xPos, 0);
            }else {
                healthBar.drawImage(emptySkull, xPos, 0);
            }
        }
        
        setImage(healthBar);
    }
}
