
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
    protected boolean isStatic = false; // True when no camera (static positioning)
    
    // Animation
    protected ArrayList<GreenfootImage> idleImages = new ArrayList<>();
    protected ArrayList<GreenfootImage> walkImages = new ArrayList<>();
    protected ArrayList<GreenfootImage> attackImages = new ArrayList<>();
    protected ArrayList<GreenfootImage> dieImages = new ArrayList<>();
    
    protected int animationCounter = 0;
    protected int currentFrame = 0;
    
    protected boolean isDying = false;
    protected int dieFrame = 0;
    protected int dieTimer = 0;
    protected static final int DIE_FRAME_DELAY = 15; // Death animation speed
    
    /**
     * Original constructor with camera (for scrolling worlds)
     */
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
        this.isStatic = false;
    }
    
    /**
     * New constructor for static worlds (null camera)
     * Uses regular Actor positioning instead of world coordinates
     */
    public GroundEnemy(GreenfootImage img,
                      int health, int damage,
                      int detectionRange, int attackRange,
                      int patrolSpeed, int chaseSpeed, 
                      int gravity, int wallCheckDistance) {
        super(null, img, health, damage, detectionRange, attackRange);
        this.patrolSpeed = patrolSpeed;
        this.chaseSpeed = chaseSpeed;
        this.gravity = gravity;
        this.wallCheckDistance = wallCheckDistance;
        this.isStatic = true;
    }
    
    /**
     * Override setWorldPosition to handle both static and scrolling modes
     */
    @Override
    public void setWorldPosition(int x, int y) {
        if (isStatic) {
            // Static mode: just use regular Actor positioning
            setLocation(x, y);
        } else {
            // Scrolling mode: use parent's world coordinate system
            super.setWorldPosition(x, y);
        }
    }
    
    /**
     * Override getWorldX to work in both modes
     */
    @Override
    public int getWorldX() {
        if (isStatic) {
            return getX();
        } else {
            return super.getWorldX();
        }
    }
    
    /**
     * Override getWorldY to work in both modes
     */
    @Override
    public int getWorldY() {
        if (isStatic) {
            return getY();
        } else {
            return super.getWorldY();
        }
    }
    
    protected abstract void updateAnimation();
    
    //apply fall for all grounded enemies
    protected void fall() {
        if (!onGround()) {
            velY += gravity;
            if (isStatic) {
                setLocation(getX(), getY() + (int)(velY / 2.4));
            } else {
                setWorldPosition(worldX, worldY + (int)(velY / 2.4));
            }
        } else {
            velY = 0;
        }
    }
    
    protected boolean onGround() {
        // Check directly below the enemy's feet
        int checkX = isStatic ? getX() : worldX;
        int checkY = (isStatic ? getY() : worldY) + (getImage().getHeight() / 2) + 1;
        
        return isSolidAtPosition(checkX, checkY);
    }
    
    protected boolean isWallAhead() {
        // Check ahead of the enemy at body height
        int checkX = (isStatic ? getX() : worldX) + (direction * wallCheckDistance);
        int checkY = (isStatic ? getY() : worldY) + (getImage().getHeight() / 4); // Body height
        
        return isSolidAtPosition(checkX, checkY);
    }
    
    protected boolean isGroundAhead() {
        // Check ahead and below the enemy (where next step would be)
        int checkX = (isStatic ? getX() : worldX) + (direction * wallCheckDistance);
        int checkY = (isStatic ? getY() : worldY) + (getImage().getHeight() / 2) + 7;
        
        return isSolidAtPosition(checkX, checkY);
    }
    
    @Override
    public boolean isAtEdge() {
        // Check further ahead and below (edge of platform)
        int currentX = isStatic ? getX() : worldX;
        int currentY = isStatic ? getY() : worldY;
        
        int checkX = currentX + (direction * (getImage().getWidth() / 2 + 10));
        int checkY = currentY + (getImage().getHeight() / 2) + 5;
        
        return !isSolidAtPosition(checkX, checkY);
    }
    
    // Optional: More precise collision detection using area checking
    protected boolean isCollidingWithSolid() {
        int width = getImage().getWidth();
        int height = getImage().getHeight();
        int currentX = isStatic ? getX() : worldX;
        int currentY = isStatic ? getY() : worldY;
        
        // Check if enemy's entire bounding box collides with solids
        return isSolidArea(currentX, currentY, width - 5, height - 5);
    }
    
    /**
     * Helper method to check for solid tiles - works for both static and scrolling worlds
     * Uses tile-based collision system
     */
    protected boolean isSolidAtPosition(int x, int y) {
        World world = getWorld();
        if (world == null) return false;
        
        if (isStatic) {
            // Static mode: use world's tile system directly
            // Assuming your MinibossRoom has mapGrid like GameWorld
            if (world instanceof MinibossRoom) {
                MinibossRoom room = (MinibossRoom) world;
                int tileX = room.worldToTileX(x);
                int tileY = room.worldToTileY(y);
                int tileType = room.mapGrid.getTileAt(tileX, tileY);
                // Type 1 = walls, Type 2 = platforms
                return tileType == 1 || tileType == 2;
            }
            return false;
        } else {
            // Scrolling mode: use parent's tile-based collision
            return super.isSolidAtPosition(x, y);
        }
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