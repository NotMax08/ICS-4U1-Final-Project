import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
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
        
        // Check tile if is interactive door
        int tileType = world.getMapGrid().getTileAt(tileX, tileY);
        return tileType == 4; // 2 = interactive door
    }
    public void handleInput(){
        World world = getWorld();
        if (world == null) return;
        if (isTouching(Player.class)){
            if(id.equals("roomone")){
                if (messageActor == null) {
                    messageActor = new Message("entermsg.png",camera );
                    world.addObject(messageActor, 0,0);
                    messageActor.setWorldPosition(worldX, worldY - 80);
                }
                  if (Greenfoot.isKeyDown("f")) {
                    Greenfoot.setWorld(new BossRoomTwo());
                }
            } else if(id.equals("backtormone")){
                if (messageActor == null){
                    messageActor = new Message("entermsg.png", camera);
                    world.addObject(messageActor, 0,0);
                    messageActor.setWorldPosition(worldX, worldY - 80);
                }
                  if (Greenfoot.isKeyDown("f")) {
                    Greenfoot.setWorld(new RoomOne());
                }
            }else if(id.equals("enterboss")){
                if (messageActor == null) {
                    messageActor = new Message("entermsg.png", camera);
                    world.addObject(messageActor, 0,0);
                    messageActor.setWorldPosition(worldX, worldY - 80);
                }
                  if (Greenfoot.isKeyDown("f")) {
                    Greenfoot.setWorld(new BossRoom());
                }
            }else if(id.equals("npcenter")){
                if (messageActor == null) {
                    messageActor = new Message("entermsg.png", camera);
                    world.addObject(messageActor, 0, 0);
                    messageActor.setWorldPosition(worldX, worldY - 80);
                }
                if (Greenfoot.isKeyDown("f")) {
                    Greenfoot.setWorld(new NPCRoom());
                }
            } else if (id.equals("bossroom")){
                if (messageActor == null) {
                    messageActor = new Message("entermsg.png", camera);
                    world.addObject(messageActor, 0,0);
                    messageActor.setWorldPosition(worldX, worldY - 80);
                }
                if (Greenfoot.isKeyDown("f")) {
                    Greenfoot.setWorld(new RoomTwo());
                }
            }else if (id.equals("npcroom")){
                if (messageActor == null) {
                    messageActor = new Message("entermsg.png", camera);
                    world.addObject(messageActor, 0,0);
                    messageActor.setWorldPosition(worldX, worldY - 80);
                }
                if (Greenfoot.isKeyDown("f")) {
                    Greenfoot.setWorld(new RoomTwo());
                }
            }else if (id.equals("bossroomtwo")){
                if (messageActor == null) {
                    messageActor = new Message("entermsg.png", camera);
                    world.addObject(messageActor, 0,0);
                    messageActor.setWorldPosition(worldX, worldY - 80);
                }
                if (Greenfoot.isKeyDown("f")) {
                    Greenfoot.setWorld(new RoomOne());
                }
            }
            
            
        }else {
            if (messageActor != null) {
                world.removeObject(messageActor);
                messageActor = null;
            }
        }
    }
    public int getWidth() { 
        return width; 
    }
    public int getHeight() { 
        return height; 
    }
}



