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
    private boolean enabled = false; // DEBUG TOGGLE (false by default)
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
    
    /** Toggle grid visibility */
    public void toggle() {
        enabled = !enabled;
        getImage().setTransparency(enabled ? 255 : 0);
    }
    
    /** Force enable */
    public void enable() {
        enabled = true;
        getImage().setTransparency(255);
    }
    
    /** Force disable */
    public void disable() {
        enabled = false;
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
                
                // Draw coordinates (white text, only x)
                img.setColor(Color.WHITE);
                img.setFont(new Font(10));
                img.drawString(
                    String.valueOf(x),
                    x * tileSize + 3,
                    y * tileSize + 12
                );
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