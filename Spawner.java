import greenfoot.*;

/**
 * Spawner for room three for mushroom mobs.
 * 
 * @author Paul
 */
public class Spawner extends ScrollingActor {
    GreenfootImage spawner = new GreenfootImage("spawner.png");
    private final int MAX_ENEMY_COUNT = 6;
    private int health = 40;
    ScrollingStatBar healthBar;
    int healthBarYOffset = 100;
    int timer = 0;
    int spawnCooldown = 200; 
    int enemiesSpawned = 0;
    Camera camera;
    
    /**
     * spawner constructor for room three
     * 
     * @param camera for reference to update
     */
    public Spawner(Camera camera) {
        super(camera);
        this.camera = camera;
        spawner.scale(200, 250);
        setImage(spawner);
    }
    
    @Override
    protected void addedToWorld(World world) {
        healthBar = new ScrollingStatBar(camera, this, health, 70, 6, healthBarYOffset);
        world.addObject(healthBar, 0, 0);
        updateHealthBarPosition();
    }
    
    /**
     * spawns enemies until counter limit
     */
    public void act() {
        timer++;
        updateHealthBarPosition();
        
        // Spawn enemy every spawnCooldown acts
        if (timer >= spawnCooldown && enemiesSpawned < MAX_ENEMY_COUNT) {
            spawnEnemy();
            timer = 0;
        }
    }
    
    /**
     * Creates and adds Fungi to the world near the spawner
     */
    public void spawnEnemy() {
        GameWorld world = (GameWorld) getWorld();
        if (world == null) return;
        
        // Spawn offset from spawner position
        int spawnOffsetX = Greenfoot.getRandomNumber(200) - 100; // Random -100 to +100
        int spawnOffsetY = 50; // Spawn slightly below spawner
        
        int spawnWorldX = worldX + spawnOffsetX;
        int spawnWorldY = worldY + spawnOffsetY;
        
        // Convert to screen coordinates
        int spawnScreenX = camera.worldToScreenX(spawnWorldX);
        int spawnScreenY = camera.worldToScreenY(spawnWorldY);
        
        // Create and add Fungi
        Fungi fungi = new Fungi(camera);
        world.addObject(fungi, spawnScreenX, spawnScreenY);
        fungi.setWorldPosition(spawnWorldX, spawnWorldY);
        
        enemiesSpawned++;
    }
    
    protected void updateHealthBarPosition() {
        if (healthBar != null) {
            healthBar.setWorldPosition(worldX, worldY + healthBarYOffset);
        }
    }
    
    public void takeDamage(int dmg) {
        World world = getWorld();
        health -= dmg;
        
        // Update health bar
        if (healthBar != null) {
            healthBar.update(health);
        }
        
        if (health <= 0) {
            // Remove health bar
            if (healthBar != null && healthBar.getWorld() != null) {
                world.removeObject(healthBar);
            }
            world.removeObject(this);
        }
    }
}