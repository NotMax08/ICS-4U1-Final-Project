import greenfoot.*;
/**
 * 
 * @author Paul assisted by Claude
 */
public class RoomTwo extends GameWorld {
    
    public RoomTwo(Player existingPlayer) {
        super(); // This creates the camera
        
        fullBackground = new GreenfootImage("roomtwo.jpg");
        fullBackground.scale(WORLD_WIDTH, WORLD_HEIGHT);
        
        initializeMapGrid();
        createPlatformVisuals();
        InteractiveDoor doorOne = new InteractiveDoor(30,50);
        addObject(doorOne, 240,875);
        
        if (existingPlayer != null) {
            transferPlayer(existingPlayer, 525, 900);
        } else {
            player = new Player(camera);
            addObject(player, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
            player.setWorldPosition(525, 900);
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
        // Analyze the actual visible platforms in the image
        int[][] platformData = {
               // Bottom main floor 
            {250, 1060, 520, 40},
            
            // Bottom floor   
            {1675, 1350, 300, 40},
            
            // Left mid-level platform 
            {240, 900, 460, 20},
            
            // Center-left platform 
            {1000, 1225, 1040, 40},
            
            // Large center mid level platform 
            {1725, 890, 1550, 20},
            
            // Center-right platform
            {2250, 1225, 930, 40},
            
            // Center mid-level platform 
            {700, 650, 200, 20},
            
            // Upper left ledge 
            {600, 380, 200, 20},
            
            // Upper middle
            {1725, 350, 1650, 20},
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
        
        // Walls - only outer boundaries and major vertical barriers
        java.util.ArrayList<Integer> wallXList = new java.util.ArrayList<>();
        java.util.ArrayList<Integer> wallYList = new java.util.ArrayList<>();
        
        // Left wall (x=0, all y)
        for (int y = 0; y < TILES_HIGH; y++) {
            wallXList.add(0);
            wallYList.add(y);
        }
        
        // Right wall (x=124, all y)
        for (int y = 0; y < TILES_HIGH; y++) {
            wallXList.add(TILES_WIDE - 1);
            wallYList.add(y);
        }
        
        // Top wall (y=0, all x)
        for (int x = 1; x < TILES_WIDE - 1; x++) {
            wallXList.add(x);
            wallYList.add(0);
        }
        
        // Bottom wall (y=70, all x)
        for (int x = 1; x < TILES_WIDE - 1; x++) {
            wallXList.add(x);
            wallYList.add(TILES_HIGH - 1);
        }
        
        int[] wallsX = new int[wallXList.size()];
        int[] wallsY = new int[wallYList.size()];
        for (int i = 0; i < wallXList.size(); i++) {
            wallsX[i] = wallXList.get(i);
            wallsY[i] = wallYList.get(i);
        }
        
        // Doors at left and right sides
        int[] doorX = new int[]{0, TILES_WIDE - 1};
        int[] doorY = new int[]{35, 35}; // Mid-height doors
        
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
               // Bottom main floor 
            {250, 1060, 520, 40},
            
            // Bottom floor   
            {1675, 1350, 300, 40},
            
            // Left mid-level platform 
            {240, 900, 460, 20},
            
            // Center-left platform 
            {1000, 1225, 1040, 40},
            
            // Large center mid level platform 
            {1725, 890, 1550, 20},
            
            // Center-right platform
            {2250, 1225, 930, 40},
            
            // Center mid-level platform 
            {700, 650, 200, 20},
            
            // Upper left ledge 
            {600, 380, 200, 20},
            
            // Upper middle
            {1725, 350, 1650, 20},
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

