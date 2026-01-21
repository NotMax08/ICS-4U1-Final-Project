import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Starting world screen leads to intro.
 * 
 * @author Paul 
 */

public class StartWorld extends World
{
    GreenfootImage startworld = new GreenfootImage ("startworld.jpg");
    GreenfootImage title = new GreenfootImage ("title.png");

    /**
     * Constructor for objects of class StartWorld.
     * 
     */
    public StartWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(800, 600, 1); 
        setBackground(startworld);
        Button start = new Button ("Start", 65, 200, Color.WHITE, 4, Color.BLACK, 35, Color.BLACK, "startworld", false );
        addObject(start,getWidth()/2,(getHeight()/4) * 3); 
        GreenfootImage bg = getBackground();
        title.scale(500,250);
        bg.drawImage(title, (getWidth()/4)-40,getHeight()/7);
        setBackground(bg);
        
        //SoundManager.getInstance().playBackgroundMusic("Room1Music.mp3");
    }
    
    public void started() {
        //SoundManager.getInstance().playBackgroundMusic("Room1Music.mp3");
    }
    
    public void stopped() {
        SoundManager.getInstance().pauseBackgroundMusic();
    }
}
