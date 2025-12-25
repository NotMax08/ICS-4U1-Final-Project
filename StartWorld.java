import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Starting world screen leads to intro.
 * 
 * @author Paul 
 */

public class StartWorld extends World
{
    GreenfootImage startworld = new GreenfootImage ("startworld.jpg");
    /**
     * Constructor for objects of class StartWorld.
     * 
     */
    public StartWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 400, 1); 
        setBackground(startworld);
        Button start = new Button ("Start", 50, 100, Color.WHITE, 4, Color.BLACK, 20, Color.BLACK, "startworld", false );
        addObject(start,getWidth()/2,(getHeight()/4) * 3); 
    }
}
