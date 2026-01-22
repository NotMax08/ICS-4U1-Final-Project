import greenfoot.*;

/**
 * Collectible items that exist in the game world and scroll with the camera
 */
public class CollectibleKey extends ScrollingActor {
    private GreenfootImage image;
    
    public CollectibleKey(Camera camera) {
        super(camera);
        image = new GreenfootImage("key.png");
        image.scale(image.getWidth()/5, image.getHeight()/5);
        setImage(image);
    }
    
    public void act() {
        // Check if player collects it
        Player player = (Player) getOneIntersectingObject(Player.class);
        if (player != null) {
            // Add to player inventory
            player.updateItemCount(3, 1); // Index 3 is for keys
            
            // Remove from world
            getWorld().removeObject(this);
        }
    }
}