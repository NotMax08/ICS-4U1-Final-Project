import greenfoot.*;
/**
 * 
 * @author Paul assisted by Claude
 */
public class RoomTwo extends GameWorld {
    private MapGridDebugOverlay gridDebug;
    private boolean visuals;
    public RoomTwo(Player existingPlayer) {
        super(); // This creates the camera
        
        fullBackground = new GreenfootImage("roomtwo.jpg");
        fullBackground.scale(WORLD_WIDTH, WORLD_HEIGHT);
        
        initializeMapGrid();
        visuals = false;
        if(visuals){
            createPlatformVisuals();
        }
        createInteractiveDoors();//debug visual in contructor class
        
        this.setPaintOrder(Message.class, InventoryDisplay.class, AbilityDisplay.class, Player.class, Platform.class, InteractiveDoor.class);
        
        if (existingPlayer != null) {
            transferPlayer(existingPlayer, 600, 900);
        } else {
            player = new Player(camera);
            addObject(player, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
            player.setWorldPosition(600, 900);
        }
        
        
        initalizeDisplays();
        
        if (camera != null && player != null) {
            camera.centerOn(player.getWorldX(), player.getWorldY());
            updateAllActors();
            updateBackground();
        }
        
        gridDebug = new MapGridDebugOverlay(this, mapGrid);
        addObject(gridDebug, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        gridDebug.setWorldPosition(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
        
        drawPlatformOnBackground(710, 650, 250, 100, "platform2.png");
        drawPlatformOnBackground(610, 380, 250, 100, "platform2.png");
        drawPlatformOnBackground(250, 890, 560, 100, "platform2.png");
        drawPlatformOnBackground(1675, 890, 1770, 100, "platform2.png");
    }
    
    public RoomTwo() {
        this(null);
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
        // Platforms in world coordinates
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
        
        // Create border walls 
        int wallTileCount = (TILES_WIDE * 2) + (TILES_HIGH * 2) - 4; // -4 for corner overlaps
        int[] wallsX = new int[wallTileCount + 64];
        int[] wallsY = new int[wallTileCount + 64];
        
        index = 0;
        // Bottom wall
        for (int x = 0; x < TILES_WIDE; x++) {
            wallsX[index] = x;
            wallsY[index] = TILES_HIGH - 1;
            index++;
        }
        
        // Left wall (exclude bottom corner)
        for (int y = 0; y < TILES_HIGH - 1; y++) {
            wallsX[index] = 0;
            wallsY[index] = y;
            index++;
        }
        
        // Top wall (exclude left corner)
        for(int x = 1; x < TILES_WIDE; x++){
            wallsX[index] = x;
            wallsY[index] = 0;
            index++;
        }
        
        // Right wall (exclude both corners)
        for(int y = 1; y < TILES_HIGH - 1; y++){
            wallsX[index] = 124;
            wallsY[index] = y;
            index++;  // <-- DON'T FORGET THIS!
        }
        
        //Additional walls added manually
        for(int i = 0; i < 14; i++){
            wallsX[index] = 45;
            wallsY[index] = 18 + i;
            index++;
        }
        for (int i = 0; i < 5; i++){
            wallsX[index] = 25;
            wallsY[index] = 55 + i;
            index++;
        }
        for (int i = 0; i < 31; i++){
            wallsX[index] = 24;
            wallsY[index] = 1 + i;
            index++;
        }
        for (int i = 0; i < 14; i++){
            wallsX[index] = 120;
            wallsY[index] = 46 + i;
            index++;
        }
        
        
        // Doors at left and right sides
        int[] doorX = new int[17];
        int[] doorY = new int[17];
        int tempIndex = 0;
        for(int i = 0; i < 17; i++){
            doorX[tempIndex] = 124;
            doorY[tempIndex] = 0 + i;
            tempIndex++;
        }
        
        
        int[] breakableX = new int[0];
        int[] breakableY = new int[0];
        //Interactive Doors
        //Y should be 20 pixels above visuals so visuals show the door directly
        //on platforms but the actual door tiles to not override the platform tiles
        int[][] interactiveData = {
            {250, 800, 80, 140},   // Left door
            {700, 1110, 80, 140},  // Middle door
            {2330, 790, 80, 140}   // Right door 
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
    private void createInteractiveDoors() {
            int[][] doorRegions = {
            {250, 820, 80, 140},   // Left door
            {700, 1130, 80, 140},  // Middle door
            {2330, 810, 80, 140}   // Right door       
        };
        
        String[] doorIds = {"npcenter", "backtormone", "enterboss"};
        
        for (int i = 0; i < doorRegions.length; i++) {
            int worldX = doorRegions[i][0];
            int worldY = doorRegions[i][1];
            int width = doorRegions[i][2];
            int height = doorRegions[i][3];
            
            InteractiveDoor door = new InteractiveDoor(camera, width, height, doorIds[i]);
            addObject(door, 0, 0);
            door.setWorldPosition(worldX, worldY);
        }
    }
    private void drawPlatformOnBackground(int worldX, int worldY, int width, int height, String imageName) {
        GreenfootImage platformImg = new GreenfootImage(imageName);
        platformImg.scale(width, height);
        
        int x = worldX - width/2;
        int y = worldY - height/2;
        
        fullBackground.drawImage(platformImg, x, y);
    }
}

