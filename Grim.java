import greenfoot.*;
import java.util.*;

/**
 * A stationary enemy that teleports near players when detected and attacks
 * - All enemy art come form https://www.spriters-resource.com/pc_computer/hollowknight/
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
    public static final int TELEPORT_OFFSET = 80;
    private int originX;
    private int originY;
    
    // Animation speeds
    public static final int IDLE_ANIMATION_SPEED = 3;
    public static final int ATTACK_ANIMATION_SPEED = 4;
    public static final int TELEPORT_ANIMATION_SPEED = 7;
    public static final int ATTACK_COOLDOWN = 60;
    
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
    private boolean hasTeleportedToPlayer = false;
    private int lockedTargetX = 0;
    
    private static double SCALE = 0.35;
    
    // Sound manager reference
    private SoundManager soundManager;
    
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
              0, CHARGE_SPEED,
              GRAVITY, WALL_CHECK_DISTANCE);
        
        this.isFacingRight = startFacingRight;
        loadImages();
        loadSounds();
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
        originX = worldX;
        originY = worldY;
    }
    
    private void loadSounds() {
        soundManager = SoundManager.getInstance();
        // Load 5 instances of the slash sound for overlapping playback
        soundManager.loadSound("grim_slash", "GrimSlash2.wav", 5);
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
        
        lockedTargetX = ((ScrollingActor)target).getWorldX();
        
        isTeleporting = true;
        teleportFrame = 0;
        teleportAnimationCounter = 0;
    }
    
    private void completeTeleportToPlayer() {
        int targetX = lockedTargetX;
        int currentY = worldY;
        
        int side = (targetX > worldX) ? -1 : 1;
        int teleportX = targetX + (side * TELEPORT_OFFSET);
        
        setWorldPosition(teleportX, currentY);
        velY = 0;
        
        isFacingRight = (targetX > worldX);
        chargeDirection = isFacingRight ? 1 : -1;
        
        isTeleporting = false;
        teleportFrame = 0;
        hasTeleportedToPlayer = true;
        
        behaviour = ENEMY_BEHAVIOUR.ATTACK_ANIMATION;
        attackFrame = 0;
        animationCounter = 0;
    }
    
    @Override
    protected void updateAnimation() {
        if (isTeleporting) {
            teleportAnimationCounter++;
            if (teleportAnimationCounter >= TELEPORT_ANIMATION_SPEED) {
                teleportAnimationCounter = 0;
                teleportFrame++;
                
                if (teleportFrame >= teleportImages.size()) {
                    completeTeleportToPlayer();
                }
            }
            return;
        }
        
        if (behaviour == ENEMY_BEHAVIOUR.ATTACK_ANIMATION) {
            animationCounter++;
            if (animationCounter >= ATTACK_ANIMATION_SPEED) {
                animationCounter = 0;
                attackFrame++;
                
                // Play slash sound on frame 2 (when the big swing happens)
                if (attackFrame == 2) {
                    soundManager.playSound("grim_slash");
                }
                
                // Deal damage at frames 2-5
                if (attackFrame == 2 || attackFrame == 3 || attackFrame == 4 || attackFrame == 5) {
                    dealAttackDamage();
                }
                
                if (attackFrame >= attackImages.size()) {
                    behaviour = ENEMY_BEHAVIOUR.ATTACK_COOLDOWN;
                    attackCooldownTimer = ATTACK_COOLDOWN;
                    isInAttackCooldown = true;
                    attackFrame = 0;
                }
            }
            return;
        }
        
        if (behaviour != ENEMY_BEHAVIOUR.ATTACK_ANIMATION && behaviour != ENEMY_BEHAVIOUR.ATTACK_COOLDOWN) {
            attackFrame = 0;
            animationCounter = 0;
        }
        
        if (behaviour == ENEMY_BEHAVIOUR.IDLE || behaviour == ENEMY_BEHAVIOUR.ATTACK_COOLDOWN) {
            idleFrame = 0;
        }
    }
    
    @Override
    protected void updateImage() {
        GreenfootImage finalImage;
        
        if (isTeleporting) {
            finalImage = new GreenfootImage(teleportImages.get(teleportFrame));
        }
        else if (behaviour == ENEMY_BEHAVIOUR.ATTACK_ANIMATION) {
            finalImage = new GreenfootImage(attackImages.get(attackFrame));
        }
        else {
            finalImage = new GreenfootImage(idleImage);
        }
        
        if (!isFacingRight) {
            finalImage.mirrorHorizontally();
        }
        
        setImage(finalImage);
    }
    
    @Override
    protected void attackAnimation() {
        fall();
    }
    
    @Override
    protected void attackCooldown() {
        isMoving = false;
        fall();
        
        if (attackCooldownTimer <= 0) {
            behaviour = ENEMY_BEHAVIOUR.IDLE;
            hasTeleportedToPlayer = false;
            isInAttackCooldown = false;
        }
    }
    
    @Override
    protected void chase() {
        if (target == null) return;
        
        if (!hasTeleportedToPlayer && !isInAttackCooldown && !isTeleporting) {
            startTeleportToPlayer();
            return;
        }
        
        int targetX = ((ScrollingActor)target).getWorldX();
        isMoving = false;
        
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
            target = null;
            isAggro = false;
            hasTeleportedToPlayer = false;
        }
    }
    
    @Override
    protected void patrol() {
        fall();
        isMoving = false;
    }
    
    @Override
    protected void idleBehavior() {
        isMoving = false;
        fall();
    }
}