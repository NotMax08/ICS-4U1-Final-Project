import greenfoot.*;

/**
 * Screen for entering a username before starting the game
 * Username is saved for leaderboard tracking
 * 
 * @author Claude
 */
public class UsernameScreen extends World {
    private String username = "";
    private static final int MAX_NAME_LENGTH = 15;
    private boolean shiftPressed = false;
    private int cursorBlink = 0;
    private boolean showCursor = true;
    
    // Store username globally
    private static String savedUsername = "Player";
    /**
     * constructor for username screen
     */
    public UsernameScreen() {    
        super(1200, 800, 1);
        
        // Create background with all text
        updateBackground();
    }
    /**
     * act that takes keyboard input and updates screen to show username typed out
     */
    public void act() {
        handleKeyInput();
        updateCursorBlink();
    }
    
    /**
     * Redraws the entire background with updated text
     */
    private void updateBackground() {
        GreenfootImage bg = new GreenfootImage(1200, 800);
        bg.setColor(new Color(20, 20, 30));
        bg.fill();
        
        // Title text
        bg.setColor(Color.WHITE);
        bg.setFont(new Font(32)); // Just size
        bg.drawString("Enter Your Name, Adventurer:", 400, 250);
        
        // Username display with cursor
        String display = username;
        if (showCursor && username.length() < MAX_NAME_LENGTH) {
            display += "_";
        } else if (username.length() == 0) {
            display = "_";
        }
        
        bg.setFont(new Font(40));
        bg.drawString(display, 500, 350);
        
        // Instructions
        bg.setFont(new Font(20));
        bg.drawString("Press ENTER to begin your journey", 450, 500);
        
        setBackground(bg);
    }
    
    /**
     * Redraws background with error message
     */
    private void updateBackgroundWithError(String errorMsg) {
        GreenfootImage bg = new GreenfootImage(1200, 800);
        bg.setColor(new Color(20, 20, 30));
        bg.fill();
        
        // Title text
        bg.setColor(Color.WHITE);
        bg.setFont(new Font(32));
        bg.drawString("Enter Your Name, Adventurer:", 400, 250);
        
        // Username display with cursor
        String display = username;
        if (showCursor && username.length() < MAX_NAME_LENGTH) {
            display += "_";
        } else if (username.length() == 0) {
            display = "_";
        }
        
        bg.setFont(new Font(40));
        bg.drawString(display, 500, 350);
        
        // Error message in red
        bg.setColor(Color.RED);
        bg.setFont(new Font(18));
        bg.drawString(errorMsg, 450, 450);
        
        // Instructions
        bg.setColor(Color.WHITE);
        bg.setFont(new Font(20));
        bg.drawString("Press ENTER to begin your journey", 450, 500);
        
        setBackground(bg);
    }
    
    /**
     * Handles all keyboard input for username entry
     */
    private void handleKeyInput() {
        // Check for Enter key to submit
        if (Greenfoot.isKeyDown("enter") && username.length() > 0) {
            startGame();
            return;
        }
        
        // Check for Enter with empty name
        if (Greenfoot.isKeyDown("enter") && username.length() == 0) {
            updateBackgroundWithError("Please enter a name!");
            Greenfoot.delay(2);
            return;
        }
        
        // Check for backspace
        if (Greenfoot.isKeyDown("backspace") && username.length() > 0) {
            username = username.substring(0, username.length() - 1);
            updateBackground();
            Greenfoot.delay(2); // Prevent rapid deletion
            return;
        }
        
        // Check if max length reached
        if (username.length() >= MAX_NAME_LENGTH) {
            updateBackgroundWithError("Name too long! (Max " + MAX_NAME_LENGTH + " characters)");
            return;
        }
        
        // Track shift state
        if (Greenfoot.isKeyDown("shift")) {
            shiftPressed = true;
        } else {
            shiftPressed = false;
        }
        
        // Check for letter keys
        String key = Greenfoot.getKey();
        if (key != null && key.length() == 1) {
            char c = key.charAt(0);
            
            // Allow letters, numbers, spaces, and basic punctuation
            if (Character.isLetterOrDigit(c) || c == ' ' || c == '_' || c == '-') {
                // Capitalize if shift is pressed or if it's the first character
                if (shiftPressed || username.length() == 0) {
                    username += Character.toUpperCase(c);
                } else {
                    username += c;
                }
                updateBackground();
                Greenfoot.delay(1); // Prevent double input
            }
        }
    }
    
    /**
     * Makes the cursor blink
     */
    private void updateCursorBlink() {
        cursorBlink++;
        if (cursorBlink >= 20) {
            showCursor = !showCursor;
            cursorBlink = 0;
            updateBackground();
        }
    }
    
    /**
     * Starts the game with the entered username
     */
    private void startGame() {
        // Save username globally
        savedUsername = username;
        
        // Transition to the first game world
        Greenfoot.setWorld(new RoomOne()); // Change to your first game world
        HighScoreManager.startRun();
        HighScoreManager.setPlayerName(UsernameScreen.getUsername());
    }
    
    /**
     * Get the current playthrough's username (static method for easy access)
     */
    public static String getUsername() {
        return savedUsername;
    }
    
    /**
     * Set username manually if needed
     */
    public static void setUsername(String name) {
        savedUsername = name;
    }
}