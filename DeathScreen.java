import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)"/**
 /**
 * If you die this screen spawns.
 * 
 * @author Paul
 */
public class DeathScreen extends World
{
    GreenfootImage death = new GreenfootImage("deathscreen.jpg");
    Button backToStart;
    public DeathScreen()
    {    
        super(800, 600, 1); 
        death.scale(800,600);
        setBackground(death);
        
        //Button back to start screen
        backToStart = new Button("Back to Start", 60, 250, Color.BLUE, 5, Color.BLACK, 24, Color.BLACK, "backtostart", false);
        addObject(backToStart, getWidth()/2, (getHeight()/4) * 3);
        
        SoundManager.getInstance().playBackgroundMusic("DeathScreenMusic.mp3");
    }
    
    public void started() {
        SoundManager.getInstance().playBackgroundMusic("DeathScreenMusic.mp3");
    }
    
    public void stopped() {
        SoundManager.getInstance().pauseBackgroundMusic();
    }
}
