import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * If you beat the final boss this screen spawns.
 * 
 * @author Paul
 */
public class WinScreen extends World
{
    GreenfootImage win = new GreenfootImage("winscreen.jpg");
    GreenfootImage youwin = new GreenfootImage("youwin.png");
    Button backToStart;
    public WinScreen()
    {    
        super(800, 600, 1); 
        win.scale(800,600);
        youwin.scale(300,200);
        win.drawImage(youwin, (getWidth()/2) - 150, (getHeight()/7));
        setBackground(win);
        
        //Button back to start screen
        backToStart = new Button("Back to Start", 60, 250, Color.BLUE, 5, Color.BLACK, 24, Color.BLACK, "backtostart", false);
        addObject(backToStart, getWidth()/2, (getHeight()/4) * 3);
        
        SoundManager.getInstance().playBackgroundMusic("Room1Music.mp3");
    }
    
    public void started() {
        SoundManager.getInstance().playBackgroundMusic("Room1Music.mp3");
    }
    
    public void stopped() {
        SoundManager.getInstance().pauseBackgroundMusic();
    }
}
