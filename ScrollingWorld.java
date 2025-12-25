import greenfoot.*;
/**
 * @author Paul assisted by Claude
 * 
 */
// ===== MAIN WORLD CLASS =====
public class ScrollingWorld extends World {
    private Camera camera;
    private Player player;
    
    // World dimensions (much larger than visible screen)
    private static final int WORLD_WIDTH = 2000;
    private static final int WORLD_HEIGHT = 1200;
    
    // Visible screen dimensions
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    
    public ScrollingWorld() {
        super(SCREEN_WIDTH, SCREEN_HEIGHT, 1, false);
        
        // Create camera
        camera = new Camera(SCREEN_WIDTH, SCREEN_HEIGHT, WORLD_WIDTH, WORLD_HEIGHT);
        
        // Create player at world coordinates
        player = new Player(camera);
        addObject(player, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        player.setWorldPosition(100, 100); // Starting position in world coordinates
        
        // Add some platforms at world coordinates
        addPlatformAtWorldPos(200, 400, 300, 20);
        addPlatformAtWorldPos(600, 300, 200, 20);
        addPlatformAtWorldPos(1000, 500, 400, 20);
        addPlatformAtWorldPos(1500, 200, 300, 20);
        
        // Add ground
        addPlatformAtWorldPos(1000, 700, 2000, 40);
        
        // Add some collectibles
        for (int i = 0; i < 10; i++) {
            Collectible coin = new Collectible(camera);
            addObject(coin, 0, 0);
            coin.setWorldPosition(300 + i * 150, 250);
        }
        
        camera.centerOn(player.getWorldX(), player.getWorldY());
        updateAllActors();
    }
    
    public void act() {
        // Camera follows player
        camera.centerOn(player.getWorldX(), player.getWorldY());
        
        // Update all actors' screen positions based on camera
        updateAllActors();
    }
    
    private void updateAllActors() {
        for (Object obj : getObjects(ScrollingActor.class)) {
            ScrollingActor actor = (ScrollingActor) obj;
            actor.updateScreenPosition();
        }
    }
    
    private void addPlatformAtWorldPos(int worldX, int worldY, int width, int height) {
        Platform platform = new Platform(camera, width, height);
        addObject(platform, 0, 0);
        platform.setWorldPosition(worldX, worldY);
    }
    
    public Camera getCamera() {
        return camera;
    }
}
