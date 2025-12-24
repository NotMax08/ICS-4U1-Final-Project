import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

// ===== PLAYER CLASS =====
class Player extends ScrollingActor {
    private int velocityY = 0;
    private boolean onGround = false;
    private static final int GRAVITY = 1;
    private static final int JUMP_STRENGTH = -25;
    private static final int MOVE_SPEED = 5;
    
    public Player(Camera camera) {
        super(camera);
        GreenfootImage img = new GreenfootImage(30, 40);
        img.setColor(Color.BLUE);
        img.fillRect(0, 0, 30, 40);
        setImage(img);
    }
    
    public void act() {
        handleInput();
        applyGravity();
        checkCollisions();
        updateScreenPosition();
    }
    
    private void handleInput() {
        // Horizontal movement
        if (Greenfoot.isKeyDown("left") || Greenfoot.isKeyDown("a")) {
            moveWorld(-MOVE_SPEED, 0);
        }
        if (Greenfoot.isKeyDown("right") || Greenfoot.isKeyDown("d")) {
            moveWorld(MOVE_SPEED, 0);
        }
        
        // Jumping
        if (Greenfoot.isKeyDown("space") && onGround) {
            velocityY = JUMP_STRENGTH;
            onGround = false;
        }
    }
    
    private void applyGravity() {
        velocityY += GRAVITY;
        moveWorld(0, velocityY);
    }
    
    private void checkCollisions() {
        onGround = false;
        
        // Check collision with platforms
        for (Object obj : getWorld().getObjects(Platform.class)) {
            Platform platform = (Platform) obj;
            
            if (isCollidingWith(platform)) {
                // Landing on top
                if (velocityY > 0 && worldY < platform.getWorldY()) {
                    worldY = platform.getWorldY() - platform.getHeight() / 2 - 20;
                    velocityY = 0;
                    onGround = true;
                }
                // Hitting from below
                else if (velocityY < 0 && worldY > platform.getWorldY()) {
                    worldY = platform.getWorldY() + platform.getHeight() / 2 + 20;
                    velocityY = 0;
                }
            }
        }
    }
    
    private boolean isCollidingWith(Platform platform) {
        int px = worldX;
        int py = worldY;
        int pw = 30;
        int ph = 40;
        
        int bx = platform.getWorldX();
        int by = platform.getWorldY();
        int bw = platform.getWidth();
        int bh = platform.getHeight();
        
        return px - pw/2 < bx + bw/2 && px + pw/2 > bx - bw/2 &&
               py - ph/2 < by + bh/2 && py + ph/2 > by - bh/2;
    }
}
