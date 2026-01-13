import greenfoot.*;
import java.util.*;

/**
 * GroundEnemy - Base class for all ground-based enemies
 * Handles gravity, ground detection, and platform movement
 */
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
        return getOneObjectAtWorldOffset(0, getImage().getHeight()/2 + 3, Platform.class) != null;
    }
    
    protected boolean isWallAhead() {
        return getOneObjectAtWorldOffset(direction * wallCheckDistance, 
                                         getImage().getHeight()/4, Platform.class) != null;
    }
    
    protected boolean isGroundAhead() {
        return getOneObjectAtWorldOffset(direction * wallCheckDistance, 
                                         getImage().getHeight()/2, Platform.class) != null;
    }
    
    @Override
    public boolean isAtEdge() {
        return getOneObjectAtWorldOffset(direction * (getImage().getWidth()/2 + 10), 
                                         getImage().getHeight()/2, Platform.class) == null;
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