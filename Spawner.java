import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Spawner for room three for mushroom mobs.
 * 
 * @author Paul
 */
public class Spawner extends ScrollingActor
{
    private final int ENEMY_SPAWN_COUNT = 6;
    private int health = 100;
    ScrollingStatBar healthBar;
    int healthBarYOffset = -100;
    int timer = 300;
    Camera camera;
    public Spawner(Camera camera){
        super(camera);
        this.camera = camera;
    }
    public void act()
    {
        spawnEnemy();
    }
    public void spawnEnemy(){
        for(int i = timer; i < 0; i--){
            if(i == 0){
                Fungi fungi = new Fungi(camera);
            }
        }
        timer = 300;
    }
    public void addToWorld(){
        World world = getWorld();
        healthBar = new ScrollingStatBar(camera, this, health, 70, 6, healthBarYOffset);
        world.addObject(healthBar, 0, 0);
        updateHealthBarPosition();
    }
    protected void updateHealthBarPosition() {
        if (healthBar != null) {
            healthBar.setWorldPosition(worldX, worldY + healthBarYOffset);
        }
    }
    protected void takeDamage(int dmg) {
        World world = getWorld();
        health -= dmg;
        // Update health bar
        if (healthBar != null) {
            healthBar.update(health);
        }
        
        if (health <= 0) {
            world.removeObject(this);
        }
    }
}
