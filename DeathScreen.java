import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)"/**
 /**
 * If you die this screen spawns.
 * 
 * @author Paul
 */
public class DeathScreen extends World
{

    GreenfootImage death = new GreenfootImage("deathscreen.jpg");
    GreenfootImage youDied = new GreenfootImage("youdied.png");
    Button backToStart;
    public DeathScreen()
    {    
        super(800, 600, 1); 
        death.scale(800,600);
        setBackground(death);
        
        //Button back to start screen
        backToStart = new Button("Back to Start", 60, 250, Color.BLUE, 5, Color.BLACK, 24, Color.BLACK, "backtostart", false);
        addObject(backToStart, getWidth()/2, (getHeight()/4) * 3);
        youDied.scale(400,200);
        death.drawImage(youDied, (getWidth()/2)-200, getHeight()/4);
    }
}
