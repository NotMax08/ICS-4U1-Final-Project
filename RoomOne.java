import greenfoot.*;
/**
 * @author Paul assisted by Claude
 * 
 */
public class RoomOne extends GameWorld {
    
    public RoomOne(Player existingPlayer) {
        super(); 
        
        fullBackground = new GreenfootImage("gamestartworld.jpg");
        fullBackground.scale(WORLD_WIDTH, WORLD_HEIGHT);
        
        initializeMapGrid();
        
        if (existingPlayer != null) {
            transferPlayer(existingPlayer, 100, 1200);
        } else {
            player = new Player(camera);
            addObject(player, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
            player.setWorldPosition(100, 1200);
        }
        
        createPlatformVisuals();
        Crawler enemy = new Crawler(camera);
        addObject(enemy, SCREEN_WIDTH/2 , SCREEN_HEIGHT/2);
        enemy.setWorldPosition(500, 1200);
        // Null check
        if (camera != null && player != null) {
            camera.centerOn(player.getWorldX(), player.getWorldY());
            updateAllActors();
            updateBackground();
        }
    }
    
    public RoomOne() {
        this(null);
    }
    
    protected void initializeMapGrid() {
        // Platforms in world coordinates
        int[][] platformData = {
            {200, 400, 300, 20},
            {600, 300, 200, 20},
            {1000, 500, 400, 20},
            {1500, 200, 300, 20},
            {700, 1100, 300, 20},
            {900, 850, 300, 20},
            {800, 700, 300, 20},
            {2000, 1000, 300, 20},
            {625, 1300, 1250, 40}  
        };
        
        // Convert platforms to tile coordinates
        int totalPlatformTiles = 0;
        
        // First pass: count tiles
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
        int[] platformX = new int[totalPlatformTiles];
        int[] platformY = new int[totalPlatformTiles];
        
        // Second pass: fill arrays
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
                    platformX[index] = tileX;
                    platformY[index] = tileY;
                    index++;
                }
            }
        }
        
        // Create border walls (leaving space for doors on top and right)
        int wallTileCount = (TILES_WIDE * 2) + (TILES_HIGH * 2);
        int[] wallsX = new int[wallTileCount/2];
        int[] wallsY = new int[wallTileCount/2];
        
        index = 0;
        // Bottom walls only (no top wall for door)
        for (int x = 0; x < TILES_WIDE; x++) {
            wallsX[index] = x;
            wallsY[index] = TILES_HIGH - 1;
            index++;
        }
        
        // Left wall only (no right wall for door)
        for (int y = 0; y < TILES_HIGH; y++) {
            wallsX[index] = 0;
            wallsY[index] = y;
            index++;
        }
        
        int[] doorX = new int[60];
        int[] doorY = new int[60];
        index = 60;
        for(int i = 0; i < doorX.length; i++){
            doorX[i] = index;
            doorY[i] = 1;
            index++;
        }
        int[] breakableX = new int[0];
        int[] breakableY = new int[0];
        
        mapGrid = new MapGrid(
            TILE_SIZE,
            TILE_SIZE,
            WORLD_WIDTH,
            WORLD_HEIGHT,
            true,
            true,
            true,
            false,
            wallsX,
            platformX,
            doorX,
            breakableX,
            wallsY,
            platformY,
            doorY,
            breakableY
        );
    }
    
    private void createPlatformVisuals() {
        int[][] platformRegions = {
            {200, 400, 300, 20},
            {600, 300, 200, 20},
            {1000, 500, 400, 20},
            {1500, 200, 300, 20},
            {700, 1100, 300, 20},
            {900, 850, 300, 20},
            {800, 700, 300, 20},
            {2000, 1000, 300, 20},
            {625, 1300, 1250, 40}   
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
}