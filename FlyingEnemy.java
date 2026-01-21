import greenfoot.*;
import java.util.*;

/**
 * Flying enemy that has similar traits to BaseEnemy but instead of being grounded, it flies and patrols area
 * @Author: max & claude
 */
public abstract class FlyingEnemy extends BaseEnemy {
    // Movement constants
    protected int patrolSpeed;
    protected int diveSpeed;
    protected int retreatSpeed;
    protected int leftBoundary;
    protected int rightBoundary;
    protected int patrolY;
    
    // Attack tracking
    protected int targetX, targetY;
    protected boolean isRetreating = false;
    protected int retreatTargetX, retreatTargetY;
    
    // Animation
    protected ArrayList<GreenfootImage> flyImages = new ArrayList<>();
    protected GreenfootImage attackImage;
    protected int animationCounter = 0;
    protected int currentFrame = 0;
    protected static final int ANIMATION_SPEED = 8;
    
    public FlyingEnemy(Camera camera, GreenfootImage img,
                      int health, int damage,
                      int detectionRange, int attackRange,
                      int patrolSpeed, int diveSpeed, int retreatSpeed,
                      int leftBoundary, int rightBoundary, int patrolY) {
        super(camera, img, health, damage, detectionRange, attackRange);
        this.patrolSpeed = patrolSpeed;
        this.diveSpeed = diveSpeed;
        this.retreatSpeed = retreatSpeed;
        
        // Ensure boundaries are correct
        if (leftBoundary > rightBoundary) {
            int temp = leftBoundary;
            leftBoundary = rightBoundary;
            rightBoundary = temp;
        }
        this.leftBoundary = leftBoundary;
        this.rightBoundary = rightBoundary;
        this.patrolY = patrolY;
        
        // DO NOT set direction or isFacingRight here - let the subclass decide
    }
    
    @Override
    protected void updateAnimation() {
        if (behaviour == ENEMY_BEHAVIOUR.PATROL || 
            behaviour == ENEMY_BEHAVIOUR.CHASE || 
            behaviour == ENEMY_BEHAVIOUR.ATTACK_COOLDOWN ||
            (behaviour == ENEMY_BEHAVIOUR.ATTACK_ANIMATION && isRetreating)) {
            animationCounter++;
            if (animationCounter >= ANIMATION_SPEED) {
                animationCounter = 0;
                currentFrame = (currentFrame + 1) % Math.max(1, flyImages.size());
            }
        }
    }
    
    @Override
    protected void updateImage() {
        GreenfootImage finalImage;
        
        // Use attack image only when diving (not retreating)
        if (behaviour == ENEMY_BEHAVIOUR.ATTACK_ANIMATION && !isRetreating) {
            finalImage = new GreenfootImage(attackImage);
        } else {
            // Use flying animation for patrol, chase, cooldown, and retreat
            finalImage = new GreenfootImage(flyImages.get(currentFrame % flyImages.size()));
        }
        
        // CRITICAL: Mirror ONLY if we're facing left
        // Assuming your images are drawn facing right by default
        if (!isFacingRight) {
            finalImage.mirrorHorizontally();
        }
        
        setImage(finalImage);
    }
    
    protected void moveTowards(int targetX, int targetY, int speed) {
        int dx = targetX - worldX;
        int dy = targetY - worldY;
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance > speed) {
            int moveX = (int)((dx / distance) * speed);
            int moveY = (int)((dy / distance) * speed);
            setWorldPosition(worldX + moveX, worldY + moveY);
            
            // Update direction and facing based on horizontal movement
            if (dx > 0) {
                direction = 1;
                isFacingRight = true;
            } else if (dx < 0) {
                direction = -1;
                isFacingRight = false;
            }
        } else {
            setWorldPosition(targetX, targetY);
        }
    }
    
    protected void patrolWithinBounds() {
        // Move in current direction
        setWorldPosition(worldX + (direction * patrolSpeed), worldY);
        
        // Turn at boundaries
        if (worldX <= leftBoundary && direction == -1) {
            direction = 1; // Start moving right
            isFacingRight = true; // Face right when moving right
        } else if (worldX >= rightBoundary && direction == 1) {
            direction = -1; // Start moving left
            isFacingRight = false; // Face left when moving left
        }
    }
    
    protected void updateFacingFromDirection() {
        // direction = 1 means moving right, so should face right
        // direction = -1 means moving left, so should face left
        if (direction == 1) {
            isFacingRight = true;
        } else if (direction == -1) {
            isFacingRight = false;
        }
    }
    
    protected void setDirection(int newDirection) {
        if (newDirection != direction) {
            direction = newDirection;
            updateFacingFromDirection();
        }
    }
    
    @Override
    protected void takeDamage(int dmg) {
        super.takeDamage(dmg);
        isRetreating = false;
        attackCooldownTimer = 0;
        isInAttackCooldown = false;
    }
}