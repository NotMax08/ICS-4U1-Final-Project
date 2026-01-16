import greenfoot.*;
import java.util.*;

/**
 * BaseEnemy - Contains common enemy functionality and methods
 * Creates basic states and transitions between states and the conditions for it
 * @Author: Max & Claude
 * 
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
    
    // Attack cooldown and damage tracking
    protected int attackCooldownTimer = 0;
    protected boolean isInAttackCooldown = false;
    protected boolean hasDealtDamageThisAttack = false; // NEW: Track if damage dealt this attack
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
    
    protected GreenfootImage currentImage;
    
    protected boolean isDying = false;
    protected int dieFrame = 0;
    protected int dieTimer = 0;
    protected static final int DIE_FRAME_DELAY = 15; // Death animation speed
    
    /**
     * Takes in camera object for world position
     * Takes in image to set
     * Takes in health, damage, detection range and attack range
     */
    public BaseEnemy(Camera camera, GreenfootImage img, 
                     int health, int damage, 
                     int detectionRange, int attackRange) {
        super(camera);
        this.maxHealth = this.health = health;
        this.damage = damage;
        this.detectionRange = detectionRange;
        this.attackRange = attackRange;
        this.isAggro = false;
        this.currentImage = img;
        setImage(img);
    }
    
    //abstract methods that are to be implemented for each subclass
    protected abstract void patrol();
    protected abstract void chase();
    protected abstract void attackAnimation();
    protected abstract void attackCooldown();
    protected abstract void idleBehavior();
    protected abstract void updateAnimation();
    protected abstract void updateImage();
    
    // Common methods with default implementations
    public void act() {
        //prevents null world
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
  
    //detect players within a range
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
    
    /**
     * Deal damage to player during attack animation
     * This should be called during the attack animation when the hit should occur
     * It will only deal damage once per attack animation
     */
    protected void dealAttackDamage() {
        // Only deal damage if we haven't already this attack
        if (hasDealtDamageThisAttack) {
            return;
        }
        
        // Get all players within attack range
        List<Player> playersInRange = getObjectsInWorldRange(attackRange, Player.class);
        
        for (Player player : playersInRange) {
            // Check if player is in front of the enemy (based on facing direction)
            int playerX = ((ScrollingActor)player).getWorldX();
            int relativePosition = playerX - worldX;
            
            // Damage player if they're in front of the enemy
            if ((isFacingRight && relativePosition > 0) || (!isFacingRight && relativePosition < 0)) {

                // Check vertical alignment (player should be roughly at same height)
                int playerY = ((ScrollingActor)player).getWorldY();
                int verticalDistance = Math.abs(playerY - worldY);
                
                if (this instanceof GroundEnemy){
                    // Only damage if player is within reasonable vertical range
                    if (verticalDistance < getImage().getHeight() + 20) {
                        player.takeDamage(damage);
                        hasDealtDamageThisAttack = true; // Mark damage as dealt
                        System.out.println("damage bam");
                        break; // Only damage once per attack
                    }
                } else {
                    player.takeDamage(damage);
                    hasDealtDamageThisAttack = true; // Mark damage as dealt
                    System.out.println("damage bam");
                    break;
                }
                
            }
        }
    }

    protected void checkForPlayerAndDamage() {
        dealAttackDamage();
    }
    
    //updates state based on current conditions 
    protected void updateState() {
        if (target == null) {
            if (behaviour == ENEMY_BEHAVIOUR.CHASE) {
                behaviour = ENEMY_BEHAVIOUR.PATROL;
            }
            return;
        }
        
        double distance = getWorldDistanceTo((ScrollingActor)target);
        
        //state machine for transitioning between states
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
                    hasDealtDamageThisAttack = false; // NEW: Reset damage flag when starting attack
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
    
    protected void takeDamage(int dmg) {
        health -= dmg;
        behaviour = ENEMY_BEHAVIOUR.HURT;
        
        // Update health bar
        if (healthBar != null) {
            healthBar.update(health);
        }
        
        // Create stun animation effect that follows the enemy
        if (getWorld() != null) {
            // Position it slightly above the enemy's center
            StunAnimation stunEffect = new StunAnimation(camera, getWorldX(), getWorldY() - 30, (int) (this.currentImage.getWidth() * 1.5), (int) (this.currentImage.getHeight() * 1.5));
            getWorld().addObject(stunEffect, 0, 0);
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
    
    protected static GreenfootImage scaleImage(GreenfootImage img, double scaleFactor){
        int currentWidth = img.getWidth();
        int currentHeight = img.getHeight();
        
        img.scale((int) (currentWidth * scaleFactor), (int) (currentHeight * scaleFactor));
        return img;
    }
    
    protected boolean isSolidAtPosition(int worldX, int worldY) {
        GameWorld world = (GameWorld) getWorld();
        if (world == null || world.mapGrid == null) return false;

        int tileX = world.worldToTileX(worldX);
        int tileY = world.worldToTileY(worldY);

        int tileType = world.mapGrid.getTileAt(tileX, tileY);

        // Type 1 = walls, Type 2 = platforms
        return tileType == 1 || tileType == 2;
    }
    
    /**
     * Check if a rectangular area contains any solid tiles
     */
    protected boolean isSolidArea(int centerX, int centerY, int width, int height) {
        GameWorld world = (GameWorld) getWorld();
        if (world == null || world.mapGrid == null) return false;
        
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        
        // Check multiple points within the area
        int left = centerX - halfWidth;
        int right = centerX + halfWidth;
        int top = centerY - halfHeight;
        int bottom = centerY + halfHeight;
        
        // Check corners and center
        return isSolidAtPosition(left, top) ||      // Top-left
               isSolidAtPosition(right, top) ||     // Top-right
               isSolidAtPosition(left, bottom) ||   // Bottom-left
               isSolidAtPosition(right, bottom) ||  // Bottom-right
               isSolidAtPosition(centerX, centerY); // Center
    }
}