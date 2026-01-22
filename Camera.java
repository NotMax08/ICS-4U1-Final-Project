import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * Used to display a portion of a larger map and updates when player moves
 * 
 * @author Claude prompted by Paul
 */
class Camera {
    private int x, y; // Camera position in world coordinates
    private int screenWidth, screenHeight;
    private int worldWidth, worldHeight;
    /**
     * Camera constructor for scrolling actors to use
     */
    public Camera(int screenW, int screenH, int worldW, int worldH) {
        this.screenWidth = screenW;
        this.screenHeight = screenH;
        this.worldWidth = worldW;
        this.worldHeight = worldH;
        this.x = 0;
        this.y = 0;
    }
    /**
     * centers on player
     * 
     * @param worldX world coordinate x 
     * @param worldY world coordinate y
     */
    public void centerOn(int worldX, int worldY) {
        // Center camera on target position
        x = worldX - screenWidth / 2;
        y = worldY - screenHeight / 2;
        
        // Clamp to world bounds
        x = Math.max(0, Math.min(x, worldWidth - screenWidth));
        y = Math.max(0, Math.min(y, worldHeight - screenHeight));
    }
    /**
     * getters for coordinates
     */
    public int getX() { return x; }
    public int getY() { return y; }
    /**
     * converts world to screen x coord
     * 
     * @param worldX world x coord
     */
    public int worldToScreenX(int worldX) {
        return worldX - x;
    }
    /**
     * converts world to screen coords for y
     * @param worldY world y coord
     */
    public int worldToScreenY(int worldY) {
        return worldY - y;
    }
    /**
     * checks if world position is visible in camera
     * @param worldX world x coord
     * @param worldY world y coord
     * @param width width of camera screen
     * @param height height of camera screen
     */
    public boolean isVisible(int worldX, int worldY, int width, int height) {
        int screenX = worldToScreenX(worldX);
        int screenY = worldToScreenY(worldY);
        return screenX + width > 0 && screenX < screenWidth &&
               screenY + height > 0 && screenY < screenHeight;
    }
}


