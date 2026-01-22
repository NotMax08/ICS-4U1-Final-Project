import greenfoot.*;

/**
 * @author Paul assisted by Claude
 */
public class RoomTwo extends GameWorld {
    private MapGridDebugOverlay gridDebug;
    private boolean visuals;
    
    // Default spawn position (used only for new game)
    private static final int DEFAULT_SPAWN_X = 600;
    private static final int DEFAULT_SPAWN_Y = 900;
    
    /**
     * Roomtwo constructor
     * 
     * @param sourceroom tracks room you entered from to keep coordinates for returning
     */
    public RoomTwo(String sourceRoom) {
        super();
        
        fullBackground = new GreenfootImage("roomtwo.jpg");
        fullBackground.scale(WORLD_WIDTH, WORLD_HEIGHT);
        
        initializeMapGrid();
        
        // Determine spawn position based on which room we came from
        int spawnX = DEFAULT_SPAWN_X;
        int spawnY = DEFAULT_SPAWN_Y;
        
        if (sourceRoom != null) {
            RoomPositionTracker tracker = RoomPositionTracker.getInstance();
            RoomPositionTracker.SpawnPosition spawn = tracker.getSpawnPosition(sourceRoom, "RoomTwo");
            
            if (spawn != null) {
                spawnX = spawn.x;
                spawnY = spawn.y;
            }
        }
        
        // Create player at determined position
        player = new Player(camera);
        addObject(player, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        player.setWorldPosition(spawnX, spawnY);
        
        visuals = false;
        if(visuals){
            createPlatformVisuals();
        }
        createInteractiveDoors();
        
        this.setPaintOrder(Message.class, InventoryDisplay.class, AbilityDisplay.class, 
                          Player.class, Platform.class, InteractiveDoor.class);
        
        initalizeDisplays();
        
        if (camera != null && player != null) {
            camera.centerOn(player.getWorldX(), player.getWorldY());
            updateAllActors();
            updateBackground();
        }
        
        gridDebug = new MapGridDebugOverlay(this, mapGrid);
        addObject(gridDebug, 0, 0);
        // Position overlay at world origin (top-left corner)
        gridDebug.setWorldPosition(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
        
        drawPlatformOnBackground(710, 650, 250, 100, "platform2.png");
        drawPlatformOnBackground(610, 380, 250, 100, "platform2.png");
        drawPlatformOnBackground(250, 890, 560, 100, "platform2.png");
        drawPlatformOnBackground(1675, 890, 1770, 100, "platform2.png");
        
        // Create enemies
        Grim grim = new Grim(camera, false);
        addObject(grim, SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
        grim.setWorldPosition(2120, 1100);
        
        Grim grim2 = new Grim(camera, false);
        addObject(grim2, SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
        grim2.setWorldPosition(260, 800);
        
        Grim grim3 = new Grim(camera, false);
        addObject(grim3, SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
        grim3.setWorldPosition(1260, 250);
        
        Grim grim4 = new Grim(camera, false);
        addObject(grim4, SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
        grim4.setWorldPosition(1460, 250);
        
        Knight knight = new Knight(camera);
        addObject(knight, SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
        knight.setWorldPosition(1260, 1100);
        
        Knight knight2 = new Knight(camera);
        //addObject(knight2, SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
        //knight2.setWorldPosition(1700, 250);
        
        SoundManager.getInstance().playBackgroundMusic("Room2Music.mp3");
        setPaintOrder(); 
    }
    /**
     * default constructor
     */
    public RoomTwo() {
        this((String)null);
    }
    /**
     * follows player and updates camera
     */
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
            {250, 1060, 520, 40},
            {1675, 1350, 300, 40},
            {240, 900, 460, 20},
            {1000, 1225, 1040, 40},
            {1725, 890, 1550, 20},
            {2250, 1225, 930, 40},
            {700, 650, 200, 20},
            {600, 380, 200, 20},
            {1725, 350, 1650, 20},
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
        
        int wallTileCount = (TILES_WIDE * 2) + (TILES_HIGH * 2) - 4;
        int[] wallsX = new int[wallTileCount + 64];
        int[] wallsY = new int[wallTileCount + 64];
        
        index = 0;
        for (int x = 0; x < TILES_WIDE; x++) {
            wallsX[index] = x;
            wallsY[index] = TILES_HIGH - 1;
            index++;
        }
        
        for (int y = 0; y < TILES_HIGH - 1; y++) {
            wallsX[index] = 0;
            wallsY[index] = y;
            index++;
        }
        
        for(int x = 1; x < TILES_WIDE; x++){
            wallsX[index] = x;
            wallsY[index] = 0;
            index++;
        }
        
        for(int y = 1; y < TILES_HIGH - 1; y++){
            wallsX[index] = 124;
            wallsY[index] = y;
            index++;
        }
        
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
        
        int[][] interactiveData = {
            {250, 800, 80, 140},
            {700, 1110, 80, 140},
            {2330, 790, 80, 140}
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
            {250, 1060, 520, 40}, {1675, 1350, 300, 40}, {240, 900, 460, 20},
            {1000, 1225, 1040, 40}, {1725, 890, 1550, 20}, {2250, 1225, 930, 40},
            {700, 650, 200, 20}, {600, 380, 200, 20}, {1725, 350, 1650, 20},
        };
        
        for (int[] region : platformRegions) {
            Platform platform = new Platform(camera, region[2], region[3]);
            addObject(platform, 0, 0);
            platform.setWorldPosition(region[0], region[1]);
        }
    }
    
    private void createInteractiveDoors() {
        int[][] doorRegions = {
            {250, 820, 80, 140},
            {700, 1130, 80, 140},
            {2330, 810, 80, 140}
        };
        
        String[] doorIds = {"npcenter", "backtormone", "enterboss"};
        
        for (int i = 0; i < doorRegions.length; i++) {
            InteractiveDoor door = new InteractiveDoor(camera, doorRegions[i][2], 
                                                       doorRegions[i][3], doorIds[i]);
            addObject(door, 0, 0);
            door.setWorldPosition(doorRegions[i][0], doorRegions[i][1]);
        }
    }
    
    private void drawPlatformOnBackground(int worldX, int worldY, int width, int height, String imageName) {
        GreenfootImage platformImg = new GreenfootImage(imageName);
        platformImg.scale(width, height);
        fullBackground.drawImage(platformImg, worldX - width/2, worldY - height/2);
    }
    /**
     * starts sound when entering
     */
    public void started() {
        SoundManager.getInstance().playBackgroundMusic("Room2Music.mp3");
    }
    /**
     * stops sound when instance paused
     */
    public void stopped() {
        SoundManager.getInstance().pauseBackgroundMusic();
    }
}