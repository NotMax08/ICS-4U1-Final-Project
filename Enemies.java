import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Write a description of class Enemies here.
 * 
 * @author Max Yuan 
 * @version (a version number or a date)
 */
public abstract class Enemies extends ScrollingActor
{
    
    protected int health;
    protected int damage;
    protected int detectionRange;
    protected int attackRange;
    protected boolean isAggro;
    protected Actor target; 
    protected GreenfootImage character;
    protected ArrayList<Player> detections;
    protected int direction; // 1 = right, -1 = left
    protected boolean isFacingRight; // Track which way we're facing
    protected int velY;
    
    // Attack cooldown tracking (shared by all enemies)
    protected int attackCooldownTimer = 0;
    protected boolean isInAttackCooldown = false;
    
    //fsm for enemy behaviour
    protected enum ENEMY_BEHAVIOUR { 
        IDLE,
        PATROL,
        CHASE, 
        ATTACK_ANIMATION,  // New state for forced animation
        ATTACK_COOLDOWN,   // New state for cooldown period
        HURT, 
        DEAD 
    }
        
    protected ENEMY_BEHAVIOUR behaviour;
    
    
    public Enemies(Camera camera, GreenfootImage img, int health, int damage, int detectionRange, int attackRange) {
        super(camera);
        character = img;
        this.health = health;
        this.damage = damage;
        this.detectionRange = detectionRange;
        this.attackRange = attackRange;
        this.isAggro = false;
        this.direction = 1; // Start moving right
        this.isFacingRight = true; // Start facing right
        setImage(img);
        System.out.println("--------------");
    }
    
    protected abstract void patrol();
    protected abstract void chase();
    protected abstract void attackAnimation();
    protected abstract void attackCooldown();
    protected abstract void idleBehavior();
    
    
    public void act() {
        if (health <= 0) {
            die();
            return;
        }
        
        detectPlayer();
        updateState();
        executeBehavior();
        updateAttackCooldown(); // Handle cooldown transition
    }
    
    /**
     * Handles attack cooldown and transitions back to chase
     */
    protected void updateAttackCooldown() {
        if (attackCooldownTimer > 0) {
            attackCooldownTimer--;
            isInAttackCooldown = true;
            
            // When cooldown finishes, transition back to chase if still aggro
            if (attackCooldownTimer == 0) {
                isInAttackCooldown = false;
                if (isAggro && target != null) {
                    behaviour = ENEMY_BEHAVIOUR.CHASE;
                } else {
                    behaviour = ENEMY_BEHAVIOUR.IDLE;
                }
            }
        }
    }
    
    protected void updateState() {
        // get distance to target if we have one
        int distanceToTarget = (target != null) ? 
            (int) Math.hypot(target.getX() - getX(), target.getY() - getY()) : Integer.MAX_VALUE;
    
        switch(behaviour) {
            case IDLE:
                // if aggro is on and there is target, change state to CHASE
                if (isAggro && target != null) {
                    behaviour = ENEMY_BEHAVIOUR.CHASE;
                }
                break;
                
            case PATROL:
                // if aggro is on and there is target, change state to CHASE
                if (isAggro && target != null) {
                    behaviour = ENEMY_BEHAVIOUR.CHASE;
                }
                break;
    
            case CHASE:
                if (!isAggro || target == null) {
                    //if loses sight of target, then switch to idle
                    behaviour = ENEMY_BEHAVIOUR.IDLE;
                } else if (distanceToTarget <= attackRange && !isInAttackCooldown) {
                    //if in attack range then start attack animation (only if not on cooldown)
                    behaviour = ENEMY_BEHAVIOUR.ATTACK_ANIMATION;
                }
                break;
                
            case ATTACK_ANIMATION:
                // LOCKED STATE - cannot exit until animation completes
                // Animation will transition to ATTACK_COOLDOWN when done
                break;
                
            case ATTACK_COOLDOWN:
                // LOCKED STATE - cannot exit until cooldown finishes
                // updateAttackCooldown() handles transition
                break;
                
            case HURT:
                if (health <= 0){
                    behaviour = ENEMY_BEHAVIOUR.DEAD;
                }
                
                //hurt after animation, then return to CHASE state
                break;
        }
    }
    
    protected void executeBehavior() {
        switch(behaviour) {
            case IDLE: 
                idleBehavior(); 
                break;
            case PATROL:
                patrol();
                break;
            case CHASE: 
                chase(); 
                break;
            case ATTACK_ANIMATION: 
                attackAnimation(); 
                break;
            case ATTACK_COOLDOWN:
                attackCooldown();
                break;
            case DEAD:
                die();
                break;
        }
    }
    
    // Generic helper methods that can be used by all enemies
    protected void flipImage() {
        getImage().mirrorHorizontally();
        isFacingRight = !isFacingRight;
    }
    
    //will only be used when the player is directly on top of the mob
    protected void faceRight(){
        isFacingRight = true;
    }
 
    protected void fall(int gravity) {
        if (!onGround()) {
            //enemy is supposed to be falling right now
            
            setWorldPosition(getX(), getY() + gravity);
        }
        
        
        if (onGround()){
            velY = 0;
        } else {
            velY += gravity;
            //gravity is too strong so i just did a scalar here
            setWorldPosition(getX(), getY() + (int) (velY / 2.4));
        }
    }
     
    protected boolean onGround(){ return getOneObjectAtWorldOffset(0, getImage().getHeight()/2 +  3, Platform.class) != null;}
    
    protected boolean isBlocked(int checkDistance) {
        return getOneObjectAtWorldOffset(direction * checkDistance, 0, Platform.class) != null;
    }
    
    protected void detectPlayer() {
        
        detections = (ArrayList<Player>) getObjectsInWorldRange(detectionRange, Player.class);
        
        if (!detections.isEmpty()) {
            target = detections.get(0); // Already sorted by distance!
            isAggro = true;
        } else {
            target = null;
            isAggro = false;
        }
    }
    
    protected void takeDamage(int dmg) { 
        health -= dmg;
        behaviour = ENEMY_BEHAVIOUR.HURT;
    }
    
    protected void die() {
        getWorld().removeObject(this);
    }
}