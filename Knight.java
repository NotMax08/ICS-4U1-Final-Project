import greenfoot.*;
import java.util.*;

public class Knight extends GroundEnemy {
    public static final int KNIGHT_HEALTH = 40;
    public static final int KNIGHT_DAMAGE = 1;
    public static final int KNIGHT_DETECTION_RANGE = 400;
    public static final int KNIGHT_ATTACK_RANGE = 150;
    public static final int PATROL_SPEED = 0; // No patrol movement
    public static final int CHASE_SPEED = 5;
    public static final int GRAVITY = 1;
    public static final int WALL_CHECK_DISTANCE = 10;
    
    // Scale factor for all images
    private static final double SCALE = 0.4;
    
    // Animation images
    private GreenfootImage idleImage;
    private ArrayList<GreenfootImage> runImages = new ArrayList<>();
    private ArrayList<GreenfootImage> recoverImages = new ArrayList<>();
    
    // Animation state
    private int runFrame = 0;
    private int attackFrame = 0;
    private int recoverFrame = 0;
    private int runTimer = 0;
    private int attackTimer = 0;
    private int recoverTimer = 0;
    
    // Animation speeds
    private static final int RUN_FRAME_DELAY = 3;
    private static final int ATTACK_FRAME_DELAY = 7;
    private static final int RECOVER_FRAME_DELAY = 4;
    private static final int ATTACK_COOLDOWN = 60; // Cooldown after recover sequence
    
    // Attack sequence tracking
    private boolean isAttackComplete = false;
    private boolean isRecovering = false;
    
    // Initial facing direction (set in constructor)
    private int startingDirection;
    
    public Knight(Camera camera, int facingDirection) {
        super(camera, 
              scaleImage(getUniformImage("KnightIdle.png", 620, 390), SCALE),
              KNIGHT_HEALTH, KNIGHT_DAMAGE,
              KNIGHT_DETECTION_RANGE, KNIGHT_ATTACK_RANGE,
              PATROL_SPEED, CHASE_SPEED, GRAVITY, WALL_CHECK_DISTANCE);
        
        this.startingDirection = facingDirection;
        this.direction = facingDirection;
        this.isFacingRight = (facingDirection == 1);
        
        loadImages();
        behaviour = ENEMY_BEHAVIOUR.IDLE;
        healthBarYOffset = -100;
    }
    
    // Constructor with default right-facing
    public Knight(Camera camera) {
        this(camera, 1);
    }
    
    private void loadImages() {
        // Idle - 620 by 390
        idleImage = scaleImage(getUniformImage("KnightIdle.png", 620, 390), SCALE);
        
        // Run animation - all 620 by 490
        //runImages.add(scaleImage(getUniformImage("KnightRun1.png", 620, 490), SCALE));
        runImages.add(scaleImage(getUniformImage("KnightRun2.png", 620, 490), SCALE));
        runImages.add(scaleImage(getUniformImage("KnightRun3.png", 620, 490), SCALE));
        
        // Attack animation - varying sizes
        attackImages.add(scaleImage(getUniformImage("KnightA1.png", 580, 390), SCALE)); // 580 by 390
        attackImages.add(scaleImage(getUniformImage("KnightA2.png", 700, 580), SCALE)); // 700 by 580
        attackImages.add(scaleImage(getUniformImage("KnightA3.png", 700, 600), SCALE)); // 700 by 600
        attackImages.add(scaleImage(getUniformImage("KnightA4.png", 700, 600), SCALE)); // 700 by 600
        
        // Recovery animation - all 650 by 570
        recoverImages.add(scaleImage(getUniformImage("KnightRecover1.png", 650, 570), SCALE));
        recoverImages.add(scaleImage(getUniformImage("KnightRecover2.png", 650, 570), SCALE));
        recoverImages.add(scaleImage(getUniformImage("KnightRecover3.png", 650, 570), SCALE));
    }
    
    @Override
    public void act() {
        super.act();
        
        // Update animations
        updateAnimation();
        updateImage();
    }
    
    @Override
    protected void updateAnimation() {
        // Update run animation timer
        if (behaviour == ENEMY_BEHAVIOUR.CHASE && isMoving) {
            runTimer++;
            if (runTimer >= RUN_FRAME_DELAY) {
                runTimer = 0;
                runFrame = (runFrame + 1) % runImages.size();
            }
        } else {
            runFrame = 0;
            runTimer = 0;
        }
        
        // Update attack animation
        if (behaviour == ENEMY_BEHAVIOUR.ATTACK_ANIMATION && !isRecovering) {
            attackTimer++;
            if (attackTimer >= ATTACK_FRAME_DELAY) {
                attackTimer = 0;
                attackFrame++;
                
                // Deal damage at frame 3 (the heavy swing frame)
                if (attackFrame == 3) {
                    dealAttackDamage();
                }
                
                if (attackFrame >= attackImages.size()) {
                    // Attack animation complete - start recovery
                    isAttackComplete = true;
                    isRecovering = true;
                    attackFrame = attackImages.size() - 1; // Hold on last attack frame
                    recoverFrame = 0;
                    recoverTimer = 0;
                }
            }
        }
        
        // Update recovery animation
        if (behaviour == ENEMY_BEHAVIOUR.ATTACK_ANIMATION && isRecovering) {
            recoverTimer++;
            if (recoverTimer >= RECOVER_FRAME_DELAY) {
                recoverTimer = 0;
                recoverFrame++;
                
                if (recoverFrame >= recoverImages.size()) {
                    // Recovery complete - go to cooldown
                    behaviour = ENEMY_BEHAVIOUR.ATTACK_COOLDOWN;
                    attackCooldownTimer = ATTACK_COOLDOWN;
                    isInAttackCooldown = true;
                    
                    // Reset animation states
                    attackFrame = 0;
                    recoverFrame = 0;
                    isAttackComplete = false;
                    isRecovering = false;
                }
            }
        }
        
        // Reset attack/recover states when not attacking
        if (behaviour != ENEMY_BEHAVIOUR.ATTACK_ANIMATION) {
            attackFrame = 0;
            attackTimer = 0;
            recoverFrame = 0;
            recoverTimer = 0;
            isAttackComplete = false;
            isRecovering = false;
        }
    }
    
    @Override
    protected void updateImage() {
        GreenfootImage finalImage;
        
        // Determine which image to use based on state
        if (behaviour == ENEMY_BEHAVIOUR.ATTACK_ANIMATION) {
            if (isRecovering) {
                // Show recovery animation
                int frameIndex = Math.min(recoverFrame, recoverImages.size() - 1);
                finalImage = new GreenfootImage(recoverImages.get(frameIndex));
            } else {
                // Show attack animation
                int frameIndex = Math.min(attackFrame, attackImages.size() - 1);
                finalImage = new GreenfootImage(attackImages.get(frameIndex));
            }
        } else if (behaviour == ENEMY_BEHAVIOUR.CHASE && isMoving) {
            // Running animation
            finalImage = new GreenfootImage(runImages.get(runFrame));
        } else {
            // Idle animation (for IDLE and ATTACK_COOLDOWN states)
            finalImage = new GreenfootImage(idleImage);
        }
        
        // Flip image if facing left
        if (!isFacingRight) {
            finalImage.mirrorHorizontally();
        }
        
        setImage(finalImage);
    }
    
    @Override
    protected void attackAnimation() {
        // Animation is handled in updateAnimation()
        // Knight stays in place during attack and recovery
        fall(); // Apply gravity during attack
    }
    
    @Override
    protected void takeDamage(int dmg) {
        super.takeDamage(dmg);
        // Reset all animation states when hurt
        attackFrame = 0;
        attackTimer = 0;
        recoverFrame = 0;
        recoverTimer = 0;
        isAttackComplete = false;
        isRecovering = false;
    }
    
    @Override
    protected void idleBehavior() {
        // Knight doesn't patrol - just stays idle until player detected
        fall();
        isMoving = false;
    }
    
    @Override
    protected void patrol() {
        // Knight doesn't patrol - same as idle
        fall();
        isMoving = false;
    }
    
    @Override
    protected void chase() {
        if (target == null) return;
        
        ScrollingActor scrollTarget = (ScrollingActor) target;
        int targetX = scrollTarget.getWorldX();
        
        // Face the target
        if (Math.abs(targetX - worldX) >= 5) {
            int desiredDirection = (targetX > worldX) ? 1 : -1;
            setDirection(desiredDirection);
            
            // Move towards target if no obstacles
            if (!isWallAhead() && isGroundAhead() && !isAtEdge()) {
                setWorldPosition(worldX + (desiredDirection * chaseSpeed), worldY);
                isMoving = true;
            } else {
                isMoving = false;
            }
        } else {
            isMoving = false;
        }
        
        fall();
    }
    
    @Override
    protected void attackCooldown() {
        // Stay idle during cooldown
        fall();
        isMoving = false;
    }
}