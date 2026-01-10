import greenfoot.*;
import java.util.*;
/**
 * Crawler - Ground enemy that patrols platforms and chases player
 * 
 * @author Max Yuan
 * @version 1.0
 */
public class Crawler extends Enemies
{
    //constants for movement and enemy
    public static final int CRAWLER_HEALTH = 50;
    public static final int CRAWLER_DAMAGE = 10;
    public static final int CRAWLER_DETECTION_RANGE = 300;
    public static final int CRAWLER_ATTACK_RANGE = 30;
    public static final int PATROL_SPEED = 1;
    public static final int CHASE_SPEED = 3;
    public static final int MAX_IDLE_TIME = 120; 
    public static final int MAX_PATROL_TIME = MAX_IDLE_TIME;
    public static final int GRAVITY = 1;
    public static final int KNOCKBACK_DISTANCE = 10;
    public static final int KNOCKBACK_HEIGHT = 5;
    public static final int WALL_CHECK_DISTANCE = 20;
    
    private int idleTimer;
    private int patrolTimer;

    //image stuff
    public static final int IMAGE_WIDTH = 128;   // Adjustable uniform width
    public static final int IMAGE_HEIGHT = 128;  // Adjustable uniform height
    public static final int FADE_SPEED = 15; // Higher = faster fade
    public static final int ANIMATION_SPEED = 10; // Frames between animation changes
    private GreenfootImage normalIdleImage;
    private GreenfootImage alertIdleImage;
    private ArrayList<GreenfootImage> alertWalkingImages = new ArrayList<>();
    private ArrayList<GreenfootImage> normalWalkingImages = new ArrayList<>();
    private int currentAlpha = 0; // 0 = normal, 255 = alert
    private boolean isAlert = false;
    private int animationCounter = 0;
    private int currentFrame = 0;
    
    public Crawler(Camera camera) {
        super(camera, 
              getUniformImage("Crawler.png"),
              CRAWLER_HEALTH,
              CRAWLER_DAMAGE,
              CRAWLER_DETECTION_RANGE,
              CRAWLER_ATTACK_RANGE
        );
        
        this.idleTimer = 0;
        this.patrolTimer = 0;
        
        // Store idle images with uniform size
        normalIdleImage = getUniformImage("Crawler.png");
        alertIdleImage = getUniformImage("AlertCrawler.png");
        
        // Create arrays of walking image animations with uniform size
        normalWalkingImages.add(getUniformImage("CrawlerWalking.png"));
        normalWalkingImages.add(getUniformImage("CrawlerWalking2.png"));
        
        alertWalkingImages.add(getUniformImage("AlertCrawlerWalking.png"));
        alertWalkingImages.add(getUniformImage("AlertCrawlerWalking2.png"));
        
        behaviour = ENEMY_BEHAVIOUR.IDLE;
    }
    
    /**
     * Load and resize image to uniform dimensions
     */
    private static GreenfootImage getUniformImage(String filename) {
        GreenfootImage img = new GreenfootImage(filename);
        img.scale(IMAGE_WIDTH, IMAGE_HEIGHT);
        return img;
    }
    
    @Override
    public void act() {
        super.act(); // Call parent act first
        updateAnimation(); // Update animation frame
        updateImageTransition(); // Then update image fade
    }
    
    @Override
    protected void flipImage() {
        isFacingRight = !isFacingRight;
        // Don't flip the stored images anymore - let updateImageTransition handle it
    }
    
    private void updateAnimation() {
        // Only animate when moving (patrol or chase)
        if (behaviour == ENEMY_BEHAVIOUR.PATROL || behaviour == ENEMY_BEHAVIOUR.CHASE) {
            animationCounter++;
            if (animationCounter >= ANIMATION_SPEED) {
                animationCounter = 0;
                currentFrame = (currentFrame + 1) % 2; // Toggle between 0 and 1
            }
        } else {
            // Reset to idle when not moving
            currentFrame = 0;
            animationCounter = 0;
        }
    }
    
    private void updateImageTransition() {
        // Determine if we should be in alert mode
        boolean shouldBeAlert = (behaviour == ENEMY_BEHAVIOUR.CHASE || behaviour == ENEMY_BEHAVIOUR.ATTACK);
        
        // Fade towards target state
        if (shouldBeAlert && currentAlpha < 255) {
            currentAlpha = Math.min(255, currentAlpha + FADE_SPEED);
        } else if (!shouldBeAlert && currentAlpha > 0) {
            currentAlpha = Math.max(0, currentAlpha - FADE_SPEED);
        }
        
        // Select base images based on current state
        GreenfootImage normalBase;
        GreenfootImage alertBase;
        
        if (behaviour == ENEMY_BEHAVIOUR.PATROL || behaviour == ENEMY_BEHAVIOUR.CHASE) {
            // Use walking animation
            normalBase = normalWalkingImages.get(currentFrame);
            alertBase = alertWalkingImages.get(currentFrame);
        } else {
            // Use idle images
            normalBase = normalIdleImage;
            alertBase = alertIdleImage;
        }
        
        // Create blended image (already uniform size)
        GreenfootImage finalImage;
        if (currentAlpha > 0) {
            // Create a copy of normal image
            GreenfootImage blended = new GreenfootImage(normalBase);
            
            // Create alert overlay with appropriate transparency
            GreenfootImage overlay = new GreenfootImage(alertBase);
            overlay.setTransparency(currentAlpha);
            
            // Draw alert image over normal image
            blended.drawImage(overlay, 0, 0);
            
            finalImage = blended;
        } else {
            // Just use normal image
            finalImage = new GreenfootImage(normalBase);
        }
        
        // Apply facing direction AFTER creating the image
        if (!isFacingRight) {
            finalImage.mirrorHorizontally();
        }
        
        setImage(finalImage);
    }
    
    @Override
    protected void idleBehavior() {
        idleTimer++;
        
        if (idleTimer >= MAX_IDLE_TIME) {
            behaviour = ENEMY_BEHAVIOUR.PATROL;
            idleTimer = 0;
        }
        fall(GRAVITY);
    }
    
    @Override
    protected void patrol() {
        setWorldPosition(worldX + (direction * PATROL_SPEED), worldY);
        
        // Check for edges or walls
        if (isAtEdge() || isBlocked(WALL_CHECK_DISTANCE)) {
            direction *= -1;
            flipImage();
        }
        
        patrolTimer++;
        if (patrolTimer >= MAX_PATROL_TIME) {
            behaviour = ENEMY_BEHAVIOUR.IDLE;
            patrolTimer = 0;
        }
        
        // Apply gravity
        fall(GRAVITY);
    }
    
    @Override
    protected void chase() {
        if (target == null) return;
        
        int targetX = ((ScrollingActor)target).getWorldX();
        
        //added this condition so it doesn't flip back and forth when player is directly above the enemy
        if (!(Math.abs(targetX - worldX) < 5)){
            if (targetX > worldX) {
                direction = 1;
                setWorldPosition(worldX + CHASE_SPEED, worldY);
                if (!isFacingRight) {
                    flipImage();
                }
            } else if (targetX < worldX) {
                direction = -1;
                setWorldPosition(worldX - CHASE_SPEED, worldY);
                if (isFacingRight) {
                    flipImage();
                }
            } 
        }
        
        // Check for obstacles
        if (isBlocked(WALL_CHECK_DISTANCE)) {
            direction *= -1;
            flipImage();
        }
        
        // Apply gravity
        fall(GRAVITY);
    }
    
    @Override
    protected void attack() {
        Player player = (Player) getOneIntersectingWorldObject(Player.class);
        if (player != null) {
            //player.takeDamage(damage);
        }
        
        chase();
    }
    
    @Override
    protected void takeDamage(int dmg) {
        health -= dmg;
        behaviour = ENEMY_BEHAVIOUR.HURT;
        
        setWorldPosition(worldX + (direction * -KNOCKBACK_DISTANCE), worldY - KNOCKBACK_HEIGHT);
        
        if (health <= 0) {
            behaviour = ENEMY_BEHAVIOUR.DEAD;
        }
    }
}