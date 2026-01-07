import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Grid to be called for each world. 
 * 
 * @author Paul
 * 
 */
public class MapGrid
{
    private int tileWidth;
    private int tileHeight;
    private int worldWidth;
    private int worldHeight;
    private int TILES_WIDE; 
    private int TILES_HIGH;
    private int[][] tileMap;
    private int [] wallsX;
    private int [] platformX;
    private int [] doorX;
    private int [] breakableX;
    private int [] wallsY;
    private int [] platformY;
    private int [] doorY;
    private int [] breakableY;
    private boolean hasWalls;
    private boolean hasPlatform;
    private boolean hasDoor;
    private boolean hasBreakable;
    /**
     * @author Paul
     * Create a 2d array to be used for each separate room. Accepts parameters and arrays from world class 
     * where it is called to be used to fill the 2d array for different block types for the player to interact
     * with. Has barrier walls for edge of map, platforms to jump on, doors to move rooms and breakable objects 
     * to interact with. \
     * 
     * Note: A lot of the code here assumes perfect arrays from the user meaning no overlap in tiles,
     * not calling for tiles out of bounds, dividing world to perfect tiles, etc. This is intentional.
     * 
     * @param TILE_WIDTH is width of each tile in grid
     * @param TILE_HEIGHT is height of tile in grid
     * @param WORLD_WIDTH is width of world to use to calculate how many tiles in grid
     * @param WOLRD_HEIGTH is height of world to help caluclate num tiles
     * @param WALLS is boolean if border walls exist
     * @param PLATFORM is boolean if platforms exist
     * @param DOOR is boolean if there are doors in the world
     * @param BREAKABLE is boolean if there are breakable objects
     * @param wallsDataX is array with x values in grid for walls
     * @param platformDataX is array with x values for paltforms in grid
     * @param doorDataX is array with x values for door tiles in grid
     * @param breakableDataX is array with x value for breakables in grid
     * @param wallsDataY is array with y values for walls in grid
     * @param platformDataY is array with y values for platforms in grid
     * @param doorDataY is array with y values for doors in grid
     * @param breakableDataY is array with y values for breakables in grid
     */
    public MapGrid(int TILE_WIDTH, int TILE_HEIGHT, int WORLD_WIDTH, int WORLD_HEIGHT, boolean WALLS, boolean PLATFORM, boolean DOOR, boolean BREAKABLE,int wallsDataX[], int platformDataX[], int doorDataX[], int breakableDataX[], int wallsDataY[], int platformDataY[], int doorDataY[], int breakableDataY[])
    {
        tileWidth = TILE_WIDTH;
        tileHeight = TILE_HEIGHT;
        worldWidth = WORLD_WIDTH;
        worldHeight = WORLD_HEIGHT;
        wallsX = wallsDataX;
        platformX = platformDataX;
        breakableX = breakableDataX;
        doorX = doorDataX;
        wallsY = wallsDataY;
        platformY = platformDataY;
        doorY = doorDataY;
        breakableY = breakableDataY;
        hasWalls = WALLS;
        hasPlatform = PLATFORM;
        hasDoor = DOOR;
        hasBreakable = BREAKABLE;
        
        
        TILES_WIDE = worldWidth / tileWidth;  
        TILES_HIGH = worldHeight / tileHeight;
        
        tileMap = new int[TILES_WIDE][TILES_HIGH];
        loadLevelData();
    }
    //Takes arrays with x and y data for each type of block and assigns the value
    private void loadLevelData() {
        // Fill with tile types: 0=empty, 1=solid walls, 2=platform, 3 = door, 4 = breakable
        for (int x = 0; x < TILES_WIDE; x++) {
            for (int y = 0; y < TILES_HIGH; y++) {
                tileMap[x][y] = 0;
            }
        }
        if (hasWalls && wallsX != null && wallsY != null && wallsX.length == wallsY.length){
            for(int i = 0 ; i < wallsX.length; i++){
                if(wallsX[i] >= 0 && wallsX[i] < TILES_WIDE && wallsY[i] >= 0 && wallsY[i] < TILES_HIGH){
                    tileMap[wallsX[i]][wallsY[i]] = 1;
                }
            }
        }
        if (hasPlatform && platformX != null && platformY != null && platformX.length == platformY.length){
            for(int i = 0 ; i < platformX.length; i++){
                if(platformX[i] >= 0 && platformX[i] < TILES_WIDE && platformY[i] >= 0 && platformY[i] < TILES_HIGH){
                    tileMap[platformX[i]][platformY[i]] = 2;
                }
                
            }
        }
        if (hasDoor && doorX != null && doorY != null && doorX.length == doorY.length){
            for(int i = 0 ; i < doorX.length; i++){
                if(doorX[i] >= 0 && doorX[i] < TILES_WIDE && doorY[i] >= 0 && doorY[i] < TILES_HIGH){
                    tileMap[doorX[i]][doorY[i]] = 3;
                }
            }
        }
        if (hasBreakable && breakableX != null && breakableY != null && breakableX.length == breakableY.length){
            for(int i = 0 ; i < breakableX.length; i++){
                if(breakableX[i] >= 0 && breakableX[i] < TILES_WIDE && breakableY[i] >= 0 && breakableY[i] < TILES_HIGH){
                    tileMap[breakableX[i]][breakableY[i]] = 4;
                }
            }
        }
    }
    public int[][] getTileMap() {
        return tileMap;
    }
    
    public int getTileAt(int x, int y) {
        if (x >= 0 && x < TILES_WIDE && y >= 0 && y < TILES_HIGH) {
            return tileMap[x][y];
        }
        return -1; // or 1 for solid wall outside bounds
    }
    public int getTilesWide() { 
        return TILES_WIDE; 
    }
    public int getTilesHigh() { 
        return TILES_HIGH; 
    }
    public int getTileWidth() { 
        return tileWidth; 
    }
    public int getTileHeight() { 
        return tileHeight; 
    }
}
