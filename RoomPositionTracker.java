import java.util.HashMap;

/**
 * Singleton class to track door entry/exit positions between rooms.
 * Stores which door was used to exit a room, so the player can spawn
 * at the corresponding entry door when returning.
 * 
 * @author Paul assisted by Claude
 */
public class RoomPositionTracker {
    private static RoomPositionTracker instance = null;
    
    // Maps: "FromRoom->ToRoom" -> door coordinates in FromRoom
    private HashMap<String, DoorEntry> doorEntries;
    
    private RoomPositionTracker() {
        doorEntries = new HashMap<>();
        initializeDoorMappings();
    }
    
    public static RoomPositionTracker getInstance() {
        if (instance == null) {
            instance = new RoomPositionTracker();
        }
        return instance;
    }
    
    /**
     * Initialize all door positions for each room transition.
     * 
     * For SCROLLING rooms (RoomOne, RoomTwo):
     * - Offsets carefully avoid walls, platforms, and door tiles
     * - Horizontal doors: ±200 horizontal offset
     * - Vertical doors: -200 vertical offset
     * 
     * For STATIC rooms (BossRoom, BossRoomTwo, NPCRoom):
     * - Spawn at (150, 500) - safe position on platform, away from regular door tile at (0,15)
     * - Interactive doors are at (50, 500) - safe to spawn near them
     * - Regular door tiles MUST be avoided to prevent auto-return
     */
    private void initializeDoorMappings() {
        // === ROOM ONE CONNECTIONS (Scrolling) ===
        
        // RoomOne -> BossRoom (scrolling to static)
        // Spawn at safe position in BossRoom, away from regular door tile at (0, 15)
        doorEntries.put("RoomOne->BossRoom", new DoorEntry(150, 500, 0, 0));
        
        // BossRoom -> RoomOne (static to scrolling)
        // Return to RoomOne at interactive door (2090, 650) with offset to avoid door tiles
        doorEntries.put("BossRoom->RoomOne", new DoorEntry(2090, 650, -200, -100));
        
        // === ROOM TWO CONNECTIONS (Scrolling) ===
        
        // RoomTwo -> RoomOne (via "backtormone" door at 700, 1130)
        // Vertical door, spawn above it on platform
        doorEntries.put("RoomTwo->RoomOne", new DoorEntry(1350, 130, 0, 0));
        
        // RoomOne -> RoomTwo (reverse)
        doorEntries.put("RoomOne->RoomTwo", new DoorEntry(700, 1130, 0, -200));
        
        // RoomTwo -> NPCRoom (scrolling to static)
        // Spawn at safe position in NPCRoom, away from regular door tile
        doorEntries.put("RoomTwo->NPCRoom", new DoorEntry(150, 500, 0, 0));
        
        // NPCRoom -> RoomTwo (static to scrolling)
        // Return to RoomTwo at "npcenter" door (250, 820) with offset
        doorEntries.put("NPCRoom->RoomTwo", new DoorEntry(250, 820, 150, -100));
        
        // RoomTwo -> BossRoomTwo (scrolling to static)
        // Spawn at safe position in BossRoomTwo, away from regular door tile
        doorEntries.put("RoomTwo->BossRoomTwo", new DoorEntry(150, 500, 0, 0));
        
        // BossRoomTwo -> RoomTwo (static to scrolling)
        // Return to RoomTwo at "enterboss" door (2330, 810) with offset
        doorEntries.put("BossRoomTwo->RoomTwo", new DoorEntry(2330, 810, -200, -100));
    }
    
    /**
     * Get spawn position when entering targetRoom from sourceRoom
     * Returns null if no mapping exists (use default spawn)
     */
    public SpawnPosition getSpawnPosition(String sourceRoom, String targetRoom) {
        String key = sourceRoom + "->" + targetRoom;
        DoorEntry entry = doorEntries.get(key);
        
        if (entry != null) {
            return new SpawnPosition(
                entry.x + entry.offsetX,
                entry.y + entry.offsetY
            );
        }
        return null; // No mapping, use default
    }
    
    /**
     * Add or update a door mapping
     */
    public void setDoorMapping(String fromRoom, String toRoom, int doorX, int doorY, int offsetX, int offsetY) {
        String key = fromRoom + "->" + toRoom;
        doorEntries.put(key, new DoorEntry(doorX, doorY, offsetX, offsetY));
    }
    
    /**
     * Clear all mappings (useful for new game)
     */
    public void reset() {
        doorEntries.clear();
        initializeDoorMappings();
    }
    
    /**
     * Validate a spawn position against a MapGrid to ensure it's safe.
     * Checks if the position is not inside walls, platforms, or doors.
     * 
     * @param mapGrid The map grid to check against
     * @param worldX World X coordinate to check
     * @param worldY World Y coordinate to check
     * @param tileSize Size of each tile
     * @return true if position is safe (empty tile), false if blocked
     */
    public static boolean isSpawnPositionSafe(MapGrid mapGrid, int worldX, int worldY, int tileSize) {
        // Convert world coords to tile coords
        int tileX = worldX / tileSize;
        int tileY = worldY / tileSize;
        
        // Check the tile at spawn position
        int tileType = mapGrid.getTileAt(tileX, tileY);
        
        // 0 = empty/safe, anything else is blocked
        return tileType == 0;
    }
    
    /**
     * Get detailed debug info about a spawn position
     */
    public static String getSpawnDebugInfo(MapGrid mapGrid, int worldX, int worldY, int tileSize) {
        int tileX = worldX / tileSize;
        int tileY = worldY / tileSize;
        int tileType = mapGrid.getTileAt(tileX, tileY);
        
        String typeDesc;
        switch(tileType) {
            case 0: typeDesc = "SAFE (empty)"; break;
            case 1: typeDesc = "BLOCKED (wall)"; break;
            case 2: typeDesc = "BLOCKED (platform)"; break;
            case 3: typeDesc = "BLOCKED (door)"; break;
            case 4: typeDesc = "BLOCKED (breakable)"; break;
            case 5: typeDesc = "BLOCKED (interactive door)"; break;
            default: typeDesc = "UNKNOWN"; break;
        }
        
        return String.format("Spawn at world(%d,%d) = tile(%d,%d) = %s", 
                           worldX, worldY, tileX, tileY, typeDesc);
    }
    
    // Inner classes for data storage
    private static class DoorEntry {
        int x, y;           // Door position in world coordinates
        int offsetX, offsetY; // Offset from door to spawn player safely
        
        DoorEntry(int x, int y, int offsetX, int offsetY) {
            this.x = x;
            this.y = y;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }
    }
    
    public static class SpawnPosition {
        public final int x, y;
        
        SpawnPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}