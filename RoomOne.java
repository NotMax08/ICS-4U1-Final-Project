import greenfoot.*;

/**
 * @author Paul assisted by Claude
 */
public class RoomOne extends GameWorld {
    private boolean visuals;
    private MapGridDebugOverlay gridDebug;
    
    // Default spawn position (used only for new game)
    private static final int DEFAULT_SPAWN_X = 900;
    private static final int DEFAULT_SPAWN_Y = 1200;
    
    // Constructor with source room info
    public RoomOne(String sourceRoom) {
        super(); 
        
        fullBackground = new GreenfootImage("roomone.jpg");
        fullBackground.scale(WORLD_WIDTH, WORLD_HEIGHT);
        
        initializeMapGrid();
        
        // Determine spawn position based on which room we came from
        int spawnX = DEFAULT_SPAWN_X;
        int spawnY = DEFAULT_SPAWN_Y;
        
        if (sourceRoom != null) {
            RoomPositionTracker tracker = RoomPositionTracker.getInstance();
            RoomPositionTracker.SpawnPosition spawn = tracker.getSpawnPosition(sourceRoom, "RoomOne");
            
            if (spawn != null) {
                spawnX = spawn.x;
                spawnY = spawn.y;
            }
        }
        
        // Create player at determined position
        player = new Player(camera);
        addObject(player, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        player.setWorldPosition(spawnX, spawnY);
        
        initalizeDisplays();
        
        visuals = false;
        if(visuals){
            createPlatformVisuals();
        }
        
        createInteractiveDoors();
        
        // Create enemies
        Golem golem = new Golem(camera);
        addObject(golem, SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
        golem.setWorldPosition(500, 1100 - golem.getImage().getHeight()/2);
        
        Golem golem2 = new Golem(camera);
        addObject(golem2, SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
        golem2.setWorldPosition(1800, 500);
        
        BasicFly fly1 = new BasicFly(camera, 1200, 1800, 1100);
        addObject(fly1, SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
        fly1.setWorldPosition(1500, 1100);
        
        BasicFly fly2 = new BasicFly(camera, 700, 1100, 320);  
        addObject(fly2, SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
        fly2.setWorldPosition(800, 320);
        
        Fungi fungi1 = new Fungi(camera);
        addObject(fungi1, SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
        fungi1.setWorldPosition(888, 680 - 100);
        
        if (camera != null && player != null) {
            camera.centerOn(player.getWorldX(), player.getWorldY());
            updateAllActors();
            updateBackground();
        }
        
        gridDebug = new MapGridDebugOverlay(this, mapGrid);
        addObject(gridDebug, 0, 0);
        // Position overlay at world origin (top-left corner)
        gridDebug.setWorldPosition(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
        
        drawPlatformOnBackground(910, 500, 420, 40, "platform1.png");
        drawPlatformOnBackground(610, 300, 220, 40, "platform1.png");
        drawPlatformOnBackground(1800, 440, 320, 40, "platform1.png");
        drawPlatformOnBackground(1400, 200, 320, 40, "platform1.png");
        
        SoundManager.getInstance().playBackgroundMusic("Room1Music.mp3");
    }
    
    // Default constructor for new game
    public RoomOne() {
        this((String)null);
    }
    
    public void act() {
        if (camera != null && player != null) {
            super.act();
        }
        if (Greenfoot.isKeyDown("g")) {
            gridDebug.toggle();
            Greenfoot.delay(10);
        }
    }
    
    protected void initializeMapGrid() {
        int[][] platformData = {
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
        
        int wallTileCount = (TILES_WIDE * 2) + (TILES_HIGH * 2);
        int[] wallsX = new int[wallTileCount/2 + 65];
        int[] wallsY = new int[wallTileCount/2 + 65];
        
        index = 0;
        for (int x = 0; x < TILES_WIDE; x++) {
            wallsX[index] = x;
            wallsY[index] = TILES_HIGH - 1;
            index++;
        }
        
        for (int y = 0; y < TILES_HIGH; y++) {
            wallsX[index] = 0;
            wallsY[index] = y;
            index++;
        }
        
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
        
        int[][] interactiveData = {
            {2090, 650, 100, 150}  
        };
        
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
            TILE_SIZE, TILE_SIZE, WORLD_WIDTH, WORLD_HEIGHT,
            true, true, true, false, true,
            wallsX, platformX, doorX, breakableX,
            wallsY, platformY, doorY, breakableY,
            interactiveX, interactiveY
        );
    }
    
    private void createPlatformVisuals() {
        int[][] platformRegions = {
            {35, 800, 70, 20}, {600, 300, 200, 20}, {900, 500, 400, 20},
            {1400, 200, 300, 20}, {550, 1100, 500, 20}, {890, 740, 360, 20},
            {510, 740, 280, 20}, {1925, 1120, 175, 20}, {2200, 980, 380, 20},
            {1000, 1350, 400, 20}, {1730, 1170, 200, 20}, {1610, 1220, 150, 20},
            {1350, 1320, 370, 20}, {230, 1030, 150, 20}, {115,980,80,20},
            {1750, 740, 520, 20}, {2180, 740, 120, 20}, {1110, 870, 80, 20},
            {1200, 940, 100, 20}, {1450, 830, 100, 20}, {35, 530, 70, 20},
            {140, 430, 140, 20}, {250, 290, 60, 20}, {310, 240, 60, 20},
            {370, 190, 60, 20}, {480, 130, 170, 20}, {620, 60, 170, 20},
            {760, 20, 120, 20}, {960, 60, 300, 20}, {1800, 70, 800, 20},
            {2440, 740, 120, 20}, {2420, 540, 120, 20}, {1800, 440, 300, 20}, 
            {2300, 210, 160, 20}, {2200, 150, 100, 20}, {1380, 30, 60, 20}
        };
        
        for (int[] region : platformRegions) {
            Platform platform = new Platform(camera, region[2], region[3]);
            addObject(platform, 0, 0);
            platform.setWorldPosition(region[0], region[1]);
        }
    }
    
    private void createInteractiveDoors() {
        int[][] doorRegions = {{2090, 650, 100, 150}};
        
        for(int[] region : doorRegions) {
            InteractiveDoor door = new InteractiveDoor(camera, region[2], region[3], "roomone");
            addObject(door, 0, 0);
            door.setWorldPosition(region[0], region[1]);
        }
    }
    
    private void drawPlatformOnBackground(int worldX, int worldY, int width, int height, String imageName) {
        GreenfootImage platformImg = new GreenfootImage(imageName);
        platformImg.scale(width, height);
        fullBackground.drawImage(platformImg, worldX - width/2, worldY - height/2);
    }
    
    public void started() {
        SoundManager.getInstance().playBackgroundMusic("Room1Music.mp3");
    }
    
    public void stopped() {
        SoundManager.getInstance().pauseBackgroundMusic();
    }
}