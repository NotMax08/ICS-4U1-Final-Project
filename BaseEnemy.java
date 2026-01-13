import greenfoot.*;
import java.util.*;

/**
 * BaseEnemy - Contains common enemy functionality
 * All specific enemies should extend this
 */
public abstract class BaseEnemy extends ScrollingActor {
    // Core enemy stats
    protected int health;
    protected int maxHealth;
    protected int damage;
    protected int detectionRange;
    protected int attackRange;
    
    // State management
    protected boolean isAggro;
    protected Actor target;
    protected int direction = 1; // 1 = right, -1 = left
    protected boolean isFacingRight = true;
    
    // Attack cooldown
    protected int attackCooldownTimer = 0;
    protected boolean isInAttackCooldown = false;
    protected boolean shouldUpdateAnimation = true;
    
    // Behaviour states
    protected enum ENEMY_BEHAVIOUR { 
        IDLE, PATROL, CHASE, ATTACK_ANIMATION, 
        ATTACK_COOLDOWN, HURT, DEAD 
    }
    protected ENEMY_BEHAVIOUR behaviour;
    
    // Health bar
    protected ScrollingStatBar healthBar;
    protected int healthBarYOffset = -50;
    
    public BaseEnemy(Camera camera, GreenfootImage img, 
                     int health, int damage, 
                     int detectionRange, int attackRange) {
        super(camera);
        this.maxHealth = this.health = health;
        this.damage = damage;
        this.detectionRange = detectionRange;
        this.attackRange = attackRange;
        this.isAggro = false;
        setImage(img);
    }
    
    // Abstract methods that must be implemented
    protected abstract void patrol();
    protected abstract void chase();
    protected abstract void attackAnimation();
    protected abstract void attackCooldown();
    protected abstract void idleBehavior();
    protected abstract void updateAnimation();
    protected abstract void updateImage();
    
    // Common methods with default implementations
    public void act() {
        if (health <= 0) {
            die();
            return;
        }
        
        detectPlayer();
        updateState();
        executeBehavior();
        updateAttackCooldown();
        
        // Don't call updateAnimation() or updateImage() here
        // Let subclasses handle their own animation timing
    }
  
    protected void detectPlayer() {
        List<Player> players = getObjectsInWorldRange(detectionRange, Player.class);
        if (!players.isEmpty()) {
            target = players.get(0);
            isAggro = true;
        } else {
            target = null;
            isAggro = false;
        }
    }
    
    protected void updateState() {
        if (target == null) {
            if (behaviour == ENEMY_BEHAVIOUR.CHASE) {
                behaviour = ENEMY_BEHAVIOUR.PATROL;
            }
            return;
        }
        
        double distance = getWorldDistanceTo((ScrollingActor)target);
        
        switch(behaviour) {
            case IDLE:
            case PATROL:
                if (isAggro) {
                    behaviour = ENEMY_BEHAVIOUR.CHASE;
                }
                break;
                
            case CHASE:
                if (!isAggro) {
                    behaviour = ENEMY_BEHAVIOUR.PATROL;
                } else if (distance <= attackRange && !isInAttackCooldown) {
                    behaviour = ENEMY_BEHAVIOUR.ATTACK_ANIMATION;
                }
                break;
                
            case HURT:
                if (health <= 0) {
                    behaviour = ENEMY_BEHAVIOUR.DEAD;
                } else {
                    behaviour = ENEMY_BEHAVIOUR.PATROL;
                }
                break;
                
            default:
                // Other states are locked
                break;
        }
    }
    
    protected void executeBehavior() {
        switch(behaviour) {
            case IDLE: idleBehavior(); break;
            case PATROL: patrol(); break;
            case CHASE: chase(); break;
            case ATTACK_ANIMATION: attackAnimation(); break;
            case ATTACK_COOLDOWN: attackCooldown(); break;
            case DEAD: die(); break;
        }
    }
    
    protected void updateAttackCooldown() {
        if (attackCooldownTimer > 0) {
            attackCooldownTimer--;
            isInAttackCooldown = true;
            if (attackCooldownTimer == 0) {
                isInAttackCooldown = false;
                behaviour = isAggro ? ENEMY_BEHAVIOUR.CHASE : ENEMY_BEHAVIOUR.PATROL;
            }
        }
    }
    
    // Health bar management
    @Override
    protected void addedToWorld(World world) {
        healthBar = new ScrollingStatBar(camera, this, maxHealth, 70, 6, healthBarYOffset);
        world.addObject(healthBar, 0, 0);
        updateHealthBarPosition();
    }
    
    protected void updateHealthBarPosition() {
        if (healthBar != null) {
            healthBar.setWorldPosition(worldX, worldY + healthBarYOffset);
        }
    }
    
    // Common helper methods
    protected void flipImage() {
        getImage().mirrorHorizontally();
        isFacingRight = !isFacingRight;
    }
    
    protected void setDirection(int newDirection) {
        direction = newDirection;
        if (direction == 1 && !isFacingRight) {
            isFacingRight = true;
        } else if (direction == -1 && isFacingRight) {
            isFacingRight = false;
        }
    }
    
    protected void faceTarget() {
        if (target instanceof ScrollingActor) {
            ScrollingActor scrollTarget = (ScrollingActor) target;
            boolean shouldFaceRight = scrollTarget.getWorldX() > worldX;
            if (shouldFaceRight != isFacingRight) {
                isFacingRight = shouldFaceRight;
            }
        }
    }
    
    // Damage and death
    protected void takeDamage(int dmg) {
        health -= dmg;
        behaviour = ENEMY_BEHAVIOUR.HURT;
        if (healthBar != null) {
            healthBar.update(health);
        }
        if (health <= 0) {
            behaviour = ENEMY_BEHAVIOUR.DEAD;
        }
    }
    
    protected void die() {
        if (healthBar != null && getWorld() != null) {
            getWorld().removeObject(healthBar);
        }
        if (getWorld() != null) {
            getWorld().removeObject(this);
        }
    }
    
    // Static helper
    protected static GreenfootImage getUniformImage(String filename, int width, int height) {
        GreenfootImage img = new GreenfootImage(filename);
        img.scale(width, height);
        return img;
    }
}