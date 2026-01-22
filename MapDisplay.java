import greenfoot.*;

/**
 * A display that shows the game map with room layouts
 * 
 * Art by ChatGPT
 * 
 * @author Paul and Claude
 */
public class MapDisplay extends Display {
    // Image variables
    private GreenfootImage mapImage = new GreenfootImage("fullMap.png");
    private GreenfootImage closedMap = new GreenfootImage("mapIcon.png");

    private boolean isOpen = false;
    private boolean mWasDown = false;

    /**
     * Create map display at a fixed screen position
     * 
     * @param screenX X position on screen (not world position)
     * @param screenY Y position on screen (not world position)
     * @param camera Camera reference for scrolling
     */
    public MapDisplay(int screenX, int screenY, Camera camera) {
        super(screenX, screenY, camera);

        // Scale the closed map icon (small icon in corner)
        closedMap = scaleImage("mapIcon.png", 80, 80, 225);
        
        // Scale the full map (when opened)
        mapImage = scaleImage("fullMap.png", 700, 500, 255);

        setImage(closedMap);
    }

    @Override
    protected void updateDisplay(){
        if(!isOpen){
            // Show small icon at fixed screen position
            setLocation(screenX - 60, screenY + 380);
            setImage(closedMap);
        } else {
            // Position the map UI (similar to inventory positioning)
            int mapX = screenX + 300;
            int mapY = screenY + 250;
            setLocation(mapX, mapY);
            setImage(mapImage);
        }
    }

    @Override
    public void act(){
        checkMapPress();
        updateDisplay();
    }

    /**
     * Toggle map open/closed with M key
     */
    private void checkMapPress(){
        boolean mIsDown = Greenfoot.isKeyDown("m");
        if (mIsDown && !mWasDown) {
            isOpen = !isOpen;
        }
        mWasDown = mIsDown;
    }

    /**
     * Check if the map is currently open
     */
    public boolean isMapOpen() {
        return isOpen;
    }

    /**
     * Force close the map
     */
    public void closeMap() {
        isOpen = false;
    }

    /**
     * Force open the map
     */
    public void openMap() {
        isOpen = true;
    }
}