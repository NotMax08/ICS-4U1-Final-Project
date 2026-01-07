import greenfoot.*;
/**
 * @author Claude
 */
public abstract class ScrollingActor extends Actor {
    protected int worldX, worldY;
    protected Camera camera;
    
    public ScrollingActor(Camera camera) {
        this.camera = camera;
        this.worldX = 0;
        this.worldY = 0;
    }
    
    public void setWorldPosition(int x, int y) {
        this.worldX = x;
        this.worldY = y;
        updateScreenPosition();
    }
    
    public void updateScreenPosition() {
        if (camera != null) {
            int screenX = camera.worldToScreenX(worldX);
            int screenY = camera.worldToScreenY(worldY);
            setLocation(screenX, screenY);
        }
    }
    
    public int getWorldX() { return worldX; }
    public int getWorldY() { return worldY; }
    public Camera getCamera() { return camera; }
    
    public void setCamera(Camera newCamera) {
        this.camera = newCamera;
    }
}