import greenfoot.*;
/**
 * @author paul assisted by Claude
 */

public class Message extends ScrollingActor {
    public Message(String imageName, Camera camera) {
        super(camera);
        setImage(imageName);
    }
    
    public Message(GreenfootImage image, Camera camera) {
        super(camera);
        setImage(image);
    }
    
    public void act() {
        updateScreenPosition();
    }
}