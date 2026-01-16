import greenfoot.*;
import java.util.*;

/**
 * Grim - A stationary enemy that teleports near players when detected and attacks
 * 
 * @author Max & Claude
 */
public class Grim extends GroundEnemy {
    // Stats
    public static final int GRIM_HEALTH = 15;
    public static final int GRIM_DAMAGE = 1;
    public static final int GRIM_DETECTION_RANGE = 300;
    public static final int GRIM_ATTACK_RANGE = 150;
    public static final int CHARGE_SPEED = 6;
    public static final int GRAVITY = 1;
    public static final int WALL_CHECK_DISTANCE = 20;
    
    // Teleport settings
    public static final int TELEPORT_OFFSET = 80; // How close to teleport to player
    private int originX; // Original spawn position
    private int originY;
    
    // Animation speeds
    public static final int IDLE_ANIMATION_SPEED = 3;
    public static final int ATTACK_ANIMATION_SPEED = 4;
    public static final int TELEPORT_ANIMATION_SPEED = 7;
    public static final int ATTACK_COOLDOWN = 60; // Cooldown after attack before returning to idle
    
    // Images with different sizes
    private GreenfootImage idleImage;
    private ArrayList<GreenfootImage> attackImages = new ArrayList<>();
    private ArrayList<GreenfootImage> teleportImages = new ArrayList<>();
    
    // Animation state
    private int animationCounter = 0;
    private int idleFrame = 0;
    private int attackFrame = 0;
    private int teleportFrame = 0;
    private int chargeDirection = 1;
    
    // Teleport state
    private boolean isTeleporting = false;
    private int teleportAnimationCounter = 0;
    private boolean hasTeleportedToPlayer = false; // Track if we've teleported this aggro cycle
    private int lockedTargetX = 0; // Locked target X position only
    
    private static double SCALE = 0.35;
    
    /**
     * Constructor
     * @param camera The game camera
     * @param startFacingRight Initial facing direction (true = right, false = left)
     */
    public Grim(Camera camera, boolean startFacingRight) {
        super(camera, 
              scaleImage(getUniformImage("GrimIdle.png", 330, 470), SCALE),
              GRIM_HEALTH, GRIM_DAMAGE,
              GRIM_DETECTION_RANGE, GRIM_ATTACK_RANGE,
              0, CHARGE_SPEED, // No patrol speed, only charge speed
              GRAVITY, WALL_CHECK_DISTANCE);
        
        this.isFacingRight = startFacingRight;
        loadImages();
        behaviour = ENEMY_BEHAVIOUR.IDLE;
        healthBarYOffset = -80;
    }
    
    /**
     * Constructor with default facing direction (right)
     */
    public Grim(Camera camera) {
        this(camera, true);
    }
    
    @Override
    protected void addedToWorld(World world) {
        super.addedToWorld(world);
        // Remember starting position
        originX = worldX;
        originY = worldY;
    }
    
    private void loadImages() {
        // Idle (330x470)
        idleImage = scaleImage(getUniformImage("GrimIdle.png", 330, 470), SCALE);
        
        // Attack images (varying sizes)
        attackImages.add(scaleImage(getUniformImage("GrimA1.png", 280, 400), SCALE));
        attackImages.add(scaleImage(getUniformImage("GrimA2.png", 630, 370), SCALE));
        attackImages.add(scaleImage(getUniformImage("GrimA3.png", 530, 370), SCALE));
        attackImages.add(scaleImage(getUniformImage("GrimA4.png", 620, 340), SCALE));
        attackImages.add(scaleImage(getUniformImage("GrimA5.png", 220, 400), SCALE));
        
        // Teleport images (250x430)
        teleportImages.add(scaleImage(getUniformImage("GrimT1.png", 250, 430), SCALE));
        teleportImages.add(scaleImage(getUniformImage("GrimT2.png", 250, 430), SCALE));
        teleportImages.add(scaleImage(getUniformImage("GrimT3.png", 250, 430), SCALE));
    }
    
    @Override
    public void act() {
        super.act();
        
        updateAnimation();
        updateImage();
    }
    
    private void startTeleportToPlayer() {
        if (target == null) return;
        
        // Lock onto the player's current X position only
        lockedTargetX = ((ScrollingActor)target).getWorldX();
        
        isTeleporting = true;
        teleportFrame = 0;
        teleportAnimationCounter = 0;
    }
    
    private void completeTeleportToPlayer() {
        // Use the LOCKED X position, keep current Y (ground level)
        int targetX = lockedTargetX;
        int currentY = worldY; // Stay at current ground level
        
        // Determine which side of locked position to teleport to
        int side = (targetX > worldX) ? -1 : 1; // Teleport to opposite side
        int teleportX = targetX + (side * TELEPORT_OFFSET);
        
        // Teleport to the X position at current ground level
        setWorldPosition(teleportX, currentY);
        velY = 0;
        
        // Face toward the locked target position
        isFacingRight = (targetX > worldX);
        chargeDirection = isFacingRight ? 1 : -1;
        
        // Complete teleport and start attack
        isTeleporting = false;
        teleportFrame = 0;
        hasTeleportedToPlayer = true;
        
        // Start attack animation immediately
        behaviour = ENEMY_BEHAVIOUR.ATTACK_ANIMATION;
        attackFrame = 0;
        animationCounter = 0;
    }
    
    @Override
    protected void updateAnimation() {
        // Handle teleport animation first (highest priority)
        if (isTeleporting) {
            teleportAnimationCounter++;
            if (teleportAnimationCounter >= TELEPORT_ANIMATION_SPEED) {
                teleportAnimationCounter = 0;
                teleportFrame++;
                
                if (teleportFrame >= teleportImages.size()) {
                    // Teleport animation complete - now teleport and start attack
                    completeTeleportToPlayer();
                }
            }
            return; // Don't update other animations while teleporting
        }
        
        // Handle attack animation
        if (behaviour == ENEMY_BEHAVIOUR.ATTACK_ANIMATION) {
            animationCounter++;
            if (animationCounter >= ATTACK_ANIMATION_SPEED) {
                animationCounter = 0;
                attackFrame++;
                
                // Deal damage at frame 2 (when the big swing happens)
                if (attackFrame == 2 || attackFrame == 3 || attackFrame == 4 || attackFrame == 5) {
                    dealAttackDamage();
                }
                
                // Check if attack animation is complete
                if (attackFrame >= attackImages.size()) {
                    // Attack complete - transition to cooldown
                    behaviour = ENEMY_BEHAVIOUR.ATTACK_COOLDOWN;
                    attackCooldownTimer = ATTACK_COOLDOWN;
                    isInAttackCooldown = true;
                    attackFrame = 0; // Reset to show idle during cooldown
                }
            }
            return;
        }
        
        // Reset attack animation when NOT attacking
        if (behaviour != ENEMY_BEHAVIOUR.ATTACK_ANIMATION && behaviour != ENEMY_BEHAVIOUR.ATTACK_COOLDOWN) {
            attackFrame = 0;
            animationCounter = 0;
        }
        
        // Idle animation (just use single frame)
        if (behaviour == ENEMY_BEHAVIOUR.IDLE || behaviour == ENEMY_BEHAVIOUR.ATTACK_COOLDOWN) {
            idleFrame = 0;
        }
    }
    
    @Override
    protected void updateImage() {
        GreenfootImage finalImage;
        
        // Use teleport animation when teleporting
        if (isTeleporting) {
            finalImage = new GreenfootImage(teleportImages.get(teleportFrame));
        }
        // Use attack animation when attacking
        else if (behaviour == ENEMY_BEHAVIOUR.ATTACK_ANIMATION) {
            finalImage = new GreenfootImage(attackImages.get(attackFrame));
        }
        // Use idle image for cooldown and idle
        else {
            finalImage = new GreenfootImage(idleImage);
        }
        
        // Flip based on direction
        if (!isFacingRight) {
            finalImage.mirrorHorizontally();
        }
        
        setImage(finalImage);
    }
    
    @Override
    protected void attackAnimation() {
        // Grim stays still during attack animation
        fall();
    }
    
    @Override
    protected void attackCooldown() {
        // Stay still during cooldown, show idle image
        isMoving = false;
        fall();
        
        // After cooldown, reset and go back to idle
        if (attackCooldownTimer <= 0) {
            behaviour = ENEMY_BEHAVIOUR.IDLE;
            hasTeleportedToPlayer = false; // Reset so it can teleport again
            isInAttackCooldown = false;
        }
    }
    
    @Override
    protected void chase() {
        if (target == null) return;
        
        // If we haven't teleported to player yet and not in cooldown, start teleport
        if (!hasTeleportedToPlayer && !isInAttackCooldown && !isTeleporting) {
            startTeleportToPlayer();
            return;
        }
        
        // Otherwise just stay still and face player
        int targetX = ((ScrollingActor)target).getWorldX();
        isMoving = false;
        
        // Face the player
        int desiredDirection = (targetX > worldX) ? 1 : -1;
        isFacingRight = (desiredDirection == 1);
        
        fall();
    }
    
    @Override
    protected void detectPlayer() {
        List<Player> players = getObjectsInWorldRange(detectionRange, Player.class);
        if (!players.isEmpty()) {
            target = players.get(0);
            isAggro = true;
        } else {
            // Player left range - reset state
            target = null;
            isAggro = false;
            hasTeleportedToPlayer = false; // Allow teleport again when player returns
        }
    }
    
    @Override
    protected void patrol() {
        // Grim doesn't patrol - just stays at origin
        fall();
        isMoving = false;
    }
    
    @Override
    protected void idleBehavior() {
        isMoving = false;
        fall();
        // Grim stays idle until player detected
    }
}