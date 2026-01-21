import greenfoot.*;

/**
 * DeepnestFog - Creates an oppressive, drifting fog effect
 * inspired by Hollow Knight's Deepnest area.
 * 
 * This fog obscures visibility, drifts irregularly, and creates
 * a claustrophobic, hostile atmosphere.
 */
public class DeepnestFog extends Actor
{
    // Movement properties
    private double driftSpeed;
    private double verticalOffset;
    private double waveSpeed;
    private double wavePhase;
    
    // Visual properties
    private int opacity;
    private int fadeDirection;
    private int baseOpacity;
    private Color fogColor;
    
    // Size and depth
    private int fogWidth;
    private int fogHeight;
    private double depthLayer; // 0.0 to 1.0, affects speed and darkness
    
    // Image cache
    private GreenfootImage fogImage;
    
    /**
     * Constructor - creates a fog particle with random properties
     */
    public DeepnestFog()
    {
        this(Greenfoot.getRandomNumber(100) / 100.0);
    }
    
    /**
     * Constructor with depth parameter
     * @param depth - 0.0 (background) to 1.0 (foreground)
     */
    public DeepnestFog(double depth)
    {
        this.depthLayer = depth;
        
        // Size varies with depth - foreground fog is larger
        fogWidth = 80 + (int)(depth * 120) + Greenfoot.getRandomNumber(60);
        fogHeight = 60 + (int)(depth * 80) + Greenfoot.getRandomNumber(40);
        
        // Speed varies with depth - foreground moves faster (parallax)
        driftSpeed = 0.1 + (depth * 0.4) + (Greenfoot.getRandomNumber(20) / 100.0);
        
        // Vertical wavering
        waveSpeed = 0.02 + (Greenfoot.getRandomNumber(15) / 1000.0);
        wavePhase = Greenfoot.getRandomNumber(360);
        verticalOffset = 0;
        
        // Opacity setup - make more visible for testing
        baseOpacity = 150 + (int)(depth * 60) + Greenfoot.getRandomNumber(30);
        opacity = baseOpacity;
        fadeDirection = Greenfoot.getRandomNumber(2) == 0 ? 1 : -1;
        
        // Color varies - lighter for better visibility
        int colorChoice = Greenfoot.getRandomNumber(3);
        switch(colorChoice) {
            case 0: // Dull gray
                fogColor = new Color(100, 100, 105);
                break;
            case 1: // Sickly brown
                fogColor = new Color(95, 85, 75);
                break;
            case 2: // Dark charcoal
                fogColor = new Color(85, 85, 88);
                break;
            default:
                fogColor = new Color(95, 95, 98);
        }
        
        // Create the fog image
        createFogImage();
    }
    
    /**
     * Creates an irregular, organic fog particle image
     */
    private void createFogImage()
    {
        fogImage = new GreenfootImage(fogWidth, fogHeight);
        
        // Use radial gradient effect for soft, organic edges
        for (int x = 0; x < fogWidth; x++) {
            for (int y = 0; y < fogHeight; y++) {
                // Calculate distance from center (irregular)
                double dx = (x - fogWidth/2.0) / (fogWidth/2.0);
                double dy = (y - fogHeight/2.0) / (fogHeight/2.0);
                
                // Add irregularity to the shape
                double noise = Math.sin(x * 0.1) * Math.cos(y * 0.15) * 0.3;
                double dist = Math.sqrt(dx*dx + dy*dy) + noise;
                
                // Soft falloff
                if (dist < 1.0) {
                    int alpha = (int)((1.0 - dist) * opacity);
                    alpha = Math.max(0, Math.min(255, alpha));
                    
                    // Create Greenfoot Color with alpha
                    Color pixelColor = new Color(
                        fogColor.getRed(),
                        fogColor.getGreen(),
                        fogColor.getBlue(),
                        alpha
                    );
                    fogImage.setColorAt(x, y, pixelColor);
                }
            }
        }
        
        setImage(fogImage);
    }
    
    /**
     * Main act method - handles all fog behavior
     */
    public void act()
    {
        drift();
        waver();
        fadeInOut();
        updateImage();
        checkEdge();
    }
    
    /**
     * Slow, irregular horizontal drift
     */
    private void drift()
    {
        // Add slight randomness to drift
        double irregularity = (Greenfoot.getRandomNumber(20) - 10) / 100.0;
        setLocation(getX() + (int)(driftSpeed + irregularity), getY() + (int)verticalOffset);
    }
    
    /**
     * Subtle vertical wavering motion
     */
    private void waver()
    {
        wavePhase += waveSpeed;
        verticalOffset = Math.sin(Math.toRadians(wavePhase)) * 0.5;
    }
    
    /**
     * Gradual opacity variation for breathing effect
     */
    private void fadeInOut()
    {
        // Slowly fade in and out
        opacity += fadeDirection;
        
        // Reverse direction at limits
        if (opacity >= baseOpacity + 30) {
            fadeDirection = -1;
        } else if (opacity <= baseOpacity - 30) {
            fadeDirection = 1;
        }
        
        // Clamp opacity
        opacity = Math.max(50, Math.min(200, opacity));
    }
    
    /**
     * Update the fog image with new opacity
     */
    private void updateImage()
    {
        // Recreate image only occasionally for performance
        if (Greenfoot.getRandomNumber(30) == 0) {
            createFogImage();
        }
    }
    
    /**
     * Respawn on opposite side when leaving screen
     */
    private void checkEdge()
    {
        World world = getWorld();
        if (world == null) return;
        
        // If drifted off right edge, respawn on left
        if (getX() > world.getWidth() + fogWidth/2) {
            setLocation(-fogWidth/2, Greenfoot.getRandomNumber(world.getHeight()));
            
            // Randomize properties slightly on respawn
            wavePhase = Greenfoot.getRandomNumber(360);
            baseOpacity = 120 + (int)(depthLayer * 40) + Greenfoot.getRandomNumber(30);
            opacity = baseOpacity;
        }
    }
    
    /**
     * Get the depth layer of this fog particle
     */
    public double getDepth()
    {
        return depthLayer;
    }
}