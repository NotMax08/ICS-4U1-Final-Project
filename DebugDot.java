import greenfoot.*;

public class DebugDot extends ScrollingActor {
    private Color color;
    private Golem enemy; // Reference to the enemy to follow
    private int offsetX; // X offset from enemy center
    private int offsetY; // Y offset from enemy center
    private int checkType; // 0=wall, 1=ground, 2=edge
    
    public DebugDot(Golem enemy, Color color, int offsetX, int offsetY, int checkType, Camera camera) {
        super(camera);
        this.enemy = enemy;
        this.color = color; 
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.checkType = checkType;
        
        GreenfootImage img = new GreenfootImage(15, 15);
        img.setColor(color);
        img.fillOval(0, 0, 15, 15);
        
        // Add label based on check type
        img.setColor(Color.WHITE);
        img.setFont(new Font("Arial", 10));
        switch(checkType) {
            case 0: img.drawString("W", 3, 12); break; // Wall
            case 1: img.drawString("G", 3, 12); break; // Ground
            case 2: img.drawString("E", 3, 12); break; // Edge
        }
        
        setImage(img);
    }
    
    public void act() {
        if (enemy.getWorld() == null) {
            // Enemy was removed, remove this dot too
            getWorld().removeObject(this);
            return;
        }
        
        // Update position based on enemy's current world position and direction
        int enemyWorldX = enemy.getWorldX();
        int enemyWorldY = enemy.getWorldY();
        int enemyDirection = enemy.getDirection(); // You'll need to add getDirection() to Golem
        
        // Calculate actual offset based on enemy's facing direction
        int actualOffsetX = offsetX * enemyDirection;
        
        // Update world position
        setWorldPosition(enemyWorldX + actualOffsetX, enemyWorldY + offsetY);
        
        // Optional: Update color based on check result
        updateColorBasedOnCheck();
    }
    
    private void updateColorBasedOnCheck() {
        
        boolean checkResult = false;
        
        switch(checkType) {
            case 0: // Wall check
                checkResult = enemy.isWallAhead();
                break;
            case 1: // Ground check
                checkResult = enemy.isGroundAhead();
                break;
            case 2: // Edge check
                checkResult = enemy.isAtEdge();
                break;
        }
        
        // Change dot color based on result (green=true, red=false)
        GreenfootImage img = getImage();
        img.clear();
        img.setColor(checkResult ? Color.GREEN : Color.RED);
        img.fillOval(0, 0, 15, 15);
        img.setColor(Color.WHITE);
        img.setFont(new Font("Arial", 10));
        switch(checkType) {
            case 0: img.drawString("W", 3, 12); break;
            case 1: img.drawString("G", 3, 12); break;
            case 2: img.drawString("E", 3, 12); break;
        }
        
    }
    
    // Getter for the check type (optional)
    public int getCheckType() {
        return checkType;
    }
}