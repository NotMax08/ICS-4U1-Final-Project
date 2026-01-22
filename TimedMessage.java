import greenfoot.*;
/**
 * timed message on screen
 * @author Paul and Claude
 */
public class TimedMessage extends Actor {
    private int duration;
    private int timer = 0;
    /**
     * message constructor
     * @param text text to display
     * @param duration duration to display
     */
    public TimedMessage(String text, int duration) {
        this.duration = duration;
        GreenfootImage img = new GreenfootImage(text, 24, Color.WHITE, new Color(0,0,0,150));
        setImage(img);
    }
    /**
     * timer to remove text
     */
    public void act() {
        timer++;
        if (timer >= duration) {
            getWorld().removeObject(this);
        }
    }
}