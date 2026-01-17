import greenfoot.*;
import java.util.HashSet;

/**
 * A display that shows the game map, revealing rooms as the player visits them
 * Press 'M' to toggle the map display
 * 
 * @author Claude
 */
public class MapDisplay extends Display {
    // Map images
    private GreenfootImage fullMap;
    private GreenfootImage mapOverlay;
    private GreenfootImage closedMapIcon;
    
    // Map state
    private boolean isOpen = false;
    private boolean mWasDown = false;
    private HashSet<String> visitedRooms;
    
    // Map dimensions
    private static final int MAP_WIDTH = 500;
    private static final int MAP_HEIGHT = 400;
    private static final int ICON_SIZE = 60;
    
    // Room definitions (positions on the map image)
    private RoomData[] rooms;
    
    /**
     * Create map display at a fixed screen position
     */
    public MapDisplay(int screenX, int screenY, Camera camera) {
        super(screenX, screenY, camera);
        
        visitedRooms = new HashSet<String>();
        
        // Load and scale images
        fullMap = new GreenfootImage("fullMap.png");
        fullMap.scale(MAP_WIDTH, MAP_HEIGHT);
        
        closedMapIcon = new GreenfootImage("mapIcon.png");
        closedMapIcon.scale(ICON_SIZE, ICON_SIZE);
        
        // Initialize room positions and sizes on the map
        initializeRooms();
        
        setImage(closedMapIcon);
    }
    
    private void initializeRooms() {
        // Define each room's position and size on the map image
        // Adjust these coordinates to match your actual map layout
        rooms = new RoomData[] {
            new RoomData("RoomOne", 50, 180, 120, 100, RoomType.MAIN),
            new RoomData("RoomTwo", 200, 180, 120, 100, RoomType.MAIN),
            new RoomData("RoomThree", 350, 180, 120, 100, RoomType.MAIN),
            new RoomData("BossRoomOne", 125, 50, 100, 100, RoomType.BOSS),
            new RoomData("BossRoomTwo", 275, 50, 100, 100, RoomType.BOSS),
            new RoomData("NPCRoom", 200, 300, 100, 80, RoomType.NPC)
        };
    }
    
    @Override
    protected void updateDisplay() {
        if (!isOpen) {
            setLocation(screenX, screenY);
            setImage(closedMapIcon);
        } else {
            setLocation(screenX + 300, screenY - 200);
            
            // Create the revealed map
            GreenfootImage canvas = createRevealedMap();
            setImage(canvas);
        }
    }
    
    /**
     * Creates the map image with only visited rooms visible
     */
    private GreenfootImage createRevealedMap() {
        // Start with a dark/black background
        GreenfootImage canvas = new GreenfootImage(MAP_WIDTH, MAP_HEIGHT);
        canvas.setColor(new Color(20, 20, 30));
        canvas.fill();
        
        // Draw border (draw multiple rectangles for thickness)
        canvas.setColor(new Color(139, 69, 19)); // Brown border
        for (int i = 0; i < 4; i++) {
            canvas.drawRect(i, i, MAP_WIDTH - 1 - (i * 2), MAP_HEIGHT - 1 - (i * 2));
        }
        
        // Draw title
        canvas.setColor(Color.WHITE);
        canvas.setFont(new Font("Serif", true, false, 24));
        canvas.drawString("Map", 20, 30);
        
        // Create overlay mask showing only visited rooms
        mapOverlay = new GreenfootImage(fullMap);
        
        // Darken the entire map first
        mapOverlay.setColor(new Color(0, 0, 0, 230)); // Almost black overlay
        mapOverlay.fill();
        
        // Reveal visited rooms by clearing the overlay in those areas
        for (RoomData room : rooms) {
            if (visitedRooms.contains(room.name)) {
                revealRoom(mapOverlay, room);
            }
        }
        
        // Draw the full map with overlay
        canvas.drawImage(fullMap, 0, 0);
        canvas.drawImage(mapOverlay, 0, 0);
        
        // Draw room labels and icons for visited rooms
        for (RoomData room : rooms) {
            if (visitedRooms.contains(room.name)) {
                drawRoomLabel(canvas, room);
            }
        }
        
        // Draw player position if in a visited room
        drawPlayerPosition(canvas);
        
        return canvas;
    }
    
    /**
     * Reveals a room by clearing the dark overlay in that area
     */
    private void revealRoom(GreenfootImage overlay, RoomData room) {
        // Clear the overlay in this room's area to reveal the map beneath
        overlay.setColor(new Color(0, 0, 0, 0)); // Transparent
        overlay.fillRect(room.x, room.y, room.width, room.height);
        
        // Add a slight glow effect around revealed rooms
        overlay.setColor(new Color(255, 255, 200, 30)); // Soft yellow glow
        for (int i = 0; i < 3; i++) {
            overlay.drawRect(room.x - 2 - i, room.y - 2 - i, room.width + 4 + (i * 2), room.height + 4 + (i * 2));
        }
    }
    
    /**
     * Draws labels and icons for visited rooms
     */
    private void drawRoomLabel(GreenfootImage canvas, RoomData room) {
        int centerX = room.x + room.width / 2;
        int centerY = room.y + room.height / 2;
        
        // Draw room type icon
        canvas.setColor(getRoomColor(room.type));
        int iconSize = 12;
        
        switch (room.type) {
            case BOSS:
                // Draw skull icon for boss rooms
                canvas.fillOval(centerX - iconSize/2, centerY - iconSize/2, iconSize, iconSize);
                canvas.setColor(Color.RED);
                for (int i = 0; i < 2; i++) {
                    canvas.drawOval(centerX - iconSize/2 - i, centerY - iconSize/2 - i, iconSize + (i * 2), iconSize + (i * 2));
                }
                break;
            case NPC:
                // Draw person icon for NPC room
                canvas.fillOval(centerX - iconSize/2, centerY - iconSize/2, iconSize, iconSize);
                canvas.setColor(Color.GREEN);
                for (int i = 0; i < 2; i++) {
                    canvas.drawOval(centerX - iconSize/2 - i, centerY - iconSize/2 - i, iconSize + (i * 2), iconSize + (i * 2));
                }
                break;
            case MAIN:
                // Draw square for main rooms
                canvas.fillRect(centerX - iconSize/2, centerY - iconSize/2, iconSize, iconSize);
                break;
        }
        
        // Draw room name
        canvas.setColor(Color.WHITE);
        canvas.setFont(new Font("Arial", false, false, 10));
        String displayName = getDisplayName(room.name);
        int textWidth = displayName.length() * 5; // Approximate width
        canvas.drawString(displayName, centerX - textWidth/2, room.y + room.height + 15);
    }
    
    /**
     * Draws the player's current position on the map
     */
    private void drawPlayerPosition(GreenfootImage canvas) {
        GameWorld world = (GameWorld) getWorld();
        if (world == null) return;
        
        String currentRoom = world.getClass().getSimpleName();
        
        for (RoomData room : rooms) {
            if (room.name.equals(currentRoom)) {
                int centerX = room.x + room.width / 2;
                int centerY = room.y + room.height / 2;
                
                // Draw pulsing player indicator
                canvas.setColor(new Color(0, 255, 255)); // Cyan
                canvas.fillOval(centerX - 6, centerY - 6, 12, 12);
                canvas.setColor(Color.WHITE);
                for (int i = 0; i < 2; i++) {
                    canvas.drawOval(centerX - 6 - i, centerY - 6 - i, 12 + (i * 2), 12 + (i * 2));
                }
                break;
            }
        }
    }
    
    /**
     * Gets the color associated with a room type
     */
    private Color getRoomColor(RoomType type) {
        switch (type) {
            case BOSS: return new Color(180, 0, 0);
            case NPC: return new Color(0, 180, 0);
            case MAIN: return new Color(100, 100, 180);
            default: return Color.GRAY;
        }
    }
    
    /**
     * Gets a user-friendly display name for a room
     */
    private String getDisplayName(String roomName) {
        switch (roomName) {
            case "RoomOne": return "Entry";
            case "RoomTwo": return "Hall";
            case "RoomThree": return "Chamber";
            case "BossRoomOne": return "Boss 1";
            case "BossRoomTwo": return "Boss 2";
            case "NPCRoom": return "Shop";
            default: return roomName;
        }
    }
    
    @Override
    public void act() {
        checkMapKey();
        checkMouseClick();
        updateCurrentRoom();
        updateDisplay();
    }
    
    /**
     * Toggle map with 'M' key
     */
    private void checkMapKey() {
        boolean mIsDown = Greenfoot.isKeyDown("m");
        
        if (mIsDown && !mWasDown) {
            isOpen = !isOpen;
        }
        
        mWasDown = mIsDown;
    }
    
    /**
     * Toggle map with mouse click
     */
    private void checkMouseClick() {
        boolean mouseClickedMap = Greenfoot.mousePressed(this);
        boolean mouseIsDown = Greenfoot.mousePressed(null);
        
        if (!isOpen) {
            if (mouseClickedMap) {
                isOpen = true;
            }
        } else {
            if (!mouseClickedMap && mouseIsDown) {
                isOpen = false;
            }
        }
    }
    
    /**
     * Automatically mark the current room as visited
     */
    private void updateCurrentRoom() {
        GameWorld world = (GameWorld) getWorld();
        if (world == null) return;
        
        String currentRoom = world.getClass().getSimpleName();
        visitedRooms.add(currentRoom);
    }
    
    /**
     * Manually mark a room as visited (useful for connected rooms)
     */
    public void markRoomVisited(String roomName) {
        visitedRooms.add(roomName);
    }
    
    /**
     * Check if a room has been visited
     */
    public boolean hasVisitedRoom(String roomName) {
        return visitedRooms.contains(roomName);
    }
    
    /**
     * Get the number of rooms visited
     */
    public int getVisitedRoomCount() {
        return visitedRooms.size();
    }
    
    /**
     * Reset the map (clear all visited rooms)
     */
    public void resetMap() {
        visitedRooms.clear();
    }
}

/**
 * Data structure to hold room information
 */
class RoomData {
    String name;
    int x, y;           // Position on map image
    int width, height;  // Size on map image
    RoomType type;
    
    public RoomData(String name, int x, int y, int width, int height, RoomType type) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
    }
}

/**
 * Enum for different room types
 */
enum RoomType {
    MAIN,    // Regular exploration rooms
    BOSS,    // Boss battle rooms
    NPC      // NPC/Shop rooms
}