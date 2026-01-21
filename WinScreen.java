import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * If you beat the final boss this screen spawns.
 * 
 * @author Paul
 */
public class WinScreen extends World
{

    GreenfootImage win = new GreenfootImage("winscreen.jpg");
    GreenfootImage youWin = new GreenfootImage("youwin.png");
    Button backToStart;
    public WinScreen()
    {    
        super(800, 600, 1); 
        win.scale(800,600);
        setBackground(win);
        
        //Button back to start screen
        backToStart = new Button("Back to Start", 60, 250, Color.BLUE, 5, Color.BLACK, 24, Color.BLACK, "backtostart", false);
        addObject(backToStart, getWidth()/2, (getHeight()/4) * 3);
        youWin.scale(400,250);
        win.drawImage(youWin, (getWidth()/2)-200, getHeight()/4);
    }
}
