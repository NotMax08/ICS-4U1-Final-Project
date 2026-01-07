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
        setImage(img);
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
    
    protected void detectPlayer() {
        detections = (ArrayList) getObjectsInRange(detectionRange, Player.class);
    
        // set target to closest player
        if (!detections.isEmpty()) {
            target = detections.get(0); // get closest player
            isAggro = true;
        } else {
            target = null;
            isAggro = false;
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
    
    protected void takeDamage(int dmg) { 
        health -= dmg;
        behaviour = ENEMY_BEHAVIOUR.HURT;
    }
    
    protected void die() {
        getWorld().removeObject(this);
    }
}