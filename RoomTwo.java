import greenfoot.*;
/**
 * @author Paul assisted by Claude
 * 
 */
// ===== Second Room =====
public class RoomTwo extends World {
    private Camera camera;
    private Player player;
    public MapGrid mapGrid;
    // World dimensions (much larger than visible screen)
    private static final int WORLD_WIDTH = 2500;
    private static final int WORLD_HEIGHT = 1420;
    
    // Visible screen dimensions
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private GreenfootImage fullBackground;
    int[] platformX;
    int[] platformY;
    
    
    private static final int TILE_SIZE = 20; // Size of each tile in world coords
    private static final int TILES_WIDE = WORLD_WIDTH / TILE_SIZE;  // 125 tiles
    private static final int TILES_HIGH = WORLD_HEIGHT / TILE_SIZE; // 71 tiles
    public RoomTwo() {
        super(SCREEN_WIDTH, SCREEN_HEIGHT, 1, false);
        
        //fullBackground = new GreenfootImage("gamestartworld.jpg");
        //fullBackground.scale(WORLD_WIDTH, WORLD_HEIGHT);
        // Create camera
        camera = new Camera(SCREEN_WIDTH, SCREEN_HEIGHT, WORLD_WIDTH, WORLD_HEIGHT);
        
        initializeMapGrid();
        
        // Create player at world coordinates
        player = new Player(camera);
        addObject(player, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        player.setWorldPosition(100, 1200); // Starting position in world coordinates
        
        
        camera.centerOn(player.getWorldX(), player.getWorldY());
        updateAllActors();
        updateBackground();
    }
    
    public void act() {
        // Camera follows player
        camera.centerOn(player.getWorldX(), player.getWorldY());
        
        // Update all actors' screen positions based on camera
        updateAllActors();
        updateBackground();
    }
    private void updateBackground() {
        GreenfootImage bg = getBackground();
        bg.clear(); // Clear the previous frame
        
        // Create a copy of the portion we want to display
        GreenfootImage visiblePortion = new GreenfootImage(SCREEN_WIDTH, SCREEN_HEIGHT);
        
        // Copy the visible region from the full background
        visiblePortion.drawImage(fullBackground, -camera.getX(), -camera.getY());
        
        // Draw it to the world background
        bg.drawImage(visiblePortion, 0, 0);
    }
    private void updateAllActors() {
        for (Object obj : getObjects(ScrollingActor.class)) {
            ScrollingActor actor = (ScrollingActor) obj;
            actor.updateScreenPosition();
        }
    }
    private void initializeMapGrid() {
        // Platforms in world coordinates
        // Each platform: {x, y, width, height}
        
        int[][] platformData = {
            /**
            {200, 400, 300, 20},
            {600, 300, 200, 20},
            {1000, 500, 400, 20},
            {1500, 200, 300, 20},
            {700, 1100, 300, 20},
            {900, 850, 300, 20},
            {700, 600, 300, 20},
            {2000, 1000, 300, 20},
            {625, 1300, 1250, 40}
            */
        };
        
        // Convert platforms to tile coordinates and create arrays
        int totalPlatformTiles = 0;
        
        // Count how many tiles we need
        for (int[] platform : platformData) {
            int worldX = platform[0];
            int worldY = platform[1];
            int width = platform[2];
            int height = platform[3];
            
            int startTileX = worldToTileX(worldX - width/2);
            int endTileX = worldToTileX(worldX + width/2);
            int startTileY = worldToTileY(worldY - height/2);
            int endTileY = worldToTileY(worldY + height/2);
            
            int tilesWide = endTileX - startTileX + 1;
            int tilesHigh = endTileY - startTileY + 1;
            totalPlatformTiles += tilesWide * tilesHigh;
        }
        
        // Create arrays for platform tiles
        platformX = new int[totalPlatformTiles];
        platformY = new int[totalPlatformTiles];
        
        // Second pass: fill the arrays
        int index = 0;
        for (int[] platform : platformData) {
            int worldX = platform[0];
            int worldY = platform[1];
            int width = platform[2];
            int height = platform[3];
            
            int startTileX = worldToTileX(worldX - width/2);
            int endTileX = worldToTileX(worldX + width/2);
            int startTileY = worldToTileY(worldY - height/2);
            int endTileY = worldToTileY(worldY + height/2);
            
            // Fill all tiles for this platform
            for (int tileX = startTileX; tileX <= endTileX; tileX++) {
                for (int tileY = startTileY; tileY <= endTileY; tileY++) {
                    platformX[index] = tileX;
                    platformY[index] = tileY;
                    index++;
                }
            }
        }
        
        // Create border walls
        int wallTileCount = (TILES_WIDE * 2) + (TILES_HIGH * 2); // Top, bottom, left, right edges
        int[] wallsX = new int[wallTileCount];
        int[] wallsY = new int[wallTileCount]; 
        
        index = 0;
        // Top and bottom walls
        for (int x = 0; x < TILES_WIDE; x++) {
            
            wallsX[index] = x;
            wallsY[index] = 0; // Top
            index++;
            
            
            wallsX[index] = x;
            wallsY[index] = TILES_HIGH - 1; // Bottom
            index++;
        }
        // Left and right walls
        for (int y = 0; y < TILES_HIGH; y++) {
            wallsX[index] = 0;
            wallsY[index] = y; // Left
            index++;
            
            wallsX[index] = TILES_WIDE - 1;
            wallsY[index] = y; // Right
            index++;
            
        }
        
        // Empty arrays for doors and breakables
        int[] doorX = new int[0];
        int[] doorY = new int[0];
        int[] breakableX = new int[0];
        int[] breakableY = new int[0];
        
        
        mapGrid = new MapGrid(
            TILE_SIZE,           // tileWidth
            TILE_SIZE,           // tileHeight
            WORLD_WIDTH,         // worldWidth
            WORLD_HEIGHT,        // worldHeight
            true,                // hasWalls
            true,                // hasPlatforms
            false,               // hasDoors
            false,               // hasBreakables
            wallsX,              // wallsDataX
            platformX,           // platformDataX
            doorX,               // doorDataX
            breakableX,          // breakableDataX
            wallsY,              // wallsDataY
            platformY,           // platformDataY
            doorY,               // doorDataY
            breakableY           // breakableDataY
        );
        //createPlatformVisuals();
    }
    
    public MapGrid getMapGrid() {
        return mapGrid;
    }
    private void createPlatformVisuals() {
        // Define original platform regions (same as before)
        int[][] platformRegions = {
            {200, 400, 300, 20},
            {600, 300, 200, 20},
            {1000, 500, 400, 20},
            {1500, 200, 300, 20},
            {700, 1100, 300, 20},
            {900, 850, 300, 20},
            {700, 600, 300, 20},
            {2000, 1000, 300, 20},
            {625, 1300, 1250, 40}  
        };
        
        // Create one visual platform per region
        for (int[] region : platformRegions) {
            int worldX = region[0];
            int worldY = region[1];
            int width = region[2];
            int height = region[3];
            
            Platform platform = new Platform(camera, width, height);
            addObject(platform, 0, 0);
            platform.setWorldPosition(worldX, worldY);
        }
    }
    
    
    public Camera getCamera() {
        return camera;
    }
        public int worldToTileX(int worldX) {
        return worldX / TILE_SIZE;
    }
    
    public int worldToTileY(int worldY) {
        return worldY / TILE_SIZE;
    }
    
    public int tileToWorldX(int tileX) {
        return tileX * TILE_SIZE;
    }
    
    public int tileToWorldY(int tileY) {
        return tileY * TILE_SIZE;
    }
}

