import greenfoot.*;
// ===== COLLECTIBLE CLASS =====
class Collectible extends ScrollingActor {
    public Collectible(Camera camera) {
        super(camera);
        GreenfootImage img = new GreenfootImage(20, 20);
        img.setColor(Color.BLACK);
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
