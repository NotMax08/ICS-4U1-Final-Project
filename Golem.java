import greenfoot.*;
import java.util.*;

public class Golem extends GroundEnemy {
    public static final int GOLEM_HEALTH = 50;
    public static final int GOLEM_DAMAGE = 1;
    public static final int GOLEM_DETECTION_RANGE = 300;
    public static final int GOLEM_ATTACK_RANGE = 100;
    public static final int PATROL_SPEED = 1;
    public static final int CHASE_SPEED = 4;
    public static final int GRAVITY = 1;
    public static final int WALL_CHECK_DISTANCE = 10;
    public static final int IMAGE_SIZE = 220;
    
    // Animation images
    private GreenfootImage normalIdleImage;
    private GreenfootImage alertIdleImage;
    private ArrayList<GreenfootImage> alertWalkImages = new ArrayList<>();
    private ArrayList<GreenfootImage> dieImages = new ArrayList<>();
    
    // Animation state
    private int currentAlpha = 0;
    private int walkFrame = 0;
    private int attackFrame = 0;
    private int walkTimer = 0;
    private int attackTimer = 0;
    private int alphaTimer = 0;
    
    // Animation speeds
    private static final int WALK_FRAME_DELAY = 10;
    private static final int ATTACK_FRAME_DELAY = 8;
    private static final int ALPHA_CHANGE_DELAY = 3;
    private static final int ATTACK_COOLDOWN = 1;
    
    private boolean isAttackComplete = false;
    private int attackHoldTimer = 0;
    private static final int ATTACK_HOLD_FRAMES = 20;
    
    // State timers
    private int idleTimer = 0;
    private int patrolTimer = 0;
    public static final int MAX_IDLE_TIME = 120;
    public static final int MAX_PATROL_TIME = MAX_IDLE_TIME;
    
    // Sound manager reference
    private SoundManager soundManager;
    
    public Golem(Camera camera) {
        super(camera, 
              getUniformImage("Golem.png", IMAGE_SIZE, IMAGE_SIZE),
              GOLEM_HEALTH, GOLEM_DAMAGE,
              GOLEM_DETECTION_RANGE, GOLEM_ATTACK_RANGE,
              PATROL_SPEED, CHASE_SPEED, GRAVITY, WALL_CHECK_DISTANCE);
        
        loadImages();
        loadSounds();
        behaviour = ENEMY_BEHAVIOUR.IDLE;
        healthBarYOffset = -120;
    }
    
    private void loadSounds() {
        soundManager = SoundManager.getInstance();
        // Load 5 instances of the smash sound for overlapping playback
        soundManager.loadSound("golem_smash", "Smash.wav", 5);
    }
    
    private void loadImages() {
        normalIdleImage = getUniformImage("Golem.png", IMAGE_SIZE, IMAGE_SIZE);
        alertIdleImage = getUniformImage("AlertGolem.png", IMAGE_SIZE, IMAGE_SIZE);
        
        walkImages.add(getUniformImage("GolemWalking.png", IMAGE_SIZE, IMAGE_SIZE));
        walkImages.add(getUniformImage("GolemWalking2.png", IMAGE_SIZE, IMAGE_SIZE));
        
        alertWalkImages.add(getUniformImage("AlertGolemWalking.png", IMAGE_SIZE, IMAGE_SIZE));
        alertWalkImages.add(getUniformImage("AlertGolemWalking2.png", IMAGE_SIZE, IMAGE_SIZE));
        
        attackImages.add(getUniformImage("GolemAttack1.png", IMAGE_SIZE, IMAGE_SIZE));
        attackImages.add(getUniformImage("GolemAttack2.png", IMAGE_SIZE, IMAGE_SIZE));
        attackImages.add(getUniformImage("GolemAttack3.png", IMAGE_SIZE, IMAGE_SIZE));
        attackImages.add(getUniformImage("GolemAttack4.png", IMAGE_SIZE, IMAGE_SIZE));
    }
    
    @Override
    public void act() {
        super.act();
        updateAnimation();
        updateImage();
    }
    
    @Override
    protected void updateAnimation() {
        walkTimer++;
        
        if ((behaviour == ENEMY_BEHAVIOUR.PATROL || behaviour == ENEMY_BEHAVIOUR.CHASE) && isMoving) {
            if (walkTimer >= WALK_FRAME_DELAY) {
                walkTimer = 0;
                walkFrame = (walkFrame + 1) % 2;
            }
        } else {
            walkFrame = 0;
            walkTimer = 0;
        }
        
        if (behaviour == ENEMY_BEHAVIOUR.ATTACK_ANIMATION) {
            if (!isAttackComplete) {
                attackTimer++;
                if (attackTimer >= ATTACK_FRAME_DELAY) {
                    attackTimer = 0;
                    attackFrame++;
                    
                    if (attackFrame >= attackImages.size()) {
                        attackFrame = attackImages.size() - 1;
                        isAttackComplete = true;
                        attackHoldTimer = 0;
                        
                        Player player = (Player) getOneIntersectingWorldObject(Player.class);
                        if (player != null) {
                            // player.takeDamage(damage);
                        }
                    }
                    
                    // Play smash sound on frame 3 (when attack lands)
                    if (attackFrame == 3) {
                        soundManager.playSound("golem_smash");
                        dealAttackDamage();
                    }
                }
            } else {
                attackHoldTimer++;
                if (attackHoldTimer >= ATTACK_HOLD_FRAMES) {
                    behaviour = ENEMY_BEHAVIOUR.ATTACK_COOLDOWN;
                    attackCooldownTimer = ATTACK_COOLDOWN;
                    isInAttackCooldown = true;
                    
                    attackFrame = 0;
                    isAttackComplete = false;
                    attackHoldTimer = 0;
                }
            }
        } else {
            attackFrame = 0;
            attackTimer = 0;
            isAttackComplete = false;
            attackHoldTimer = 0;
        }
        
        alphaTimer++;
        if (alphaTimer >= ALPHA_CHANGE_DELAY) {
            alphaTimer = 0;
            boolean shouldBeAlert = (behaviour == ENEMY_BEHAVIOUR.CHASE || 
                                    behaviour == ENEMY_BEHAVIOUR.ATTACK_ANIMATION || 
                                    behaviour == ENEMY_BEHAVIOUR.ATTACK_COOLDOWN);
            
            if (shouldBeAlert && currentAlpha < 255) {
                currentAlpha = Math.min(255, currentAlpha + 15);
            } else if (!shouldBeAlert && currentAlpha > 0) {
                currentAlpha = Math.max(0, currentAlpha - 15);
            }
        }
    }
    
    @Override
    protected void updateImage() {
        GreenfootImage finalImage;
        
        if (behaviour == ENEMY_BEHAVIOUR.ATTACK_ANIMATION || 
            behaviour == ENEMY_BEHAVIOUR.ATTACK_COOLDOWN) {
            int frameIndex = Math.min(attackFrame, attackImages.size() - 1);
            finalImage = new GreenfootImage(attackImages.get(frameIndex));
        } else if ((behaviour == ENEMY_BEHAVIOUR.PATROL || behaviour == ENEMY_BEHAVIOUR.CHASE) && isMoving) {
            if (currentAlpha > 127) {
                finalImage = new GreenfootImage(alertWalkImages.get(walkFrame));
            } else {
                finalImage = new GreenfootImage(walkImages.get(walkFrame));
            }
        } else {
            if (currentAlpha > 127) {
                finalImage = new GreenfootImage(alertIdleImage);
            } else if (currentAlpha > 0) {
                GreenfootImage blended = new GreenfootImage(normalIdleImage);
                GreenfootImage overlay = new GreenfootImage(alertIdleImage);
                overlay.setTransparency(currentAlpha);
                blended.drawImage(overlay, 0, 0);
                finalImage = blended;
            } else {
                finalImage = new GreenfootImage(normalIdleImage);
            }
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
        isAttackComplete = false;
        attackHoldTimer = 0;
    }
    
    @Override
    protected void idleBehavior() {
        idleTimer++;
        if (idleTimer >= MAX_IDLE_TIME) {
            behaviour = ENEMY_BEHAVIOUR.PATROL;
            idleTimer = 0;
        }
        fall();
    }
    
    @Override
    protected void patrol() {
        fall();
        
        boolean shouldTurn = isAtEdge() || isWallAhead() || !isGroundAhead();
        if (shouldTurn) {
            setDirection(-direction);
            isMoving = false;
        } else {
            setWorldPosition(worldX + (direction * patrolSpeed), worldY);
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
        
        ScrollingActor scrollTarget = (ScrollingActor) target;
        int targetX = scrollTarget.getWorldX();
        
        if (Math.abs(targetX - worldX) >= 5) {
            int desiredDirection = (targetX > worldX) ? 1 : -1;
            setDirection(desiredDirection);
            
            if (!isWallAhead() && isGroundAhead() && !isAtEdge()) {
                int speed = (desiredDirection == direction) ? chaseSpeed : patrolSpeed;
                setWorldPosition(worldX + (desiredDirection * speed), worldY);
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
    }
}