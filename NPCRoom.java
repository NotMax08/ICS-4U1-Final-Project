import greenfoot.*;
/**
 * 
 * @author Paul assisted by Claude
 */
public class NPCRoom extends GameWorld {
    private static final int NPC_WORLD_WIDTH = 800;
    private static final int NPC_WORLD_HEIGHT = 600;
    
    private static final int NPC_TILES_WIDE = NPC_WORLD_WIDTH / TILE_SIZE;
    private static final int NPC_TILES_HIGH = NPC_WORLD_HEIGHT / TILE_SIZE;
    public NPCRoom(Player existingPlayer) {
        super(); // This creates the camera
        
        camera.centerOn(NPC_WORLD_WIDTH / 2, NPC_WORLD_HEIGHT / 2);
        
        fullBackground = new GreenfootImage("npcroom.jpg");
        fullBackground.scale(NPC_WORLD_WIDTH, NPC_WORLD_HEIGHT);
        
        initializeMapGrid();
        createPlatformVisuals();
        createInteractiveDoorVisuals();
        
        
        if (existingPlayer != null) {
            transferPlayer(existingPlayer, 400, 500);
        } else {
            player = new Player(camera);
            addObject(player, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
            player.setWorldPosition(400, 500);
        }
        
        
        initalizeDisplays();
        
        if (camera != null) {
            updateAllActors();
            updateBackground();
        }
        
        PotionMerchant potionMerchant = new PotionMerchant();
        addObject(potionMerchant, getWidth()/2, getHeight()/2 + 200);
    }
    
    public NPCRoom() {
        this(null);
    }
    
    // Override act() to keep camera static (don't follow player)
    public void act() {
        if (camera != null && player != null) {
            // Don't call super.act() which would follow the player
            // Just update positions without moving camera
            updateAllActors();
        }
    }
    
    protected void initializeMapGrid() {
        // Platforms in world coordinates (scaled for 800x600 room)
        int[][] platformData = {
            {410, 580, 830, 40}  // Floor platform - full width near bottom
        };
        
        // Convert platforms to tiles
        int totalPlatformTiles = 0;
        
        for (int[] platform : platformData) {
            int worldX = platform[0];
            int worldY = platform[1];
            int width = platform[2];
            int height = platform[3];
        
            int startTileX = worldToTileX(worldX - width/2);
            int endTileX = worldToTileX(worldX + width/2);
            int startTileY = worldToTileY(worldY - height/2);
            int endTileY = worldToTileY(worldY + height/2);
            
            int tilesWide = Math.max(1, endTileX - startTileX + 1);
            int tilesHigh = Math.max(1, endTileY - startTileY + 1);
            totalPlatformTiles += tilesWide * tilesHigh;
        }
        
        int[] platformX = new int[totalPlatformTiles];
        int[] platformY = new int[totalPlatformTiles];
        
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
            
            for (int tileX = startTileX; tileX <= endTileX; tileX++) {
                for (int tileY = startTileY; tileY <= endTileY; tileY++) {
                    if (index < platformX.length) {
                        platformX[index] = tileX;
                        platformY[index] = tileY;
                        index++;
                    }
                }
            }
        }
        
        // Walls - outer boundaries for 800x600 room (40 tiles wide x 30 tiles high)
        java.util.ArrayList<Integer> wallXList = new java.util.ArrayList<>();
        java.util.ArrayList<Integer> wallYList = new java.util.ArrayList<>();
        
        // Left wall (x=0, all y)
        for (int y = 0; y < NPC_TILES_HIGH; y++) {
            wallXList.add(0);
            wallYList.add(y);
        }
        
        // Right wall (x=39, all y)
        for (int y = 0; y < NPC_TILES_HIGH; y++) {
            wallXList.add(NPC_TILES_WIDE - 1);
            wallYList.add(y);
        }
        
        // Top wall (y=0, all x)
        for (int x = 1; x < NPC_TILES_WIDE - 1; x++) {
            wallXList.add(x);
            wallYList.add(0);
        }
        
        // Bottom wall (y=29, all x) - removed to prevent glitching
        // Add a floor platform instead if needed in platformData
        
        int[] wallsX = new int[wallXList.size()];
        int[] wallsY = new int[wallYList.size()];
        for (int i = 0; i < wallXList.size(); i++) {
            wallsX[i] = wallXList.get(i);
            wallsY[i] = wallYList.get(i);
        }
        
        // Doors at left side (scaled for smaller room)
        int[] doorX = new int[]{0};
        int[] doorY = new int[]{15}; // Mid-height door for 30-tile-high room
        
        int[] breakableX = new int[0];
        int[] breakableY = new int[0];
        
        //Interactive Doors (scaled for 800x600 room)
        int[][] interactiveData = {
            {50, 500, 80, 140},   // Left door (adjusted for smaller room)
        };
        
        // Convert doors to tiles
        int totalInteractiveTiles = 0;
        
        for (int[] door : interactiveData) {
            int worldX = door[0];
            int worldY = door[1];
            int width = door[2];
            int height = door[3];
            
            int startTileX = worldToTileX(worldX - width/2);
            int endTileX = worldToTileX(worldX + width/2);
            int startTileY = worldToTileY(worldY - height/2);
            int endTileY = worldToTileY(worldY + height/2);
            
            int tilesWide = Math.max(1, endTileX - startTileX + 1);
            int tilesHigh = Math.max(1, endTileY - startTileY + 1);
            totalInteractiveTiles += tilesWide * tilesHigh;
        }
        
        int[] interactiveX = new int[totalInteractiveTiles];
        int[] interactiveY = new int[totalInteractiveTiles];
        
        int doorIndex = 0;
        for (int[] door : interactiveData) {
            int worldX = door[0];
            int worldY = door[1];
            int width = door[2];
            int height = door[3];
            
            int startTileX = worldToTileX(worldX - width/2);
            int endTileX = worldToTileX(worldX + width/2);
            int startTileY = worldToTileY(worldY - height/2);
            int endTileY = worldToTileY(worldY + height/2);
            
            for (int tileX = startTileX; tileX <= endTileX; tileX++) {
                for (int tileY = startTileY; tileY <= endTileY; tileY++) {
                    if (doorIndex < interactiveX.length) {
                        interactiveX[doorIndex] = tileX;
                        interactiveY[doorIndex] = tileY;
                        doorIndex++;
                    }
                }
            }
        }
        
        // Use room dimensions for MapGrid
        mapGrid = new MapGrid(
            TILE_SIZE,
            TILE_SIZE,
            NPC_WORLD_WIDTH,  // Use 800 instead of WORLD_WIDTH
            NPC_WORLD_HEIGHT, // Use 600 instead of WORLD_HEIGHT
            true,
            true,
            true,
            false,
            true,
            wallsX,
            platformX,
            doorX,
            breakableX,
            wallsY,
            platformY,
            doorY,
            breakableY,
            interactiveX,
            interactiveY
        );
    }
    
    private void createPlatformVisuals() {
        int[][] platformRegions = {
            {410, 580, 830, 40}  // Floor platform visual
        };
            
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
    
    private void createInteractiveDoorVisuals() {
        int[][] doorRegions = {
            {50, 500, 80, 140},   // Left door (adjusted for smaller room)
        };
        
        for(int[] region : doorRegions) {
            int worldX = region[0];
            int worldY = region[1];
            int width = region[2];
            int height = region[3];
            
            InteractiveDoor door = new InteractiveDoor(camera, width, height, "bossroom");
            addObject(door, 0, 0);
            door.setWorldPosition(worldX, worldY);
        }
    }
}

