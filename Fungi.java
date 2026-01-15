import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Write a description of class Fungi3 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Fungi extends GroundEnemy
{
    public static final int FUNGI_HEALTH = 10;
    public static final int FUNGI_DAMAGE = 1;
    public static final int FUNGI_DETECTION_RANGE = 200;
    public static final int FUNGI_ATTACK_RANGE = 100;
    public static final int PATROL_SPEED = 1;
    public static final int CHASE_SPEED = 6;
    public static final int ROLL_SPEED = 5; // Speed during roll attack
    public static final int GRAVITY = 1;
    public static final int WALL_CHECK_DISTANCE = 20;
    public static final int IMAGE_WIDTH = 62;
    public static final int IMAGE_HEIGHT = 71;
    
    // Animation constants - MAKE THESE HIGHER (slower animation)
    public static final int ANIMATION_SPEED = 10; // Increased from 10
    public static final int ATTACK_ANIMATION_SPEED = 10; // Increased from 4 (slower attack)
    public static final int ATTACK_COOLDOWN = 60; // Increased from 30
    
    // Images
    private ArrayList<GreenfootImage> idleImages = new ArrayList<>();
    private ArrayList<GreenfootImage> attackImages = new ArrayList<>();
    
    // Animation state
    private int animationCounter = 0;
    private int currentFrame = 0;
    private int attackFrame = 0;
    private int attackAnimationCounter = 0;
    private boolean isMoving = false;
    
    // Timers for animation delays
    private int frameDelayCounter = 0;
    private static final int FRAME_DELAY = 3; // Only update animation every 3 frames
    
    private int rollDirection = 1;
    
    
    public Fungi(Camera camera) {
        super(camera, 
              getUniformImage("Fungi.png", IMAGE_WIDTH, IMAGE_HEIGHT),
              FUNGI_HEALTH, FUNGI_DAMAGE,
              FUNGI_DETECTION_RANGE, FUNGI_ATTACK_RANGE,
              PATROL_SPEED, CHASE_SPEED, GRAVITY, WALL_CHECK_DISTANCE);
        
        loadImages();
        behaviour = ENEMY_BEHAVIOUR.IDLE;
        healthBarYOffset = -120;
    }
    
    private void loadImages() {
        // Load idle/walking images
        idleImages.add(getUniformImage("Fungi.png", IMAGE_WIDTH, IMAGE_HEIGHT));
        idleImages.add(getUniformImage("Fungi2.png", IMAGE_WIDTH, IMAGE_HEIGHT));
        idleImages.add(getUniformImage("Fungi3.png", IMAGE_WIDTH, IMAGE_HEIGHT));
        
        // Load attack/roll images
        attackImages.add(getUniformImage("FungiA1.png", IMAGE_WIDTH, IMAGE_HEIGHT));
        attackImages.add(getUniformImage("FungiA2.png", IMAGE_WIDTH, IMAGE_HEIGHT));
        attackImages.add(getUniformImage("FungiA3.png", IMAGE_WIDTH, IMAGE_HEIGHT));
        attackImages.add(getUniformImage("FungiA4.png", IMAGE_WIDTH, IMAGE_HEIGHT));
        attackImages.add(getUniformImage("FungiA5.png", IMAGE_WIDTH, IMAGE_HEIGHT));
        attackImages.add(getUniformImage("FungiA6.png", IMAGE_WIDTH, IMAGE_HEIGHT));
        attackImages.add(getUniformImage("FungiA7.png", IMAGE_WIDTH, IMAGE_HEIGHT));
    }
    
    public void act() {
        super.act();
        
        // Only update animation every FRAME_DELAY frames to prevent lag
        /*
        frameDelayCounter++;
        if (frameDelayCounter >= FRAME_DELAY) {
            frameDelayCounter = 0;
            updateAnimation();
        }
        */
        updateAnimation();
        updateImage();
    }
    
    @Override
    protected void updateAnimation() {
        if (behaviour == ENEMY_BEHAVIOUR.ATTACK_ANIMATION) {
            attackAnimationCounter++;
            if (attackAnimationCounter >= ATTACK_ANIMATION_SPEED) {
                attackAnimationCounter = 0;
                attackFrame++;
                
                // Deal damage at a specific frame (e.g., frame 3 or 4)
                if (attackFrame == 0 || attackFrame == 1 || attackFrame == 2 ||attackFrame == 3 || attackFrame == 4) {  // Choose the frame where impact should occur
                    dealAttackDamage();  // Call the base class method
                }
                
                // Check if attack animation is complete
                if (attackFrame >= attackImages.size()) {
                    attackFrame = attackImages.size() - 1;
                    
                    // Transition to cooldown
                    behaviour = ENEMY_BEHAVIOUR.ATTACK_COOLDOWN;
                    attackCooldownTimer = ATTACK_COOLDOWN;
                    isInAttackCooldown = true;
                }
            }
            return;
        }
        // Handle attack animation
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
                        // player.takeDamage(damage);
                    }
                    
                    // Transition to cooldown
                    behaviour = ENEMY_BEHAVIOUR.ATTACK_COOLDOWN;
                    attackCooldownTimer = ATTACK_COOLDOWN;
                    isInAttackCooldown = true;
                }
            }
            return; // Don't update walking animation during attack
        }
        
        // Reset attack animation when NOT attacking
        if (behaviour != ENEMY_BEHAVIOUR.ATTACK_ANIMATION && behaviour != ENEMY_BEHAVIOUR.ATTACK_COOLDOWN) {
            attackFrame = 0;
            attackAnimationCounter = 0;
        }
        
        // Animate idle/walking when moving
        if ((behaviour == ENEMY_BEHAVIOUR.PATROL || behaviour == ENEMY_BEHAVIOUR.CHASE || behaviour == ENEMY_BEHAVIOUR.IDLE) && isMoving) {
            animationCounter++;
            if (animationCounter >= ANIMATION_SPEED) {
                animationCounter = 0;
                currentFrame = (currentFrame + 1) % idleImages.size();
            }
        } else if (!isMoving) {
            currentFrame = 0;
            animationCounter = 0;
        }
        
        
        
    }
    
    @Override
    protected void updateImage() {
        GreenfootImage finalImage;
        
        // Use attack animation when rolling
        if (behaviour == ENEMY_BEHAVIOUR.ATTACK_ANIMATION || behaviour == ENEMY_BEHAVIOUR.ATTACK_COOLDOWN) {
            finalImage = new GreenfootImage(attackImages.get(attackFrame));
        } else {
            // Use idle/walking animation
            finalImage = new GreenfootImage(idleImages.get(currentFrame));
        }
        
        // Flip based on direction
        if (isFacingRight) {
            finalImage.mirrorHorizontally();
        }
        
        setImage(finalImage);
    }
    
    @Override
    protected void attackAnimation() {
        
        if ( (ScrollingActor)target != null){
            int targetX = ((ScrollingActor)target).getWorldX();
            int desiredDirection = (targetX > worldX) ? 1 : -1;
            direction = desiredDirection;
            
            // Check if close enough to start roll attack
            int distanceToTarget = Math.abs(targetX - worldX);
            if (distanceToTarget <= attackRange && !isInAttackCooldown) {
                // Start roll attack!
                // EXPLICITLY calculate roll direction from target position
                rollDirection = (targetX > worldX) ? 1 : -1;
                isFacingRight = (rollDirection == 1); // Update facing immediately
            }
            
            // Check for walls/edges before moving
            if (isWallAhead() || isAtEdge() || !isGroundAhead()) {
                // Hit an obstacle, stop rolling
                behaviour = ENEMY_BEHAVIOUR.ATTACK_COOLDOWN;
                attackCooldownTimer = ATTACK_COOLDOWN;
                isInAttackCooldown = true;
                return;
            }
            
            // Move in the roll direction
            setWorldPosition(worldX + (rollDirection * ROLL_SPEED), worldY);
        }
        /*
        int targetX = ((ScrollingActor)target).getWorldX();
        int desiredDirection = (targetX > worldX) ? 1 : -1;
        direction = desiredDirection;
        
        // Check if close enough to start roll attack
        int distanceToTarget = Math.abs(targetX - worldX);
        if (distanceToTarget <= attackRange && !isInAttackCooldown) {
            // Start roll attack!
            // EXPLICITLY calculate roll direction from target position
            rollDirection = (targetX > worldX) ? 1 : -1;
            isFacingRight = (rollDirection == 1); // Update facing immediately
        }
        
        // Check for walls/edges before moving
        if (isWallAhead() || isAtEdge() || !isGroundAhead()) {
            // Hit an obstacle, stop rolling
            behaviour = ENEMY_BEHAVIOUR.ATTACK_COOLDOWN;
            attackCooldownTimer = ATTACK_COOLDOWN;
            isInAttackCooldown = true;
            return;
        }
        
        // Move in the roll direction
        setWorldPosition(worldX + (rollDirection * ROLL_SPEED), worldY);
        */
        // Apply gravity
        fall();
    }
    
    @Override
    protected void attackCooldown() {
        // Stay still during cooldown
        isMoving = false;
        if (target != null && target instanceof ScrollingActor) {
            ScrollingActor scrollTarget = (ScrollingActor) target;
            int targetX = scrollTarget.getWorldX();
            
            int desiredDirection = (targetX > worldX) ? 1 : -1;
            direction = desiredDirection;
            isFacingRight = (direction == 1);
        }
        fall();
    }
    
    @Override
    protected void chase() {
        if (target == null) return;
        
        int targetX = ((ScrollingActor)target).getWorldX();
        isMoving = false;
        
        // Don't flip back and forth when player is directly above
        if (Math.abs(targetX - worldX) < 5) {
            fall();
            return;
        }
        
        // Determine desired direction
        int desiredDirection = (targetX > worldX) ? 1 : -1;
        direction = desiredDirection;
        
        // Check if close enough to start roll attack
        int distanceToTarget = Math.abs(targetX - worldX);
        if (distanceToTarget <= attackRange && !isInAttackCooldown) {
            // Start roll attack!
            // EXPLICITLY calculate roll direction from target position
            rollDirection = (targetX > worldX) ? 1 : -1;
            isFacingRight = (rollDirection == 1); // Update facing immediately
            behaviour = ENEMY_BEHAVIOUR.ATTACK_ANIMATION;
            attackFrame = 0;
            attackAnimationCounter = 0;
            return;
        }
        
        // Move toward target (normal chase)
        if (!isWallAhead() && isGroundAhead() && !isAtEdge()) {
            if (desiredDirection == 1) {
                setWorldPosition(worldX + CHASE_SPEED, worldY);
                if (!isFacingRight) {
                    isFacingRight = true;
                }
            } else {
                setWorldPosition(worldX - CHASE_SPEED, worldY);
                if (isFacingRight) {
                    isFacingRight = false;
                }
            }
            isMoving = true;
        } else {
            // Face the player even if can't move
            if (desiredDirection == 1 && !isFacingRight) {
                isFacingRight = true;
            } else if (desiredDirection == -1 && isFacingRight) {
                isFacingRight = false;
            }
            isMoving = false;
        }
        
        fall();
    }
    
    @Override
    protected void patrol() {
        fall();
        
        boolean shouldTurn = isAtEdge() || isWallAhead() || !isGroundAhead();
        
        
        
        if (shouldTurn) {
            direction *= -1;
            isFacingRight = !isFacingRight;
            isMoving = false;
        } else {
            setWorldPosition(worldX + (direction * PATROL_SPEED), worldY);
            isMoving = true;
        }
    }
    
    @Override
    protected void idleBehavior() {
        isMoving = false;
        fall();
        
        // Transition to patrol after a moment
        if (Greenfoot.getRandomNumber(100) < 2) {
            behaviour = ENEMY_BEHAVIOUR.PATROL;
        }
    }

}
