import greenfoot.*;
import java.util.*;
/**
 * GOLEM - Ground enemy that patrols platforms and chases player
 * 
 * @author Max Yuan
 * @version 1.0
 */
public class Golem extends Enemies
{
    //constants for movement and enemy
    public static final int GOLEM_HEALTH = 50;
    public static final int GOLEM_DAMAGE = 10;
    public static final int GOLEM_DETECTION_RANGE = 300;
    public static final int GOLEM_ATTACK_RANGE = 150;
    public static final int PATROL_SPEED = 1;
    public static final int CHASE_SPEED = 3;
    public static final int MAX_IDLE_TIME = 120; 
    public static final int MAX_PATROL_TIME = MAX_IDLE_TIME;
    public static final int GRAVITY = 1;
    public static final int KNOCKBACK_DISTANCE = 10;
    public static final int KNOCKBACK_HEIGHT = 5;
    public static final int WALL_CHECK_DISTANCE = 10;
    
    private int idleTimer;
    private int patrolTimer;

    //image stuff
    public static final int IMAGE_WIDTH = 220;
    public static final int IMAGE_HEIGHT = 220;
    public static final int FADE_SPEED = 15;
    public static final int ANIMATION_SPEED = 10;
    public static final int ATTACK_ANIMATION_SPEED = 6;
    public static final int ATTACK_COOLDOWN = 10;
    
    private GreenfootImage normalIdleImage;
    private GreenfootImage alertIdleImage;
    private ArrayList<GreenfootImage> alertWalkingImages = new ArrayList<>();
    private ArrayList<GreenfootImage> normalWalkingImages = new ArrayList<>();
    private ArrayList<GreenfootImage> attackImages = new ArrayList<>();
    
    private int currentAlpha = 0;
    private boolean isAlert = false;
    private int animationCounter = 0;
    private int currentFrame = 0;
    
    // Attack state tracking
    private int attackFrame = 0;
    private int attackAnimationCounter = 0;
    
    private boolean isMoving = false;
    
    private ScrollingStatBar healthBar;
    

    public Golem(Camera camera) {
        super(camera, 
              getUniformImage("Golem.png", IMAGE_WIDTH, IMAGE_HEIGHT),
              GOLEM_HEALTH,
              GOLEM_DAMAGE,
              GOLEM_DETECTION_RANGE,
              GOLEM_ATTACK_RANGE
        );
        
        this.idleTimer = 0;
        this.patrolTimer = 0;
        
        normalIdleImage = getUniformImage("Golem.png", IMAGE_WIDTH, IMAGE_HEIGHT);
        alertIdleImage = getUniformImage("AlertGolem.png", IMAGE_WIDTH, IMAGE_HEIGHT);
        
        normalWalkingImages.add(getUniformImage("GolemWalking.png", IMAGE_WIDTH, IMAGE_HEIGHT));
        normalWalkingImages.add(getUniformImage("GolemWalking2.png", IMAGE_WIDTH, IMAGE_HEIGHT));
        
        alertWalkingImages.add(getUniformImage("AlertGolemWalking.png", IMAGE_WIDTH, IMAGE_HEIGHT));
        alertWalkingImages.add(getUniformImage("AlertGolemWalking2.png", IMAGE_WIDTH, IMAGE_HEIGHT));
        
        attackImages.add(getUniformImage("GolemAttack1.png", IMAGE_WIDTH, IMAGE_HEIGHT));
        attackImages.add(getUniformImage("GolemAttack2.png", IMAGE_WIDTH, IMAGE_HEIGHT));
        attackImages.add(getUniformImage("GolemAttack3.png", IMAGE_WIDTH, IMAGE_HEIGHT));
        attackImages.add(getUniformImage("GolemAttack4.png", IMAGE_WIDTH, IMAGE_HEIGHT));
        
        behaviour = ENEMY_BEHAVIOUR.IDLE;
        
        // Create health bar - positioned above the golem
        //healthBar = new ScrollingStatBar(camera, this, GOLEM_HEALTH, 80, 8, -120);
        healthBar = null;
    }
    @Override
    protected void addedToWorld(World world) {
        // Create and add the health bar AFTER being added to world
        healthBar = new ScrollingStatBar(camera, this, GOLEM_HEALTH, 80, 8, -120);
        world.addObject(healthBar, 0, 0);
        
        // Set initial position
        healthBar.setWorldPosition(worldX, worldY - 120);
    }

    
    @Override
    public void act() {
        super.act();
        updateAnimation();
        updateImageTransition();
        
        
        GreenfootImage img = getImage();
        /*
        System.out.println("-------");
        System.out.println("Wall check: " + isWallAhead());
        System.out.println("Ground check: "+ isGroundAhead());
        System.out.println("Edge check: " + isAtEdge());
        */
        
        
        createDebugDots();
    
    }
    
    @Override
    protected void flipImage() {
        isFacingRight = !isFacingRight;
    }
    
    private void updateAnimation() {
        // Reset attack animation when entering ATTACK_ANIMATION state fresh
        if (behaviour == ENEMY_BEHAVIOUR.ATTACK_ANIMATION && attackFrame == 0 && attackAnimationCounter == 0) {
            // Starting fresh attack
        }
        
        // Handle attack animation - FORCED to complete
        if (behaviour == ENEMY_BEHAVIOUR.ATTACK_ANIMATION) {
            attackAnimationCounter++;
            if (attackAnimationCounter >= ATTACK_ANIMATION_SPEED) {
                attackAnimationCounter = 0;
                attackFrame++;
                
                // Check if attack animation is complete
                if (attackFrame >= attackImages.size()) {
                    attackFrame = attackImages.size() - 1; // Hold on last frame
                    
                    // Deal damage on last frame
                    Player player = (Player) getOneIntersectingWorldObject(Player.class);
                    if (player != null) {
                        //player.takeDamage(damage);
                    }
                    
                    // Transition to cooldown state
                    behaviour = ENEMY_BEHAVIOUR.ATTACK_COOLDOWN;
                    attackCooldownTimer = ATTACK_COOLDOWN;
                    isInAttackCooldown = true;
                }
            }
            return; // Don't update walking animation during attack
        }
        
        // Reset attack animation when NOT in attack states
        if (behaviour != ENEMY_BEHAVIOUR.ATTACK_ANIMATION && behaviour != ENEMY_BEHAVIOUR.ATTACK_COOLDOWN) {
            attackFrame = 0;
            attackAnimationCounter = 0;
        }
        
        // Only animate when ACTUALLY moving (patrol or chase AND isMoving flag is true)
        if ((behaviour == ENEMY_BEHAVIOUR.PATROL || behaviour == ENEMY_BEHAVIOUR.CHASE) && isMoving) {
            animationCounter++;
            if (animationCounter >= ANIMATION_SPEED) {
                animationCounter = 0;
                currentFrame = (currentFrame + 1) % 2;
            }
        } else {
            currentFrame = 0;
            animationCounter = 0;
        }
    }
    
    private void updateImageTransition() {
        // Determine if we should be in alert mode
        boolean shouldBeAlert = (behaviour == ENEMY_BEHAVIOUR.CHASE || 
                                behaviour == ENEMY_BEHAVIOUR.ATTACK_ANIMATION || 
                                behaviour == ENEMY_BEHAVIOUR.ATTACK_COOLDOWN);
        
        // Fade towards target state
        if (shouldBeAlert && currentAlpha < 255) {
            currentAlpha = Math.min(255, currentAlpha + FADE_SPEED);
        } else if (!shouldBeAlert && currentAlpha > 0) {
            currentAlpha = Math.max(0, currentAlpha - FADE_SPEED);
        }
        
        // Select base images based on current state
        GreenfootImage finalImage;
        
        // Use attack animation if in attack states
        if (behaviour == ENEMY_BEHAVIOUR.ATTACK_ANIMATION || behaviour == ENEMY_BEHAVIOUR.ATTACK_COOLDOWN) {
            finalImage = new GreenfootImage(attackImages.get(attackFrame));
        } else if ((behaviour == ENEMY_BEHAVIOUR.PATROL || behaviour == ENEMY_BEHAVIOUR.CHASE) && isMoving) {
            // Only use walking animation if ACTUALLY MOVING
            if (currentAlpha > 127) { // More than halfway alert
                finalImage = new GreenfootImage(alertWalkingImages.get(currentFrame));
            } else {
                finalImage = new GreenfootImage(normalWalkingImages.get(currentFrame));
            }
        } else {
            // Use idle animation when not moving (or in IDLE state)
            if (currentAlpha > 127) {
                // Fully alert - use alert idle image
                finalImage = new GreenfootImage(alertIdleImage);
            } else if (currentAlpha > 0) {
                // Transitioning - blend idle images
                GreenfootImage blended = new GreenfootImage(normalIdleImage);
                GreenfootImage overlay = new GreenfootImage(alertIdleImage);
                overlay.setTransparency(currentAlpha);
                blended.drawImage(overlay, 0, 0);
                finalImage = blended;
            } else {
                // Normal - use normal idle image
                finalImage = new GreenfootImage(normalIdleImage);
            }
        }
        
        if (!isFacingRight) {
            finalImage.mirrorHorizontally();
        }
        
        setImage(finalImage);
    }
    
    @Override
    protected void idleBehavior() {
        idleTimer++;
        if (idleTimer >= MAX_IDLE_TIME) {
            behaviour = ENEMY_BEHAVIOUR.PATROL;
            idleTimer = 0;
        }
        fall(GRAVITY);
    }
    
    @Override
    protected void patrol() {
        // First, always apply gravity
        fall(GRAVITY);
        
        // Then check if we should turn
        boolean shouldTurn = isAtEdge() || isWallAhead() || !isGroundAhead();
        
        if (shouldTurn) {
            direction *= -1;
            flipImage();
            // Wait a moment after turning to avoid flip-flopping
            Greenfoot.delay(5);
            isMoving = false;
        } else {
            setWorldPosition(worldX + (direction * PATROL_SPEED), worldY);
            isMoving = true;
        }
        
        patrolTimer++;
        if (patrolTimer >= MAX_PATROL_TIME) {
            behaviour = ENEMY_BEHAVIOUR.IDLE;
            patrolTimer = 0;
        }
    }
    
    @Override
    protected void chase() {
        if (target == null) return;
        
        int targetX = ((ScrollingActor)target).getWorldX();
        
        isMoving = false; // Reset movement flag
        
        //added this condition so it doesn't flip back and forth when player is directly above the enemy
        if (!(Math.abs(targetX - worldX) < 5)){
            // Determine desired direction
            int desiredDirection = (targetX > worldX) ? 1 : -1;
            
            // Update direction first to check correctly
            direction = desiredDirection;
            
            // Check if we can move in that direction (not blocked, has ground, and not at edge)
            if (!isWallAhead() && isGroundAhead() && !isAtEdge()) {
                if (desiredDirection == 1) {
                    setWorldPosition(worldX + CHASE_SPEED, worldY);
                    if (!isFacingRight) {
                        flipImage();
                    }
                } else {
                    setWorldPosition(worldX - CHASE_SPEED, worldY);
                    if (isFacingRight) {
                        flipImage();
                    }
                }
                isMoving = true; // We actually moved!
            } else {
                // If blocked, no ground ahead, or at edge - stop and just face the player
                if (desiredDirection == 1 && !isFacingRight) {
                    flipImage();
                } else if (desiredDirection == -1 && isFacingRight) {
                    flipImage();
                }
                // Don't move, just stay at the edge facing the player
                isMoving = false; // Not moving
            }
        }
        
        fall(GRAVITY);
    }
 
    
    
    @Override
    protected void attackAnimation() {
        // Stay still during attack animation, just apply gravity
        fall(GRAVITY);
    }
    
    @Override
    protected void attackCooldown() {
        // Stay still during cooldown, hold last attack frame
        fall(GRAVITY);
    }
    
    @Override
    protected void takeDamage(int dmg) {
        health -= dmg;
        
        // Update the health bar
        if (healthBar != null) {
            healthBar.update(health);
        }
        
        behaviour = ENEMY_BEHAVIOUR.HURT;
        
        // Reset attack state when hurt
        attackFrame = 0;
        attackAnimationCounter = 0;
        attackCooldownTimer = 0;
        isInAttackCooldown = false;
        
        setWorldPosition(worldX + (direction * -KNOCKBACK_DISTANCE), worldY - KNOCKBACK_HEIGHT);
        
        if (health <= 0) {
            behaviour = ENEMY_BEHAVIOUR.DEAD;
        }
    }
    
    public void createDebugDots() {
        if (getWorld() == null) return;
        
        // Remove any existing debug dots
        List<DebugDot> existingDots = getWorld().getObjects(DebugDot.class);
        for (DebugDot dot : existingDots) {
            getWorld().removeObject(dot);
        }
        
        // Calculate offsets to match the actual check methods
        int wallCheckX = getImage().getWidth() / 2;
        int wallCheckY = getImage().getHeight() / 4;
        
        int groundCheckX = WALL_CHECK_DISTANCE;
        int groundCheckY = getImage().getHeight() / 2;
        
        int edgeCheckX = getImage().getWidth()/2 + 10; // Check slightly ahead of feet
        int edgeCheckY = getImage().getHeight()/2;
        
        // Wall check dot (red) - matches isWallAhead()
        DebugDot wallDot = new DebugDot(this, Color.RED, wallCheckX, wallCheckY, 0, camera);
        getWorld().addObject(wallDot, 0, 0);
        wallDot.setWorldPosition(worldX + (direction * wallCheckX), worldY + wallCheckY);
        
        // Ground check dot (blue) - matches isGroundAhead()
        DebugDot groundDot = new DebugDot(this, Color.BLUE, groundCheckX, groundCheckY, 1, camera);
        getWorld().addObject(groundDot, 0, 0);
        groundDot.setWorldPosition(worldX + (direction * groundCheckX), worldY + groundCheckY);
        
        // Edge check dot (yellow) - matches isAtEdge()
        DebugDot edgeDot = new DebugDot(this, Color.YELLOW, edgeCheckX, edgeCheckY, 2, camera);
        getWorld().addObject(edgeDot, 0, 0);
        edgeDot.setWorldPosition(worldX + (direction * edgeCheckX), worldY + edgeCheckY);
    }
    
    // Checks if there's ground to walk on ahead
    protected boolean isGroundAhead() {
        int groundCheckY = getImage().getHeight()/2;
        // Pass OFFSETS, not absolute coordinates
        return getOneObjectAtWorldOffset(0, groundCheckY, Platform.class) != null;
    }
    
    // Checks if there's a wall/platform blocking movement ahead
    protected boolean isWallAhead() {
        int wallCheckX = getImage().getWidth() / 2;
        int wallCheckY = getImage().getHeight() / 4; // Check at body height, not feet
        return getOneObjectAtWorldOffset(direction * wallCheckX, wallCheckY, Platform.class) != null;
    }
    
    @Override
    public boolean isAtEdge() {
        int edgeCheckX = getImage().getWidth()/2 + 10; // Check slightly ahead of feet
        int edgeCheckY = getImage().getHeight()/2; // Check BELOW feet, not at center
        // An edge is when there's NO ground ahead and down
        return getOneObjectAtWorldOffset(direction * edgeCheckX, edgeCheckY, Platform.class) == null;
    }
    
    protected void die() {
        if (healthBar != null && getWorld() != null) {
            getWorld().removeObject(healthBar);
        }
        getWorld().removeObject(this);
    }
    
    public int getXPos(){
        return getX();
    }
    
    public int getYPos(){
        return getY();
    }
    
    public int getDirection(){
        return direction;
    }
}