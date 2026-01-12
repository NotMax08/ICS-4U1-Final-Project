import greenfoot.*;
/**
 * @author Paul assisted by Claude
 * 
 */
public class RoomOne extends GameWorld {
    
    public RoomOne(Player existingPlayer) {
        super(); 
        
        fullBackground = new GreenfootImage("roomone.jpg");
        fullBackground.scale(WORLD_WIDTH, WORLD_HEIGHT);
        
        initializeMapGrid();
        
        if (existingPlayer != null) {
            transferPlayer(existingPlayer, 800, 1200);
        } else {
            player = new Player(camera);
            addObject(player, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
            player.setWorldPosition(800, 1200);
        }
        
        setIcons();
        
        createPlatformVisuals();
        Crawler enemy = new Crawler(camera);
        addObject(enemy, SCREEN_WIDTH/2 , SCREEN_HEIGHT/2);
        enemy.setWorldPosition(500, 1100 - enemy.getImage().getHeight()/2); 
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
            {550, 1100, 500, 20},
            {890, 740, 360, 20},
            {510, 740, 280, 20},
            {1925, 1120, 175, 20},
            {2200, 980, 380, 20},
            {1000, 1350, 400, 20},
            {1730, 1170, 200, 20},
            {1610, 1220, 150, 20},
            {1350, 1320, 370, 20},
            {230, 1030, 150, 20}
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
        //Interactive Doors
        int[][] interactiveData = {
            
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
            false,
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
            {200, 400, 300, 20},
            {600, 300, 200, 20},
            {1000, 500, 400, 20},
            {1500, 200, 300, 20},
            {550, 1100, 500, 20},
            {890, 740, 360, 20},
            {510, 740, 280, 20},
            {1925, 1120, 175, 20},
            {2200, 980, 380, 20},
            {1000, 1350, 400, 20},
            {1730, 1170, 200, 20},
            {1610, 1220, 150, 20},
            {1350, 1320, 370, 20},
            {230, 1030, 150, 20}
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
            {250, 820, 80, 140},   //Left door
            {700, 1130, 80, 140},  //Middle door
            {2330, 810, 80, 140}   // Right door       
        };
        for(int[] region : doorRegions) {
            int worldX = region[0];
            int worldY = region[1];
            int width = region[2];
            int height = region[3];
            
            InteractiveDoor door = new InteractiveDoor(camera, width, height);
            addObject(door, 0, 0);
            door.setWorldPosition(worldX, worldY);
        }
    }
}