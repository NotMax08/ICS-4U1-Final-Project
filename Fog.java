import greenfoot.*;

/**
 * Simple, performant fog of war with clear vision radius
 * 0-200px: Perfectly clear
 * 200-300px: Translucent gradient
 * 300+px: Completely opaque fog
 * Intended to reduce vision and make traversal more difficult
 * 
 * @author Paul and Claude
 */
public class Fog extends Actor {
    // Vision settings
    private static final int CLEAR_RADIUS = 200;        // Perfectly clear vision
    private static final int GRADIENT_RADIUS = 300;     // End of gradient (CLEAR + 100)
    
    // Fog appearance
    private static final Color FOG_COLOR = new Color(70, 75, 80);
    private static final int FULL_OPACITY = 255;        // Completely opaque
    
    // Canvas
    private static final int CANVAS_WIDTH = 2500;
    private static final int CANVAS_HEIGHT = 1400;
    
    // Pre-rendered fog (only create once)
    private static GreenfootImage baseFog;
    
    // Animation layers (very simple)
    private double layer1Offset = 0;
    private double layer2Offset = 0;
    private int animCounter = 0;
    
    Player player;
    
    public Fog(Player player) {
        this.player = player;
        
        //createFogImage();
    }
    
    /**
     * Creates the base fog with radial gradient (ONE TIME ONLY)
     */
    private void createBaseFog() {
        baseFog = new GreenfootImage(CANVAS_WIDTH, CANVAS_HEIGHT);
        
        int centerX = CANVAS_WIDTH / 2;
        int centerY = CANVAS_HEIGHT / 2;
        
        // Step 1: Fill entire canvas with OPAQUE fog
        baseFog.setColor(new Color(FOG_COLOR.getRed(), FOG_COLOR.getGreen(), FOG_COLOR.getBlue(), FULL_OPACITY));
        baseFog.fill();
        
        // Step 2: Draw gradient circles from outside to inside
        // This creates smooth transition from opaque → translucent → clear
        int steps = 50; // Number of gradient steps
        for (int i = steps; i >= 0; i--) {
            double progress = (double) i / steps;
            int radius = CLEAR_RADIUS + (int)(progress * (GRADIENT_RADIUS - CLEAR_RADIUS));
            
            // Calculate opacity: 255 at GRADIENT_RADIUS, 0 at CLEAR_RADIUS
            int opacity = (int)(progress * FULL_OPACITY);
            
            baseFog.setColor(new Color(FOG_COLOR.getRed(), FOG_COLOR.getGreen(), FOG_COLOR.getBlue(), opacity));
            baseFog.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        }
        
        // Step 3: Draw perfectly clear center circle
        baseFog.setColor(new Color(0, 0, 0, 0)); // Fully transparent
        baseFog.fillOval(
            centerX - CLEAR_RADIUS, 
            centerY - CLEAR_RADIUS, 
            CLEAR_RADIUS * 2, 
            CLEAR_RADIUS * 2
        );
    }
    
    /**
    protected void updateDisplay() {
        // Keep centered on screen
        setLocation(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);        
        // Very minimal animation
        animCounter++;
        if (animCounter % 5 == 0) { // Only update every 5 frames
            layer1Offset += 0.3;
            layer2Offset -= 0.2;
            
            if (layer1Offset > 20) layer1Offset = -20;
            if (layer2Offset < -20) layer2Offset = 20;
        }
        
        render();
    }
    */
    /**
     * Render the fog (very simple - just copy base and add subtle animation)
     */
    private void render() {
        // Copy the base fog
        GreenfootImage canvas = new GreenfootImage(baseFog);
        
        // Add VERY subtle animated wisps (optional - comment out if still laggy)
        addSubtleWisps(canvas);
        
        setImage(canvas);
    }
    
    /**
     * Adds very simple animated wisps (minimal performance impact)
     */
    private void addSubtleWisps(GreenfootImage canvas) {
        int centerX = CANVAS_WIDTH / 2;
        int centerY = CANVAS_HEIGHT / 2;
        
        // Only draw wisps OUTSIDE the clear vision area
        // Draw 2 simple wisps that drift slowly
        
        // Wisp 1
        int wisp1X = centerX + 350 + (int)layer1Offset;
        int wisp1Y = centerY - 150;
        canvas.setColor(new Color(60, 65, 70, 40));
        canvas.fillOval(wisp1X - 40, wisp1Y - 30, 80, 60);
        
        // Wisp 2
        int wisp2X = centerX - 400 + (int)layer2Offset;
        int wisp2Y = centerY + 200;
        canvas.setColor(new Color(60, 65, 70, 35));
        canvas.fillOval(wisp2X - 50, wisp2Y - 40, 100, 80);
    }
}