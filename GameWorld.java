import greenfoot.*;

/**
 * Abstract base class for all game worlds/levels
 * Handles common functionality like camera, displays and player tracking
 * 
 * 
 * @author Paul and Robin
 * 
 * Block Comment:
 * Full Squire (Hollow Knight Inspired Platformer)
    Group Members: Paul, Max, Julian, Robin, Angelina
    
    Instructions:
    Play the game as intended with several tries for the best experience. Intended to be difficult and rewarding to complete. Main point is to explore all rooms and fight final boss to win.
    Keybinds: 
    -> Movement: space/w jump, a/d or left/right arrow keys for direction
    -> Attack: j or e for basic attack, k for magic attack.
    -> Abilities:  q or h to heal, hold for 2 seconds and shift or k for magic ability
    -? Inventory: tab (empty by default, does not count your weapon)
    -> Map: m to open
    -> Interactive Doors: f to open
    -> Items: buying and using items are done with mouse click in shop and inventory ui respectively (resets when moving rooms)
    ->Debug: g for map grid and y for room position entry testing, platform and interactive door debug visuals are booleans set in constructors
    Recommended Keybinds:
    The keybinds are made to support both arrow key movement and a/d movement so double keybinds are used to make either easy to use.
    Arrow Keys:
    - e to attack, q to heal, shift for magic attack, space to jump
    A/D
    - j to attack, h to heal, k for magic attack, w for jump
    
    Cheat Code:
    - god mode can be activated to nullify all damage via key p
    - room one to room two entrance is at the top of room one
    - room two to room three entrance is at the top righ tof room two
    - npc room and miniboss room are found in the right and left doorways roughly in the middle y level in room two respectively
    - killing miniboss grants weapon upgrade that deals more damage and makes boss easier
    - boss room is found in room one and is unlocked via key found in room three
    - room three needs a shrinking potion to access the top and find the key
    
    Features:
    - 2D Array system that forms a grid in each room, different numbers mean empty space, platforms, breakable objects (not implemented due to time constraints), interactive doors, regular doors (auto move to next room) and walls
    - Has a debug that shows the grid when pressing g and color codes different tile types. Press once to show x number on tiles and twice to see y.
    - Player has a needle as a weapon that deals melee damage and has a weapon upgrade to be found that deals more damage. 
    - 7 different enemies including one boss and one miniboss.
    - Boss has three attacks and two phases.
    - Miniboss has two attacks and rewards weapon upgrade when defeated.
    - Gravity system for player and enemies jumping and falling.
    - Health system for player and enemies.
    - Soul system to gather energy when player hits enemies and charges to heal.
    - Breakable Spawner that spawns enemies every so often.
    - 3 main rooms, two boss rooms housing the miniboss and main boss and an npc room for a potion selling npc.
    - Main rooms have a camera effect allowing for players to discover a larger room space than the world screen and update background image as the player moves.
    - 3 different potion types, strength, shield and shrink.
    - Leaderboard save file that tracks highscores based on total playthrough time (only counts if you win)
    - Player and all enemies are animated with image frames for all movement and attacks
    - Inventory system keeps track of items player has like potion, key to unlcok boss room and displays items in inventory ui
    - NPC shopkeeper that sells three types of potions, has custom dialogue class and shop ui to interact with
    - Map that show all rooms
    
    Credits:
    Sound credits:
     * "sword"  by @freesound_community on pixabay
     * "magic"  by @yodguard on pixabay
     * "heal"   by @yodguard on pixabay
     * "jump"   by @Jofae on pixabay
     * "hit"    by @mixkit
     * "damage" by @freesound_community on pixabay
     * "electricBolt" by @PNMCarrieRailfan on freesound.org
     * "strike" by SoundFlakes on freesound.org
     * "throw" by Anton on freesound.org
     * "Bzzt" sound by @danielpodlovics on freesound
     * BossMusic by @Komiku on chosic
     * All other background music by @Teecup_Thief on itch.io
     * 
    Art credit:
     * Boss art by Angelina
     * Other art, Leonardo Ai and ChatGPT
    
    Author Paul
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
    /**
     * Super class constructor for all room worlds
     */
    public GameWorld() {
        super(SCREEN_WIDTH, SCREEN_HEIGHT, 1, false);
        camera = new Camera(SCREEN_WIDTH, SCREEN_HEIGHT, WORLD_WIDTH, WORLD_HEIGHT);

        // Initalize sound manager and load sounds 
        initializeSounds();

        setPaintOrder();
    }

    protected void setPaintOrder() {
        setPaintOrder(
            TimedMessage.class,
            TextBox.class,
            SpeedPotion.class,
            StrengthPotion.class,
            ShrinkPotion.class,
            Key.class,
            Items.class,
            ShopIcons.class,
            ShopUI.class,
            MapGridDebugOverlay.class,
            MapDisplay.class,
            InventoryDisplay.class,
            CurrencyDisplay.class,
            AbilityDisplay.class,
            HealthDisplay.class,
            ManaDisplay.class,
            SlashAnimation.class,
            Player.class,
            Platform.class 
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
        if (HighScoreManager.isRunInProgress()) {
            int currentTime = HighScoreManager.getCurrentRunTime();
            showText("Time: " + HighScoreManager.formatTime(currentTime), 700, 30);
        }
        
        // God Mode display
        String str = player.isGodMode() ? "Activated" : "Deactivated";
        showText("God mode " + str, SCREEN_WIDTH - 150, 100); 
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
        
        MapDisplay mapDisplay = new MapDisplay(120, 60, camera);
        addObject(mapDisplay, 120, 60);
        
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
    /**
     * all getters
     */
    public Camera getCamera() { return camera; }

    public MapGrid getMapGrid() { return mapGrid; }

    public InventoryDisplay getInventory() { return inventory;}

    public int worldToTileX(int worldX) { return worldX / TILE_SIZE; }

    public int worldToTileY(int worldY) { return worldY / TILE_SIZE; }

    public int tileToWorldX(int tileX) { return tileX * TILE_SIZE; }

    public int tileToWorldY(int tileY) { return tileY * TILE_SIZE; }

    protected abstract void initializeMapGrid();
}
