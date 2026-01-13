import greenfoot.*;
import java.util.*;

/**
 * Flying Enemy - Patrols horizontally and dive-attacks player
 * 
 * @author Claude (based on Max Yuan's Enemies framework)
 * @version 1.0
 */
public class FlyingEnemy extends Enemies {
    // Constants for movement and behavior
    public static final int FLY_HEALTH = 30;
    public static final int FLY_DAMAGE = 15;
    public static final int FLY_DETECTION_RANGE = 350;
    public static final int FLY_ATTACK_RANGE = 80;
    public static final int PATROL_SPEED = 2;
    public static final int DIVE_SPEED = 14;
    public static final int RETREAT_SPEED = 12;
    public static final int ATTACK_COOLDOWN = 90;
    
    // Image constants
    public static final int IMAGE_WIDTH = 80;
    public static final int IMAGE_HEIGHT = 80;
    public static final int ANIMATION_SPEED = 8;
    
    // Images
    private ArrayList<GreenfootImage> flyImages = new ArrayList<>();
    private GreenfootImage attackImage;
    
    // Animation tracking
    private int animationCounter = 0;
    private int currentFrame = 0;
    
    // Patrol boundaries
    private int leftBoundary;
    private int rightBoundary;
    private int patrolY;
    
    // Attack state tracking
    private int targetX = 0, targetY = 0;  // Initialize these!
    private boolean isRetreating = false;
    private int retreatTargetX = 0, retreatTargetY = 0;  // Initialize these too!
    
    private ScrollingStatBar healthBar;
    
    public FlyingEnemy(Camera camera, int leftBound, int rightBound, int patrolY) {
        super(camera,
              getUniformImage("Fly1.png", IMAGE_WIDTH, IMAGE_HEIGHT),
              FLY_HEALTH,
              FLY_DAMAGE,
              FLY_DETECTION_RANGE,
              FLY_ATTACK_RANGE
        );
        
        // Load images
        flyImages.add(getUniformImage("Fly1.png", IMAGE_WIDTH, IMAGE_HEIGHT));
        flyImages.add(getUniformImage("Fly2.png", IMAGE_WIDTH, IMAGE_HEIGHT));
        attackImage = getUniformImage("FlyAttack.png", IMAGE_WIDTH, IMAGE_HEIGHT);
        
        // Set patrol boundaries - ENSURE left < right
        if (leftBound > rightBound) {
            int temp = leftBound;
            leftBound = rightBound;
            rightBound = temp;
        }
        
        this.leftBoundary = leftBound;
        this.rightBoundary = rightBound;
        this.patrolY = patrolY;
        
        behaviour = ENEMY_BEHAVIOUR.PATROL;
        healthBar = null;
        
        direction = -1;
        isFacingRight = false;
    }
    
    @Override
    protected void addedToWorld(World world) {
        healthBar = new ScrollingStatBar(camera, this, FLY_HEALTH, 70, 6, -50);
        world.addObject(healthBar, 0, 0);
        healthBar.setWorldPosition(worldX, worldY - 50);
    }
    
    @Override
    public void act() {
        super.act();
        updateAnimation();
        updateImage();
    }
    
    @Override
    protected void flipImage() {
        isFacingRight = !isFacingRight;
    }
    
    @Override
    protected void updateState() {
        // Simplified state logic
        if (target == null || !(target instanceof ScrollingActor)) {
            if (behaviour == ENEMY_BEHAVIOUR.CHASE) {
                behaviour = ENEMY_BEHAVIOUR.PATROL;
            }
            return;
        }
        
        ScrollingActor scrollTarget = (ScrollingActor) target;
        int playerX = scrollTarget.getWorldX();
        int playerY = scrollTarget.getWorldY();
        int horizontalDist = Math.abs(playerX - worldX);
    
        switch(behaviour) {
            case IDLE:
            case PATROL:
                // Simple check: if player is detected AND below us, chase
                if (isAggro && playerY > worldY) {
                    behaviour = ENEMY_BEHAVIOUR.CHASE;
                }
                break;
    
            case CHASE:
                // Stop chasing if player goes above or out of range
                if (!isAggro || playerY <= worldY - 100) {
                    behaviour = ENEMY_BEHAVIOUR.PATROL;
                    break;
                }
                
                // Don't auto-attack in updateState - let chase() handle it
                break;
                
            case ATTACK_ANIMATION:
            case ATTACK_COOLDOWN:
            case HURT:
                // Locked states - handled elsewhere
                if (health <= 0) {
                    behaviour = ENEMY_BEHAVIOUR.DEAD;
                }
                break;
        }
    }
    
    private void updateAnimation() {
        if (behaviour == ENEMY_BEHAVIOUR.PATROL || 
            behaviour == ENEMY_BEHAVIOUR.CHASE || 
            behaviour == ENEMY_BEHAVIOUR.ATTACK_COOLDOWN ||
            (behaviour == ENEMY_BEHAVIOUR.ATTACK_ANIMATION && isRetreating)) {
            animationCounter++;
            if (animationCounter >= ANIMATION_SPEED) {
                animationCounter = 0;
                currentFrame = (currentFrame + 1) % 2;
            }
        }
    }
    
    private void updateImage() {
        GreenfootImage finalImage;
        
        if (behaviour == ENEMY_BEHAVIOUR.ATTACK_ANIMATION && !isRetreating) {
            finalImage = new GreenfootImage(attackImage);
        } else {
            finalImage = new GreenfootImage(flyImages.get(currentFrame));
        }
        
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
        // Move horizontally
        setWorldPosition(worldX + (direction * PATROL_SPEED), worldY);
        
        // Update facing based on direction
        if (direction == -1 && isFacingRight) {
            isFacingRight = false;
        } else if (direction == 1 && !isFacingRight) {
            isFacingRight = true;
        }
        
        // Turn at boundaries
        if (worldX <= leftBoundary && direction == -1) {
            direction = 1;
        } else if (worldX >= rightBoundary && direction == 1) {
            direction = -1;
        }
    }
    
    
    
    @Override
    protected void attackAnimation() {
        if (!isRetreating) {
            // DIVE toward stored target
            int dx = targetX - worldX;
            int dy = targetY - worldY;
            double distance = Math.sqrt(dx * dx + dy * dy);
            
            if (distance > DIVE_SPEED) {
                int moveX = (int) ((dx / distance) * DIVE_SPEED);
                int moveY = (int) ((dy / distance) * DIVE_SPEED);
                setWorldPosition(worldX + moveX, worldY + moveY);
                
                if (dx > 0 && !isFacingRight) isFacingRight = true;
                else if (dx < 0 && isFacingRight) isFacingRight = false;
            } else {
                // Hit target - deal damage and retreat
                Player player = (Player) getOneIntersectingWorldObject(Player.class);
                if (player != null) {
                    // player.takeDamage(damage);
                }
                
                // Set retreat target
                int range = rightBoundary - leftBoundary;
                retreatTargetX = (range > 0) ? Greenfoot.getRandomNumber(range) + leftBoundary : leftBoundary;
                retreatTargetY = patrolY;
                isRetreating = true;
            }
        } else {
            // RETREAT to patrol height
            int dx = retreatTargetX - worldX;
            int dy = retreatTargetY - worldY;
            double distance = Math.sqrt(dx * dx + dy * dy);
            
            if (distance > RETREAT_SPEED) {
                int moveX = (int) ((dx / distance) * RETREAT_SPEED);
                int moveY = (int) ((dy / distance) * RETREAT_SPEED);
                setWorldPosition(worldX + moveX, worldY + moveY);
                
                if (dx > 0 && !isFacingRight) isFacingRight = true;
                else if (dx < 0 && isFacingRight) isFacingRight = false;
            } else {
                // Retreat complete - enter cooldown
                behaviour = ENEMY_BEHAVIOUR.ATTACK_COOLDOWN;
                attackCooldownTimer = ATTACK_COOLDOWN;
                isInAttackCooldown = true;
                direction = isFacingRight ? 1 : -1;
            }
        }
    }
    
    @Override
    protected void attackCooldown() {
        // Continue patrolling during cooldown
        setWorldPosition(worldX + (direction * PATROL_SPEED), worldY);
        
        if (direction == -1 && isFacingRight) {
            isFacingRight = false;
        } else if (direction == 1 && !isFacingRight) {
            isFacingRight = true;
        }
        
        if (worldX <= leftBoundary && direction == -1) {
            direction = 1;
        } else if (worldX >= rightBoundary && direction == 1) {
            direction = -1;
        }
    }
    
    @Override
    protected void chase() {
        if (target == null || !(target instanceof ScrollingActor)) {
            behaviour = ENEMY_BEHAVIOUR.PATROL;
            return;
        }
        
        ScrollingActor scrollTarget = (ScrollingActor) target;
        int playerX = scrollTarget.getWorldX();
        
        // Check if ready to attack
        int horizontalDist = Math.abs(playerX - worldX);
        if (horizontalDist <= attackRange && !isInAttackCooldown) {
            // Lock on to current player position
            targetX = playerX;
            targetY = scrollTarget.getWorldY();
            isRetreating = false;
            behaviour = ENEMY_BEHAVIOUR.ATTACK_ANIMATION;
            return;
        }
        
        // Move toward player (within boundaries)
        int targetDir = (playerX > worldX) ? 1 : -1;
        int newX = worldX + (targetDir * PATROL_SPEED);
        newX = Math.max(leftBoundary, Math.min(rightBoundary, newX));
        
        setWorldPosition(newX, worldY);
        
        if (targetDir == 1 && !isFacingRight) isFacingRight = true;
        else if (targetDir == -1 && isFacingRight) isFacingRight = false;
    }
    
    @Override
    protected void takeDamage(int dmg) {
        health -= dmg;
        
        if (healthBar != null) {
            healthBar.update(health);
        }
        
        isRetreating = false;
        attackCooldownTimer = 0;
        isInAttackCooldown = false;
        
        setWorldPosition(worldX + (direction * -5), worldY - 10);
        
        if (health <= 0) {
            behaviour = ENEMY_BEHAVIOUR.DEAD;
        } else {
            behaviour = ENEMY_BEHAVIOUR.PATROL;
        }
    }
    
    @Override
    protected void die() {
        if (healthBar != null && getWorld() != null) {
            getWorld().removeObject(healthBar);
        }
        if (getWorld() != null) {
            getWorld().removeObject(this);
        }
    }
    
    @Override
    protected void fall(int gravity) {}
    
    @Override
    public boolean isAtEdge() { return false; }
}