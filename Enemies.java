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
    
    //fsm for enemy behaviour
    protected enum ENEMY_BEHAVIOUR { 
        IDLE,
        PATROL,
        CHASE, 
        ATTACK, 
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
    protected abstract void attack();
    protected abstract void idleBehavior();
    
    
    public void act() {
        if (health <= 0) {
            die();
            return;
        }
        
        detectPlayer();
        updateState();
        executeBehavior();
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
                    //if loses sight of target, then switch to patrol
                    behaviour = ENEMY_BEHAVIOUR.IDLE;
                } else if (distanceToTarget <= attackRange) {
                    //if in attack range then attack
                    behaviour = ENEMY_BEHAVIOUR.ATTACK;
                }
                break;
                
            case ATTACK:
                if (!isAggro || target == null || distanceToTarget > attackRange) {
                    //if no longer is in agro and no target while greater than attack range, we chase
                    behaviour = ENEMY_BEHAVIOUR.CHASE;
                }
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
            case ATTACK: 
                attack(); 
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