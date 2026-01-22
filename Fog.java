import greenfoot.*;

/**
 * Optimized fog overlay for room three meant to obscure vision
 * Uses pre-rendered gradient with simple concentric circles
 * Much faster than pixel-by-pixel rendering
 * 
 * UNFINISHED and not called
 * 
 * @author Paul and Claude
 */
public class Fog extends Actor {
    // Vision settings
    private static final int CLEAR_RADIUS = 200;
    private static final int GRADIENT_WIDTH = 100;
    private static final int MAX_RADIUS = CLEAR_RADIUS + GRADIENT_WIDTH;
    
    // Fog appearance
    private static final Color FOG_COLOR = new Color(70, 75, 80, 255);
    
    // Screen dimensions (actual Greenfoot screen)
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    
    // Pre-rendered fog (created once and reused)
    private static GreenfootImage fogImage = null;
    /**
     * constructor for fog
     */
    public Fog() {
        // Create fog gradient only once
        if (fogImage == null) {
            createOptimizedFog();
        }
        setImage(fogImage);
    }
    
    /**
     * Creates fog using concentric circles (MUCH faster than pixel-by-pixel)
     */
    private void createOptimizedFog() {
        
        fogImage = new GreenfootImage(SCREEN_WIDTH, SCREEN_HEIGHT);
        int centerX = SCREEN_WIDTH / 2;
        int centerY = SCREEN_HEIGHT / 2;
        
        // Step 1: Fill entire screen with opaque fog
        fogImage.setColor(FOG_COLOR);
        fogImage.fill();
        
        // Step 2: Draw gradient from outside to inside using circles
        // This creates a smooth radial gradient effect
        int gradientSteps = 50; // More steps = smoother gradient
        
        for (int i = gradientSteps; i >= 0; i--) {
            double progress = (double) i / gradientSteps;
            
            // Calculate radius for this step
            int radius = CLEAR_RADIUS + (int)(progress * GRADIENT_WIDTH);
            
            // Calculate opacity (255 at edge, 0 at center)
            int alpha = (int)(progress * 255);
            
            // Draw circle with this opacity
            Color circleColor = new Color(
                FOG_COLOR.getRed(),
                FOG_COLOR.getGreen(),
                FOG_COLOR.getBlue(),
                alpha
            );
            
            fogImage.setColor(circleColor);
            fogImage.fillOval(
                centerX - radius,
                centerY - radius,
                radius * 2,
                radius * 2
            );
        }
        
        // Step 3: Draw clear center (fully transparent)
        fogImage.setColor(new Color(0, 0, 0, 0));
        fogImage.fillOval(
            centerX - CLEAR_RADIUS,
            centerY - CLEAR_RADIUS,
            CLEAR_RADIUS * 2,
            CLEAR_RADIUS * 2
        );
        
        System.out.println("Fog created!");
    }
    /**
     * keeps fog centered around player
     */
    public void act() {
        // Keep fog centered on screen (player is always at screen center)
        setLocation(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
    }
}