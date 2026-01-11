import greenfoot.*;

public abstract class GameWorld extends World {
    protected Camera camera;
    protected Player player;
    public MapGrid mapGrid;
    protected InventoryDisplay inventory;
    
    protected static final int WORLD_WIDTH = 2500;
    protected static final int WORLD_HEIGHT = 1420;
    protected static final int SCREEN_WIDTH = 800;
    protected static final int SCREEN_HEIGHT = 600;
    protected static final int TILE_SIZE = 20;
    protected static final int TILES_WIDE = WORLD_WIDTH / TILE_SIZE;
    protected static final int TILES_HIGH = WORLD_HEIGHT / TILE_SIZE;
    
    protected GreenfootImage fullBackground;
    
    public GameWorld() {
        super(SCREEN_WIDTH, SCREEN_HEIGHT, 1, false);
        camera = new Camera(SCREEN_WIDTH, SCREEN_HEIGHT, WORLD_WIDTH, WORLD_HEIGHT);
        
        // Creates inventory 
        inventory = new InventoryDisplay(60, SCREEN_HEIGHT - 60, camera);
        addObject(inventory, 0, 0);
        
        this.setPaintOrder(InventoryDisplay.class);
    }
    
    public void act() {
        if (camera != null && player != null) {
            camera.centerOn(player.getWorldX(), player.getWorldY());
            updateAllActors();
            updateBackground();
        }
    }
    
    protected void updateBackground() {
        if (camera == null || fullBackground == null) return;
        
        GreenfootImage bg = getBackground();
        bg.clear();
        GreenfootImage visiblePortion = new GreenfootImage(SCREEN_WIDTH, SCREEN_HEIGHT);
        visiblePortion.drawImage(fullBackground, -camera.getX(), -camera.getY());
        bg.drawImage(visiblePortion, 0, 0);
    }
    
    protected void updateAllActors() {
        for (Object obj : getObjects(ScrollingActor.class)) {
            ScrollingActor actor = (ScrollingActor) obj;
            actor.updateScreenPosition();
        }
    }
    
    protected void transferPlayer(Player existingPlayer, int startX, int startY) {
        this.player = existingPlayer;
        player.setCamera(this.camera);
        addObject(player, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        player.setWorldPosition(startX, startY);
    }
    
    public Camera getCamera() { return camera; }
    public MapGrid getMapGrid() { return mapGrid; }
    public InventoryDisplay getInventory() { return inventory;}
    
    public int worldToTileX(int worldX) { return worldX / TILE_SIZE; }
    public int worldToTileY(int worldY) { return worldY / TILE_SIZE; }
    public int tileToWorldX(int tileX) { return tileX * TILE_SIZE; }
    public int tileToWorldY(int tileY) { return tileY * TILE_SIZE; }
    
    protected abstract void initializeMapGrid();
}
