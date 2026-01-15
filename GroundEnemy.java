import greenfoot.*;
import java.util.*;

public abstract class GroundEnemy extends BaseEnemy {
    // Movement constants
    protected int patrolSpeed;
    protected int chaseSpeed;
    protected int gravity;
    protected int wallCheckDistance;
    
    // State tracking
    protected int velY = 0;
    protected boolean isMoving = false;
    
    // Animation
    protected ArrayList<GreenfootImage> idleImages = new ArrayList<>();
    protected ArrayList<GreenfootImage> walkImages = new ArrayList<>();
    protected ArrayList<GreenfootImage> attackImages = new ArrayList<>();
    protected int animationCounter = 0;
    protected int currentFrame = 0;
    
    public GroundEnemy(Camera camera, GreenfootImage img,
                      int health, int damage,
                      int detectionRange, int attackRange,
                      int patrolSpeed, int chaseSpeed, 
                      int gravity, int wallCheckDistance) {
        super(camera, img, health, damage, detectionRange, attackRange);
        this.patrolSpeed = patrolSpeed;
        this.chaseSpeed = chaseSpeed;
        this.gravity = gravity;
        this.wallCheckDistance = wallCheckDistance;
    }
    
    protected abstract void updateAnimation();
    
    protected void fall() {
        if (!onGround()) {
            velY += gravity;
            setWorldPosition(worldX, worldY + (int)(velY / 2.4));
        } else {
            velY = 0;
        }
    }
    
    protected boolean onGround() {
        // Check directly below the enemy's feet
        int checkX = worldX;
        int checkY = worldY + (getImage().getHeight() / 2) + 3;
        
        return isSolidAtPosition(checkX, checkY);
    }
    
    protected boolean isWallAhead() {
        // Check ahead of the enemy at body height
        int checkX = worldX + (direction * wallCheckDistance);
        int checkY = worldY + (getImage().getHeight() / 4); // Body height
        
        return isSolidAtPosition(checkX, checkY);
    }
    
    protected boolean isGroundAhead() {
        // Check ahead and below the enemy (where next step would be)
        int checkX = worldX + (direction * wallCheckDistance);
        int checkY = worldY + (getImage().getHeight() / 2) + 7;
        
        return isSolidAtPosition(checkX, checkY);
    }
    
    @Override
    public boolean isAtEdge() {
        // Check further ahead and below (edge of platform)
        int checkX = worldX + (direction * (getImage().getWidth() / 2 + 10));
        int checkY = worldY + (getImage().getHeight() / 2) + 5;
        
        return !isSolidAtPosition(checkX, checkY);
    }
    
    // Optional: More precise collision detection using area checking
    protected boolean isCollidingWithSolid() {
        int width = getImage().getWidth();
        int height = getImage().getHeight();
        
        // Check if enemy's entire bounding box collides with solids
        return isSolidArea(worldX, worldY, width - 5, height - 5);
    }
    
    // Ground enemies always need to apply gravity
    @Override
    protected void patrol() {
        fall();
        // Default patrol logic can be overridden
    }
    
    @Override
    protected void chase() {
        fall();
        // Default chase logic can be overridden
    }
}