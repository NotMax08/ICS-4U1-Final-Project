import greenfoot.*;

/**
 * Debug utility to test and visualize spawn positions.
 * Add this to a room temporarily to verify all door spawn positions are safe.
 * 
 * Usage:
 * 1. Add to room constructor: addObject(new SpawnPositionTester(this), 0, 0);
 * 2. Check console output for spawn position safety
 * 3. Press 'T' in-game to visualize spawn markers
 * 
 * not perfectly functional
 * 
 * @author Paul assisted by Claude
 */
public class SpawnPositionTester extends Actor {
    private GameWorld world;
    private boolean showMarkers = false;
    private java.util.List<SpawnMarker> markers;
    /**
     * constuctor for debug class
     * @param world world for reference
     */
    public SpawnPositionTester(GameWorld world) {
        this.world = world;
        this.markers = new java.util.ArrayList<>();
        
        // Make invisible
        setImage(new GreenfootImage(1, 1));
        
        // Test all spawn positions for this room
        testSpawnPositions();
    }
    /**
     * checks for key input to use
     */
    public void act() {
        if (Greenfoot.isKeyDown("y")) {
            toggleMarkers();
            Greenfoot.delay(10); // Debounce
        }
    }
    
    private void testSpawnPositions() {
        String roomName = world.getClass().getSimpleName();
        RoomPositionTracker tracker = RoomPositionTracker.getInstance();
        MapGrid mapGrid = world.getMapGrid();
        int tileSize = world.TILE_SIZE;
        
        System.out.println("\n=== TESTING SPAWN POSITIONS FOR " + roomName + " ===");
        
        // Test all possible entry points to this room
        String[] sourceRooms = {"RoomOne", "RoomTwo", "BossRoom", "BossRoomTwo", "NPCRoom"};
        
        for (String sourceRoom : sourceRooms) {
            RoomPositionTracker.SpawnPosition spawn = tracker.getSpawnPosition(sourceRoom, roomName);
            
            if (spawn != null) {
                boolean isSafe = RoomPositionTracker.isSpawnPositionSafe(mapGrid, spawn.x, spawn.y, tileSize);
                String debugInfo = RoomPositionTracker.getSpawnDebugInfo(mapGrid, spawn.x, spawn.y, tileSize);
                
                String status = isSafe ? "✓ SAFE" : "✗ UNSAFE";
                System.out.println(String.format("  %s <- %s: %s | %s", 
                    roomName, sourceRoom, status, debugInfo));
                
                // Store for visualization
                if (!isSafe) {
                    System.out.println("    WARNING: Player will spawn inside obstacle!");
                }
            }
        }
        
        System.out.println("=== END SPAWN POSITION TEST ===\n");
    }
    
    private void toggleMarkers() {
        showMarkers = !showMarkers;
        
        if (showMarkers) {
            createMarkers();
            System.out.println("Spawn markers visible (green = safe, red = unsafe)");
        } else {
            removeMarkers();
            System.out.println("Spawn markers hidden");
        }
    }
    
    private void createMarkers() {
        String roomName = world.getClass().getSimpleName();
        RoomPositionTracker tracker = RoomPositionTracker.getInstance();
        MapGrid mapGrid = world.getMapGrid();
        int tileSize = world.TILE_SIZE;
        
        String[] sourceRooms = {"RoomOne", "RoomTwo", "BossRoom", "BossRoomTwo", "NPCRoom"};
        
        for (String sourceRoom : sourceRooms) {
            RoomPositionTracker.SpawnPosition spawn = tracker.getSpawnPosition(sourceRoom, roomName);
            
            if (spawn != null) {
                boolean isSafe = RoomPositionTracker.isSpawnPositionSafe(mapGrid, spawn.x, spawn.y, tileSize);
                
                SpawnMarker marker = new SpawnMarker(world.getCamera(), isSafe, sourceRoom);
                world.addObject(marker, 0, 0);
                marker.setWorldPosition(spawn.x, spawn.y);
                markers.add(marker);
            }
        }
    }
    
    private void removeMarkers() {
        for (SpawnMarker marker : markers) {
            world.removeObject(marker);
        }
        markers.clear();
    }
}

/**
 * Visual marker for spawn positions
 */
class SpawnMarker extends ScrollingActor {
    /**
     * constructor for marker
     */
    public SpawnMarker(Camera camera, boolean isSafe, String sourceRoom) {
        super(camera);
        
        // Create marker image
        GreenfootImage img = new GreenfootImage(40, 60);
        
        // Draw colored rectangle
        Color color = isSafe ? new Color(0, 255, 0, 150) : new Color(255, 0, 0, 150);
        img.setColor(color);
        img.fillRect(0, 0, 40, 60);
        
        // Draw border
        img.setColor(Color.BLACK);
        img.drawRect(0, 0, 39, 59);
        
        // Draw label
        img.setColor(Color.WHITE);
        img.setFont(new Font(8));
        img.drawString(sourceRoom.substring(0, Math.min(4, sourceRoom.length())), 5, 15);
        img.drawString(isSafe ? "SAFE" : "UNSAFE", 3, 30);
        
        setImage(img);
    }
    /**
     * updates screen position with camera
     */
    public void act() {
        updateScreenPosition();
    }
}