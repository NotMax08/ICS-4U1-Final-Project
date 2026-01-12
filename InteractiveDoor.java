import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * @author Paul
 */
class InteractiveDoor extends ScrollingActor {
    private int width, height;
    private String id;
    private GreenfootImage entermsg = new GreenfootImage("entermsg.png");
    private boolean showingMessage = false;
    public InteractiveDoor(Camera camera, int width, int height, String id) {
        super(camera);
        this.width = width;
        this.height = height;
        this.id = id;
        
        GreenfootImage img = new GreenfootImage(width, height);
        img.setColor(Color.RED);
        img.fillRect(0, 0, width, height);
        setImage(img);
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
                if (world != null) {
                    
                    GreenfootImage bg = world.getBackground();
                    GreenfootImage msgImage = new GreenfootImage("entermsg.png");
                    bg.drawImage(msgImage, getX() - msgImage.getWidth()/2, getY() - 50);
                    showingMessage = true;
                }
                  if (Greenfoot.isKeyDown("f")) {
                    Greenfoot.setWorld(new RoomTwo());
                }
            } else if(id.equals("backtormone")){
                if (world != null) {
                    
                    GreenfootImage bg = world.getBackground();
                    GreenfootImage msgImage = new GreenfootImage("entermsg.png");
                    bg.drawImage(msgImage, getX() - msgImage.getWidth()/2, getY() - 50);
                    showingMessage = true;
                }
                  if (Greenfoot.isKeyDown("f")) {
                    Greenfoot.setWorld(new RoomOne());
                }
            }else if(id.equals("bossroom")){
                if (world != null) {
                    
                    GreenfootImage bg = world.getBackground();
                    GreenfootImage msgImage = new GreenfootImage("entermsg.png");
                    bg.drawImage(msgImage, getX() - msgImage.getWidth()/2, getY() - 50);
                    showingMessage = true;
                }
                  if (Greenfoot.isKeyDown("f")) {
                    Greenfoot.setWorld(new BossRoom());
                }
            }else if(id.equals("backtormone")){
                if (world != null) {
                    
                    GreenfootImage bg = world.getBackground();
                    GreenfootImage msgImage = new GreenfootImage("entermsg.png");
                    bg.drawImage(msgImage, getX() - msgImage.getWidth()/2, getY() - 50);
                    showingMessage = true;
                }
                  if (Greenfoot.isKeyDown("f")) {
                    Greenfoot.setWorld(new NPCRoom());
                }
            }
            
            
        }else {
            if (showingMessage) {
                showingMessage = false;
                world.setBackground("roomtwo.jpg");
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



