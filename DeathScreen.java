import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)"/**
 /**
 * If you die this screen spawns.
 * 
 * Images from Leonardo Ai and ChatGPT
 * 
 * @author Paul
 */
public class DeathScreen extends World
{
    GreenfootImage death = new GreenfootImage("deathscreen.jpg");
    GreenfootImage youdied = new GreenfootImage("youdied.png");
    Button backToStart;
    /**
     * Death screen trigger by player dying at any point.
     */
    public DeathScreen()
    {    
        super(800, 600, 1); 
        youdied.scale(200,100);
        death.drawImage(youdied, (getWidth()/2) - 230, (getHeight()/7));
        death.scale(800,600);
        setBackground(death);
        
        //Button back to start screen
        backToStart = new Button("Back to Start", 60, 250, Color.BLUE, 5, Color.BLACK, 24, Color.BLACK, "backtostart", false);
        addObject(backToStart, getWidth()/2, (getHeight()/4) * 3);
        
        SoundManager.getInstance().playBackgroundMusic("DeathScreenMusic.mp3");
        
        Player.resetInventoryAndMoney();
    }
    /**
     * Starts background music when world is spawned and instance is started
     */
    public void started() {
        SoundManager.getInstance().playBackgroundMusic("DeathScreenMusic.mp3");
    }
    /**
     * Stops music when instance is paused
     */
    public void stopped() {
        SoundManager.getInstance().pauseBackgroundMusic();
    }
}
