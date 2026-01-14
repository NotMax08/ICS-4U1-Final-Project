import greenfoot.*;
/**
 * @author Paul assisted by Claude
 * 
 */
public class RoomOne extends GameWorld {
    private boolean visuals; //debug to show platforms, doors, walls, etc 
    private MapGridDebugOverlay gridDebug;
    public RoomOne(Player existingPlayer) {
        super(); 
        
        fullBackground = new GreenfootImage("roomone.jpg");
        fullBackground.scale(WORLD_WIDTH, WORLD_HEIGHT);
        
        initializeMapGrid();
        
        if (existingPlayer != null) {
            transferPlayer(existingPlayer, 900, 1200);
        } else {
            player = new Player(camera);
            addObject(player, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
            player.setWorldPosition(900, 1200);
        }
        
        setIcons();
        
        visuals = true;
        if(visuals){
            createPlatformVisuals();
            
        }
        
        createInteractiveDoors();//debug visual in contructor class
        
        // Create different enemy types easily
        Golem golem = new Golem(camera);
        addObject(golem, SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
        golem.setWorldPosition(500, 1100 - golem.getImage().getHeight()/2);
        
        BasicFly fly1 = new BasicFly(camera, 1200, 1800, 1100);
        addObject(fly1, SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
        fly1.setWorldPosition(1500, 1100);
        
        Fungi fungi1 = new Fungi(camera);
        addObject(fungi1, SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
        fungi1.setWorldPosition(888, 680 - 100);
        
        // Null check
        if (camera != null && player != null) {
            camera.centerOn(player.getWorldX(), player.getWorldY());
            updateAllActors();
            updateBackground();
        }
        
        gridDebug = new MapGridDebugOverlay(this, mapGrid);
        addObject(gridDebug, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        gridDebug.setWorldPosition(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
        
        
        drawPlatformOnBackground(910, 500, 420, 40, "platform1.png");
        drawPlatformOnBackground(610, 300, 220, 40, "platform1.png");
        drawPlatformOnBackground(1800, 440, 320, 40, "platform1.png");
        drawPlatformOnBackground(1400, 200, 320, 40, "platform1.png");
    }
    
    public RoomOne() {
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
            //Pause and inspect and find world coord variables to figure 
            //out which platform is which
            {35, 800, 70, 20},
            {600, 300, 200, 20},
            {900, 500, 400, 20},
            {1400, 200, 300, 20},
            {550, 1100, 480, 20},
            {890, 740, 360, 20},
            {510, 740, 280, 20},
            {1925, 1120, 175, 20},
            {2200, 980, 380, 20},
            {1000, 1350, 400, 20},
            {1730, 1170, 200, 20},
            {1610, 1220, 150, 20},
            {1350, 1320, 370, 20},
            {230, 1030, 150, 20},
            {115,980,80,20},
            {1750, 740, 520, 20},
            {2180, 740, 120, 20},
            {1110, 870, 80, 20},
            {1200, 940, 100, 20},
            {1450, 830, 100, 20},
            {35, 530, 70, 20},
            {140, 430, 140, 20},
            {250, 290, 60, 20},
            {310, 240, 60, 20},
            {370, 190, 60, 20},
            {480, 130, 170, 20},
            {620, 60, 170, 20},
            {760, 20, 120, 20},
            {960, 60, 300, 20},
            {1800, 70, 800, 20},
            {2440, 740, 120, 20},
            {2420, 540, 120, 20},
            {1800, 440, 300, 20}, 
            {2300, 210, 160, 20},
            {2200, 150, 100, 20},
            {1380, 30, 60, 20}
            
            
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
        int wallTileCount = (TILES_WIDE * 2) + (TILES_HIGH * 2 ); 
        int[] wallsX = new int[wallTileCount/2 + 65]; //Plus additional wall barriers counted 
        int[] wallsY = new int[wallTileCount/2 + 65]; //manually with the debug menu
        
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
        //Additional wall barriers
        for (int y = 0; y < 7; y++){
            wallsX[index] = 3;
            wallsY[index] = 41 + y;
            index++;
        }
        for (int y = 0; y < 11; y++){
            wallsX[index] = 39;
            wallsY[index] = 56 + y;
            index++;
        }
        for (int y = 0; y < 5; y++){
            wallsX[index] = 100;
            wallsY[index] = 50 + y;
            index++;
        }
        for (int y = 0; y < 10; y++){
            wallsX[index] = 119;
            wallsY[index] = 38 + y;
            index++;
        }
        for (int y = 0; y < 8; y++){
            wallsX[index] = 124;
            wallsY[index] = 28 + y;
            index++;
        }
        for(int y = 0; y < 14; y++){
            wallsX[index] = 118;
            wallsY[index] = 12 + y;
            index++;
        }
        for(int y = 0; y < 5; y++){
            wallsX[index] = 10;
            wallsY[index] = 16 + y;
            index++;
        }
        for(int i = 0; i < 5; i++){
            wallsX[index] = 53;
            wallsY[index] = 38 + i;
            index++;
        }
        
        
        int[] doorX = new int[11];
        int[] doorY = new int[11];
        index = 56;
        for(int i = 0; i < doorX.length; i++){
            doorX[i] = index;
            doorY[i] = 0;
            index++;
        }
        int[] breakableX = new int[0];
        int[] breakableY = new int[0];
        //Interactive Doors
        int[][] interactiveData = {
            {2090, 650, 100, 150}  
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
            //Pause and inspect and find world coord variables to figure 
            //out which platform is which
            {35, 800, 70, 20},
            {600, 300, 200, 20},
            {900, 500, 400, 20},
            {1400, 200, 300, 20},
            {550, 1100, 500, 20},
            {890, 740, 360, 20},
            {510, 740, 280, 20},
            {1925, 1120, 175, 20},
            {2200, 980, 380, 20},
            {1000, 1350, 400, 20},
            {1730, 1170, 200, 20},
            {1610, 1220, 150, 20},
            {1350, 1320, 370, 20},
            {230, 1030, 150, 20},
            {115,980,80,20},
            {1750, 740, 520, 20},
            {2180, 740, 120, 20},
            {1110, 870, 80, 20},
            {1200, 940, 100, 20},
            {1450, 830, 100, 20},
            {35, 530, 70, 20},
            {140, 430, 140, 20},
            {250, 290, 60, 20},
            {310, 240, 60, 20},
            {370, 190, 60, 20},
            {480, 130, 170, 20},
            {620, 60, 170, 20},
            {760, 20, 120, 20},
            {960, 60, 300, 20},
            {1800, 70, 800, 20},
            {2440, 740, 120, 20},
            {2420, 540, 120, 20},
            {1800, 440, 300, 20}, 
            {2300, 210, 160, 20},
            {2200, 150, 100, 20},
            {1380, 30, 60, 20}
            
               
            
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
            {2090, 650, 100, 150}     
        };
        for(int[] region : doorRegions) {
            int worldX = region[0];
            int worldY = region[1];
            int width = region[2];
            int height = region[3];
            
            InteractiveDoor door = new InteractiveDoor(camera, width, height, "roomone");
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