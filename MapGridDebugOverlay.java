import greenfoot.*;

/**
 * Debug-only overlay that renders the MapGrid visually.
 * Draws translucent tiles, grid lines, and tile coordinates.
 * 
 * @author Paul assisted by Claude
 */
public class MapGridDebugOverlay extends ScrollingActor {
    private GameWorld world;
    private MapGrid mapGrid;
    private int displayMode = 0; // 0=off, 1=show X, 2=show Y
    private int tileSize;
    private int tilesWide;
    private int tilesHigh;
    
    public MapGridDebugOverlay(GameWorld world, MapGrid mapGrid) {
        super(world.camera);
        this.world = world;
        this.mapGrid = mapGrid;
        this.tileSize = world.TILE_SIZE;
        this.tilesWide = mapGrid.getTilesWide();
        this.tilesHigh = mapGrid.getTilesHigh();
        
        GreenfootImage img = new GreenfootImage(
            tilesWide * tileSize,
            tilesHigh * tileSize
        );
        setImage(img);
        redraw();
    }
    
    public void act() {
        // Update screen position based on camera
        updateScreenPosition();
    }
    
    /** Toggle grid visibility through modes: off -> X coords -> Y coords -> off */
    public void toggle() {
        displayMode = (displayMode + 1) % 3;
        redraw();
        getImage().setTransparency(displayMode == 0 ? 0 : 255);
    }
    
    /** Force enable with X coordinates */
    public void enable() {
        displayMode = 1;
        redraw();
        getImage().setTransparency(255);
    }
    
    /** Force disable */
    public void disable() {
        displayMode = 0;
        getImage().setTransparency(0);
    }
    
    /** Draw entire grid once */
    private void redraw() {
        GreenfootImage img = getImage();
        img.clear();
        
        // --- Draw tiles ---
        for (int x = 0; x < tilesWide; x++) {
            for (int y = 0; y < tilesHigh; y++) {
                int tileType = mapGrid.getTileAt(x, y);
                if (tileType != 0) {
                    img.setColor(getTileColor(tileType));
                    img.fillRect(
                        x * tileSize,
                        y * tileSize,
                        tileSize,
                        tileSize
                    );
                }
                
                // Draw coordinates (white text)
                if (displayMode > 0) {
                    img.setColor(Color.WHITE);
                    img.setFont(new Font(10));
                    String coord = (displayMode == 1) ? String.valueOf(x) : String.valueOf(y);
                    img.drawString(
                        coord,
                        x * tileSize + 3,
                        y * tileSize + 12
                    );
                }
            }
        }
        
        // --- Draw grid lines ---
        img.setColor(new Color(0, 0, 0, 100));
        for (int x = 0; x <= tilesWide; x++) {
            img.drawLine(
                x * tileSize, 0,
                x * tileSize, tilesHigh * tileSize
            );
        }
        for (int y = 0; y <= tilesHigh; y++) {
            img.drawLine(
                0, y * tileSize,
                tilesWide * tileSize, y * tileSize
            );
        }
        
        // Start hidden
        img.setTransparency(0);
    }
    
    private Color getTileColor(int type) {
        switch (type) {
            case 1: return new Color(0, 0, 255, 80);     // Blue - Walls
            case 2: return new Color(255, 0, 0, 80);     // Red - Platforms
            case 3: return new Color(128, 0, 128, 80);   // Purple - Doors
            case 4: return new Color(0, 255, 0, 80);     // Green - Breakable
            case 5: return new Color(255, 165, 0, 80);   // Orange - Interactive Doors
            default: return null;
        }
    }
}