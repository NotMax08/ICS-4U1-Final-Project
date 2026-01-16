import greenfoot.*;
import java.util.*;

/**
 * Flying enemy class that has a hover range and shoots towards the player, and attack cooldow
 * Author: Max & Claude
 */
public class BasicFly extends FlyingEnemy {
    public static final int FLY_HEALTH = 14;
    public static final int FLY_DAMAGE = 1;
    public static final int FLY_DETECTION_RANGE = 350;
    public static final int FLY_ATTACK_RANGE = 100;
    public static final int PATROL_SPEED = 2;
    public static final int DIVE_SPEED = 14;
    public static final int RETREAT_SPEED = 12;
    public static final int ATTACK_COOLDOWN = 90;
    public static final int IMAGE_SIZE = 80;
    
    // Animation timer specific to BasicFly
    private int animationTimer = 0;
    private static final int ANIMATION_SPEED = 8;
    
     public BasicFly(Camera camera, int leftBound, int rightBound, int patrolY) {
        super(camera,
              getUniformImage("Fly1.png", IMAGE_SIZE, IMAGE_SIZE),
              FLY_HEALTH, FLY_DAMAGE,
              FLY_DETECTION_RANGE, FLY_ATTACK_RANGE,
              PATROL_SPEED, DIVE_SPEED, RETREAT_SPEED,
              leftBound, rightBound, patrolY);
        
        loadImages();
        behaviour = ENEMY_BEHAVIOUR.PATROL;
        
        // Start moving LEFT and facing LEFT
        direction = -1;
        isFacingRight = false;
    }
    
    private void loadImages() {
        flyImages.add(getUniformImage("Fly1.png", IMAGE_SIZE, IMAGE_SIZE));
        flyImages.add(getUniformImage("Fly2.png", IMAGE_SIZE, IMAGE_SIZE));
        attackImage = getUniformImage("FlyAttack.png", IMAGE_SIZE, IMAGE_SIZE);
    }
    
    @Override
    public void act() {
        super.act(); // Call parent for basic enemy logic
        
        // Update animation
        updateAnimation();
        updateImage();
    }
    
    @Override
    protected void updateAnimation() {
        // Animate during patrol, chase, cooldown, and retreat
        if (behaviour == ENEMY_BEHAVIOUR.PATROL || 
            behaviour == ENEMY_BEHAVIOUR.CHASE || 
            behaviour == ENEMY_BEHAVIOUR.ATTACK_COOLDOWN ||
            (behaviour == ENEMY_BEHAVIOUR.ATTACK_ANIMATION && isRetreating)) {
            
            animationTimer++;
            if (animationTimer >= ANIMATION_SPEED) {
                animationTimer = 0;
                currentFrame = (currentFrame + 1) % flyImages.size();
            }
        } else if (behaviour == ENEMY_BEHAVIOUR.ATTACK_ANIMATION && !isRetreating) {
            // During dive attack, use attack image (no animation)
            currentFrame = 0; // Reset to first frame
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
            finalImage = new GreenfootImage(flyImages.get(currentFrame));
        }
        
        // Flip based on facing direction
        
        if (isFacingRight) {
            finalImage.mirrorHorizontally();
        }
        
        setImage(finalImage);
    }
    
    @Override
    protected void idleBehavior() {
        behaviour = ENEMY_BEHAVIOUR.PATROL;
    }
    
    @Override
    protected void patrol() {
        patrolWithinBounds();
    }
    
   @Override
    protected void chase() {
        if (target == null || !(target instanceof ScrollingActor)) {
            behaviour = ENEMY_BEHAVIOUR.PATROL;
            return;
        }
        
        ScrollingActor scrollTarget = (ScrollingActor) target;
        int playerX = scrollTarget.getWorldX();
        
        int horizontalDist = Math.abs(playerX - worldX);
        
        if (horizontalDist <= attackRange && !isInAttackCooldown) {
            targetX = playerX;
            targetY = scrollTarget.getWorldY();
            isRetreating = false;
            behaviour = ENEMY_BEHAVIOUR.ATTACK_ANIMATION;
            return;
        }
        
        // Move toward player - update both direction AND facing
        if (playerX > worldX) {
            direction = 1;
            isFacingRight = true;
        } else {
            direction = -1;
            isFacingRight = false;
        }
        
        int newX = worldX + (direction * PATROL_SPEED);
        newX = Math.max(leftBoundary, Math.min(rightBoundary, newX));
        
        setWorldPosition(newX, worldY);
    }
    
    @Override
    protected void attackAnimation() {
        if (!isRetreating) {
            moveTowards(targetX, targetY, DIVE_SPEED);
            
            double distance = Math.hypot(targetX - worldX, targetY - worldY);
            if (distance <= DIVE_SPEED) {
                dealAttackDamage();
                
                retreatTargetX = leftBoundary + Greenfoot.getRandomNumber(rightBoundary - leftBoundary);
                retreatTargetY = patrolY;
                isRetreating = true;
                hasDealtDamageThisAttack = false;
            }
        } else {
            moveTowards(retreatTargetX, retreatTargetY, RETREAT_SPEED);
            
            double distance = Math.hypot(retreatTargetX - worldX, retreatTargetY - worldY);
            if (distance <= RETREAT_SPEED) {
                behaviour = ENEMY_BEHAVIOUR.ATTACK_COOLDOWN;
                attackCooldownTimer = ATTACK_COOLDOWN;
                isInAttackCooldown = true;
                
                // Set direction based on retreat position
                if (worldX < (leftBoundary + rightBoundary) / 2) {
                    direction = 1; // Move right
                    isFacingRight = true;
                } else {
                    direction = -1; // Move left
                    isFacingRight = false;
                }
            }
        }
    }
    
    @Override
    protected void attackCooldown() {
        patrolWithinBounds();
    }
}