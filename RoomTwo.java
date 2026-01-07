import greenfoot.*;

public class RoomTwo extends GameWorld {
    
    public RoomTwo(Player existingPlayer) {
        super(); // This creates the camera
        
        fullBackground = new GreenfootImage("roomtwo.jpg");
        fullBackground.scale(WORLD_WIDTH, WORLD_HEIGHT);
        
        initializeMapGrid();
        
        if (existingPlayer != null) {
            transferPlayer(existingPlayer, 100, 1200);
        } else {
            player = new Player(camera);
            addObject(player, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
            player.setWorldPosition(100, 1200);
        }
        
        if (camera != null && player != null) {
            camera.centerOn(player.getWorldX(), player.getWorldY());
            updateAllActors();
            updateBackground();
        }
    }
    
    public RoomTwo() {
        this(null);
    }
    
    // Override act() to add null check
    public void act() {
        if (camera != null && player != null) {
            super.act(); // Call GameWorld's act()
        }
    }
    
    protected void initializeMapGrid() {
        // Empty platform data for this room
        int[][] platformData = {};
        
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
            
            int tilesWide = endTileX - startTileX + 1;
            int tilesHigh = endTileY - startTileY + 1;
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
                    platformX[index] = tileX;
                    platformY[index] = tileY;
                    index++;
                }
            }
        }
        
        // Create border walls
        int wallTileCount = (TILES_WIDE * 2) + (TILES_HIGH * 2);
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
        
        // Door back to RoomOne (example: bottom-left)
        int[] doorX = new int[]{1, 2};
        int[] doorY = new int[]{TILES_HIGH - 1, TILES_HIGH - 1};
        
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
}

