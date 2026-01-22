import greenfoot.*;
import java.util.*;
/**
 * Enemy that runs towards a player, similar to golem but is faster and swings a mace around
 * - All enemy art come form https://www.spriters-resource.com/pc_computer/hollowknight/
 * - Sounds are all from mixkit https://mixkit.co/free-sound-effects/
 * @author Max & Claude
 */
public class Knight extends GroundEnemy {
    public static final int KNIGHT_HEALTH = 40;
    public static final int KNIGHT_DAMAGE = 1;
    public static final int KNIGHT_DETECTION_RANGE = 400;
    public static final int KNIGHT_ATTACK_RANGE = 150;
    public static final int PATROL_SPEED = 0;
    public static final int CHASE_SPEED = 5;
    public static final int GRAVITY = 1;
    public static final int WALL_CHECK_DISTANCE = 10;
    
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
    private static final int ATTACK_COOLDOWN = 60;
    
    // Attack sequence tracking
    private boolean isAttackComplete = false;
    private boolean isRecovering = false;
    
    private int startingDirection;
    
    // Sound manager reference
    private SoundManager soundManager;
    
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
        loadSounds();
        behaviour = ENEMY_BEHAVIOUR.IDLE;
        healthBarYOffset = -100;
    }
    
    public Knight(Camera camera) {
        this(camera, 1);
    }
    /**
     * Constructor for static worlds (no camera)
     */
    public Knight(int facingDirection) {
        super(scaleImage(getUniformImage("KnightIdle.png", 620, 390), SCALE),
              KNIGHT_HEALTH, KNIGHT_DAMAGE,
              KNIGHT_DETECTION_RANGE, KNIGHT_ATTACK_RANGE,
              PATROL_SPEED, CHASE_SPEED, GRAVITY, WALL_CHECK_DISTANCE);
        
        this.startingDirection = facingDirection;
        this.direction = facingDirection;
        this.isFacingRight = (facingDirection == 1);
        
        loadImages();
        loadSounds();
        behaviour = ENEMY_BEHAVIOUR.IDLE;
        healthBarYOffset = -100;
    }
    
    private void loadSounds() {
        soundManager = SoundManager.getInstance();
        // Load 5 instances of the slash sound for overlapping playback
        soundManager.loadSound("knight_slash", "KnightSlash.wav", 5);
    }
    
    private void loadImages() {
        // Idle - 620 by 390
        idleImage = scaleImage(getUniformImage("KnightIdle.png", 620, 390), SCALE);
        
        // Run animation - all 620 by 490
        runImages.add(scaleImage(getUniformImage("KnightRun2.png", 620, 490), SCALE));
        runImages.add(scaleImage(getUniformImage("KnightRun3.png", 620, 490), SCALE));
        
        // Attack animation - varying sizes
        attackImages.add(scaleImage(getUniformImage("KnightA1.png", 580, 390), SCALE));
        attackImages.add(scaleImage(getUniformImage("KnightA2.png", 700, 580), SCALE));
        attackImages.add(scaleImage(getUniformImage("KnightA3.png", 700, 600), SCALE));
        attackImages.add(scaleImage(getUniformImage("KnightA4.png", 700, 600), SCALE));
        
        // Recovery animation - all 650 by 570
        recoverImages.add(scaleImage(getUniformImage("KnightRecover1.png", 650, 570), SCALE));
        recoverImages.add(scaleImage(getUniformImage("KnightRecover2.png", 650, 570), SCALE));
        recoverImages.add(scaleImage(getUniformImage("KnightRecover3.png", 650, 570), SCALE));
    }
    
    @Override
    public void act() {
        super.act();
        
        updateAnimation();
        updateImage();
    }
    
    @Override
    protected void updateAnimation() {
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
        
        if (behaviour == ENEMY_BEHAVIOUR.ATTACK_ANIMATION && !isRecovering) {
            attackTimer++;
            if (attackTimer >= ATTACK_FRAME_DELAY) {
                attackTimer = 0;
                attackFrame++;
                
                // Play slash sound and deal damage at frame 3 (the heavy swing frame)
                if (attackFrame == 3) {
                    soundManager.playSound("knight_slash");
                    dealAttackDamage();
                }
                
                if (attackFrame >= attackImages.size()) {
                    isAttackComplete = true;
                    isRecovering = true;
                    attackFrame = attackImages.size() - 1;
                    recoverFrame = 0;
                    recoverTimer = 0;
                }
            }
        }
        
        if (behaviour == ENEMY_BEHAVIOUR.ATTACK_ANIMATION && isRecovering) {
            recoverTimer++;
            if (recoverTimer >= RECOVER_FRAME_DELAY) {
                recoverTimer = 0;
                recoverFrame++;
                
                if (recoverFrame >= recoverImages.size()) {
                    behaviour = ENEMY_BEHAVIOUR.ATTACK_COOLDOWN;
                    attackCooldownTimer = ATTACK_COOLDOWN;
                    isInAttackCooldown = true;
                    
                    attackFrame = 0;
                    recoverFrame = 0;
                    isAttackComplete = false;
                    isRecovering = false;
                }
            }
        }
        
        if (behaviour != ENEMY_BEHAVIOUR.ATTACK_ANIMATION) {
            //reset all animation frames and timers!
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
        
        if (behaviour == ENEMY_BEHAVIOUR.ATTACK_ANIMATION) {
            if (isRecovering) {
                int frameIndex = Math.min(recoverFrame, recoverImages.size() - 1);
                finalImage = new GreenfootImage(recoverImages.get(frameIndex));
            } else {
                int frameIndex = Math.min(attackFrame, attackImages.size() - 1);
                finalImage = new GreenfootImage(attackImages.get(frameIndex));
            }
        } else if (behaviour == ENEMY_BEHAVIOUR.CHASE && isMoving) {
            finalImage = new GreenfootImage(runImages.get(runFrame));
        } else {
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
    protected void takeDamage(int dmg) {
        super.takeDamage(dmg);
        attackFrame = 0;
        attackTimer = 0;
        recoverFrame = 0;
        recoverTimer = 0;
        isAttackComplete = false;
        isRecovering = false;
    }
    
    @Override
    protected void idleBehavior() {
        fall();
        isMoving = false;
    }
    
    @Override
    protected void patrol() {
        fall();
        isMoving = false;
    }
    
    @Override
    protected void chase() {
        if (target == null) return;
        
        ScrollingActor scrollTarget = (ScrollingActor) target;
        int targetX = scrollTarget.getWorldX();
        
        if (Math.abs(targetX - worldX) >= 5) {
            int desiredDirection = (targetX > worldX) ? 1 : -1;
            setDirection(desiredDirection);
            
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
        fall();
        isMoving = false;
    }
}