import greenfoot.*;

/**
 * Abstract base class for all game worlds/levels
 * Handles common functionality like camera, displays and player tracking
 * 
 * Sound credits:
 * "sword"  by @freesound_community on pixabay
 * "magic"  by @yodguard on pixabay
 * "heal"   by @yodguard on pixabay
 * "jump"   by @Jofae on pixabay
 * "hit"    by @mixkit
 * "damage" by @freesound_community on pixabay
 * 
 * @author Paul and Robin
 */
public abstract class GameWorld extends World {
    // Core game objects
    protected Camera camera;
    protected static Player player;
    public MapGrid mapGrid;

    // Display icons
    protected InventoryDisplay inventory;
    protected AbilityDisplay abilityDisplay;
    protected HealthDisplay healthDisplay;
    protected ManaDisplay manaDisplay;
    protected CurrencyDisplay currencyDisplay;

    // Sound Manager
    protected static SoundManager soundManager;
    protected static boolean soundsLoaded = false;

    // World image constants
    protected static final int WORLD_WIDTH = 2500;
    protected static final int WORLD_HEIGHT = 1420;
    protected static final int SCREEN_WIDTH = 800;
    protected static final int SCREEN_HEIGHT = 600;
    protected static final int TILE_SIZE = 20;
    protected static final int TILES_WIDE = WORLD_WIDTH / TILE_SIZE;
    protected static final int TILES_HIGH = WORLD_HEIGHT / TILE_SIZE;

    // Background
    protected GreenfootImage fullBackground;

    public GameWorld() {
        super(SCREEN_WIDTH, SCREEN_HEIGHT, 1, false);
        camera = new Camera(SCREEN_WIDTH, SCREEN_HEIGHT, WORLD_WIDTH, WORLD_HEIGHT);

        // Initalize sound manager and load sounds 
        initializeSounds();

        setPaintOrder();
    }

    protected void setPaintOrder(){
        setPaintOrder(
            TextBox.class,
            ShopItems.class,
            ShopIcons.class,
            ShopUI.class,
            MapGridDebugOverlay.class,
            InventoryDisplay.class,
            CurrencyDisplay.class,
            AbilityDisplay.class,
            HealthDisplay.class,
            ManaDisplay.class,
            SlashAnimation.class,
            Player.class
        );
    }

    /**
     * Initialize the sound manager and load all game sounds
     * Only loads sounds once, even if called multiple times
     */
    protected void initializeSounds(){
        if(!soundsLoaded){
            soundManager = SoundManager.getInstance();

            // Load player sounds
            soundManager.loadSound("jump", "jumpSound.wav");
            soundManager.loadSound("run", "runSound.wav");
            soundManager.loadSound("sword", "swordSound.wav");
            soundManager.loadSound("magic", "magicSound.wav");
            soundManager.loadSound("hit", "Slash.mp3");
            soundManager.loadSound("damage", "damageSound.wav");
            soundManager.loadSound("heal", "healSound.wav");
        }
    }

    /**
     * Main act loop - update camera, actors and background 
     */
    public void act() {
        if (camera != null && player != null) {
            camera.centerOn(player.getWorldX(), player.getWorldY());
            updateAllActors();
            updateBackground();
        }
    }

    /**
     * Initialize display elements
     * Can only be called after the player is created 
     */
    protected void initalizeDisplays(){
        if(player == null){
            throw new IllegalStateException("Player must be created before initializing displays");
        }

        // Create inventory icon
        inventory = new InventoryDisplay(60, SCREEN_HEIGHT - 60, camera, player);
        addObject(inventory, 0, 0);

        // Create ability icons
        abilityDisplay = new AbilityDisplay(SCREEN_WIDTH - 50, SCREEN_HEIGHT - 50, camera, player);
        addObject(abilityDisplay, 0, 0);

        // Create health icons
        healthDisplay = new HealthDisplay(310, 40, camera, player);
        addObject(healthDisplay, 0, 0);

        // Create mana bar
        manaDisplay = new ManaDisplay(150, 100, camera, player);
        addObject(manaDisplay, 0, 0);

        //Creates currency display
        currencyDisplay = new CurrencyDisplay(SCREEN_WIDTH - 150, -15, camera, player);
        addObject(currencyDisplay, SCREEN_WIDTH - 150, 0);
    }

    protected void updateBackground() {
        if (camera == null || fullBackground == null) return;

        GreenfootImage bg = getBackground();
        bg.clear();

        GreenfootImage visiblePortion = new GreenfootImage(SCREEN_WIDTH, SCREEN_HEIGHT);
        visiblePortion.drawImage(fullBackground, -camera.getX(), -camera.getY());
        bg.drawImage(visiblePortion, 0, 0);
    }

    protected void updateAllActors() {
        for (Object obj : getObjects(ScrollingActor.class)) {
            ScrollingActor actor = (ScrollingActor) obj;
            actor.updateScreenPosition();
        }
    }

    protected void transferPlayer(Player existingPlayer, int startX, int startY) {
        this.player = existingPlayer;
        player.setCamera(this.camera);
        addObject(player, SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        player.setWorldPosition(startX, startY);

        initalizeDisplays();
    }
    
    public Camera getCamera() { return camera; }

    public MapGrid getMapGrid() { return mapGrid; }

    public InventoryDisplay getInventory() { return inventory;}

    public int worldToTileX(int worldX) { return worldX / TILE_SIZE; }

    public int worldToTileY(int worldY) { return worldY / TILE_SIZE; }

    public int tileToWorldX(int tileX) { return tileX * TILE_SIZE; }

    public int tileToWorldY(int tileY) { return tileY * TILE_SIZE; }

    protected abstract void initializeMapGrid();
}
