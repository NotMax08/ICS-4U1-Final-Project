import greenfoot.*;
/**
 * @author Paul & Robin
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

    // Counting variables
    private int fallCount = 0;
    private int moveCount = 0;
    private int movingIndex = 0;

    // Set Inventory
    private InventoryDisplay inventory;
    
    // Animation Frames
    private GreenfootImage standingRight = new GreenfootImage("standingRight.png");
    private GreenfootImage standingLeft;
    private GreenfootImage runningR1;
    private GreenfootImage runningR2;
    private GreenfootImage runningR3;
    private GreenfootImage runningR4;
    private GreenfootImage runningL1;
    

    public Player(Camera camera) {
        super(camera);
        /*
        GreenfootImage img = new GreenfootImage(30, 40);
        img.setColor(Color.BLUE);
        img.fillRect(0, 0, 30, 40);
        setImage(img);
         */
        setInventory(inventory);
        
        standingRight.scale(50,100);
        
        setImage(standingRight);
    }
    public void act() {
        handleInput();
        applyGravity();
        checkFall();
        checkDoor();
        moveHorizontal();
        moveVertical();
    }

    private void handleInput() {
        // Horizontal movement
        if (Greenfoot.isKeyDown("left") || Greenfoot.isKeyDown("a")) {
            direction = false;
            velocityX = -MOVE_SPEED;
            movingAnimation();
        } else if (Greenfoot.isKeyDown("right") || Greenfoot.isKeyDown("d")) {
            direction = true;
            velocityX = MOVE_SPEED;
            movingAnimation();
        } else {
            velocityX = 0;
            if(!direction){

            }
            // set standing image according to direction (true or false)
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

    private void applyGravity() {
        velocityY += GRAVITY;
        if (velocityY > MAX_FALL_SPEED) {
            velocityY = MAX_FALL_SPEED;
        }
    }

    private void checkFall(){
        if(fallCount < 100 && !onGround){
            fallCount++;
        }else{
            // stun when landed
            // use boolean
            fallCount = 0;
        }
    }
    
    private void movingAnimation(){
        if(movingIndex == 1){
            if(direction){
                
            }else{
                
            }
        }
        moveCount++;
        if(moveCount % 3 == 0){
            movingIndex++;
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