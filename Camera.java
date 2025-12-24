import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

// ===== CAMERA CLASS =====
class Camera {
    private int x, y; // Camera position in world coordinates
    private int screenWidth, screenHeight;
    private int worldWidth, worldHeight;
    
    public Camera(int screenW, int screenH, int worldW, int worldH) {
        this.screenWidth = screenW;
        this.screenHeight = screenH;
        this.worldWidth = worldW;
        this.worldHeight = worldH;
        this.x = 0;
        this.y = 0;
    }
    
    public void centerOn(int worldX, int worldY) {
        // Center camera on target position
        x = worldX - screenWidth / 2;
        y = worldY - screenHeight / 2;
        
        // Clamp to world bounds
        x = Math.max(0, Math.min(x, worldWidth - screenWidth));
        y = Math.max(0, Math.min(y, worldHeight - screenHeight));
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    
    // Convert world coordinates to screen coordinates
    public int worldToScreenX(int worldX) {
        return worldX - x;
    }
    
    public int worldToScreenY(int worldY) {
        return worldY - y;
    }
    
    // Check if world position is visible on screen
    public boolean isVisible(int worldX, int worldY, int width, int height) {
        int screenX = worldToScreenX(worldX);
        int screenY = worldToScreenY(worldY);
        return screenX + width > 0 && screenX < screenWidth &&
               screenY + height > 0 && screenY < screenHeight;
    }
}


