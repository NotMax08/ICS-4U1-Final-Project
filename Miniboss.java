import greenfoot.*;
import java.util.List;

/**
 * Miniboss before the main boss. 
 * Paces back and forth and retreats when player gets close. 
 * Has swiping and summoning attack (summons knight)
 * 
 * @author Paul and Claude
 */
public class Miniboss extends Actor {
    // Health
    private final int MAX_HP = 300;
    private int health = 300;
    
    // Attack system
    private int attackTimer = 100;
    private int attackNum;
    private boolean attackHitRegistered = false;
    
    // Animation
    private int animationFrame = 0;
    private int animationSpeed = 8;
    private int animationCounter = 0;
    private boolean isAttacking = false;
    private int attackAnimationFrame = 0;
    private boolean isDying = false;
    private int deathFrame = 0;
    
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
    MinibossRoom room;
    Player player;
    SuperStatBar healthBar;
    private int healthBarYOffset = -100;
    private SummonBackground currentSummonBg = null;
    
    // Walking frames
    GreenfootImage m1 = new GreenfootImage("m1.png");
    GreenfootImage m2 = new GreenfootImage("m2.png");
    GreenfootImage[] walking = {m1, m2};
    GreenfootImage[] mWalk;
    
    // Jumping frames not used
    GreenfootImage m3 = new GreenfootImage("m3.png");
    GreenfootImage m4 = new GreenfootImage("m4.png");
    GreenfootImage[] jumping = {m3, m4};
    GreenfootImage[] mJump;
    
    // Explosion frames
    GreenfootImage ex1 = new GreenfootImage("stun1.png");
    GreenfootImage ex2 = new GreenfootImage("stun2.png");
    GreenfootImage ex3 = new GreenfootImage("stun3.png");
    GreenfootImage ex4 = new GreenfootImage("stun4.png");
    GreenfootImage[] exFrames = {ex1, ex2, ex3, ex4};
    
    // Attack One Frames
    GreenfootImage a1 = new GreenfootImage("a1.png");
    GreenfootImage a2 = new GreenfootImage("a2.png");
    GreenfootImage a3 = new GreenfootImage("a3.png");
    GreenfootImage a4 = new GreenfootImage("a4.png");
    GreenfootImage a5 = new GreenfootImage("a5.png");
    GreenfootImage[] attackOne = {a1, a2, a3, a4, a5};
    GreenfootImage[] mA1;
    
    // Attack Two Frames (no mirrored needed - facing camera)
    GreenfootImage aa1 = new GreenfootImage("aa1.png");
    GreenfootImage aa2 = new GreenfootImage("aa2.png");
    GreenfootImage[] attackTwo = {aa1, aa2};
    
    private int knightCounter = 0;
    public Miniboss(Player player) {
        this.player = player;
        scaleAllImages();
        initializeMirroredImages();
    }
    
    /**
     * Scales all images to 100x100
     */
    private void scaleAllImages() {
        // Scale walking frames
        for (GreenfootImage img : walking) {
            img.scale(100, 100);
        }
        
        // Scale jumping frames
        for (GreenfootImage img : jumping) {
            img.scale(100, 100);
        }
        
        // Scale explosion frames
        for (GreenfootImage img : exFrames) {
            img.scale(100, 100);
        }
        
        // Scale attack one frames
        for (GreenfootImage img : attackOne) {
            img.scale(100, 100);
        }
        
        // Scale attack two frames
        for (GreenfootImage img : attackTwo) {
            img.scale(100, 100);
        }
    }
    
    /**
     * Creates mirrored versions of all directional images
     */
    private void initializeMirroredImages() {
        // Mirror walking frames
        mWalk = new GreenfootImage[walking.length];
        for (int i = 0; i < walking.length; i++) {
            mWalk[i] = new GreenfootImage(walking[i]);
            mWalk[i].mirrorHorizontally();
        }
        
        // Mirror jumping frames
        mJump = new GreenfootImage[jumping.length];
        for (int i = 0; i < jumping.length; i++) {
            mJump[i] = new GreenfootImage(jumping[i]);
            mJump[i].mirrorHorizontally();
        }
        
        // Mirror attack one frames
        mA1 = new GreenfootImage[attackOne.length];
        for (int i = 0; i < attackOne.length; i++) {
            mA1[i] = new GreenfootImage(attackOne[i]);
            mA1[i].mirrorHorizontally();
        }
        
        // Set initial image
        setImage(walking[0]);
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
        
        // Set initial image
        if (facingRight) {
            setImage(walking[0]);
        } else {
            setImage(mWalk[0]);
        }
    }
    
    public void act() {
        if (isDying) {
            playDeathAnimation();
            return;
        }
        
        if (retreatCooldown > 0) {
            retreatCooldown--;
        }
        
        if (isAttacking) {
            // Continue attack animation
            if (attackNum == 1 || attackNum == 2) {
                attackOne();
            } else if (attackNum == 3) {
                attackTwo();
            }
        } else {
            checkPlayerProximity();
            updateMovement();
            animateMovement();
            attack();
        }
        
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
            state = BossState.RETREATING;
        } else if (distanceToPlayer < DETECTION_RANGE) {
            state = BossState.ALERT;
            facingRight = player.getX() > getX();
        } else {
            state = BossState.PACING;
        }
    }
    
    /**
     * Updates movement based on current state
     */
    private void updateMovement() {
        switch (state) {
            case PACING:
                pacingMovement();
                break;
            case ALERT:
                alertMovement();
                break;
            case RETREATING:
                retreatingMovement();
                break;
        }
    }
    
    /**
     * Paces back and forth within boundaries
     */
    private void pacingMovement() {
        if (getX() <= leftBound) {
            facingRight = true;
        } else if (getX() >= rightBound) {
            facingRight = false;
        }
        
        if (isAtEdge()) {
            facingRight = !facingRight;
        }
        
        if (facingRight) {
            setLocation(getX() + PACE_SPEED, getY());
        } else {
            setLocation(getX() - PACE_SPEED, getY());
        }
    }
    
    /**
     * Stops and faces player when alert
     */
    private void alertMovement() {
        if (player != null) {
            facingRight = player.getX() > getX();
        }
    }
    
    /**
     * Backs away from player quickly
     */
    private void retreatingMovement() {
        if (player == null) return;
        
        if (player.getX() < getX()) {
            setLocation(getX() + PACE_SPEED * 2, getY());
            facingRight = false;
        } else {
            setLocation(getX() - PACE_SPEED * 2, getY());
            facingRight = true;
        }
        
        retreatCooldown = RETREAT_COOLDOWN_TIME;
    }
    
    /**
     * Animates walking movement
     */
    private void animateMovement() {
        animationCounter++;
        
        if (animationCounter >= animationSpeed) {
            animationCounter = 0;
            animationFrame = (animationFrame + 1) % walking.length;
            
            if (facingRight) {
                setImage(walking[animationFrame]);
            } else {
                setImage(mWalk[animationFrame]);
            }
        }
    }
    
    public void attack() {
        if (isAttacking) {
            return;
        }
        
        attackTimer--;
        
        if (attackTimer <= 0) {
            if (state == BossState.ALERT) {
                attackNum = Greenfoot.getRandomNumber(3) + 1;
                knightCounter = room.getObjects(Knight.class).size();
                if(knightCounter == 2){
                    attackNum = 1;
                }
                isAttacking = true;
                attackAnimationFrame = 0;
                attackHitRegistered = false;
            }
            
            attackTimer = 100;
        }
    }
    
    /**
     * Melee attack that damages player on contact
     */
    private void attackOne() {
        if (attackAnimationFrame < attackOne.length * 5) {
            int frameIndex = attackAnimationFrame / 5;
            
            if (frameIndex < attackOne.length) {
                if (facingRight) {
                    setImage(attackOne[frameIndex]);
                } else {
                    setImage(mA1[frameIndex]);
                }
                
                // Check for hit on the damage frame (frame 2 - the swing)
                if (frameIndex == 2 && !attackHitRegistered) {
                    Player hitPlayer = (Player) getOneIntersectingObject(Player.class);
                    if (hitPlayer != null) {
                        hitPlayer.takeDamage(1);
                        attackHitRegistered = true;
                    }
                }
            }
            
            attackAnimationFrame++;
        } else {
            // Reset to walking animation
            if (facingRight) {
                setImage(walking[0]);
            } else {
                setImage(mWalk[0]);
            }
            
            isAttacking = false;
            attackAnimationFrame = 0;
            animationCounter = 0;
            animationFrame = 0;
        }
    }
    
    /**
     * Summoning attack with background effect
     */
    private void attackTwo() {
        if (attackAnimationFrame < attackTwo.length * 20) {
            int frameIndex = attackAnimationFrame / 20;
            
            if (frameIndex < attackTwo.length) {
                setImage(attackTwo[frameIndex]);
                
                // Spawn or update summon background
                if (currentSummonBg == null) {
                    currentSummonBg = new SummonBackground();
                    getWorld().addObject(currentSummonBg, getX(), getY());
                    // Move summon background behind the boss
                    currentSummonBg.getImage().setTransparency(200);
                }
                
                // Update background frame (mirrored on second frame)
                currentSummonBg.setFrame(frameIndex);
                currentSummonBg.setLocation(getX(), getY());
            }
            
            attackAnimationFrame++;
        } else {
            // Clean up summon background
            if (currentSummonBg != null && currentSummonBg.getWorld() != null) {
                getWorld().removeObject(currentSummonBg);
                currentSummonBg = null;
            }
            
            // Spawn summoned enemies BEFORE resetting attack state
            spawnSummonedAttack();
            
            // Reset to walking animation
            if (facingRight) {
                setImage(walking[0]);
            } else {
                setImage(mWalk[0]);
            }
            
            isAttacking = false;
            attackAnimationFrame = 0;
            animationCounter = 0;
            animationFrame = 0;
        }
    }
    
    /**
     * Spawns the actual summoned attack after animation
     */
    private void spawnSummonedAttack() {
        GameWorld world = (GameWorld) getWorld();
        if (world == null) return;
        
        // Get camera reference from the world
        Camera camera = world.camera;
        if (camera == null) return;
        
        // Use original Knight constructor with camera for consistency
        Knight knight = new Knight(camera, -1);   // facing left
        
        // In MinibossRoom, screen position = world position since camera is locked
        // So we can use getX() and getY() as world coordinates
        int myWorldX = getX();
        int myWorldY = getY();
        
        // Add knight to world at screen position first (required for ScrollingActor)
        int screenX = camera.worldToScreenX(myWorldX - 150);
        int screenY = camera.worldToScreenY(myWorldY - 50);
        world.addObject(knight, screenX, screenY);
        
        // Then set the world position (this updates internal worldX/worldY)
        knight.setWorldPosition(myWorldX - 150, myWorldY - 100);
    }
    
    public void takeDamage(int dmg) {
        health -= dmg;
        
        // Flash effect
        GreenfootImage img = getImage();
        img.setTransparency(150);
        Greenfoot.delay(2);
        img.setTransparency(255);
        
        if (health <= 0) {
            isDying = true;
            deathFrame = 0;
        }
    }
    
    /**
     * Plays death explosion animation
     */
    private void playDeathAnimation() {
        if (deathFrame < exFrames.length * 8) {
            int frameIndex = deathFrame / 8;
            
            if (frameIndex < exFrames.length) {
                setImage(exFrames[frameIndex]);
            }
            
            deathFrame++;
        } else {
            die();
        }
    }
    
    private void updateHealthBar() {
        if (healthBar != null) {
            healthBar.update(health);
        }
    }
    
    private void die() {
        if (healthBar != null) {
            getWorld().removeObject(healthBar);
        }
        
        if (currentSummonBg != null && currentSummonBg.getWorld() != null) {
            getWorld().removeObject(currentSummonBg);
        }
        
        getWorld().removeObject(this);
    }
}

/**
 * Enum for boss states
 */
enum BossState {
    PACING,
    ALERT,
    RETREATING
}

/**
 * Background effect for summoning attack
 */
class SummonBackground extends Actor {
    private GreenfootImage summonbg = new GreenfootImage("summonbg.png");
    private GreenfootImage summonbgMirrored;
    
    public SummonBackground() {
        summonbg.scale(300, 100);
        summonbgMirrored = new GreenfootImage(summonbg);
        summonbgMirrored.mirrorHorizontally();
        setImage(summonbg);
    }
    
    public void setFrame(int frame) {
        if (frame == 0) {
            setImage(summonbg);
        } else {
            setImage(summonbgMirrored);
        }
    }
}