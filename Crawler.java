import greenfoot.*;
/**
 * Crawler - Ground enemy that patrols platforms and chases player
 * 
 * @author Max Yuan
 * @version 1.0
 */
public class Crawler extends Enemies
{
    public static final int CRAWLER_HEALTH = 50;
    public static final int CRAWLER_DAMAGE = 10;
    public static final int CRAWLER_DETECTION_RANGE = 200;
    public static final int CRAWLER_ATTACK_RANGE = 30;
    public static final int PATROL_SPEED = 2;
    public static final int CHASE_SPEED = 4;
    public static final int MAX_IDLE_TIME = 120; 
    public static final int GRAVITY = 5;
    public static final int KNOCKBACK_DISTANCE = 10;
    public static final int KNOCKBACK_HEIGHT = 5;
    public static final int WALL_CHECK_DISTANCE = 20;
    
    private int direction; // 1 = right, -1 = left
    private int idleTimer;
    
    public Crawler(Camera camera) {
        super(camera, 
              new GreenfootImage("crawler.png"),
              CRAWLER_HEALTH,
              CRAWLER_DAMAGE,
              CRAWLER_DETECTION_RANGE,
              CRAWLER_ATTACK_RANGE
        );
        
        this.direction = 1; 
        this.idleTimer = 0;
        
        behaviour = ENEMY_BEHAVIOUR.IDLE;
    }
    
    @Override
    protected void idleBehavior() {
        // Stand still, occasionally switch to patrol
        idleTimer++;
        
        if (idleTimer >= MAX_IDLE_TIME) {
            behaviour = ENEMY_BEHAVIOUR.PATROL;
            idleTimer = 0;
        }
    }
    
    @Override
    protected void patrol() {
        // Move back and forth along ground
        setLocation(getX() + (direction * PATROL_SPEED), getY());
        
        // Check for edges or walls
        if (isAtEdge() || isBlocked()) {
            direction *= -1; // Turn around
            flipImage();
        }
        
        // Apply gravity
        fall();
    }
    
    @Override
    protected void chase() {
        if (target == null) return;
        
        // Move toward player horizontally
        int targetX = target.getX();
        int myX = getX();
        
        if (targetX > myX) {
            direction = 1;
            setLocation(getX() + CHASE_SPEED, getY());
        } else if (targetX < myX) {
            direction = -1;
            setLocation(getX() - CHASE_SPEED, getY());
        }
        
        // Make sure we're facing the right direction
        if (direction == 1) {
            flipImage();
        } else if (direction == -1) {
            flipImage();
        }
        
        // Check for obstacles
        if (isBlocked()) {
            direction *= -1;
            flipImage();
        }
        
        // Apply gravity
        fall();
    }
    
    @Override
    protected void attack() {
        // Contact damage - hurt player if touching
        Player player = (Player) getOneIntersectingObject(Player.class);
        if (player != null) {
            //player.takeDamage(damage);
            System.out.println("bam");
        }
        
        // Keep chasing while in attack range
        chase();
    }
    
    // Helper methods
    private void flipImage() {
        getImage().mirrorHorizontally();
    }
    
    private boolean isBlocked() {
        // Check if there's a wall/obstacle in front
        Actor wall = getOneObjectAtOffset(direction * WALL_CHECK_DISTANCE, 0, Platform.class);
        return wall != null;
    }
    
    private void fall() {
        // Simple gravity - adjust based on your physics system
        if (!onGround()) {
            setLocation(getX(), getY() + GRAVITY);
        }
    }
    
    private boolean onGround() {
        // Check if standing on platform
        Actor ground = getOneObjectAtOffset(0, getImage().getHeight()/2 + 1, Platform.class);
        return ground != null;
    }
    
    @Override
    protected void takeDamage(int dmg) {
        health -= dmg;
        behaviour = ENEMY_BEHAVIOUR.HURT;
        
        // Optional: knockback effect
        setLocation(getX() + (direction * -KNOCKBACK_DISTANCE), getY() - KNOCKBACK_HEIGHT);
        
        if (health <= 0) {
            behaviour = ENEMY_BEHAVIOUR.DEAD;
        }
    }
}