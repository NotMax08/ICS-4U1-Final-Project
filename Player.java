import greenfoot.*;
/**
 * @author Paul & Robin with help from Claude
 */
public class Player extends ScrollingActor {
    // Movement constants
    private double velocityX = 0;
    private double velocityY = 0;
    private static final double GRAVITY = 1;
    private static final double JUMP_STRENGTH = -25;
    private static final double MOVE_SPEED = 5;
    private static final double MAX_FALL_SPEED = 100;

    // State
    private boolean onGround = false;
    private boolean inDoor = false;
    private boolean direction = true; // false = left, true = right
    private boolean isStunned = false;

    // Counting variables
    private int fallCount = 0;
    private int stunTimer = 0;
    private int animationCounter = 0;
    private int currentFrame = 0;
    private static final int ANIMATION_SPEED = 5; 
    private static final int FALL_DAMAGE_THRESHOLD = 60; // Acts of falling before stun
    private static final int STUN_DURATION = 30; // Acts to remain stunned

    // Set Inventory
    private InventoryDisplay inventory;

    // Player image constants
    private static int P_WIDTH = 50;
    private static int P_HEIGHT = 100;

    // Load animation frames
    private GreenfootImage standingRight = new GreenfootImage("standingRight.png");
    private GreenfootImage standingLeft = new GreenfootImage("standingLeft.png");

    private GreenfootImage runningR1 = new GreenfootImage("running1.png");
    private GreenfootImage runningR2 = new GreenfootImage("running2.png");
    private GreenfootImage runningR3 = new GreenfootImage("running3.png");
    private GreenfootImage runningR4 = new GreenfootImage("running4.png");
    private GreenfootImage[] runningRight;

    private GreenfootImage runningL1;
    private GreenfootImage runningL2;
    private GreenfootImage runningL3;
    private GreenfootImage runningL4;
    private GreenfootImage[] runningLeft;

    private GreenfootImage jumpR1 = new GreenfootImage("jump1.png");
    private GreenfootImage jumpR2 = new GreenfootImage("jump2.png");
    private GreenfootImage[] jumpingRight;

    private GreenfootImage jumpL1;
    private GreenfootImage jumpL2;
    private GreenfootImage[] jumpingLeft;

    private GreenfootImage fallR1 = new GreenfootImage("fall1.png");
    private GreenfootImage fallR2 = new GreenfootImage("fall2.png");
    private GreenfootImage[] fallingRight;

    private GreenfootImage fallL1;
    private GreenfootImage fallL2;
    private GreenfootImage[] fallingLeft;

    public Player(Camera camera) {
        super(camera);
        setInventory(inventory); 

        scaleImages(); 
        createMirroredImages();
        setupAnimationArrays();

        setImage(standingRight);
    }

    public void act() {
        // If stunned, count down stun timer and don't allow movement
        if (isStunned) {
            stunTimer--;
            if (stunTimer <= 0) {
                isStunned = false;
            }
            applyGravity();
            moveVertical(); // Still affected by gravity while stunned
            return; // Skip normal input handling
        }

        handleInput();
        applyGravity();
        checkFall();
        checkDoor();
        moveHorizontal();
        moveVertical();
    }

    private void scaleImages(){
        standingRight.scale(P_WIDTH,P_HEIGHT);
        standingLeft.scale(P_WIDTH,P_HEIGHT);
        runningR1.scale(P_WIDTH + 30,P_HEIGHT - 20); // Wider for running pose
        runningR2.scale(P_WIDTH + 30,P_HEIGHT - 20);
        runningR3.scale(P_WIDTH + 30,P_HEIGHT - 20);
        runningR4.scale(P_WIDTH + 30,P_HEIGHT - 20);
        jumpR1.scale(P_WIDTH + 30,P_HEIGHT - 20);
        jumpR2.scale(P_WIDTH + 30,P_HEIGHT - 20);
        fallR1.scale(P_WIDTH + 30,P_HEIGHT - 20);
        fallR2.scale(P_WIDTH + 30,P_HEIGHT - 20);
    }

    private void createMirroredImages(){
        // Create mirrored versions for left-facing animation
        runningL1 = new GreenfootImage(runningR1);
        runningL1.mirrorHorizontally();

        runningL2 = new GreenfootImage(runningR2);
        runningL2.mirrorHorizontally();

        runningL3 = new GreenfootImage(runningR3);
        runningL3.mirrorHorizontally();

        runningL4 = new GreenfootImage(runningR4);
        runningL4.mirrorHorizontally();

        jumpL1 = new GreenfootImage(jumpR1);
        jumpL1.mirrorHorizontally();

        jumpL2 = new GreenfootImage(jumpR2);
        jumpL2.mirrorHorizontally();

        fallL1 = new GreenfootImage(fallR1);
        fallL1.mirrorHorizontally();

        fallL2 = new GreenfootImage(fallR2);
        fallL2.mirrorHorizontally();
    }

    private void setupAnimationArrays() {
        // Create animation arrays for easy cycling
        runningRight = new GreenfootImage[]{runningR1, runningR2, runningR3, runningR4};
        runningLeft = new GreenfootImage[]{runningL1, runningL2, runningL3, runningL4};
        jumpingRight = new GreenfootImage[]{jumpR1, jumpR2};
        jumpingLeft = new GreenfootImage[]{jumpL1, jumpL2};
        fallingRight = new GreenfootImage[]{fallR1, fallR2};
        fallingLeft = new GreenfootImage[]{fallL1, fallL2};
    }

    private void handleInput() {
        if(!onGround){
            if(velocityY < 0){
                animateJumping();
            }else{
                animateFalling();
            }

            // Moving in the air
            if (Greenfoot.isKeyDown("left") || Greenfoot.isKeyDown("a")) {
                direction = false;
                velocityX = -MOVE_SPEED * 0.8; // Slightly reduced air control
            } else if (Greenfoot.isKeyDown("right") || Greenfoot.isKeyDown("d")) {
                direction = true;
                velocityX = MOVE_SPEED * 0.8;
            } else {
                velocityX = 0;
            }
        }else{

            // Horizontal movement
            if (Greenfoot.isKeyDown("left") || Greenfoot.isKeyDown("a")) {
                direction = false;
                velocityX = -MOVE_SPEED;
                animateRunning();
            } else if (Greenfoot.isKeyDown("right") || Greenfoot.isKeyDown("d")) {
                direction = true;
                velocityX = MOVE_SPEED;
                animateRunning();
            } else {
                velocityX = 0;
                if(velocityY == 0){
                    if(!direction){
                        setImage(standingLeft);
                    }else{
                        setImage(standingRight);
                    }
                }
            }
        }

        // Jump
        if ((Greenfoot.isKeyDown("up") || Greenfoot.isKeyDown("w") || Greenfoot.isKeyDown("space")) && onGround) {
            velocityY = JUMP_STRENGTH;
            onGround = false;
        }

        // Fall faster
        if ((Greenfoot.isKeyDown("down") || Greenfoot.isKeyDown("s") && !onGround)){
            velocityY += GRAVITY;
        }
    }

    private void animateRunning(){
        animationCounter++;

        // Change frame based on animation speed
        if (animationCounter >= ANIMATION_SPEED) {
            animationCounter = 0;
            currentFrame++;

            // Loop back to first frame
            if (currentFrame >= 4) {
                currentFrame = 0;
            }
        }

        // Set the appropriate frame based on direction
        if (direction) {
            setImage(runningRight[currentFrame]);
        } else {
            setImage(runningLeft[currentFrame]);
        }
    }
    
    private void animateJumping(){
        animationCounter++;
        
        // Change frame based on animation speed
        if(animationCounter >= ANIMATION_SPEED * 2) {
            animationCounter = 0;
            currentFrame++;
            
            // Loop back to first frame
            if(currentFrame >= 2){
                currentFrame = 0;
            }
        }
        
        // Set frame based on direction
        if(direction){
            setImage(jumpingRight[currentFrame]);
        }else{
            setImage(jumpingLeft[currentFrame]);
        }
    }
    
    private void animateFalling(){
        animationCounter++;
        
        // Change frame based on animation speed
        if(animationCounter >= ANIMATION_SPEED * 2) {
            animationCounter = 0;
            currentFrame++;
            
            // Loop back to first frame
            if(currentFrame >= 2){
                currentFrame = 0;
            }
        }
        
        // Set frame based on direction
        if(direction){
            setImage(fallingRight[currentFrame]);
        }else{
            setImage(fallingLeft[currentFrame]);
        }
    }

    private void applyGravity() {
        velocityY += GRAVITY;
        if (velocityY > MAX_FALL_SPEED) {
            velocityY = MAX_FALL_SPEED;
        }
    }

    private void checkFall(){
        if (!onGround && velocityY > 0) {
            // Player is falling - increment fall counter
            fallCount++;
        } else if (fallCount > 0) {
            // Player just landed - check if fall was long enough to cause stun
            if (fallCount >= FALL_DAMAGE_THRESHOLD) {
                isStunned = true;
                stunTimer = STUN_DURATION;

                // Visual feedback - flash the player red
                GreenfootImage currentImage = getImage();
                currentImage.setTransparency(150); // Make slightly transparent
            }

            // Reset fall counter
            fallCount = 0;
        }
    }

    private void moveHorizontal() {
        // Try to move horizontally
        int targetX = (int)(worldX + velocityX);

        if (!isSolidAtPosition(targetX, worldY)) {
            // Path is clear, move freely
            worldX = targetX;
        } else {
            // Hit a wall, slide as close as possible
            int step = velocityX > 0 ? 1 : -1;
            while (!isSolidAtPosition(worldX + step, worldY)) {
                worldX += step;
            }
            velocityX = 0;
        }
    }

    private void moveVertical() {
        // Try to move vertically
        int targetY = (int)(worldY + velocityY);

        if (!isSolidAtPosition(worldX, targetY)) {
            // Path is clear, move freely
            worldY = targetY;
            onGround = false;
        } else {
            // Hit floor or ceiling, slide as close as possible
            int step = velocityY > 0 ? 1 : -1;
            while (!isSolidAtPosition(worldX, worldY + step)) {
                worldY += step;
            }

            // Check if we landed on ground
            if (velocityY > 0) {
                onGround = true;
            }
            velocityY = 0;
        }
    }

    private void checkDoor(){
        if (isDoorAtPosition(worldX, worldY)) {
            inDoor = true;

            Greenfoot.setWorld(new RoomTwo());
        } else {
            inDoor = false;
        }
    }
    // Check if the player's hitbox at this position would collide with solid tiles
    private boolean isSolidAtPosition(int posX, int posY) {
        int halfWidth = getImage().getWidth() / 2;
        int halfHeight = getImage().getHeight() / 2;

        // Check four corners of the hitbox (inset by 1 pixel for tighter collision)
        return checkTileAt(posX - halfWidth + 1, posY - halfHeight + 1) ||  // Top-left
        checkTileAt(posX + halfWidth - 1, posY - halfHeight + 1) ||  // Top-right
        checkTileAt(posX - halfWidth + 1, posY + halfHeight - 1) ||  // Bottom-left
        checkTileAt(posX + halfWidth - 1, posY + halfHeight - 1);    // Bottom-right
    }

    private boolean isDoorAtPosition(int posX, int posY) {
        int halfWidth = getImage().getWidth() / 2;
        int halfHeight = getImage().getHeight() / 2;

        // Check four corners and center
        return checkDoorAt(posX - halfWidth + 1, posY - halfHeight + 1) ||  // Top-left
        checkDoorAt(posX + halfWidth - 1, posY - halfHeight + 1) ||  // Top-right
        checkDoorAt(posX - halfWidth + 1, posY + halfHeight - 1) ||  // Bottom-left
        checkDoorAt(posX + halfWidth - 1, posY + halfHeight - 1) ||  // Bottom-right
        checkDoorAt(posX, posY);                                      // Center
    }
    // Check if a specific world coordinate contains a solid tile
    private boolean checkTileAt(int worldX, int worldY) {
        GameWorld world = (GameWorld) getWorld();
        if (world == null) return false;

        int tileX = world.worldToTileX(worldX);
        int tileY = world.worldToTileY(worldY);

        int tileType = world.mapGrid.getTileAt(tileX, tileY);

        // Type 1 = walls, Type 2 = platforms, Type 3 = doors
        return tileType == 1 || tileType == 2 ;
    }

    private boolean checkDoorAt(int worldX, int worldY){
        GameWorld world = (GameWorld) getWorld();
        if (world == null) return false;

        int tileX = world.worldToTileX(worldX);
        int tileY = world.worldToTileY(worldY);

        int tileType = world.mapGrid.getTileAt(tileX, tileY);

        return tileType == 3 ;
    }

    private void enterDoor() {
        GameWorld world = (GameWorld) getWorld();
        if (world == null) return;

        if (world instanceof RoomOne) {
            Greenfoot.setWorld(new RoomTwo(this)); // Pass player to new world
        } else if (world instanceof RoomTwo) {
            Greenfoot.setWorld(new RoomOne(this)); // Pass player back
        }
    }

    public void setInventory(InventoryDisplay inventory){
        this.inventory = inventory;
    }
    // Getters
    public boolean isOnGround() {
        return onGround;
    }

    public boolean isInDoor(){
        return inDoor;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }
}