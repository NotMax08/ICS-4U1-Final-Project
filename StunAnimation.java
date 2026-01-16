import greenfoot.*;
import java.util.*;

/**
 * StunAnimation - Shows a quick sequential animation of stun images
 * 
 * @author Max & Claude
 * @version 1.0
 */
public class StunAnimation extends ScrollingActor {
    
    // Position
    private int startWorldX;
    private int startWorldY;
    
    // Animation constants
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 400;
    private static final int FRAMES_PER_IMAGE = 3;  // How many frames to show each image
    
    // Images and state
    private ArrayList<GreenfootImage> slashImages = new ArrayList<>();
    private int currentImageIndex = 0;
    private int frameCounter = 0;
    
    // Optional: track the enemy to follow its movement
    private ScrollingActor followTarget = null;
    private int xOffset = 0;
    private int yOffset = 0;
    
    // Constructor with specific position
    public StunAnimation(Camera camera, int worldX, int worldY, int width, int height) {
        super(camera);
        this.startWorldX = worldX;
        this.startWorldY = worldY;
        addImages(width, height);
    }
    
    // Constructor with default size
    public StunAnimation(Camera camera, int worldX, int worldY) {
        super(camera);
        this.startWorldX = worldX;
        this.startWorldY = worldY;
        addImages(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    // Constructor that follows with default size
    public StunAnimation(Camera camera, ScrollingActor target, int xOffset, int yOffset) {
        super(camera);
        this.followTarget = target;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.startWorldX = target.getWorldX() + xOffset;
        this.startWorldY = target.getWorldY() + yOffset;
        addImages(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    private void addImages(int width, int height) {
        slashImages.add(getUniformImage("hit.png", width, height));
        slashImages.add(getUniformImage("hit1.png", width, height));
        slashImages.add(getUniformImage("hit2.png", width, height));
    }
    
    protected static GreenfootImage getUniformImage(String filename, int width, int height) {
        GreenfootImage img = new GreenfootImage(filename);
        img.scale(width, height);
        return img;
    }
    
    @Override
    public void addedToWorld(World w) {
        setWorldPosition(startWorldX, startWorldY);
        updateImage();
    }
    
    public void act() {
        // Follow the target if one is set
        if (followTarget != null && followTarget.getWorld() != null) {
            setWorldPosition(followTarget.getWorldX() + xOffset, followTarget.getWorldY() + yOffset);
        }
        
        // Count frames
        frameCounter++;
        
        // Move to next image after holding for FRAMES_PER_IMAGE frames
        if (frameCounter >= FRAMES_PER_IMAGE) {
            frameCounter = 0;
            currentImageIndex++;
            
            // Check if animation is complete
            if (currentImageIndex >= slashImages.size()) {
                // Animation complete - remove from world
                getWorld().removeObject(this);
                return;
            }
            
            updateImage();
        }
    }
    
    private void updateImage() {
        if (currentImageIndex >= slashImages.size()) {
            return;
        }
        
        setImage(slashImages.get(currentImageIndex));
    }
}