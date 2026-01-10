import greenfoot.*;
import java.util.*;
/**
 * Crawler - Ground enemy that patrols platforms and chases player
 * 
 * @author Max Yuan
 * @version 1.0
 */
public class Crawler extends Enemies
{
    //constants for movement and enemy
    public static final int CRAWLER_HEALTH = 50;
    public static final int CRAWLER_DAMAGE = 10;
    public static final int CRAWLER_DETECTION_RANGE = 300;
    public static final int CRAWLER_ATTACK_RANGE = 70;
    public static final int PATROL_SPEED = 1;
    public static final int CHASE_SPEED = 3;
    public static final int MAX_IDLE_TIME = 120; 
    public static final int MAX_PATROL_TIME = MAX_IDLE_TIME;
    public static final int GRAVITY = 1;
    public static final int KNOCKBACK_DISTANCE = 10;
    public static final int KNOCKBACK_HEIGHT = 5;
    public static final int WALL_CHECK_DISTANCE = 20;
    
    private int idleTimer;
    private int patrolTimer;

    //image stuff
    public static final int IMAGE_WIDTH = 192;
    public static final int IMAGE_HEIGHT = 192;
    public static final int FADE_SPEED = 15;
    public static final int ANIMATION_SPEED = 10;
    public static final int ATTACK_ANIMATION_SPEED = 12;
    public static final int ATTACK_COOLDOWN = 120;
    
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
    
    public Crawler(Camera camera) {
        super(camera, 
              getUniformImage("Crawler.png"),
              CRAWLER_HEALTH,
              CRAWLER_DAMAGE,
              CRAWLER_DETECTION_RANGE,
              CRAWLER_ATTACK_RANGE
        );
        
        this.idleTimer = 0;
        this.patrolTimer = 0;
        
        normalIdleImage = getUniformImage("Crawler.png");
        alertIdleImage = getUniformImage("AlertCrawler.png");
        
        normalWalkingImages.add(getUniformImage("CrawlerWalking.png"));
        normalWalkingImages.add(getUniformImage("CrawlerWalking2.png"));
        
        alertWalkingImages.add(getUniformImage("AlertCrawlerWalking.png"));
        alertWalkingImages.add(getUniformImage("AlertCrawlerWalking2.png"));
        
        attackImages.add(getUniformImage("CrawlerAttack1.png"));
        attackImages.add(getUniformImage("CrawlerAttack2.png"));
        attackImages.add(getUniformImage("CrawlerAttack3.png"));
        attackImages.add(getUniformImage("CrawlerAttack4.png"));
        
        behaviour = ENEMY_BEHAVIOUR.IDLE;
    }
    
    private static GreenfootImage getUniformImage(String filename) {
        GreenfootImage img = new GreenfootImage(filename);
        img.scale(IMAGE_WIDTH, IMAGE_HEIGHT);
        return img;
    }
    
    @Override
    public void act() {
        super.act();
        updateAnimation();
        updateImageTransition();
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
        
        // Only animate when moving (patrol or chase)
        if (behaviour == ENEMY_BEHAVIOUR.PATROL || behaviour == ENEMY_BEHAVIOUR.CHASE) {
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
        } else {
            GreenfootImage normalBase;
            GreenfootImage alertBase;
            
            if (behaviour == ENEMY_BEHAVIOUR.PATROL || behaviour == ENEMY_BEHAVIOUR.CHASE) {
                normalBase = normalWalkingImages.get(currentFrame);
                alertBase = alertWalkingImages.get(currentFrame);
            } else {
                normalBase = normalIdleImage;
                alertBase = alertIdleImage;
            }
            
            if (currentAlpha > 0) {
                GreenfootImage blended = new GreenfootImage(normalBase);
                GreenfootImage overlay = new GreenfootImage(alertBase);
                overlay.setTransparency(currentAlpha);
                blended.drawImage(overlay, 0, 0);
                finalImage = blended;
            } else {
                finalImage = new GreenfootImage(normalBase);
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
        setWorldPosition(worldX + (direction * PATROL_SPEED), worldY);
        
        if (isAtEdge() || isBlocked(WALL_CHECK_DISTANCE)) {
            direction *= -1;
            flipImage();
        }
        
        patrolTimer++;
        if (patrolTimer >= MAX_PATROL_TIME) {
            behaviour = ENEMY_BEHAVIOUR.IDLE;
            patrolTimer = 0;
        }
        
        fall(GRAVITY);
    }
    
    @Override
    protected void chase() {
        if (target == null) return;
        
        int targetX = ((ScrollingActor)target).getWorldX();
        
        if (!(Math.abs(targetX - worldX) < 5)){
            if (targetX > worldX) {
                direction = 1;
                setWorldPosition(worldX + CHASE_SPEED, worldY);
                if (!isFacingRight) {
                    flipImage();
                }
            } else if (targetX < worldX) {
                direction = -1;
                setWorldPosition(worldX - CHASE_SPEED, worldY);
                if (isFacingRight) {
                    flipImage();
                }
            } 
        }
        
        if (isBlocked(WALL_CHECK_DISTANCE)) {
            direction *= -1;
            flipImage();
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
}