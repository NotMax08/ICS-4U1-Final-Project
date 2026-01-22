import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * Debug visuals for platforms
 * @author Paul
 */
class Platform extends ScrollingActor {
    private int width, height;
    /**
     * platform debug visuals constructor
     * 
     * @param camera camera for reference
     * @param width width of platform
     * @param height height of platform
     */
    public Platform(Camera camera, int width, int height) {
        super(camera);
        this.width = width;
        this.height = height;
        
        GreenfootImage img = new GreenfootImage(width, height);
        img.setColor(Color.GREEN);
        img.fillRect(0, 0, width, height);
        setImage(img);
        
    }
    /**
     * checks which tiles are set to platform
     * 
     * @param worldX world x coord
     * @param worldY world y coord
     */
    public boolean containsWorldPoint(int worldX, int worldY) {
        GameWorld world = (GameWorld) getWorld();
        int tileX = world.worldToTileX(worldX);
        int tileY = world.worldToTileY(worldY);
        
        // Check tile if is platform
        int tileType = world.getMapGrid().getTileAt(tileX, tileY);
        return tileType == 2; // 2 = platform
    }
    /**
     * getters
     */
    public int getWidth() { 
        return width; 
    }
    public int getHeight() { 
        return height; 
    }
}


