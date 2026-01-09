import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * @author Paul
 */
class InteractiveDoor extends ScrollingActor {
    private int width, height;
    
    public InteractiveDoor(Camera camera, int width, int height) {
        super(camera);
        this.width = width;
        this.height = height;
        
        GreenfootImage img = new GreenfootImage(width, height);
        img.setColor(Color.RED);
        img.fillRect(0, 0, width, height);
        setImage(img);
    }
    public boolean containsWorldPoint(int worldX, int worldY) {
        GameWorld world = (GameWorld) getWorld();
        int tileX = world.worldToTileX(worldX);
        int tileY = world.worldToTileY(worldY);
        
        // Check tile if is interactive door
        int tileType = world.getMapGrid().getTileAt(tileX, tileY);
        return tileType == 4; // 2 = interactive door
    }
    public int getWidth() { 
        return width; 
    }
    public int getHeight() { 
        return height; 
    }
}



