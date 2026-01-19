import greenfoot.*;

/**
 * @author Paul
 */
class InteractiveDoor extends ScrollingActor {
    private int width, height;
    private String id;
    private GreenfootImage entermsg = new GreenfootImage("entermsg.png");
    private boolean showingMessage = false;
    private Message messageActor = null;
    private boolean debugVisuals;
    
    public InteractiveDoor(Camera camera, int width, int height, String id) {
        super(camera);
        this.width = width;
        this.height = height;
        this.id = id;
        
        debugVisuals = false;
        if(debugVisuals){
            GreenfootImage img = new GreenfootImage(width, height);
            img.setColor(Color.RED);
            img.fillRect(0, 0, width, height);
            setImage(img);
        } else {
            Color transparentColor = new Color(0, 0, 0, 0);
            GreenfootImage img = new GreenfootImage(width, height);
            img.setColor(transparentColor);
            img.fillRect(0,0, width, height);
            setImage(img);
        }
    }
    
    public void act(){
        handleInput();
    }
    
    public boolean containsWorldPoint(int worldX, int worldY) {
        GameWorld world = (GameWorld) getWorld();
        int tileX = world.worldToTileX(worldX);
        int tileY = world.worldToTileY(worldY);
        
        int tileType = world.getMapGrid().getTileAt(tileX, tileY);
        return tileType == 5; // 5 = interactive door
    }
    
    public void handleInput(){
        World world = getWorld();
        if (world == null) return;
        
        if (isTouching(Player.class)){
            // Show message
            if (messageActor == null) {
                messageActor = new Message("entermsg.png", camera);
                world.addObject(messageActor, 0, 0);
                messageActor.setWorldPosition(worldX, worldY - 80);
            }
            
            // Handle door transitions
            if (Greenfoot.isKeyDown("f")) {
                String currentRoom = getCurrentRoomName(world);
                
                // Transition to new room with source room info
                transitionToRoom(id, currentRoom);
            }
        } else {
            if (messageActor != null) {
                world.removeObject(messageActor);
                messageActor = null;
            }
        }
    }
    
    /**
     * Get the current room name based on world class
     */
    private String getCurrentRoomName(World world) {
        return world.getClass().getSimpleName();
    }
    
    /**
     * Get target room name from door ID
     */
    private String getTargetRoomName(String doorId) {
        switch(doorId) {
            case "roomone": return "BossRoom";
            case "backtormone": return "RoomOne";
            case "enterboss": return "BossRoomTwo";
            case "npcenter": return "NPCRoom";
            case "bossroom": return "RoomOne";
            case "npcroom": return "RoomTwo";
            case "bossroomtwo": return "RoomTwo";
            default: return "";
        }
    }
    
    /**
     * Transition to the appropriate room based on door ID
     */
    private void transitionToRoom(String doorId, String sourceRoom) {
        switch(doorId) {
            case "roomone":
                Greenfoot.setWorld(new BossRoom(sourceRoom));
                break;
            case "backtormone":
                Greenfoot.setWorld(new RoomOne(sourceRoom));
                break;
            case "enterboss":
                Greenfoot.setWorld(new BossRoomTwo(sourceRoom));
                break;
            case "npcenter":
                Greenfoot.setWorld(new NPCRoom(sourceRoom));
                break;
            case "bossroom":
                Greenfoot.setWorld(new RoomOne(sourceRoom));
                break;
            case "npcroom":
                Greenfoot.setWorld(new RoomTwo(sourceRoom));
                break;
            case "bossroomtwo":
                Greenfoot.setWorld(new RoomTwo(sourceRoom));
                break;
        }
    }
    
    public int getWidth() { 
        return width; 
    }
    
    public int getHeight() { 
        return height; 
    }
}