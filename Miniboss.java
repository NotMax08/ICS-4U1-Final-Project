import greenfoot.*;
import java.util.List;

/**
 * Miniboss before the main boss. 
 * Paces back and forth and retreats when player gets close.
 * 
 * @author Paul and Claude
 */
public class Miniboss extends Actor {
    // Health
    private final int MAX_HP = 500;
    private int health = 500;
    
    // Attack system
    private int attackTimer = 300;
    private int attackNum;
    
    // Movement constants
    private static final int PACE_SPEED = 2;
    private static final int DETECTION_RANGE = 200;
    private static final int RETREAT_DISTANCE = 120;
    private static final int PACE_DISTANCE = 150;
    
    // Movement state
    private int startX;
    private int leftBound;
    private int rightBound;
    private boolean facingRight = true;
    private BossState state = BossState.PACING;
    private int retreatCooldown = 0;
    private static final int RETREAT_COOLDOWN_TIME = 60;
    
    // References
    BossRoomTwo room;
    Player player;
    SuperStatBar healthBar;
    private int healthBarYOffset = 100;
    
    //Walking frames
    GreenfootImage trob1 = new GreenfootImage("trob1");
    GreenfootImage trob2 = new GreenfootImage("trob2");
    GreenfootImage trob3 = new GreenfootImage("trob3");
    GreenfootImage trob4 = new GreenfootImage("trob4");
    GreenfootImage[] walking = {trob1,trob2,trob3,trob4};
    GreenfootImage[] mWalk;
    
    //Jumping frames
    GreenfootImage trob5 = new GreenfootImage("trob5");
    GreenfootImage trob6 = new GreenfootImage("trob6");
    GreenfootImage trob7 = new GreenfootImage("trob7");
    GreenfootImage trob8 = new GreenfootImage("trob8");
    GreenfootImage[] jumping = {trob5,trob6,trob7,trob8};
    GreenfootImage[] mJump;
    
    // Explosion frames
    GreenfootImage ex1 = new GreenfootImage("ex1");
    GreenfootImage ex2 = new GreenfootImage("ex2");
    GreenfootImage ex3 = new GreenfootImage("ex3");
    GreenfootImage ex4 = new GreenfootImage("ex4");
    GreenfootImage[] exFrames = {ex1, ex2, ex3, ex4};
    
    // Attack One Frames
    GreenfootImage a1 = new GreenfootImage("a1");
    GreenfootImage a2 = new GreenfootImage("a2");
    GreenfootImage a3 = new GreenfootImage("a3");
    GreenfootImage a4 = new GreenfootImage("a4");
    GreenfootImage a5 = new GreenfootImage("a5");
    GreenfootImage[] attackOne = {a1, a2, a3};
    GreenfootImage[] mA1;
    
    // Attack Two Frames 
    GreenfootImage aa1 = new GreenfootImage("aa1");
    GreenfootImage aa2 = new GreenfootImage("aa2");
    GreenfootImage aa3 = new GreenfootImage("aa3");
    GreenfootImage[] attackTwo = {aa1, aa2, aa3};
    GreenfootImage[] mA2;
    
    // Attack Three Frames 
    GreenfootImage aaa1 = new GreenfootImage("aaa1");
    GreenfootImage aaa2 = new GreenfootImage("aaa2");
    GreenfootImage aaa3 = new GreenfootImage("aaa3");
    GreenfootImage[] attackThree = {aaa1, aaa2, aaa3};
    GreenfootImage[] mA3;
    
    //to switch image frames
    int alert;
    int retreat;
    int pacing;
    public Miniboss(Player player) {
        this.player = player;
    }
    
    @Override
    protected void addedToWorld(World world) {
        healthBar = new SuperStatBar(MAX_HP, health, this, 70, 6, healthBarYOffset, Color.BLUE, Color.RED, true);
        world.addObject(healthBar, 0, 0);
        
        // Set pacing boundaries
        startX = getX();
        leftBound = startX - PACE_DISTANCE;
        rightBound = startX + PACE_DISTANCE;
        
        // Random starting direction
        facingRight = Greenfoot.getRandomNumber(2) == 0;
    }
    
    public void act() {
        if (retreatCooldown > 0) {
            retreatCooldown--;
        }
        
        checkPlayerProximity();
        updateMovement();
        attack();
        updateHealthBar();
    }
    
    /**
     * Checks distance to player and updates state
     */
    private void checkPlayerProximity() {
        if (player == null || player.getWorld() == null) {
            state = BossState.PACING;
            return;
        }
        
        int distanceToPlayer = Math.abs(player.getX() - getX());
        
        if (distanceToPlayer < RETREAT_DISTANCE && retreatCooldown == 0) {
            // Player too close - retreat!
            state = BossState.RETREATING;
        } else if (distanceToPlayer < DETECTION_RANGE) {
            // Player in range - alert/attacking
            state = BossState.ALERT;
            // Face the player
            facingRight = player.getX() > getX();
        } else {
            // Player far away - resume pacing
            state = BossState.PACING;
        }
    }
    
    /**
     * Updates movement based on current state
     */
    private void updateMovement() {
        
        switch (state) {
            case PACING:
                pacing = 0;
                pacingMovement();
                break;
            case ALERT:
                alert = 0;
                alertMovement();
                break;
            case RETREATING:
                retreat = 0;
                retreatingMovement();
                break;
        }
    }
    
    /**
     * Paces back and forth within boundaries
     */
    private void pacingMovement() {
        // Check boundaries
        if (getX() <= leftBound) {
            facingRight = true;
        } else if (getX() >= rightBound) {
            facingRight = false;
        }
        
        // Check for walls
        if (isAtEdge()) {
            facingRight = !facingRight;
        }
        
        // Move in current direction
        if (facingRight) {
            setLocation(getX() + PACE_SPEED, getY());
        } else {
            setLocation(getX() - PACE_SPEED, getY());
        }
        
        // Update image direction
        updateImageDirection();
    }
    
    /**
     * Stops and faces player when alert
     */
    private void alertMovement() {
        // Stay still but track player
        if (player != null) {
            facingRight = player.getX() > getX();
        }
        updateImageDirection();
    }
    
    /**
     * Backs away from player quickly
     */
    private void retreatingMovement() {
        if (player == null) return;
        
        // Move away from player
        if (player.getX() < getX()) {
            // Player on left, move right
            setLocation(getX() + PACE_SPEED * 2, getY());
            facingRight = false; // Face the player while backing away
        } else {
            // Player on right, move left
            setLocation(getX() - PACE_SPEED * 2, getY());
            facingRight = true; // Face the player while backing away
        }
        
        // Set cooldown
        retreatCooldown = RETREAT_COOLDOWN_TIME;
        
        updateImageDirection();
    }
    
    /**
     * Flips image based on direction
     */
    private void updateImageDirection() {
        GreenfootImage img = getImage();
        
        // Mirror image based on direction
        if (facingRight && img.toString().contains("mirrored")) {
            img.mirrorHorizontally();
        } else if (!facingRight && !img.toString().contains("mirrored")) {
            img.mirrorHorizontally();
        }
    }
    
    public void attack() {
        attackTimer--;
        
        if (attackTimer == 0) {
            // Only attack if player is in range
            if (state == BossState.ALERT) {
                attackNum = Greenfoot.getRandomNumber(3) + 1;
                
                switch (attackNum) {
                    case 1:
                        attackOne();
                        break;
                    case 2:
                        attackTwo();
                        break;
                    case 3:
                        attackThree();
                        break;
                }
            }
            
            attackTimer = 300;
        }
    }
    
    private void attackOne() {
        // TODO: Implement attack 1
        // Example: Shoot projectile
    }
    
    private void attackTwo() {
        // TODO: Implement attack 2
        // Example: Area of effect attack
    }
    
    private void attackThree() {
        // TODO: Implement attack 3
        // Example: Summon minions
    }
    
    public void takeDamage(int dmg) {
        health -= dmg;
        
        // Flash red when hit
        GreenfootImage img = getImage();
        img.setTransparency(150);
        
        if (health <= 0) {
            die();
        }
    }
    
    private void updateHealthBar() {
        if (healthBar != null) {
            healthBar.update(health);
        }
    }
    
    private void die() {
        explode();
        
        if (healthBar != null) {
            getWorld().removeObject(healthBar);
        }
        
        getWorld().removeObject(this);
    }
    
    private void explode() {
        // TODO: Add explosion animation
        // Could spawn explosion effects at boss position
    }
}

/**
 * Enum for boss states
 */
enum BossState {
    PACING,     // Normal patrol
    ALERT,      // Player detected, ready to attack
    RETREATING  // Player too close, backing away
}