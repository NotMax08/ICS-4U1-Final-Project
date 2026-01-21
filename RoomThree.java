import greenfoot.*;
/**
 * 
 * @author Paul assisted by Claude
 */
public class RoomThree extends GameWorld {
    private MapGridDebugOverlay gridDebug;
    private boolean visuals;
    
    // Default spawn position
    private static final int DEFAULT_SPAWN_X = 1000;
    private static final int DEFAULT_SPAWN_Y = 1250;
    
    public RoomThree(String sourceRoom) {
        super(); // This creates the camera
        
        fullBackground = new GreenfootImage("roomthree.jpg");
        fullBackground.scale(WORLD_WIDTH, WORLD_HEIGHT);
        
        initializeMapGrid();
        
        visuals = false;
        if(visuals){
            createPlatformVisuals();
        }
        createInteractiveDoors();
        
        this.setPaintOrder(Message.class, InventoryDisplay.class, AbilityDisplay.class, Player.class, Platform.class, InteractiveDoor.class, Fog.class, BaseEnemy.class, Spawner.class);
        
        // Determine spawn position
        int spawnX = DEFAULT_SPAWN_X;
        int spawnY = DEFAULT_SPAWN_Y;
        
        if (sourceRoom != null) {
            RoomPositionTracker tracker = RoomPositionTracker.getInstance();
            RoomPositionTracker.SpawnPosition spawn = tracker.getSpawnPosition(sourceRoom, "RoomThree");
            
            if (spawn != null) {
                spawnX = spawn.x;
                spawnY = spawn.y;
            }
        }
        
        // Create player at spawn position
        player = new Player(camera);
        addObject(player, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        player.setWorldPosition(spawnX, spawnY);
        
        // Create fog world effect to reduce visibility
        Fog fog = new Fog(player);
        addObject(fog, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        
        initalizeDisplays();
        
        if (camera != null && player != null) {
            camera.centerOn(player.getWorldX(), player.getWorldY());
            updateAllActors();
            updateBackground();
        }
        
        gridDebug = new MapGridDebugOverlay(this, mapGrid);
        addObject(gridDebug, 0, 0);
        gridDebug.setWorldPosition(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
        
        Fungi fungi1 = new Fungi(camera);
        addObject(fungi1, SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
        fungi1.setWorldPosition(1550, 1200);
        
        Fungi fungi2 = new Fungi(camera);
        addObject(fungi2, SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
        fungi2.setWorldPosition(1600, 1200);
        
        Fungi fungi3 = new Fungi(camera);
        addObject(fungi3, SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
        fungi3.setWorldPosition(240, 900);
         
        Fungi fungi4 = new Fungi(camera);
        addObject(fungi4, SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
        fungi4.setWorldPosition(456, 720);
        
        Fungi fungi5 = new Fungi(camera);
        addObject(fungi5, SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
        fungi5.setWorldPosition(2155, 800);
        
        Fungi fungi6 = new Fungi(camera);
        addObject(fungi6, SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
        fungi6.setWorldPosition(1323, 780);
        
        SoundManager.getInstance().playBackgroundMusic("Room3Music.mp3");
    }
    
    public RoomThree() {
        this((String)null);
    }
    
    // Override act() to add null check
    public void act() {
        if (camera != null && player != null) {
            super.act(); // Call GameWorld's act()
        }
        if (Greenfoot.isKeyDown("g")) {
            gridDebug.toggle();
            Greenfoot.delay(10); // debounce
        }
    }
    
    protected void initializeMapGrid() {
        // Platforms in tile coordinates {x start, x end, y}
        int[][] platformData = {
            {25, 27, 59},
            {28, 33, 64},
            {33, 56, 66},
            {65, 87, 66},
            {67, 73, 57},
            {63, 67, 54},
            {34, 36, 57},
            {33, 41, 56},
            {45, 62, 52},
            {41, 44, 54},
            {73, 85, 54},
            {29, 31, 44},
            {14, 28, 42},
            {23, 24, 50},
            {4, 22, 48},
            {5, 6, 41},
            {17, 24, 39},
            {7, 9 , 36},
            {10, 10, 34},
            {10, 11, 33},
            {12, 21, 32},
            {25, 37, 40},
            {28, 37, 36},
            {28, 31, 32},
            {22, 25, 23},
            {23, 25, 17},
            {23, 25, 10},
            {26, 28 , 7},
            {29, 52, 4},
            {73, 85, 45},
            {88, 89, 64},
            {90, 95, 59},
            {96, 99, 48},
            {78, 89, 36},
            {99, 114, 43},
            {90, 94, 39},
            {96, 109, 35},
            {95, 109, 31},
            {92, 107, 27},
            {79, 102, 22},
            {57, 78, 20},
            {61, 105, 4},
            {115, 118, 40},
            {69, 70, 42},
            {71, 72, 43},
            {62, 68, 41},
            {61, 61, 42},
            {60, 60, 43},
            {56, 59, 44},
            {57, 66, 29},
            {67, 68, 30},
            {69, 71, 31},
            {53, 60, 3},
            {72, 73, 32},
            {74, 74, 33},
            {75, 75, 34},
            {76, 77, 35},
            {53, 55, 11},
            {56, 56, 12},
            {42, 49, 10},
            {103, 104, 23},
            {105, 105, 24},
            {106, 106, 25},
            {107, 107, 26},
            {117, 118, 30},
            {106, 110, 7},
            {112, 116, 23},
            {53, 53, 29},
            {54, 54, 30},
            {55, 55, 31},
            {56, 56, 32},
            {51, 51, 32},
            {52, 52, 33},
            {53, 53, 34},
            {54, 54, 35}, 
        };
        
        // Convert platforms to tiles
        int totalPlatformTiles = 0;
        
        for (int[] platform : platformData) {
            int tileXStart = platform[0];
            int tileXEnd = platform[1];
            
            totalPlatformTiles += (tileXEnd - tileXStart) + 1;
        }
        
        int[] platformX = new int[totalPlatformTiles];
        int[] platformY = new int[totalPlatformTiles];
        
        int index = 0;
        for (int[] platform : platformData) {
            int tileXStart = platform[0];
            int tileXEnd = platform[1];
            int tileY = platform[2];
            
            for(int i = tileXStart; i <= tileXEnd; i++){
                platformX[index] = i;
                platformY[index] = tileY;
                index++;
            }
        }
        
        // Wall data format: {tileXStart, tileXEnd, tileYStart, tileYEnd}
        int[][] wallData = {
            {0, 124, 70, 70},    // Bottom wall
            {0, 124, 0, 0},      // Top wall
            {0, 0, 1, 69},       // Left wall
            {124, 124, 1, 69},   // Right wall
            //Additional walls
            {24, 24, 51, 58},
            {32, 32, 45, 55},
            {27, 27, 60, 63},
            {41, 41, 55, 55},
            {44, 44, 53, 53},
            {4, 4, 42, 47},
            {63, 63, 53, 53},
            {67, 67, 55, 56},
            {73, 73, 55, 56},
            {56, 56, 67, 69},
            {65, 65, 67, 69},
            {88, 88, 65, 65},
            {90, 90, 60, 63},
            {96, 96, 49, 58},
            {85, 85, 46, 53},
            {57, 57, 30, 33},
            {55, 55, 35, 43},
            {99, 99, 44, 47},
            {119, 119, 31, 39},
            {115, 115, 41, 42},
            {15, 15, 41, 41},
            {16, 16, 40, 40},
            {6, 6, 37, 40},
            {9, 9 , 35, 35},
            {28, 28, 33, 35},
            {32, 32, 5, 31},
            {21, 21, 24, 31},
            {25, 25, 18, 22},
            {22, 22, 11, 16},
            {25, 25, 8, 9},
            {28, 28, 5, 6},
            {22, 22, 49, 49},
            {90, 90, 37, 38},
            {95, 95, 36, 38},
            {109, 109, 32, 34},
            {117, 117, 24, 29},
            {106, 106, 5, 6},
            {57, 57, 13, 19},
            {53, 53, 12, 28},
            {78, 78, 21, 21},
            {49, 49, 11, 31},
            {50, 50, 32, 32},
            {38, 38, 5, 36},
            {41, 41, 10, 36},
            {111, 111, 8, 22},
            {29, 29, 43, 43},
            {72, 72, 44, 44}
        };
        
        int totalWallTiles = 0;
        
        for (int[] wall : wallData) {
            int tileXStart = wall[0];
            int tileXEnd = wall[1];
            int tileYStart = wall[2];
            int tileYEnd = wall[3];
            
            int tilesWide = (tileXEnd - tileXStart) + 1;
            int tilesHigh = (tileYEnd - tileYStart) + 1;
            totalWallTiles += tilesWide * tilesHigh;
        }
        
        int[] wallsX = new int[totalWallTiles];
        int[] wallsY = new int[totalWallTiles];
        
        index = 0;
        for (int[] wall : wallData) {
            int tileXStart = wall[0];
            int tileXEnd = wall[1];
            int tileYStart = wall[2];
            int tileYEnd = wall[3];
            
            for(int x = tileXStart; x <= tileXEnd; x++){
                for(int y = tileYStart; y <= tileYEnd; y++){
                    wallsX[index] = x;
                    wallsY[index] = y;
                    index++;
                }
            }
        }
        
        //Doors {startX, endX, startY, endY}
        int[][] doorData = {
            
        };
        
        int totalDoorTiles = 0;
        
        for (int[] door : doorData) {
            int tileXStart = door[0];
            int tileXEnd = door[1];
            int tileYStart = door[2];
            int tileYEnd = door[3];
            
            int tilesWide = (tileXEnd - tileXStart) + 1;
            int tilesHigh = (tileYEnd - tileYStart) + 1;
            totalDoorTiles += tilesWide * tilesHigh;
        }
        
        int[] doorX = new int[totalDoorTiles];
        int[] doorY = new int[totalDoorTiles];
        
        index = 0;
        for (int[] door : doorData) {
            int tileXStart = door[0];
            int tileXEnd = door[1];
            int tileYStart = door[2];
            int tileYEnd = door[3];
            
            for(int x = tileXStart; x <= tileXEnd; x++){
                for(int y = tileYStart; y <= tileYEnd; y++){
                    doorX[index] = x;
                    doorY[index] = y;
                    index++;
                }
            }
        }
        
        int[] breakableX = new int[0];
        int[] breakableY = new int[0];
        
        // Interactive Doors in TILE coordinates
        int[][] interactiveData = {
             {59, 63, 65, 69}  // Tile ranges: X from 59-63, Y from 65-69
        };
        
        // Convert doors to tiles
        int totalInteractiveTiles = 0;
        
        for (int[] door : interactiveData) {
            int tileXStart = door[0];
            int tileXEnd = door[1];
            int tileYStart = door[2];
            int tileYEnd = door[3];
            
            int tilesWide = (tileXEnd - tileXStart) + 1;  
            int tilesHigh = (tileYEnd - tileYStart) + 1;  
            totalInteractiveTiles += tilesWide * tilesHigh;        
        }
        
        int[] interactiveX = new int[totalInteractiveTiles];
        int[] interactiveY = new int[totalInteractiveTiles];
        
        index = 0;
        
        for(int[]door : interactiveData){
            int tileXStart = door[0];
            int tileXEnd = door[1];
            int tileYStart = door[2];
            int tileYEnd = door[3];
            
            for (int x = tileXStart; x <= tileXEnd; x++) {
                for (int y = tileYStart; y <= tileYEnd; y++) {
                    interactiveX[index] = x;
                    interactiveY[index] = y;
                    index++;
                }
            }
        }
        
        mapGrid = new MapGrid(
            TILE_SIZE,
            TILE_SIZE,
            WORLD_WIDTH,
            WORLD_HEIGHT,
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
    
    private void createInteractiveDoors() {
        // Interactive door tiles: X from 59-63, Y from 65-69
        // Convert to world coordinates: center of tile range
        int[][] doorTileRanges = {
            {59, 63, 65, 69}  // Tile coordinates
        };
        
        for (int i = 0; i < doorTileRanges.length; i++) {
            int tileXStart = doorTileRanges[i][0];
            int tileXEnd = doorTileRanges[i][1];
            int tileYStart = doorTileRanges[i][2];
            int tileYEnd = doorTileRanges[i][3];
            
            // Calculate center tile
            int centerTileX = (tileXStart + tileXEnd) / 2;
            int centerTileY = (tileYStart + tileYEnd) / 2;
            
            // Calculate size in tiles
            int tilesWide = tileXEnd - tileXStart + 1;
            int tilesHigh = tileYEnd - tileYStart + 1;
            
            // Convert to world coordinates
            int worldX = (centerTileX * TILE_SIZE) + (TILE_SIZE / 2);
            int worldY = (centerTileY * TILE_SIZE) + (TILE_SIZE / 2);
            int width = tilesWide * TILE_SIZE;
            int height = tilesHigh * TILE_SIZE;
            
            InteractiveDoor door = new InteractiveDoor(camera, width, height, "roomthree");
            addObject(door, 0, 0);
            door.setWorldPosition(worldX, worldY);
        }
    }
    
    public void started() {
        SoundManager.getInstance().playBackgroundMusic("Room3Music.mp3");
    }
    
    public void stopped() {
        SoundManager.getInstance().pauseBackgroundMusic();
    }
}