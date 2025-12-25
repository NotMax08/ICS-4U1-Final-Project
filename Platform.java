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
    
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}

// ===== COLLECTIBLE CLASS =====
class Collectible extends ScrollingActor {
    public Collectible(Camera camera) {
        super(camera);
        GreenfootImage img = new GreenfootImage(20, 20);
        img.setColor(Color.YELLOW);
        img.fillOval(0, 0, 20, 20);
        setImage(img);
    }
    
    public void act() {
        updateScreenPosition();
        
        // Check if player touches coin
        if (intersects((Player) getWorld().getObjects(Player.class).get(0))) {
            getWorld().removeObject(this);
        }
    }
    
    private boolean intersects(Player player) {
        int dist = (int) Math.hypot(worldX - player.getWorldX(), 
                                     worldY - player.getWorldY());
        return dist < 30;
    }
}
