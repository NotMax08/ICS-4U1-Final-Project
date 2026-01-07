import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * @author Paul
 */
class Platform extends ScrollingActor {
    private int width, height;
    
    public Platform(Camera camera, int width, int height) {
        super(camera);
        this.width = width;
        this.height = height;
        
        GreenfootImage img = new GreenfootImage(width, height);
        img.setColor(Color.GREEN);
        img.fillRect(0, 0, width, height);
        setImage(img);
    }
    public boolean containsWorldPoint(int worldX, int worldY) {
        RoomOne world = (RoomOne) getWorld();
        int tileX = world.worldToTileX(worldX);
        int tileY = world.worldToTileY(worldY);
        
        // Check tile if is platform
        int tileType = world.getMapGrid().getTileAt(tileX, tileY);
        return tileType == 2; // 2 = platform
    }
    public int getWidth() { 
        return width; 
    }
    public int getHeight() { 
        return height; 
    }
}


