import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Doorways to new rooms opened by pressing key on keyboard.
 * 
 * @author Paul 
 */
public class InteractiveDoor extends Actor
{
    int width;
    int height;
    public InteractiveDoor(int height, int width){
        this.width = width;
        this.height = height;
        GreenfootImage img = new GreenfootImage(width,height);
        img.setColor(Color.GREEN);
        img.fillRect(0, 0, width, height);
        setImage(img);
    }
    
}
