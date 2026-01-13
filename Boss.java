import greenfoot.*;
/**
 * @author Angelina
 */
public class Boss extends Actor
{   
    private boolean entering = false;
    
    private boolean teleporting = false;
    private boolean teleported = false;
    
    private int teleportX;
    private int teleportY;    
    private String teleportImage;
    
    private int currentX;
    private int currentY;
    private String currentImage;
    
    private double scale = 4;
    
    private boolean isNew;
    
    GreenfootImage staBeeFront;
    GreenfootImage staBeeLeftUp;
    
    private double teleportFrames = 100;
    
    public Boss(int variant)
    {
        staBeeFront = new GreenfootImage("StaBee/Character/Front.PNG");
        staBeeLeftUp = new GreenfootImage("StaBee/Character/LeftUp.PNG");
        
        this.setI("Front");
        currentImage = "Front";
        
        isNew = true;
        
        if (variant == 1){
            enterScreen();
        }
    }
    
    private void addedToWorld(){
        currentX = this.getX();
        currentY = this.getY();
        isNew=false;
    }
    
    public void act(){
        if (entering){
            this.setLocation(BossRoom.BOSS_WORLD_WIDTH/2,this.getY()+2);
            if (this.getY() == 300){
                entering = false;
                teleport(600, this.getY(), "LeftUp");
            }
        }
        if (teleporting){
            teleporting(teleportX, teleportY,teleportImage);
        }
    }
    
    private void teleport(int x, int y, String newPose){
        teleportX = x;
        teleportY = y;
        teleportImage = newPose;
        
        teleporting = true;
        teleported = false;
    }
    
    private void teleporting(int x, int y, String newPose){
        int newTransparency;
        if (!teleported){
           newTransparency = this.getImage().getTransparency() - (int)(255/(teleportFrames/2));
        }
        else{
           newTransparency = this.getImage().getTransparency() + (int)(255/(teleportFrames/2));
        }
        if (newTransparency <= 0){
            newTransparency = 0;
            this.setI(newPose);
            this.setLocation(teleportX,teleportY);
            teleported = true;
        }
        else if(newTransparency >= 255){
            newTransparency = 255;
            teleporting = false;
        }
        this.getImage().setTransparency(newTransparency);
    }
    
    private void enterScreen(){
        entering = true;
    }
    
    private void setI(String imageName){
        GreenfootImage image = new GreenfootImage("StaBee/Character/" + imageName + ".PNG");
        image.scale((int)(image.getWidth()/scale),(int)(image.getHeight()/scale));
        this.setImage(image);
    }
}
