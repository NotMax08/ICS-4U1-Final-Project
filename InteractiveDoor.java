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
    Player player;
    /**
     * constructor for doors that are opened via key f
     * 
     * @param camera camera for reference
     * @param width width of door
     * @param height height of door
     * @param id id to know which door to enter specific rooms
     */
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
    /**
     * handles input if player is tocuhing door 
     */
    public void act(){
        handleInput();
    }
    /**
     * checks for which tiles are set to interactive door
     */
    public boolean containsWorldPoint(int worldX, int worldY) {
        GameWorld world = (GameWorld) getWorld();
        int tileX = world.worldToTileX(worldX);
        int tileY = world.worldToTileY(worldY);
        
        int tileType = world.getMapGrid().getTileAt(tileX, tileY);
        return tileType == 5; // 5 = interactive door
    }
    /**
     * checks for touching door and spawn enter prompt
     */
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
            case "enterboss": return "MinibossRoom";
            case "npcenter": return "NPCRoom";
            case "bossroom": return "RoomOne";
            case "npcroom": return "RoomTwo";
            case "bossroomtwo": return "RoomTwo";
            case "roomthree" : return "RoomTwo";
            default: return "";
        }
    }
    
    /**
     * Transition to the appropriate room based on door ID
     */
    private void transitionToRoom(String doorId, String sourceRoom) {
        // Get player reference from world
        GameWorld world = (GameWorld) getWorld();
        if (world == null) return;
        
        Player player = world.getObjects(Player.class).isEmpty() ? null : world.getObjects(Player.class).get(0);
        if (player == null) return;
        switch(doorId) {
            case "roomone":
                if(player.getItemCount(3) == 1){
                    Greenfoot.setWorld(new BossRoom(sourceRoom));
                } else{
                    TimedMessage msg = new TimedMessage("You need a key to enter", 150);
                    world.addObject(msg, world.getWidth()/2, world.getHeight()/2);
                }
                break;
            case "backtormone":
                Greenfoot.setWorld(new RoomOne(sourceRoom));
                break;
            case "enterboss":
                Greenfoot.setWorld(new MinibossRoom(sourceRoom));
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
            case "roomthree":
                Greenfoot.setWorld(new RoomTwo(sourceRoom));
                break;
        }
    }
    /**
     * getters
     */
    public int getWidth() { 
        return width; 
    }
    
    public int getHeight() { 
        return height; 
    }
}