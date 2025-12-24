import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

// ===== BASE SCROLLING ACTOR CLASS =====
abstract class ScrollingActor extends Actor {
    protected Camera camera;
    protected int worldX, worldY; // Position in world coordinates
    
    public ScrollingActor(Camera camera) {
        this.camera = camera;
    }
    
    public void setWorldPosition(int x, int y) {
        this.worldX = x;
        this.worldY = y;
        updateScreenPosition();
    }
    
    public void moveWorld(int dx, int dy) {
        worldX += dx;
        worldY += dy;
    }
    
    public void updateScreenPosition() {
        int screenX = camera.worldToScreenX(worldX);
        int screenY = camera.worldToScreenY(worldY);
        setLocation(screenX, screenY);
    }
    
    public int getWorldX() { return worldX; }
    public int getWorldY() { return worldY; }
}

